package ph.com.mysite.tbg.modulename.website.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import ph.com.mysite.tbg.modulename.dto.DataTableRequest;
import ph.com.mysite.tbg.modulename.dto.DataTablesResponse;
import ph.com.mysite.tbg.modulename.dto.ReportTypeBean;
import ph.com.mysite.tbg.modulename.website.report.PdfExporterConfig;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportExporterInput;
import ph.com.mysite.tbg.modulename.website.report.ReportExporterOutput;
import ph.com.mysite.tbg.modulename.website.report.ReportUtility;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;
import ph.com.mysite.tbg.modulename.website.service.BranchReportService;
import ph.com.mysite.tbg.modulename.website.service.OrganizationService;
import ph.com.org.tbg.modulename.ApplicationModule;
import ph.com.org.tbg.modulename.AuditEventType;
import ph.com.org.tbg.modulename.audit.AuditTrailService;
import ph.com.org.tbg.modulename.config.modulenameEncryptionProperties;
import ph.com.org.tbg.modulename.domain.Organization;
import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.org.tbg.modulename.security.AuthenticationService;
import ph.com.org.tbg.modulename.util.DateUtil;

@Controller
public class BranchReportController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BranchReportController.class);

	private static final String BRANCH_REPORT_URL = "/branch-report";

	private static final String BRANCH_REPORT_DOWNLOAD_URL = BRANCH_REPORT_URL + "/download";
	
	private static final String OVERRIDE = "override";

	private static final String NO_CONNECTION = "ph.com.org.tbg.modulename.message.BranchReport.no.connection";

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private BranchReportService branchReportService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	protected modulenameEncryptionProperties modulenameEncryptionProperties;
	
	@Autowired
	private AuditTrailService auditTrailService;
	
	@Autowired
	protected EntityManager entityManager;
	
	@RequestMapping(BRANCH_REPORT_URL)
	public String viewBranchReportList(Model model) {
		model.addAttribute("maxRecords", PlatformReportType.values().length);
		return "branch-report";
	}

	@RequestMapping(value = BRANCH_REPORT_URL + "/getAllReports", method = RequestMethod.GET)
	@ResponseBody
	public DataTablesResponse<ReportTypeBean> getPayments(
			@ModelAttribute DataTableRequest dataTableRequest) {

		logger.info("Fetching records with the following datatable params {}", dataTableRequest);

		List<ReportTypeBean> reportBeans = new ArrayList<ReportTypeBean>();

		for (PlatformReportType type : PlatformReportType.values()) {
			reportBeans.add(new ReportTypeBean(type.getId(), type.getLabel()));
		}
		
		return new DataTablesResponse<ReportTypeBean>(reportBeans, reportBeans.size(), dataTableRequest.getDraw());
	}

	@RequestMapping(value = BRANCH_REPORT_DOWNLOAD_URL + "/{id}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public void fullReport(@PathVariable String id, HttpServletResponse response, HttpServletRequest request)
			throws JRException, IOException, ServletException {

		// GET CURRENT USER
		UserAccountDetails currentUser = authenticationService.getCurrentAuditor();

		// GET REPORT TYPE
		PlatformReportType selectedType = null;

		for (PlatformReportType type : PlatformReportType.values()) {
			if (type.getId().toString().equals(id)) {
				selectedType = type;
			}
		}

		if (Objects.nonNull(selectedType)) {
			try {
				
				Date startDate = extractDateFromParam(request, "startDate");
				Date endDate = extractDateFromParam(request, "endDate");

				Organization myOrg = organizationService.findByLoggedInUser();
				
				logger.info("controller EM null? "+ (entityManager == null));

				// INSTANTIATE PARAMS
				ReportUtilityParams params = new ReportUtilityParams(selectedType, null, startDate, endDate, currentUser, myOrg);
				params.setBranchReportService(branchReportService);
				params.setEncryptionKey(modulenameEncryptionProperties.getEncryption().getKey());
				params.setEncryptionKeySpecs(modulenameEncryptionProperties.getEncryption().getKeyspecs());
				params.setEncryptionAlgorithm(modulenameEncryptionProperties.getEncryption().getAlgorithm());
				params.setEntityManager(entityManager);
				
				// GENERATE JASPER REPORT
				ReportUtility otcReportUtility = new ReportUtility();
				JasperPrint jasperPrint = otcReportUtility.downloadBranchReport(params);

				response.setContentType("application/pdf");
				response.setHeader("Content-disposition", "inline; filename=" + DateUtil.formatDate(new java.util.Date()) + " - "
						+ selectedType.getLabel() + ".pdf");

				String typeLabel = selectedType.getLabel();

				PdfExporterConfig config = new PdfExporterConfig();
				config.setTitle(DateUtil.formatDate(new Date()) + " - " + typeLabel + ".pdf");
				config.setCreator(currentUser.getFullName());
				config.setAuthor("org modulename");
				OutputStreamExporterOutput output = new ReportExporterOutput(response);
				ExporterInput input = new ReportExporterInput(jasperPrint);

				JRPdfExporter pdfExporter = new JRPdfExporter();
				pdfExporter.setConfiguration(config);
				pdfExporter.setExporterInput(input);
				pdfExporter.setExporterOutput(output);
				pdfExporter.exportReport();

				logger.info(currentUser.getUserId()+": "+"Finished generating report: " + selectedType.name() + "...");

				auditTrailService.logOtcEvent(AuditEventType.OTHER, ApplicationModule.REPORTS, null, "Generating " + selectedType,
						"", "", "", currentUser, new Date());
			} catch (SQLException e) {
				request.setAttribute(ERROR_MESSAGE_FOR_VIEW, getMessage(NO_CONNECTION, new Object[] {}, request.getLocale()));
				request.setAttribute(OVERRIDE, true);
				request.getRequestDispatcher(BRANCH_REPORT_URL).forward(request, response);
			} catch (Exception e) {
				logger.error(currentUser.getUserId()+": "+"Encountered error generating " + selectedType.name());
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}
	
	@RequestMapping(BRANCH_REPORT_URL + "/viewer")
	public String viewPdf() {
		return "view-pdf";
	}

}
