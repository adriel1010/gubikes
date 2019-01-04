package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.jdbc.Work; 
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ChamarRelatorio  implements Work {
	
	
	private String caminhoRelatorio;
	private String nomeRelatorio;
	private HashMap param;
	
	
	public ChamarRelatorio(String caminho, HashMap parametros, String nome){
		this.caminhoRelatorio = caminho;
		this.nomeRelatorio = nome;
		this.param = parametros;
		
		System.out.println("no chama");
	}
	
	
	
	
	@Override
	public void execute(Connection connection) throws SQLException {

		try {
			
			HashMap parametros = param;

			FacesContext facesContext = FacesContext.getCurrentInstance();

			facesContext.responseComplete();

			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

			JasperPrint jasperPrint = JasperFillManager
					.fillReport(scontext.getRealPath("/WEB-INF/relatorios/" + caminhoRelatorio), parametros, connection);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

			byte[] bytes = baos.toByteArray();

			if (bytes != null && bytes.length > 0) {

				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

				response.setContentType("application/pdf");

				response.setHeader("Content-disposition", "attachment; filename=\"" + nomeRelatorio + ".pdf\"");

				response.setContentLength(bytes.length);

				ServletOutputStream outputStream = response.getOutputStream();

				outputStream.write(bytes, 0, bytes.length);

				outputStream.flush();

				outputStream.close();

				baos.close();
			}

		} catch (Exception e) {

			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio, e);

		}

	}
	/*
	
	public void imprimeRelatorio(String caminhoRelatorio, HashMap param, String nomeRelatorio)
			throws IOException, SQLException {

	
		Connection con = ConexaoBanco.getConexao().getConnection();


		HashMap parametros = param;

		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();

			facesContext.responseComplete();

			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

			JasperPrint jasperPrint = JasperFillManager
					.fillReport(scontext.getRealPath("/WEB-INF/relatorios/" + caminhoRelatorio), parametros, con);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

			byte[] bytes = baos.toByteArray();

			if (bytes != null && bytes.length > 0) {

				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

				response.setContentType("application/pdf");

				response.setHeader("Content-disposition", "attachment; filename=\"" + nomeRelatorio + ".pdf\"");

				response.setContentLength(bytes.length);

				ServletOutputStream outputStream = response.getOutputStream();

				outputStream.write(bytes, 0, bytes.length);

				outputStream.flush();

				outputStream.close();

				baos.close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}*/


	
	
	
	
}
