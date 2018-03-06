package ph.com.org.tbg.modulename.website.report;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import net.sf.jasperreports.engine.JasperPrint;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ph.com.org.tbg.modulename.domain.Organization;
import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.mysite.tbg.modulename.website.report.branch.template.TransactionStatisticsBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.TransactionStatisticsTellerReportTemplate;
import ph.com.mysite.tbg.modulename.website.service.BranchReportService;
import ph.com.mysite.tbg.modulename.website.service.TellerReportService;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ReportUtilityTest {

	@Mock
	private ReportTemplate otcReportTemplate;

	@Test
	public void generateBranchReport() throws SQLException {

		ReportUtility reportUtility = new ReportUtility();

		// Map<OTCReportType, OTCReportTemplate> reportTemplates =
		// Mockito.mock(HashMap.class);
		BranchReportService branchReportService = Mockito
				.mock(BranchReportService.class);
		UserAccountDetails userAccountDetails = new UserAccountDetails();
		Organization mockOrg = new Organization();
		mockOrg.setName("Test");

		ReportUtilityParams parameters = new ReportUtilityParams(
				PlatformReportType.STATISTICS, null, new java.util.Date(),
				new java.util.Date(), userAccountDetails, mockOrg);
		parameters.setBranchReportService(branchReportService);
		
		ReportTemplate template = Mockito.mock(ReportTemplate.class);
				
		JasperPrint mockPrint = new JasperPrint();
		when(template.createJasperPrint(parameters)).thenReturn(mockPrint);
		
		JasperPrint receivedPrint = reportUtility
				.downloadBranchReport(parameters);

		assertTrue(parameters.getTemplate().getClass()
				.equals(TransactionStatisticsBranchReportTemplate.class));
		assertNotNull(receivedPrint);
	}
	
	@Test
	public void generateTellerReport() throws SQLException {

		ReportUtility reportUtility = new ReportUtility();

		// Map<OTCReportType, OTCReportTemplate> reportTemplates =
		// Mockito.mock(HashMap.class);
		TellerReportService tellerReportService = Mockito
				.mock(TellerReportService.class);
		UserAccountDetails userAccountDetails = new UserAccountDetails();
		userAccountDetails.setTellerId("111111");
		Organization mockOrg = new Organization();
		mockOrg.setName("Test");

		ReportUtilityParams parameters = new ReportUtilityParams(
				PlatformReportType.STATISTICS, null, new java.util.Date(),
				new java.util.Date(), userAccountDetails, mockOrg);
		parameters.setTellerReportService(tellerReportService);
		
		ReportTemplate template = Mockito.mock(ReportTemplate.class);
		
		JasperPrint mockPrint = new JasperPrint();
		when(template.createJasperPrint(parameters)).thenReturn(mockPrint);

		JasperPrint receivedPrint = reportUtility
				.downloadTellerReport(parameters);

		assertTrue(parameters.getTemplate().getClass()
				.equals(TransactionStatisticsTellerReportTemplate.class));
		assertNotNull(receivedPrint);
	}

}
