package controle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import modelo.Funcionario;
import modelo.Produto;
import service.ProdutoService;
import util.UsuarioSessaoMB;

@ViewScoped
@Named("produtoMB")
public class ProdutoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Produto produto;
	private List<Produto> listProduto;
	private BigDecimal porcent;


	@Inject
	private GenericDAO<Produto> daoProduto;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private ValidaCadastro validaCadastro;
	


	@PostConstruct
	public void inicializar() {
		
		produto = new Produto();
		listProduto = daoProduto.listaComStatus(Produto.class);
		
	}

	public void preencherLista(Produto t) {
		this.produto = t;
		calculaPorcentagem();

	}

	public void calcula() {

		BigDecimal valorProcentagem = (produto.getValorCompra().multiply(porcent)).divide(new BigDecimal(100));
		produto.setValorVenda(produto.getValorCompra().add(valorProcentagem));

	}

	public void calculaPorcentagem() {
		BigDecimal valorProcentagem = (produto.getValorVenda().divide(produto.getValorCompra(), MathContext.DECIMAL32)
				.subtract(new BigDecimal(1)));
		porcent = valorProcentagem.multiply(new BigDecimal(100));

	}

	public void inativarProduto(Produto t) {
		produtoService.update(" Produto set status = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {
		produto.setNome(produto.getNome().toUpperCase());
		try {
			if (produto.getId() == null) {
				if (validaCadastro.buscarProduto(produto)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTROPRODUTO);
				} else {
					produto.setStatus(true);
					produtoService.inserirAlterar(produto);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogProduto();
					carregarLista();
				}
			} else {
				if (validaCadastro.buscarProduto(produto) && validaCadastro.buscarProdutoAlterar(produto)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTROPRODUTO);
				} else {
					produtoService.inserirAlterar(produto);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogProduto();
					carregarLista();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void criarNovoObjeto() {
		produto = new Produto();
		porcent = new BigDecimal(0);
	}
	
	 

	public void carregarLista() {
		listProduto = daoProduto.listaComStatus(Produto.class);
	}

	public List<Produto> getListProduto() {
		return listProduto;
	}

	public void setListProduto(List<Produto> listProduto) {
		this.listProduto = listProduto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public BigDecimal getPorcent() {
		return porcent;
	}

	public void setPorcent(BigDecimal porcent) {
		this.porcent = porcent;
	}

}
