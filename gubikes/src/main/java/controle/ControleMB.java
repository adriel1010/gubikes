package controle;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCadastro;
import util.ValidacoesGerirUsuarios;
import dao.GenericDAO;
import modelo.Cidade;
import modelo.ControleVencimento;
import modelo.Produto;
import service.CidadeService;
import service.ControleVencimentoService;

@ViewScoped
@Named("controleMB")
public class ControleMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private ControleVencimento vencimento;
	private List<ControleVencimento> listControleVencimento;
	private ControleVencimento codigoVencimento;
	private String codigoAtivacao;
	private String codigoPavaAtiva;
	private boolean escondeTexto = false;
	private String dataVencimento;

	@Inject
	private GenericDAO<ControleVencimento> daoControleVencimento;

	@Inject
	private ControleVencimentoService controleVencimentoService;

	@PostConstruct
	public void inicializar() {

		codigoVencimento = new ControleVencimento();

	}

	public void voltar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../valida.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void principal() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("cadastro/inicio.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void controleVencimento() {
 
		listControleVencimento = daoControleVencimento.listaComStatus(ControleVencimento.class);

		if (listControleVencimento.size() == 0) {
			voltar();
		} else {

			for (ControleVencimento c : listControleVencimento) {
				vencimento = c;
			}

			String formato = "yyyy-MM-dd";
			SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
			
			String formatoBR = "dd/MM/yyyy";
			SimpleDateFormat dataFormatadaBR = new SimpleDateFormat(formatoBR);
			
			Date dia = new Date();
			
			String dat = String.valueOf(dia);
			String dataSystema = String.valueOf(vencimento.getData());
			
			String formatada = dataFormatada.format(dia);
			String formatadaSus = dataFormatada.format(vencimento.getData());
			
			System.out.println("ecibre dia "+formatada +" sys "+formatadaSus);
			
			 
			
			try {
		     	 
				Date dataSys = dataFormatada.parse(formatadaSus);
				Date dataSyss = dataFormatada.parse(formatada);
				
				
				
				System.out.println("ecibre dataSys   - "+dataSys +" sys dataSyss   - "+dataSyss);
				
				dataVencimento = dataFormatadaBR.format(dataSys);
				
				if(dataSys.before(dataSyss)){
					
					vencimento.setStatus(false);
					controleVencimentoService.inserirAlterar(vencimento);
					voltar();
					
				} 
				if(dataSys.equals(dataSyss)){
					ExibirMensagem.exibirMensagem("Hoje é o ultimo dia válido de licença");
				}
				
				
			} catch (ParseException e) { 
				e.printStackTrace();
			}

		}
			
			
			

	}
	
	
	public void valida(){
		
		String formato = "dd/MM/yyyy";
		SimpleDateFormat dataFormatada = new SimpleDateFormat(formato); 
		Date dia = new Date();
		String data = dataFormatada.format(dia);
		double mes = data.charAt(3)+data.charAt(4)+data.charAt(0)+data.charAt(9)+data.charAt(1);
		double divisao = (( mes / 2 ) * 5) /3 ;
		String vod = String.valueOf(divisao); 
		String codigoGerado = String.valueOf("CVDS"+divisao+"ADMGBKS"+divisao+"CG"+vod.charAt(1));
		
		if(codigoGerado.equals(codigoPavaAtiva)){
			
			LocalDate dataHoje = LocalDate.now();
			LocalDate meses = dataHoje.plusMonths(1);
			Date date = java.sql.Date.valueOf(meses);

			codigoVencimento.setData(date);
			codigoVencimento.setStatus(true);
			controleVencimentoService.inserirAlterar(codigoVencimento);
			principal();
			
			
		}else{
			ExibirMensagem.exibirMensagem("Código Inválido");
		}
		
	}
 
	
	public void gerarValidacao() {

		String formato = "dd/MM/yyyy";
		SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
  
		Date dia = new Date();
		String data = dataFormatada.format(dia);
		double mes = data.charAt(3)+data.charAt(4)+data.charAt(0)+data.charAt(9)+data.charAt(1);
		double divisao = (( mes / 2 ) * 5) /3 ;
		String vod = String.valueOf(divisao); 
		codigoAtivacao = String.valueOf("CVDS"+divisao+"ADMGBKS"+divisao+"CG"+vod.charAt(1));
		
	}
	
	

	public boolean isEscondeTexto() {
		return escondeTexto;
	}
	
	

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public void setEscondeTexto(boolean escondeTexto) {
		this.escondeTexto = escondeTexto;
	}

	public String getCodigoAtivacao() {
		return codigoAtivacao;
	}

	public void setCodigoAtivacao(String codigoAtivacao) {
		this.codigoAtivacao = codigoAtivacao;
	}

	public ControleVencimento getCodigoVencimento() {
		return codigoVencimento;
	}

	public void setCodigoVencimento(ControleVencimento codigoVencimento) {
		this.codigoVencimento = codigoVencimento;
	}

	public String getCodigoPavaAtiva() {
		return codigoPavaAtiva;
	}

	public void setCodigoPavaAtiva(String codigoPavaAtiva) {
		this.codigoPavaAtiva = codigoPavaAtiva;
	}

}
