package ph.com.mysite.tbg.modulename.website.report.branch.template;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.org.tbg.modulename.util.DateUtil;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportGenerationException;
import ph.com.mysite.tbg.modulename.website.report.ReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;

public class TransactionStatisticsJasperBranchReportTemplate extends ReportTemplate {

	private static final Logger logger = LoggerFactory.getLogger(TransactionStatisticsJasperBranchReportTemplate.class);

	private static PlatformReportType myReportType = PlatformReportType.STATISTICS;

	public TransactionStatisticsJasperBranchReportTemplate() {
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

			logger.info("Report Params: \n"+jasperParams);

			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");
			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, jasperParams, myConnection);

			return jasperPrint;

		} catch (Exception e) {
			throw new ReportGenerationException("Error creating " + myReportType.getLabel(), e);
		}
	}

}
