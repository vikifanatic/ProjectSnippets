package ph.com.mysite.tbg.modulename.website.report;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterInputItem;
import net.sf.jasperreports.export.ReportExportConfiguration;

public class ReportExporterInput implements ExporterInput {
	
	private JasperPrint jasperPrint;

	@Override
	public List<ExporterInputItem> getItems() {
		
		List<ExporterInputItem> newList = new ArrayList<>();
		
		ExporterInputItem item = new ExporterInputItem() {
			
			@Override
			public JasperPrint getJasperPrint() {
				return jasperPrint;
			}
			
			@Override
			public ReportExportConfiguration getConfiguration() {
				return null;
			}
		};
		
		newList.add(item);
		
		return newList;
	}

	public ReportExporterInput(JasperPrint jasperPrint) {
		super();
		this.jasperPrint = jasperPrint;
	}

}
