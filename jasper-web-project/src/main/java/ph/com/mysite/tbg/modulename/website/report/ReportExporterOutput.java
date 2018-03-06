package ph.com.mysite.tbg.modulename.website.report;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.export.OutputStreamExporterOutput;

public class ReportExporterOutput implements OutputStreamExporterOutput {

	private HttpServletResponse response;
	
	public ReportExporterOutput(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	public OutputStream getOutputStream() {
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void close() {
		try {
			response.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
