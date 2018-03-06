package ph.com.mysite.tbg.modulename.website.report;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.OrientationEnum;
import ph.com.org.tbg.modulename.config.ReadOnlyConnection;
import ph.com.mysite.tbg.modulename.website.report.branch.template.BankPaymentCancelledBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.branch.template.BankPaymentLateCheckBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.branch.template.BankPaymentProoflistBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.branch.template.BankPaymentSummaryJasperBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.branch.template.TransactionStatisticsJasperBranchReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.BankPaymentCancelledTellerReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.BankPaymentLateCheckTellerReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.BankPaymentProoflistTellerReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.BankPaymentSummaryJasperTellerReportTemplate;
import ph.com.mysite.tbg.modulename.website.report.teller.template.TransactionStatisticsJasperTellerReportTemplate;

/**
 * will contain all the report definitions.
 * 
 * @author karl
 *
 */

@Service
public class ReportUtility {
	
	//@Value("${usd.code}")
	private static final String USD_CURRENCY_CODE = "USD";

	private static final Logger logger = LoggerFactory
			.getLogger(ReportUtility.class);

	public static final String REPORT_GENERATION_FAILED_TXT = "Report Generation failed. ";

	private static final Map<PlatformReportType, ReportTemplate> tellerReportTemplates = new HashMap<>();

	private static final Map<PlatformReportType, ReportTemplate> branchReportTemplates = new HashMap<>();
	
	protected EntityManager entityManager;
	
	public ReportUtility() {
		tellerReportTemplates.put(PlatformReportType.STATISTICS, new TransactionStatisticsJasperTellerReportTemplate());
		tellerReportTemplates.put(PlatformReportType.SUMMARY, new BankPaymentSummaryJasperTellerReportTemplate());
		tellerReportTemplates.put(PlatformReportType.PROOFS, new BankPaymentProoflistTellerReportTemplate());
		tellerReportTemplates.put(PlatformReportType.DEFERREDPAYMENTS, new BankPaymentLateCheckTellerReportTemplate());
		tellerReportTemplates.put(PlatformReportType.CANCELLEDREPORT, new BankPaymentCancelledTellerReportTemplate());

		branchReportTemplates.put(PlatformReportType.STATISTICS, new TransactionStatisticsJasperBranchReportTemplate());
		branchReportTemplates.put(PlatformReportType.SUMMARY, new BankPaymentSummaryJasperBranchReportTemplate());
		branchReportTemplates.put(PlatformReportType.PROOFS, new BankPaymentProoflistBranchReportTemplate());
		branchReportTemplates.put(PlatformReportType.DEFERREDPAYMENTS, new BankPaymentLateCheckBranchReportTemplate());
		branchReportTemplates.put(PlatformReportType.CANCELLEDREPORT, new BankPaymentCancelledBranchReportTemplate());
	}

	@ReadOnlyConnection
	public JasperPrint downloadTellerReport(ReportUtilityParams parameters) throws SQLException {
		
		Connection myConnection = null;
		
		try {
			
			entityManager = parameters.getEntityManager();
			
			myConnection = getConnection();
			parameters.setConnection(myConnection);
			
			ReportTemplate selectedTemplate = tellerReportTemplates .getOrDefault(parameters.getReportType(), null);

			selectedTemplate.setTellerReportService(parameters.getTellerReportService());

			parameters.setTemplate(selectedTemplate);
			parameters.setUsdCurrencyCode(USD_CURRENCY_CODE);
			
			logger.info("Generating " + selectedTemplate);
			logger.info(parameters.toString());
			
			JasperPrint finalReport = selectedTemplate.createJasperPrint(parameters);
			finalReport.setOrientation(OrientationEnum.LANDSCAPE);
			
			return finalReport;
			
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception generating report. Creating empty report contingency.");
			e.printStackTrace();
			return createEmptyReport(new HashMap<String, Object>());
		} finally {
			if(myConnection != null) myConnection.close();
		}
		
	
	}

	@ReadOnlyConnection
	public JasperPrint downloadBranchReport(ReportUtilityParams parameters) throws SQLException {
		
		Connection myConnection = null;
		
		try {
			
			entityManager = parameters.getEntityManager();
			
			myConnection = getConnection();
			parameters.setConnection(myConnection);
			
			ReportTemplate selectedTemplate = branchReportTemplates
					.getOrDefault(parameters.getReportType(), null);
			selectedTemplate.setBranchReportService(parameters.getBranchReportService());
			
			parameters.setTemplate(selectedTemplate);
			parameters.setUsdCurrencyCode(USD_CURRENCY_CODE);
			
			logger.info("Generating "+selectedTemplate);
			logger.info(parameters.toString());

			JasperPrint finalReport = selectedTemplate.createJasperPrint(parameters);
			finalReport.setOrientation(OrientationEnum.LANDSCAPE);
			
			return finalReport;
			
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			logger.info("Exception generating report. Creating empty report contingency.");
			e.printStackTrace();
			return createEmptyReport(new HashMap<String, Object>());
		} finally {
			if(myConnection != null) myConnection.close();
		}

	}

	public static JasperPrint createEmptyReport(Map<String, Object> parameters) {
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource mainReportStream = loader.getResource("classpath:/jasperreports/report1.jrxml");
			JasperReport report = JasperCompileManager.compileReport(mainReportStream.getInputStream());

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters);

			return jasperPrint;
		} catch (Exception e) {
			throw new ReportGenerationException("Error encountered generating report. ", e);
		}
	}

	protected Connection getConnection() throws SQLException {
		
		logger.info("is entityManager null? "+ (entityManager == null));
		
		Session session = (Session) entityManager.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		return sessionFactory.getSessionFactoryOptions().getServiceRegistry()
					.getService(ConnectionProvider.class).getConnection();
	}
}
