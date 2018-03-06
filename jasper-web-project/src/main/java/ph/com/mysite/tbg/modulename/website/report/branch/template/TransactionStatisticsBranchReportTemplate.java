package ph.com.mysite.tbg.modulename.website.report.branch.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.org.tbg.modulename.domain.report.StringAndLongGenericPojo;
import ph.com.org.tbg.modulename.util.DateUtil;
import ph.com.mysite.tbg.modulename.dto.TxnStatisticsField;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportGenerationException;
import ph.com.mysite.tbg.modulename.website.report.ReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;
import ph.com.mysite.tbg.modulename.website.service.TellerReportService;

@Component
public class TransactionStatisticsBranchReportTemplate extends ReportTemplate {

	private static final Logger logger = LoggerFactory.getLogger(TransactionStatisticsBranchReportTemplate.class);
	
	private static PlatformReportType myReportType = PlatformReportType.STATISTICS;

	@Override
	public JasperPrint createJasperPrint(ReportUtilityParams params) {

		UserAccountDetails currentUser = (UserAccountDetails) params.getUserDetails();

		// GET DATA
		List<TxnStatisticsField> txnList = new ArrayList<TxnStatisticsField>();

		List<StringAndLongGenericPojo> totalTxnList = branchReportService
				.getNumberOfTransactions(params.getStartDate(), params.getEndDate(), currentUser.getBranchCode());
		List<StringAndLongGenericPojo> txnCancelledList = branchReportService
				.getNumberOfECTransactions(params.getStartDate(), params.getEndDate(), currentUser.getBranchCode());

		logger.info("totalTxnList.size() " + totalTxnList.size());
		logger.info("txnCancelledList.size() " + txnCancelledList.size());

		List<String> userList = new ArrayList<String>();
		HashMap<String, Long> totalMap = new HashMap<String, Long>();
		HashMap<String, Long> cancelledMap = new HashMap<String, Long>();

		for (StringAndLongGenericPojo t : totalTxnList) {
			if (!userList.contains(t.getMyString().trim())) {
				userList.add(t.getMyString().trim());
			}

			totalMap.put(t.getMyString().trim(), t.getMyLong());
		}

		for (StringAndLongGenericPojo t : txnCancelledList) {
			if (!userList.contains(t.getMyString().trim())) {
				userList.add(t.getMyString().trim());
			}

			cancelledMap.put(t.getMyString().trim(), t.getMyLong());
		}

		for (String user : userList) {
			txnList.add(new TxnStatisticsField(user,
					totalMap.get(user) != null ? totalMap.get(user) : 0L,
					cancelledMap.get(user) != null ? cancelledMap.get(user)
							: 0L));
		}

		JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				txnList, false);

		try {
			Map<String, Object> jasperParams = new HashMap<String, Object>();
			jasperParams.put("BRANCH_NAME", currentUser.getBranch());
			jasperParams.put("BRANCH_CODE", currentUser.getBranchCode());
			jasperParams.put("ORGANIZATION_NAME", params.getOrganization().getName());
			jasperParams.put("BANK_BUSINESS_DATE", DateUtil.formatDate(params.getStartDate()));

			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");
			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, jasperParams, beanCollectionDataSource);

			return jasperPrint;

		} catch (Exception e) {
			throw new ReportGenerationException("Error creating " + myReportType.getLabel(), e);
		}

	}

	@Override
	public String toString() {
		return "TransactionStatisticsBranchReportTemplate []";
	}

	public TellerReportService getTellerReportService() {
		return tellerReportService;
	}

	public void setTellerReportService(TellerReportService tellerReportService) {
		this.tellerReportService = tellerReportService;
	}

}
