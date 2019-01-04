package controle;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.enterprise.inject.New;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

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
import service.OrcamentoProdutoService;
import service.OrcamentoService;
import service.ParcelaVendaService;
import service.ProdutoService;
import service.VendaService;

@ViewScoped
@Named("orcamentoMB")
public class OrcamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Orcamento orcamento;
	private Produto produto;
	private Cliente cliente;
	private ParcelaVenda parcelaVenda;
	private Venda venda;
	private ParcelaVenda parc;
	private Venda vendaParcela;
	private List<OrcamentoProdutos> listProdutoAdicionado;
	private List<OrcamentoProdutos> listProdutoAlteraValor;
	private List<ParcelaVenda> listParcelaVenda;
	private List<Orcamento> listOrcamento;
	private List<Cliente> listCliente;
	private int quantidade = 1;
	private boolean controleAddTipo = true;
	private BigDecimal valotTotal = new BigDecimal(0);
	private BigDecimal valotTotalFecha = new BigDecimal(0);
	private BigDecimal lucroCalculado = new BigDecimal(0);
	private BigDecimal valorMaoObra = new BigDecimal(0);
	private BigDecimal valorEntrada = new BigDecimal(0);
	private Funcionario funcionario;
	private BigDecimal valorDesconto = new BigDecimal(0);
	private boolean escondeClienteComplete = true;
	private boolean esconderBotaoVenda = true;
	private OrcamentoProdutos orcamentoPordutoAlterarValor;
	private OrcamentoProdutos orcamentoPordutoAlterarValorReferencia;
	private int controleVendaOrcamento = 0;
	private String formaPagamento;
	private boolean desabilitaParcela = true;
	private String dataGerarParcela;
	private boolean someSalva = true;
 

	@Inject
	private GenericDAO<Produto> daoProduto;

	@Inject
	private GenericDAO<Cliente> daoCliente;

	@Inject
	private GenericDAO<OrcamentoProdutos> daoOrcamentoProdutos;

	@Inject
	private GenericDAO<Orcamento> daoOrcamento;

	@Inject
	private OrcamentoService orcamentoService;

	@Inject
	private OrcamentoProdutoService orcamentoProdutoService;

	@Inject
	private VendaService vendaService;

	@Inject
	private ParcelaVendaService parcelaVendaService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private RelatorioMB relatorioMB;

	@PostConstruct
	public void inicializar() {

		orcamento = new Orcamento();
		venda = new Venda();
		listProdutoAdicionado = new ArrayList<>();
		listOrcamento = new ArrayList<>();
		listCliente = new ArrayList<>();
		listParcelaVenda = new ArrayList<>();
		produto = new Produto();
		listCliente = daoCliente.listarCodicaoLivre(Cliente.class, " statusCliente is true and controleCliente = 1");
		cliente = new Cliente();
		funcionario = new Funcionario();
		parcelaVenda = new ParcelaVenda();
		funcionario = usuarioSessao.recuperarFuncionairo();
		orcamentoPordutoAlterarValor = new OrcamentoProdutos();
		orcamentoPordutoAlterarValorReferencia = new OrcamentoProdutos();
		vendaParcela = new Venda();
		parc = new ParcelaVenda();
		preencherLista();
	}

	public void preencherLista() {
		listOrcamento = daoOrcamento.listarCodicaoLivre(Orcamento.class, " status is true and situacao = 'pendente' ");
	}

	public void carregaDados(Orcamento orcamento) {
		valotTotal = new BigDecimal(0);
		lucroCalculado = new BigDecimal(0);
		this.orcamento = daoOrcamento.buscarPorId(Orcamento.class, orcamento.getId());

		listProdutoAdicionado = new ArrayList<>();
	

		listProdutoAdicionado = daoOrcamentoProdutos.listar(OrcamentoProdutos.class,
				"orcamento = '" + orcamento.getId() + "'");
		escondeClienteComplete = true;

		listProdutoAdicionado.forEach(itensOrcamento -> {

			valotTotal = valotTotal.add(itensOrcamento.getSubTotal());
			lucroCalculado = lucroCalculado.add(itensOrcamento.getLucroUnitario());
		});

		
	 

		someSalva = false;

		listProdutoAlteraValor = new ArrayList<>();
		RequestContext.getCurrentInstance().update(":frmModalSalvarDados:client :frmModalAdicionarProd");
		// aqui

	}

	public void abrirNavegador(Orcamento orcamento) {

		if (orcamento.getCliente().getPessoa().getCelular().equals("")) {
			ExibirMensagem.exibirMensagem(Mensagem.WHATS);
		} else {

			String telefone = orcamento.getCliente().getPessoa().getCelular().replace("-", "");
			String telefone2 = telefone.replace("(", "");
			String telefone3 = telefone2.replace(")", "");
			String telefone4 = telefone3.replace(" ", "");

			try {
				java.awt.Desktop.getDesktop()
						.browse(new java.net.URI("https://api.whatsapp.com/send?1=pt_BR&phone=55" + telefone4));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public void recarrega() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cliente.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);

	}

	public void adicionaMaoObra() {
		System.out.println("valor add " + valorMaoObra);

		OrcamentoProdutos orcamentoProduto = new OrcamentoProdutos();
		orcamentoProduto.setStatus(true);

		BigDecimal arredondadoValorUnitario = valorMaoObra.multiply(new BigDecimal(0.01), MathContext.DECIMAL32);
		orcamentoProduto.setValorUnitario(arredondadoValorUnitario.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP));

		BigDecimal arredondadoLucroUnitario = valorMaoObra.multiply(new BigDecimal(0.01), MathContext.DECIMAL32);
		orcamentoProduto.setLucroUnitario(arredondadoLucroUnitario.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP));

		orcamentoProduto.setProduto(produto);
		orcamentoProduto.setQuantidade(1);

		BigDecimal arredondadoSubTotal = valorMaoObra.multiply(new BigDecimal(0.01), MathContext.DECIMAL32);
		orcamentoProduto.setSubTotal(arredondadoSubTotal.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP));

		valotTotal = valotTotal.add(orcamentoProduto.getSubTotal());
		quantidade = 1;
		lucroCalculado = lucroCalculado.add(orcamentoProduto.getLucroUnitario());
		listProdutoAdicionado.add(orcamentoProduto);
		orcamentoProduto = new OrcamentoProdutos();

		produto = new Produto();
		FecharDialog.fechaDialogObra();
		valorMaoObra = new BigDecimal(0);
	}

	public void adicionaProduto() {

		if (orcamento.getId() == null) {
			someSalva = false;
		} else {
			someSalva = true;
		}

		if (produto.getNome().equals("MÃO DE OBRA")) {
			FecharDialog.abrirDialogObra();
		} else if (produto.getNome().equals("DIVERSOS")) {
			FecharDialog.abrirDialogObra();
		} else {

			/*
			 * if (quantidade > produto.getQuantidade()) {
			 * ExibirMensagem.exibirMensagem(Mensagem.ESTOQUE); } else {
			 */
			OrcamentoProdutos orcamentoProduto = new OrcamentoProdutos();
			orcamentoProduto.setQuantidade(quantidade);
			orcamentoProduto.setStatus(true);
			orcamentoProduto.setValorUnitario(produto.getValorVenda());
			orcamentoProduto.setLucroUnitario(
					(produto.getValorVenda().subtract(produto.getValorCompra())).multiply(new BigDecimal(quantidade)));
			orcamentoProduto.setProduto(produto);
			orcamentoProduto.setSubTotal(produto.getValorVenda().multiply(new BigDecimal(quantidade)));
			valotTotal = valotTotal.add(orcamentoProduto.getSubTotal());
			quantidade = 1;
			lucroCalculado = lucroCalculado.add(orcamentoProduto.getLucroUnitario());
			listProdutoAdicionado.add(orcamentoProduto);
			orcamentoProduto = new OrcamentoProdutos();

			produto = new Produto();
			// }
		}

	}

	public void atribuirFuncionario() {

	}

	public void criarNovaParcela() {
		parc = new ParcelaVenda();
	}

	public void excluir(Orcamento orcamento) {
		orcamento.setStatus(false);
		orcamentoService.inserirAlterar(orcamento);
		preencherLista();
	}

	public void fecharVenda() {
		if (orcamento.getCliente() == null) {
			ExibirMensagem.exibirMensagem(Mensagem.CLIENTE);
		} else {
			if (orcamento.getId() == null) {

			}
		}
	}

	public void carregarDadosalterarValorPorduto(OrcamentoProdutos produto) {

		orcamentoPordutoAlterarValor = new OrcamentoProdutos();
		orcamentoPordutoAlterarValor = produto;
		orcamentoPordutoAlterarValorReferencia = produto;

	}

	public void alterarDadosProduto() {

		if (orcamento.getId() == null) {
			someSalva = false;
		} else {
			someSalva = true;
		}

		listProdutoAdicionado.remove(orcamentoPordutoAlterarValorReferencia);
		valotTotal = valotTotal.subtract(orcamentoPordutoAlterarValorReferencia.getSubTotal());
		lucroCalculado = lucroCalculado.subtract(orcamentoPordutoAlterarValorReferencia.getLucroUnitario());

		orcamentoPordutoAlterarValor.setSubTotal(orcamentoPordutoAlterarValor.getValorUnitario()
				.multiply(new BigDecimal(orcamentoPordutoAlterarValor.getQuantidade())));
		orcamentoPordutoAlterarValor.setLucroUnitario((orcamentoPordutoAlterarValor.getValorUnitario()
				.subtract(orcamentoPordutoAlterarValor.getProduto().getValorCompra()))
						.multiply(new BigDecimal(orcamentoPordutoAlterarValor.getQuantidade())));
		valotTotal = valotTotal.add(orcamentoPordutoAlterarValor.getSubTotal());
		quantidade = 1;
		lucroCalculado = lucroCalculado.add(orcamentoPordutoAlterarValor.getLucroUnitario());
		listProdutoAdicionado.add(orcamentoPordutoAlterarValor);
		orcamentoPordutoAlterarValor = new OrcamentoProdutos();
		orcamentoPordutoAlterarValorReferencia = new OrcamentoProdutos();

		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fechaDialoAddValor();
	}

	public void gerarDuplicatas() {

		listParcelaVenda.clear();
		desabilitaParcela = false;
		BigDecimal valorParcelado = new BigDecimal(0);
		BigDecimal valorSubtrai = new BigDecimal(0);
		int contadorNumeroParcela = 1;

		valorSubtrai = vendaParcela.getOrcamento().getSubTotalOrcamento().subtract(valorEntrada);
		valorParcelado = valorSubtrai.divide(new BigDecimal(parcelaVenda.getQuantidadeParcelas()),
				MathContext.DECIMAL32);
		BigDecimal aArredondado = valorParcelado.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);

		for (int i = 1; i <= parcelaVenda.getQuantidadeParcelas(); i++) {

			adicionaParcela(aArredondado, contadorNumeroParcela);
			contadorNumeroParcela += 1;
		}

		vendaParcela.setEntradaValor(valorEntrada);
		vendaService.inserirAlterar(vendaParcela);
		valorEntrada = new BigDecimal(0);

	}

	public void salvarDuplicatas() {
		for (ParcelaVenda p : listParcelaVenda) {
			parcelaVendaService.inserirAlterar(p);
		}
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fechaDialogParcela();

	}

	public void adicionaParcela(BigDecimal aArredondado, int numeroParcela) {

		parc.setQuantidadeParcelas(parcelaVenda.getQuantidadeParcelas());
		parc.setSituacao("pendente");
		parc.setStatus(true);
		parc.setValorTotalVenda(vendaParcela.getOrcamento().getSubTotalOrcamento());
		parc.setNumeroParcela(numeroParcela);
		parc.setVenda(vendaParcela);
		parc.setValorParcela(aArredondado);

		String[] dataSeparada = dataGerarParcela.split("/");
		LocalDate hoje = LocalDate.of(Integer.parseInt(dataSeparada[2]), Integer.parseInt(dataSeparada[1]),
				Integer.parseInt(dataSeparada[0]));
		LocalDate mes = hoje.plusMonths(numeroParcela);
		Date dataInse = java.sql.Date.valueOf(mes);
		parc.setData(dataInse);
		listParcelaVenda.add(parc);
		criarNovaParcela();

	}

	public void removerItem(OrcamentoProdutos itemRemove) {
		listProdutoAdicionado.remove(itemRemove);
		valotTotal = valotTotal.subtract(itemRemove.getSubTotal());
		lucroCalculado = lucroCalculado.subtract(itemRemove.getLucroUnitario());
	}

	public void abrirConfirmacaoVenda() {

		if (orcamento.getCliente() == null) {
			ExibirMensagem.exibirMensagem(Mensagem.CLIENTE);
		} else {
			if (listProdutoAdicionado.size() == 0) {
				ExibirMensagem.exibirMensagem(Mensagem.PROD);
			} else {
				FecharDialog.abrirDialogFinalizaOrcamento();
				orcamento.setDesconto(new BigDecimal(0));
				valotTotalFecha = valotTotal;
				valorDesconto = new BigDecimal(0);
				esconderBotaoVenda = false;
				controleVendaOrcamento = 1;
				formaPagamento = null;

			}
		}
	}

	public void abrirConfirmacaoOrcamento() {

		if (orcamento.getCliente() == null) {
			ExibirMensagem.exibirMensagem(Mensagem.CLIENTE);
		} else {
			FecharDialog.abrirDialogFinalizaOrcamento();
			orcamento.setDesconto(new BigDecimal(0));
			valotTotalFecha = valotTotal;
			valorDesconto = new BigDecimal(0);
			esconderBotaoVenda = true;
			controleVendaOrcamento = 0;
		}
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void calculaDescontoAuto(AjaxBehaviorEvent event) {
		if (orcamento.getDesconto() != null) {
			valotTotal = valotTotal.subtract(orcamento.getDesconto());
		}

	}

	public void calculaDesconto() {
		if (orcamento.getDesconto() != null) {
			valotTotalFecha = valotTotalFecha.subtract(orcamento.getDesconto());
			valorDesconto = valorDesconto.add(orcamento.getDesconto());
		}

	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public void baixaProduto(Venda venda) {

		List<OrcamentoProdutos> orcamentoProd = new ArrayList<>();
		orcamentoProd = daoOrcamentoProdutos.listar(OrcamentoProdutos.class,
				" orcamento = '" + venda.getOrcamento().getId() + "'");
		Produto produto = new Produto();
		for (OrcamentoProdutos o : orcamentoProd) {
			produto = daoProduto.buscarPorId(Produto.class, o.getProduto().getId());
			if (o.getQuantidade() > produto.getQuantidade()) {

				produto.setQuantidade(100);
				produtoService.inserirAlterar(produto);
			} else {

				produto.setQuantidade(produto.getQuantidade() - o.getQuantidade());
				produtoService.inserirAlterar(produto);
			}

			produto = new Produto();
		}
	}

	public void salvarOrcamento() {
		
		valorEntrada = new BigDecimal(0);
		dataGerarParcela = null;
		
		DateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formata.format(new Date());
		char dataMes2 = dataFormatada.charAt(3);
		char dataMe3s = dataFormatada.charAt(4);
		char dataMe6s = dataFormatada.charAt(6);
		char dataMe7s = dataFormatada.charAt(7);
		char dataMe8s = dataFormatada.charAt(8);
		char dataMe9s = dataFormatada.charAt(9);

		if (controleVendaOrcamento == 1) {
			if (orcamento.getId() == null) {
				venda = new Venda();

				orcamento.setDataOrcamento(dataFormatada);
				orcamento.setFuncionario(funcionario);
				orcamento.setSituacao("finalizado");
				orcamento.setStatus(true);
				orcamento.setValorTotalOrcamento(valotTotal);
				orcamento.setLucroOrcamento(lucroCalculado.subtract(valorDesconto));
				orcamento.setDesconto(valorDesconto);
				orcamento.setSubTotalOrcamento(valotTotalFecha);

				orcamentoService.inserirAlterar(orcamento);

				listProdutoAdicionado.forEach(itensOrcamento -> {

					itensOrcamento.setOrcamento(orcamento);
					orcamentoProdutoService.inserirAlterar(itensOrcamento);
					itensOrcamento = new OrcamentoProdutos();

				});

				venda = new Venda();

				if (formaPagamento.equals("APRAZO")) {

					venda.setSituacao("A PRAZO");
					venda.setSituacaoPago("devendo");
					vendaParcela = new Venda();
					FecharDialog.abrirDialogParcela();
					vendaParcela = venda;
					listParcelaVenda = new ArrayList<>();
					criarNovaParcela();

					venda.setDataVenda(dataFormatada);
					venda.setStatus(true);
					venda.setOrcamento(orcamento);
					venda.setMes(dataMes2 + "" + dataMe3s);
					venda.setAno(dataMe6s + "" + dataMe7s + "" + dataMe8s + "" + dataMe9s);

					vendaService.inserirAlterar(venda);
					baixaProduto(venda);

					parcelaVenda = new ParcelaVenda();
					desabilitaParcela = true;
					criarNovoObjeto();
					preencherLista();
					FecharDialog.fechaDialogOrcamento();
					FecharDialog.fechaDialogFinalizaOrcamento();

				} else {
					venda.setSituacao("A VISTA");
					venda.setSituacaoPago("pago");

					venda.setDataVenda(dataFormatada);
					venda.setStatus(true);
					venda.setOrcamento(orcamento);
					venda.setMes(dataMes2 + "" + dataMe3s);
					venda.setAno(dataMe6s + "" + dataMe7s + "" + dataMe8s + "" + dataMe9s);
					venda.setEntradaValor(valorEntrada);

					vendaService.inserirAlterar(venda);
					baixaProduto(venda);
					criarNovoObjeto();
					preencherLista();
					FecharDialog.fechaDialogOrcamento();
					FecharDialog.fechaDialogFinalizaOrcamento();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					desabilitaParcela = true;
				}

			} else {
				venda = new Venda();
				venda.setDataVenda(dataFormatada);

				if (formaPagamento.equals("AVISTA")) {
					venda.setSituacao("A Vista");
					venda.setSituacaoPago("pago");
					venda.setEntradaValor(new BigDecimal(0));
				} else {
					venda.setSituacao("A Prazo");
					venda.setSituacaoPago("devendo");
					vendaParcela = new Venda();
					FecharDialog.abrirDialogParcela();
					vendaParcela = venda;
					listParcelaVenda = new ArrayList<>();
					criarNovaParcela();
				}

				System.out.println("to no metodo q ta dando erro");

				venda.setStatus(true);
				venda.setOrcamento(orcamento);
				venda.setMes(dataMes2 + "" + dataMe3s);
				venda.setAno(dataMe6s + "" + dataMe7s + "" + dataMe8s + "" + dataMe9s);

				vendaService.inserirAlterar(venda);

				baixaProduto(venda);

				if (formaPagamento.equals("APRAZO")) {
					parcelaVenda = new ParcelaVenda();
					desabilitaParcela = true;

					preencherLista();
					FecharDialog.fechaDialogOrcamento();
					FecharDialog.fechaDialogFinalizaOrcamento();
				}
				orcamento.setSituacao("finalizado");
				orcamento.setValorTotalOrcamento(valotTotal);
				orcamento.setLucroOrcamento(lucroCalculado.subtract(valorDesconto));
				orcamento.setDesconto(valorDesconto);
				orcamento.setSubTotalOrcamento(valotTotalFecha);
				orcamentoService.inserirAlterar(orcamento);
				// https://api.whatsapp.com/send?phone=5544997357138&text=sua%20mensagem

				preencherLista();
				criarNovoObjeto();
				FecharDialog.fechaDialogOrcamento();
				FecharDialog.fechaDialogFinalizaOrcamento();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				System.out.println("apenas finalizando a vendaaaaa");
			}
		} else {

			orcamento.setDataOrcamento(dataFormatada);
			orcamento.setFuncionario(funcionario);
			orcamento.setSituacao("pendente");
			orcamento.setStatus(true);
			orcamento.setValorTotalOrcamento(valotTotal);
			orcamento.setLucroOrcamento(lucroCalculado.subtract(valorDesconto));
			orcamento.setDesconto(valorDesconto);
			orcamento.setSubTotalOrcamento(valotTotalFecha);

			orcamentoService.inserirAlterar(orcamento);

			listProdutoAdicionado.forEach(itensOrcamento -> {

				itensOrcamento.setOrcamento(orcamento);
				orcamentoProdutoService.inserirAlterar(itensOrcamento);
				itensOrcamento = new OrcamentoProdutos();

			});

			criarNovoObjeto();
			preencherLista();
			FecharDialog.fechaDialogOrcamento();
			FecharDialog.fechaDialogFinalizaOrcamento();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			System.out.println("apeans salvando o orçamento");
			controleVendaOrcamento = 0;
		}

		for (OrcamentoProdutos o : listProdutoAdicionado) {

			BigDecimal valor = o.getSubTotal().subtract(new BigDecimal(o.getQuantidade()), MathContext.DECIMAL32);
			BigDecimal aArredondado = valor.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);
			o.setValorUnitario(aArredondado);
			orcamentoProdutoService.inserirAlterar(o);
			o = new OrcamentoProdutos();

		}

	}

	public BigDecimal buscaValorOrcamento() {
		BigDecimal valorTotal = new BigDecimal(0);
		for (OrcamentoProdutos o : listProdutoAdicionado) {
			valorTotal = valorTotal.add(o.getValorUnitario());
		}
		return valorTotal;
	}

	public void controle() {
		controleAddTipo = false;

	}

	public List<Produto> completarProduto(String str) {
		List<Produto> pess = new ArrayList<>();
		pess = daoProduto.listarCodicaoLivre(Produto.class, " status is true");
		List<Produto> pessoasSelecionados = new ArrayList<>();
		for (Produto at : pess) {
			if (at.getNome().toLowerCase().startsWith(str)) {
				pessoasSelecionados.add(at);
			}
		}
		return pessoasSelecionados;
	}

	public List<Cliente> completarCliente(String str) {
		List<Cliente> pess = new ArrayList<>();
		pess = daoCliente.listarCodicaoLivre(Cliente.class, "statusCliente is true");
		List<Cliente> pessoasSelecionados = new ArrayList<>();
		for (Cliente at : pess) {
			if (at.getPessoa().getNome().toLowerCase().startsWith(str)) {
				pessoasSelecionados.add(at);
			}
		}
		return pessoasSelecionados;
	}

	public void criarNovoObjeto() {
		orcamento = new Orcamento();
		produto = new Produto();
		listProdutoAdicionado.clear();
		escondeClienteComplete = false;
		valotTotal = new BigDecimal(0);
		valotTotalFecha = new BigDecimal(0);
		lucroCalculado = new BigDecimal(0);
		listProdutoAlteraValor = new ArrayList<>();
	}

	public void carregarLista() {

	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<OrcamentoProdutos> getListProdutoAdicionado() {
		return listProdutoAdicionado;
	}

	public void setListProdutoAdicionado(List<OrcamentoProdutos> listProdutoAdicionado) {
		this.listProdutoAdicionado = listProdutoAdicionado;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public boolean isControleAddTipo() {
		return controleAddTipo;
	}

	public void setControleAddTipo(boolean controleAddTipo) {
		this.controleAddTipo = controleAddTipo;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public BigDecimal getValotTotal() {
		return valotTotal;
	}

	public void setValotTotal(BigDecimal valotTotal) {
		this.valotTotal = valotTotal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getValotTotalFecha() {
		return valotTotalFecha;
	}

	public void setValotTotalFecha(BigDecimal valotTotalFecha) {
		this.valotTotalFecha = valotTotalFecha;
	}

	public List<Orcamento> getListOrcamento() {
		return listOrcamento;
	}

	public void setListOrcamento(List<Orcamento> listOrcamento) {
		this.listOrcamento = listOrcamento;
	}

	public boolean isEscondeClienteComplete() {
		return escondeClienteComplete;
	}

	public void setEscondeClienteComplete(boolean escondeClienteComplete) {
		this.escondeClienteComplete = escondeClienteComplete;
	}

	public boolean isEsconderBotaoVenda() {
		return esconderBotaoVenda;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public void setEsconderBotaoVenda(boolean esconderBotaoVenda) {
		this.esconderBotaoVenda = esconderBotaoVenda;
	}

	public Venda getVendaParcela() {
		return vendaParcela;
	}

	public void setVendaParcela(Venda vendaParcela) {
		this.vendaParcela = vendaParcela;
	}

	public ParcelaVenda getParcelaVenda() {
		return parcelaVenda;
	}

	public void setParcelaVenda(ParcelaVenda parcelaVenda) {
		this.parcelaVenda = parcelaVenda;
	}

	public List<ParcelaVenda> getListParcelaVenda() {
		return listParcelaVenda;
	}

	public void setListParcelaVenda(List<ParcelaVenda> listParcelaVenda) {
		this.listParcelaVenda = listParcelaVenda;
	}

	public boolean isDesabilitaParcela() {
		return desabilitaParcela;
	}

	public void setDesabilitaParcela(boolean desabilitaParcela) {
		this.desabilitaParcela = desabilitaParcela;
	}

	public BigDecimal getValorMaoObra() {
		return valorMaoObra;
	}

	public void setValorMaoObra(BigDecimal valorMaoObra) {
		this.valorMaoObra = valorMaoObra;
	}

	public OrcamentoProdutos getOrcamentoPordutoAlterarValor() {
		return orcamentoPordutoAlterarValor;
	}

	public String getDataGerarParcela() {
		return dataGerarParcela;
	}

	public void setDataGerarParcela(String dataGerarParcela) {
		this.dataGerarParcela = dataGerarParcela;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public void setOrcamentoPordutoAlterarValor(OrcamentoProdutos orcamentoPordutoAlterarValor) {
		this.orcamentoPordutoAlterarValor = orcamentoPordutoAlterarValor;
	}

	public boolean isSomeSalva() {
		return someSalva;
	}

	public void setSomeSalva(boolean someSalva) {
		this.someSalva = someSalva;
	}

}
