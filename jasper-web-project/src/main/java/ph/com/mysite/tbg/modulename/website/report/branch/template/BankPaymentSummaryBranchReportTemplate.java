package ph.com.mysite.tbg.modulename.website.report.branch.template;

import java.util.ArrayList;
import java.util.Date;
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
import ph.com.org.tbg.modulename.domain.report.BranchBPSummaryPaymentDetailPojo;
import ph.com.org.tbg.modulename.util.DateUtil;
import ph.com.mysite.tbg.modulename.dto.BillsPaymentTxnSummaryField;
import ph.com.mysite.tbg.modulename.website.report.PlatformReportType;
import ph.com.mysite.tbg.modulename.website.report.ReportGenerationException;
import ph.com.mysite.tbg.modulename.website.report.ReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.ReportUtilityParams;

@Component
public class BankPaymentSummaryBranchReportTemplate extends ReportTemplate {

	private static final Logger logger = LoggerFactory.getLogger(BankPaymentSummaryBranchReportTemplate.class);

	private static PlatformReportType myReportType = PlatformReportType.SUMMARY;

	@Override
	public JasperPrint createJasperPrint(ReportUtilityParams params) {

		UserAccountDetails currentUser = (UserAccountDetails) params.getUserDetails();

		// UPDATE LIST
		List<BillsPaymentTxnSummaryField> bpField = new ArrayList<BillsPaymentTxnSummaryField>();

		List<BranchBPSummaryPaymentDetailPojo> associatedPayments = branchReportService
				.getPaymentDetailsForReport(params.getStartDate(),
						params.getEndDate(), currentUser.getBranchCode());

		logger.info("Completed Branch BP Summary Payment Detail extraction...");

		List<String> institutionsThatPaidThisDay = branchReportService
				.getInstitutionsThatDidPaymentsForBankingDate(
						params.getStartDate(), params.getEndDate(), currentUser.getBranchCode());

		logger.info("Completed collecting all institutions that paid this day...");

		for (String institutionId : institutionsThatPaidThisDay) {

			String instCodeandName = institutionId;
			Integer totalCount = 0;
			Double totalAmountPeso = 0.0;
			Double totalAmountDollar = 0.0;
			Double totalCashPeso = 0.0;
			Double totalCashDollar = 0.0;
			Double nonlateChecksPeso = 0.0;
			Double nonlateChecksDollar = 0.0;
			Double lateChecksPeso = 0.0;
			Double lateChecksDollar = 0.0;

			for (BranchBPSummaryPaymentDetailPojo pojo : associatedPayments) {

				if (pojo.getInstitutionId().equals(institutionId)) {

					instCodeandName = pojo.getInstitutionCode() + " - "
							+ pojo.getInstitutionName();

					totalCount++;

					if ("PHP".equals(pojo.getCurrency())) {
						totalAmountPeso += pojo.getTotalAmount();
						if ("CASH".equals(pojo.getModeType().toUpperCase())) {
							totalCashPeso += pojo.getTotalAmount();
						} else if ("CHECK".equals(pojo.getModeType().toUpperCase())) {
							if (pojo.isLateCheck()) {
								lateChecksPeso += pojo.getTotalAmount();
							} else {
								nonlateChecksPeso += pojo.getTotalAmount();
							}
						}

					} else if ("USD".equals(pojo.getCurrency())) {
						totalAmountDollar += pojo.getTotalAmount();
						if ("CASH".equals(pojo.getModeType().toUpperCase())) {
							totalCashDollar += pojo.getTotalAmount();
						} else if ("CHECK".equals(pojo.getModeType().toUpperCase())) {
							if (pojo.isLateCheck()) {
								lateChecksDollar += pojo.getTotalAmount();
							} else {
								nonlateChecksDollar += pojo.getTotalAmount();
							}
						}
					}

				}
			}

			bpField.add(new BillsPaymentTxnSummaryField(instCodeandName, totalCount, totalAmountPeso, totalAmountDollar,
					totalCashPeso, totalCashDollar, nonlateChecksPeso, nonlateChecksDollar, lateChecksPeso, lateChecksDollar));
		}

		JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				bpField, false);

		try {

			Map<String, Object> jasperParams = new HashMap<String, Object>();
			jasperParams.put("BRANCH_NAME", currentUser.getBranch());
			jasperParams.put("BRANCH_CODE", currentUser.getBranchCode());
			jasperParams.put("ORGANIZATION_NAME", params.getOrganization().getName());
			jasperParams.put("BANK_BUSINESS_DATE", DateUtil.formatDate(new Date()));
			jasperParams.put("DATE_FROM", DateUtil.formatDate(params.getStartDate()));
			jasperParams.put("DATE_TO", DateUtil.formatDate(params.getEndDate()));
			jasperParams.put("USD_CODE", params.getUsdCurrencyCode());

			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader.getResource("classpath:/jasperreports/<reportname>.jrxml");
			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, jasperParams, beanCollectionDataSource);

			return jasperPrint;

		} catch (Exception e) {
			throw new ReportGenerationException("Error creating "
					+ myReportType.getLabel(), e);
		}
	}

	@Override
	public String toString() {
		return "BankPaymentSummaryBranchReportTemplate []";
	}

}
