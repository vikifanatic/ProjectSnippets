package ph.com.mysite.tbg.modulename.website.report.branch.template;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ph.com.mysite.tbg.modulename.domain.UserAccountDetails;
import ph.com.mysite.tbg.modulename.util.DateUtil;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportGenerationException;
import ph.com.mysite.tbg.modulename.website.report.ReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;

@Component
public class BankPaymentCancelledBranchReportTemplate extends ReportTemplate {

	private static final Logger logger = LoggerFactory.getLogger(BankPaymentCancelledBranchReportTemplate.class);

	private static PlatformReportType myReportType = PlatformReportType.CANCELLEDREPORT;

	public BankPaymentCancelledBranchReportTemplate() {
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

			logger.info("Report Params: " + jasperParams);
			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader .getResource("classpath:/jasperreports/<reportname>.jrxml");

			String reportPath = mainReportStream.getURL().toString().replace("<reportname>.jrxml", "");
			jasperParams.put("SUBREPORT_DIR", reportPath);
			
			Resource grandTotalSubReport = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");
			JasperCompileManager.compileReport(grandTotalSubReport.getInputStream());
			Resource subTotalSubReport = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");
			JasperCompileManager.compileReport(subTotalSubReport.getInputStream());

			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, jasperParams, myConnection);

			return jasperPrint;

		} catch (Exception e) {
			throw new ReportGenerationException("Error creating " + myReportType.getLabel(), e);
		}
	}

	@Override
	public String toString() {
		return "BankPaymentCancelledBranchReportTemplate []";
	}

}
