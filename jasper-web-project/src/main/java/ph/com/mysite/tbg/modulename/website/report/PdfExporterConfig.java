package ph.com.mysite.tbg.modulename.website.report;

import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

public class PdfExporterConfig implements PdfExporterConfiguration {
	
	private String title;
	private String author;
	private String keywords;
	private String creator;
	private String subject;

	@Override
	public Boolean isOverrideHints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCreatingBatchModeBookmarks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isCompressed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isEncrypted() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean is128BitKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOwnerPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PdfVersionEnum getPdfVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPdfJavaScript() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PdfPrintScalingEnum getPrintScaling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTagged() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PdfaConformanceEnum getPdfaConformance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIccProfilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllowedPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeniedPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMetadataTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getMetadataAuthor() {
		// TODO Auto-generated method stub
		return author;
	}

	@Override
	public String getMetadataSubject() {
		// TODO Auto-generated method stub
		return subject;
	}

	@Override
	public String getMetadataKeywords() {
		// TODO Auto-generated method stub
		return keywords;
	}

	@Override
	public String getMetadataCreator() {
		// TODO Auto-generated method stub
		return creator;
	}

	@Override
	public Boolean isDisplayMetadataTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
