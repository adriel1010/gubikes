package controle;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.New;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.UsuarioSessaoMB;
import util.ValidaCadastro;
import dao.GenericDAO;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Orcamento;
import modelo.OrcamentoProdutos;
import modelo.ParcelaVenda;
import modelo.Pessoas;
import modelo.Produto;
import modelo.Venda;
import service.ClienteService;
import service.OrcamentoProdutoService;
import service.OrcamentoService;
import service.ProdutoService;
import service.VendaService;

@ViewScoped
@Named("vendaMB")
public class VendaMB implements Serializable {

	private static final long serialVersionUID = 1L;
 
	private Venda venda;
	private List<Venda> listVenda;
	private Venda parcelaBaixaVenda;
	private List<ParcelaVenda> listParcelaVenda;
	private List<ParcelaVenda> listParcelaVendaBaixar;
	private String formaPagamento;
	private ParcelaVenda parcelaVenda;
	private boolean esconder = true;
  

	@Inject
	private GenericDAO<Venda> daoVenda;
	
	@Inject
	private GenericDAO<ParcelaVenda> daoParcelaVenda;
	
	@Inject
	private GenericDAO<Produto> daoProduto;
	
	@Inject
	private GenericDAO<OrcamentoProdutos> daoOrcamentoProduto;

	@Inject
	private VendaService vendaService;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private RelatorioMB relatorioMB;

	@PostConstruct
	public void inicializar() { 
		venda = new Venda();
		parcelaVenda = new ParcelaVenda();
		parcelaBaixaVenda = new Venda();
		listVenda = new ArrayList<>(); 
		listParcelaVenda = new ArrayList<>(); 
		listParcelaVendaBaixar = new ArrayList<>();
		preencherLista();
	}

	public void preencherLista() {
		listVenda = daoVenda.listarCodicaoLivre(Venda.class, " status is true ");
	}
	
	public void carregaDados(Venda venda){
		this.venda = venda;
		if(venda.getSituacao().equals("A Vista")){
		esconder = false;	
		}else{
		esconder = true;	
		}
	}
	
	public void controleBotao(){
		esconder = false;
	}
	
	public void inativarVenda(Venda venda){
		venda.setStatus(false);
		vendaService.inserirAlterar(venda);
		voltaProduto(venda);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		preencherLista();
	}
	
	public void voltaProduto(Venda venda){
		
		List<OrcamentoProdutos> orcamentoProd = new ArrayList<>();
		orcamentoProd = daoOrcamentoProduto.listar(OrcamentoProdutos.class, " orcamento = '"+venda.getOrcamento().getId()+"'");
	     Produto produto = new Produto();
		for(OrcamentoProdutos o : orcamentoProd){
			produto = daoProduto.buscarPorId(Produto.class, o.getProduto().getId());
			produto.setQuantidade(produto.getQuantidade() + o.getQuantidade());
			produtoService.inserirAlterar(produto);
			produto = new Produto();
		}
	}
	
	public void carregarDados() {
		 
		listParcelaVendaBaixar = daoParcelaVenda.listar(ParcelaVenda.class, " venda = '" + venda.getId() + "'");
		 
	}
	
	public void chamaRelatorio(){
		System.out.println("id venda "+venda.getId()); 
		relatorioMB.imprimirRelatorioClienteParcelado(venda);
	}
	
	public void verificar(){
		if(formaPagamento.equals("APRAZO")){
			esconder = false;
		}else{
			esconder = true;
		}
	}
	
	public void abrirNavegador(Venda venda) {
		
		 

	if (venda.getOrcamento().getCliente().getPessoa().getCelular().equals("")) {
			ExibirMensagem.exibirMensagem(Mensagem.WHATS);
		} else {

			String telefone = venda.getOrcamento().getCliente().getPessoa().getCelular().replace("-", "");
			String telefone2 = telefone.replace("(", "");
			String telefone3 = telefone2.replace(")", ""); 
			String telefone4 = telefone3.replace(" ", ""); 
	 
		 
				try {
					java.awt.Desktop.getDesktop()
							.browse(new java.net.URI("https://api.whatsapp.com/send?1=pt_BR&phone=55"+telefone4));
				} catch (IOException e) { 
					e.printStackTrace();
				} catch (URISyntaxException e) { 
					e.printStackTrace();
				} 
	}
	}

	
	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Venda> getListVenda() {
		return listVenda;
	}

	public void setListVenda(List<Venda> listVenda) {
		this.listVenda = listVenda;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<ParcelaVenda> getListParcelaVenda() {
		return listParcelaVenda;
	}

	public void setListParcelaVenda(List<ParcelaVenda> listParcelaVenda) {
		this.listParcelaVenda = listParcelaVenda;
	}

	public ParcelaVenda getParcelaVenda() {
		return parcelaVenda;
	}

	public void setParcelaVenda(ParcelaVenda parcelaVenda) {
		this.parcelaVenda = parcelaVenda;
	}

	public boolean isEsconder() {
		return esconder;
	}

	public List<ParcelaVenda> getListParcelaVendaBaixar() {
		return listParcelaVendaBaixar;
	}

	public void setListParcelaVendaBaixar(List<ParcelaVenda> listParcelaVendaBaixar) {
		this.listParcelaVendaBaixar = listParcelaVendaBaixar;
	}

	public void setEsconder(boolean esconder) {
		this.esconder = esconder;
	}

	public Venda getParcelaBaixaVenda() {
		return parcelaBaixaVenda;
	}

	public void setParcelaBaixaVenda(Venda parcelaBaixaVenda) {
		this.parcelaBaixaVenda = parcelaBaixaVenda;
	}
 
	 

}
