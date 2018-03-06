package ph.com.mysite.tbg.modulename.website.report;

import java.sql.Connection;
import java.util.Date;

import javax.persistence.EntityManager;

import ph.com.org.tbg.modulename.domain.Organization;
import ph.com.org.tbg.modulename.domain.UserAccountDetails;
import ph.com.mysite.tbg.modulename.website.service.BranchReportService;
import ph.com.mysite.tbg.modulename.website.service.TellerReportService;

public class ReportUtilityParams {
	
	private PlatformReportType reportType;
	private Connection connection;
	private Date startDate;
	private Date endDate;
	private UserAccountDetails userDetails;
	private BranchReportService branchReportService;
	private ReportTemplate template;
	private Organization organization;
	private String usdCurrencyCode;
	private String encryptionKey;
	private String encryptionKeySpecs;
	private String encryptionAlgorithm;
	private EntityManager entityManager;
	
	public ReportUtilityParams(PlatformReportType reportType, Connection connection,
			Date startDate, Date endDate, UserAccountDetails userDetails, Organization organization) {
		super();
		this.reportType = reportType;
		this.connection = connection;
		this.startDate = startDate;
		this.endDate = endDate;
		this.userDetails = userDetails;
		this.organization = organization;
	}

	public BranchReportService getBranchReportService() {
		return branchReportService;
	}

	public void setBranchReportService(BranchReportService branchReportService) {
		this.branchReportService = branchReportService;
	}

	public PlatformReportType getReportType() {
		return reportType;
	}

	public Connection getConnection() {
		return connection;
	}
	

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public UserAccountDetails getUserDetails() {
		return userDetails;
	}

	public ReportTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ReportTemplate template) {
		this.template = template;
	}

	public Organization getOrganization() {
		return organization;
	}

	public String getUsdCurrencyCode() {
		return usdCurrencyCode;
	}

	public void setUsdCurrencyCode(String usdCurrencyCode) {
		this.usdCurrencyCode = usdCurrencyCode;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String getEncryptionKeySpecs() {
		return encryptionKeySpecs;
	}

	public void setEncryptionKeySpecs(String encryptionKeySpecs) {
		this.encryptionKeySpecs = encryptionKeySpecs;
	}

	public String getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	public void setEncryptionAlgorithm(String encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	@Override
	public String toString() {
		return "ReportUtilityParams [reportType=" + reportType
				+ ", connection=" + connection + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", userDetails=" + userDetails
				+ ", branchReportService=" + branchReportService
				+ ", template=" + template + ", organization=" + organization
				+ ", usdCurrencyCode=" + usdCurrencyCode + ", encryptionKey="
				+ encryptionKey + ", encryptionKeySpecs=" + encryptionKeySpecs
				+ ", encryptionAlgorithm=" + encryptionAlgorithm + "]";
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
