package ph.com.mysite.tbg.modulename.website.report;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import ph.com.org.tbg.modulename.domain.Auditable;

@Entity
@Table(name = "BRANCH_REPORT")
public class ReportModel extends Auditable<ReportModel> {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "REPORT_TYPE")
	private PlatformReportType reportType;

	@Column(name = "BRANCH_ID")
	private String branchId;

	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Basic(fetch = FetchType.LAZY)
	private byte[] reportData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PlatformReportType getReportType() {
		return reportType;
	}

	public void setReportType(PlatformReportType reportType) {
		this.reportType = reportType;
	}

	public byte[] getReportData() {
		return reportData;
	}

	public void setReportData(byte[] bs) {
		this.reportData = bs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, reportType, reportData, createdBy, modifiedBy, dateCreated, dateModified);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ReportModel)) {
			return false;
		}
		ReportModel other = (ReportModel) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(id, other.id);
		equalsBuilder.append(reportType, other.reportType);
		equalsBuilder.append(reportData, other.reportData);
		return equalsBuilder.isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		toStringBuilder.append("id", id);
		toStringBuilder.append("reportType", reportType.name());
		toStringBuilder.append("reportData", reportData);
		return toStringBuilder.toString();
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

}
