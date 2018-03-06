package ph.com.mysite.tbg.modulename.website.report;

import net.sf.jasperreports.engine.JasperPrint;
import ph.com.mysite.tbg.modulename.website.service.BranchReportService;
import ph.com.mysite.tbg.modulename.website.service.TellerReportService;

public abstract class ReportTemplate {
	
	/**
	 * This is mostly important because some reports have a connection that has to be passed to their JasperReport 
	 */
	protected Boolean IS_CONNECTION_OBJECT_REQUIRED = false;
	
	protected TellerReportService tellerReportService;
	
	protected BranchReportService branchReportService;

	public abstract JasperPrint createJasperPrint(ReportUtilityParams parameters);
	
	public boolean isConnectionRequired() {
		return IS_CONNECTION_OBJECT_REQUIRED;
	}

	public TellerReportService getTellerReportService() {
		return tellerReportService;
	}

	public void setTellerReportService(TellerReportService tellerReportService) {
		this.tellerReportService = tellerReportService;
	}

	public BranchReportService getBranchReportService() {
		return branchReportService;
	}

	public void setBranchReportService(BranchReportService branchReportService) {
		this.branchReportService = branchReportService;
	}

}
