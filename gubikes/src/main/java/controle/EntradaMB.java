package controle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.New;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
 
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCadastro;
import dao.GenericDAO;
import modelo.Entrada;
import modelo.Funcionario;
import modelo.Orcamento;
import modelo.Produto;
import service.EntradaService;
import service.ProdutoService;
import util.UsuarioSessaoMB;

@ViewScoped
@Named("entradaMB")
public class EntradaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Entrada entrada;
	private List<Entrada> listEntrada;
 


	@Inject
	private GenericDAO<Entrada> daoEntrada;

	@Inject
	private EntradaService entradaService;

	@Inject
	private ValidaCadastro validaCadastro;
	


	@PostConstruct
	public void inicializar() {
		
		entrada = new Entrada();
		carregaLista();
		
	}

	public void preencherLista(Entrada t) {
		this.entrada = t;
	 

	}
	public void carregaLista(){
		listEntrada = daoEntrada.listaComStatus(Entrada.class);
	}
 

	public void salvar() {
	     	entrada.setLocalCompra(entrada.getLocalCompra().toUpperCase());
	    	DateFormat formata = new SimpleDateFormat("dd/MM/yyyy");  
			String dataFormatada = formata.format(new Date()); 
			char dataMes2 = dataFormatada.charAt(3);
			char dataMe3s = dataFormatada.charAt(4);
			char dataMe6s = dataFormatada.charAt(6);
			char dataMe7s = dataFormatada.charAt(7);
			char dataMe8s = dataFormatada.charAt(8);
			char dataMe9s = dataFormatada.charAt(9);
		try {
		  
					entrada.setStatus(true);
					entrada.setDataCompra(new Date());
					entrada.setMes(dataMes2+""+dataMe3s);
					entrada.setAno(dataMe6s+""+dataMe7s+""+dataMe8s+""+dataMe9s);
					entradaService.inserirAlterar(entrada);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogProduto();
					carregaLista();
		 
		 
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void criarNovoObjeto() {
		entrada = new Entrada();

	}
	 
	public void excluir(Entrada entrada) {
		entrada.setStatus(false);
		entradaService.inserirAlterar(entrada);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregaLista();
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public List<Entrada> getListEntrada() {
		return listEntrada;
	}

	public void setListEntrada(List<Entrada> listEntrada) {
		this.listEntrada = listEntrada;
	}
	
	 
 

}
