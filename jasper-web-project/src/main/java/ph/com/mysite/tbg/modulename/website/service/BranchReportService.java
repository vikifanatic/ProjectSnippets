package ph.com.mysite.tbg.modulename.website.service;

import java.util.Date;
import java.util.List;

import ph.com.org.tbg.modulename.domain.report.BranchBPSummaryPaymentDetailPojo;
import ph.com.org.tbg.modulename.domain.report.StringAndLongGenericPojo;

public interface BranchReportService {

	List<StringAndLongGenericPojo> getNumberOfTransactions(Date startDate,
			Date endDate, String branchCode);

	List<StringAndLongGenericPojo> getNumberOfECTransactions(Date startDate,
			Date endDate, String branchCode);

	List<String> getInstitutionsThatDidPaymentsForBankingDate(Date startDate,
			Date endDate, String branchCode);

	List<BranchBPSummaryPaymentDetailPojo> getPaymentDetailsForReport(
			Date startDate, Date endDate, String branchCode);
}
