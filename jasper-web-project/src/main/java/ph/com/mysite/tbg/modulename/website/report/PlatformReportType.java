package ph.com.mysite.tbg.modulename.website.report;

public enum PlatformReportType {

	STATISTICS(1, "Transaction Statistics", new String[] {"BRANCH_REPORT"}),

	SUMMARY(2, "Transaction Summary", new String[] {"BRANCH_REPORT"}),

	PROOFS(3, "Payment Prooflist", new String[] {"BRANCH_REPORT"}),

	DEFERREDPAYMENTS(4, "List of Deferred Payments", new String[] {"BRANCH_REPORT"}),

	CANCELLEDREPORT(5, "List of Cancelled Transactions", new String[] {"BRANCH_REPORT"});

	private String label;
	private Integer id;
	private String[] reportGroups;

	PlatformReportType(Integer id, String label, String[] reportGroups) {
		this.label = label;
		this.id = id;
		this.reportGroups = reportGroups;
	}

	public String getLabel() {
		return label;
	}

	public Integer getId() {
		return id;
	}

	public String[] getReportGroups() {
		return reportGroups;
	}
}
