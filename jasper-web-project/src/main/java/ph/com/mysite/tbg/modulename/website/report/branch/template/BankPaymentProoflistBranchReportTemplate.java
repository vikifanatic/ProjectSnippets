package ph.com.mysite.tbg.modulename.website.report.branch.template;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.org.tbg.modulename.util.DateUtil;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportGenerationException;
import ph.com.mysite.tbg.modulename.website.report.ReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;

@Component
public class BankPaymentProoflistBranchReportTemplate extends ReportTemplate {

	private static final Logger logger = LoggerFactory.getLogger(BankPaymentProoflistBranchReportTemplate.class);

	private static PlatformReportType myReportType = PlatformReportType.PROOFS;

	public BankPaymentProoflistBranchReportTemplate() {
		IS_CONNECTION_OBJECT_REQUIRED = true;
	}

	@Override
	public JasperPrint createJasperPrint(ReportUtilityParams params) {

		UserAccountDetails currentUser = (UserAccountDetails) params.getUserDetails();
		Connection myConnection = (Connection) params.getConnection();

		try {
			Map<String, Object> jasperParams = new HashMap<String, Object>();
			jasperParams.put("BRANCH_NAME", currentUser.getBranch());
			jasperParams.put("BRANCH_CODE", currentUser.getBranchCode());
			jasperParams.put("ORGANIZATION_NAME", params.getOrganization().getName());
			jasperParams.put("BANK_BUSINESS_DATE", DateUtil.formatDate(params.getStartDate()));
			jasperParams.put("FORMATTED_BUSINESS_DATE", DateUtil.formatReportDateTime(params.getStartDate()));
			jasperParams.put("REPORT_CONNECTION", myConnection);
			
			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");

			String reportPath = mainReportStream.getURL().toString().replace("BPProofList.jrxml", "");
			jasperParams.put("SUBREPORT_DIR", reportPath);
			
			Resource grandTotalSubReport = loader.getResource("classpath:/jasperreports/BPProofList_grandtotal_subreport.jrxml");
			JasperCompileManager.compileReport(grandTotalSubReport.getInputStream());
			Resource subTotalSubReport = loader.getResource("classpath:/jasperreports/BPProofList_subtotal_subreport.jrxml");
			JasperCompileManager.compileReport(subTotalSubReport.getInputStream());
			
			logger.info("Report Params: " + jasperParams);
			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, jasperParams, myConnection);
			
			return jasperPrint;

		} catch (Exception e) {
			throw new ReportGenerationException("Error creating " + myReportType.getLabel(), e);
		}

	}

	@Override
	public String toString() {
		return "BankPaymentProoflistBranchReportTemplate []";
	}

}
