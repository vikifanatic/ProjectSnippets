package ph.com.mysite.tbg.modulename.website.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.com.org.tbg.modulename.domain.report.BranchBPSummaryPaymentDetailPojo;
import ph.com.org.tbg.modulename.domain.report.StringAndLongGenericPojo;
import ph.com.org.tbg.modulename.repository.BranchReportRepository;

@Service
class BranchReportServiceImpl implements BranchReportService {

	@Autowired
	private BranchReportRepository branchReportRepository;
	
	@Override
	public List<StringAndLongGenericPojo> getNumberOfTransactions(
			Date startDate, Date endDate, String branchCode) {
		return branchReportRepository.getNumberOfTransactions(startDate, endDate, branchCode);
	}

	@Override
	public List<StringAndLongGenericPojo> getNumberOfECTransactions(
			Date startDate, Date endDate, String branchCode) {
		return branchReportRepository.getNumberOfECTransactions(startDate, endDate, branchCode);
	}

	@Override
	public List<String> getInstitutionsThatDidPaymentsForBankingDate(
			Date startDate, Date endDate, String branchCode) {
		return branchReportRepository.getInstitutionsThatDidPaymentsForBankingDate(startDate, endDate, branchCode);
	}
	
	@Override
	public List<BranchBPSummaryPaymentDetailPojo> getPaymentDetailsForReport(
			Date startDate, Date endDate, String branchCode) {
		return branchReportRepository.getPaymentDetailsForReport(startDate, endDate, branchCode);
	}
}
