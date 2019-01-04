package controle;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import service.OrcamentoProdutoService;
import service.OrcamentoService;
import service.ParcelaVendaService;
import service.ProdutoService;
import service.VendaService;

@ViewScoped
@Named("baixaParcelaMB")
public class BaixaParcelaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private ParcelaVenda parcela;
	private BigDecimal valorEntrada;
	private ParcelaVenda parcelaGerarDuplicatas;
	private Venda parcelaBaixaVenda;
	private List<ParcelaVenda> listParcelaVenda;
	private List<ParcelaVenda> listParcelaVendaGerar;
	private List<Venda> listVendaPraso;
	private List<ParcelaVenda> listParcelaVendaBaixar;
	private BigDecimal valorPago = new BigDecimal(0);
	private BigDecimal valorTroco = new BigDecimal(0);
	private boolean sumirReparcelar;
	private String dataGerarParcela;
	private boolean desabilitaParcela = true;
	private ParcelaVenda parc;

	@Inject
	private GenericDAO<ParcelaVenda> daoParcelaVenda;

	@Inject
	private GenericDAO<Venda> daoVenda;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private ParcelaVendaService parcelaVendaService;

	@Inject
	private VendaService vendaService;

	@PostConstruct
	public void inicializar() {
		parcela = new ParcelaVenda();
		parcelaBaixaVenda = new Venda();
		listParcelaVenda = new ArrayList<>();
		listVendaPraso = new ArrayList<>();
		listParcelaVendaBaixar = new ArrayList<>();
		listParcelaVendaGerar = new ArrayList<>();
		parc = new ParcelaVenda();
		preencherLista();
	}

	public void preencherLista() {
		listVendaPraso = daoVenda.listarCodicaoLivre(Venda.class,
				" status is true and situacao = 'A PRAZO' and situacaoPago = 'devendo'");
	}

	public void carregarDados(Venda parcela) {
		parcelaBaixaVenda = parcela;
		listParcelaVendaBaixar = daoParcelaVenda.listar(ParcelaVenda.class, " venda = '" + parcela.getId() + "'");
		this.parcela = new ParcelaVenda();
		valorPago = new BigDecimal(0);
		parcelaGerarDuplicatas = new ParcelaVenda();
		valorEntrada = parcela.getEntradaValor();
		dataGerarParcela = null;
		desabilitaParcela = true;
		if(listParcelaVendaBaixar.size() == 0)
			sumirReparcelar = true;
		else
			sumirReparcelar = false;
	}

	public void carregar(Venda parcela) {
		listParcelaVendaBaixar = daoParcelaVenda.listar(ParcelaVenda.class, " venda = '" + parcela.getId() + "'");
	}
	
	public void insereNova(int valor,BigDecimal valorParcela){
		System.out.println("nbo inserir nova");
		ParcelaVenda p = new ParcelaVenda();
		p.setData(new Date());
		p.setNumeroParcela(valor);
		p.setQuantidadeParcelas(parcela.getQuantidadeParcelas());
		p.setSituacao("RESTANTE");
		p.setStatus(true);
		p.setValorParcela(valorParcela);
		p.setValorTotalVenda(parcela.getValorTotalVenda());
		p.setVenda(parcela.getVenda());
		
		parcelaVendaService.inserirAlterar(p);
	}
	
	public void baixarParcela() {

		List<ParcelaVenda> listParcela = daoParcelaVenda.listar(ParcelaVenda.class, " venda ='"+parcela.getVenda().getId()+"'");
		
		BigDecimal valor = valorPago.subtract(parcela.getValorParcela());
		int contador = 0;
		if (valor.doubleValue() < 0) {

			BigDecimal val = valor.multiply(new BigDecimal(-1));

			parcela.setDataPagamento(new Date());
			parcela.setSituacao("PAGO");
			parcela.setValorParcela(valorPago);
			parcelaVendaService.inserirAlterar(parcela);

			insereNova(listParcela.size()+1, val);

			
			
			carregar(parcela.getVenda());

		} else {

			parcela.setDataPagamento(new Date());
			parcela.setSituacao("PAGO");
			parcelaVendaService.inserirAlterar(parcela);

			carregar(parcela.getVenda());

			for (ParcelaVenda p : listParcelaVendaBaixar) {
				if (p.getSituacao().equals("PAGO") && p.getDataPagamento() != null) {
					contador += 1;
				}
			}

			int valorLista = listParcelaVendaBaixar.size();
			if (contador == listParcelaVendaBaixar.size()) {

				System.out.println("deve alterar a venda ");
 

					Venda venda = new Venda();
					venda = parcela.getVenda();
					venda.setSituacaoPago("pago");

					vendaService.inserirAlterar(venda);
					FecharDialog.fechaDialogParcela();
			}

		}
		contador = 0;
		preencherLista();
		FecharDialog.fechaDialogBaixa();

	}
	
	
	
	
	public void gerarDuplicatas() {

		listParcelaVendaGerar.clear();
		desabilitaParcela = false;
		BigDecimal valorParcelado = new BigDecimal(0);
		BigDecimal valorSubtrai = new BigDecimal(0);
		int contadorNumeroParcela = 1;

		valorSubtrai = parcelaBaixaVenda.getOrcamento().getSubTotalOrcamento().subtract(valorEntrada);
		valorParcelado = valorSubtrai.divide(new BigDecimal(parcelaGerarDuplicatas.getQuantidadeParcelas()),
				MathContext.DECIMAL32);
		BigDecimal aArredondado = valorParcelado.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);

		for (int i = 1; i <= parcelaGerarDuplicatas.getQuantidadeParcelas(); i++) {

			adicionaParcela(aArredondado, contadorNumeroParcela);
			contadorNumeroParcela += 1;
		}

		parcelaBaixaVenda.setEntradaValor(valorEntrada);
		
	}
	
	
	public void adicionaParcela(BigDecimal aArredondado, int numeroParcela) {

		parc.setQuantidadeParcelas(parcelaGerarDuplicatas.getQuantidadeParcelas());
		parc.setSituacao("pendente");
		parc.setStatus(true);
		parc.setValorTotalVenda(parcelaBaixaVenda.getOrcamento().getSubTotalOrcamento());
		parc.setNumeroParcela(numeroParcela);
		parc.setVenda(parcelaBaixaVenda);
		parc.setValorParcela(aArredondado);

		String[] dataSeparada = dataGerarParcela.split("/");
		LocalDate hoje = LocalDate.of(Integer.parseInt(dataSeparada[2]), Integer.parseInt(dataSeparada[1]),
				Integer.parseInt(dataSeparada[0]));
		LocalDate mes = hoje.plusMonths(numeroParcela);
		Date dataInse = java.sql.Date.valueOf(mes);
		parc.setData(dataInse);
		listParcelaVendaGerar.add(parc);
		criarNovaParcela();

	}
	
	public void criarNovaParcela() {
		parc = new ParcelaVenda();
	}
	
	public void salvarDuplicatas() {
		
		vendaService.inserirAlterar(parcelaBaixaVenda);
		valorEntrada = new BigDecimal(0);

		
		for (ParcelaVenda p : listParcelaVendaGerar) {
			parcelaVendaService.inserirAlterar(p);
		}
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fechaDialogParcelaCorrecao();
		FecharDialog.fechaDialogParcelaCorrecaoVer();
		preencherLista();

	}
	
	public void limpa(){
		listParcelaVendaGerar.clear();
	}
	
	


//	public void baixarParcela() {
//
//		BigDecimal valor = valorPago.subtract(parcela.getValorParcela());
//		int contador = 0;
//		if (valor.doubleValue() < 0) {
//
//			BigDecimal val = valor.multiply(new BigDecimal(-1));
//
//			parcela.setDataPagamento(new Date());
//			parcela.setSituacao("RESTANTE");
//			parcela.setValorParcela(val);
//			parcelaVendaService.inserirAlterar(parcela);
//
//			carregar(parcela.getVenda());
//
//		} else {
//
//			parcela.setDataPagamento(new Date());
//			parcela.setSituacao("PAGO");
//			parcelaVendaService.inserirAlterar(parcela);
//
//			carregar(parcela.getVenda());
//
//			for (ParcelaVenda p : listParcelaVendaBaixar) {
//				if (p.getSituacao().equals("PAGO") && p.getDataPagamento() != null) {
//					contador += 1;
//				}
//			}
//
//			int valorLista = listParcelaVendaBaixar.size();
//			if (contador == listParcelaVendaBaixar.size()) {
//
//				System.out.println("deve alterar a venda ");
// 
//
//					Venda venda = new Venda();
//					venda = parcela.getVenda();
//					venda.setSituacaoPago("pago");
//
//					vendaService.inserirAlterar(venda);
//					FecharDialog.fechaDialogParcela();
//			}
//
//		}
//		contador = 0;
//		preencherLista();
//		FecharDialog.fechaDialogBaixa();
//
//	}

	public ParcelaVenda getParcela() {
		return parcela;
	}

	public void setParcela(ParcelaVenda parcela) {
		this.parcela = parcela;
	}

	public List<ParcelaVenda> getListParcelaVenda() {
		return listParcelaVenda;
	}

	public void setListParcelaVenda(List<ParcelaVenda> listParcelaVenda) {
		this.listParcelaVenda = listParcelaVenda;
	}

	public List<ParcelaVenda> getListParcelaVendaBaixar() {
		return listParcelaVendaBaixar;
	}

	public void setListParcelaVendaBaixar(List<ParcelaVenda> listParcelaVendaBaixar) {
		this.listParcelaVendaBaixar = listParcelaVendaBaixar;
	}

	public Venda getParcelaBaixaVenda() {
		return parcelaBaixaVenda;
	}

	public void setParcelaBaixaVenda(Venda parcelaBaixaVenda) {
		this.parcelaBaixaVenda = parcelaBaixaVenda;
	}

	public List<Venda> getListVendaPraso() {
		return listVendaPraso;
	}

	public void setListVendaPraso(List<Venda> listVendaPraso) {
		this.listVendaPraso = listVendaPraso;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public BigDecimal getValorTroco() {
		return valorTroco;
	}

	public void setValorTroco(BigDecimal valorTroco) {
		this.valorTroco = valorTroco;
	}

	public boolean isSumirReparcelar() {
		return sumirReparcelar;
	}

	public void setSumirReparcelar(boolean sumirReparcelar) {
		this.sumirReparcelar = sumirReparcelar;
	}

	public ParcelaVenda getParcelaGerarDuplicatas() {
		return parcelaGerarDuplicatas;
	}

	public void setParcelaGerarDuplicatas(ParcelaVenda parcelaGerarDuplicatas) {
		this.parcelaGerarDuplicatas = parcelaGerarDuplicatas;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public String getDataGerarParcela() {
		return dataGerarParcela;
	}

	public void setDataGerarParcela(String dataGerarParcela) {
		this.dataGerarParcela = dataGerarParcela;
	}

	public List<ParcelaVenda> getListParcelaVendaGerar() {
		return listParcelaVendaGerar;
	}

	public void setListParcelaVendaGerar(List<ParcelaVenda> listParcelaVendaGerar) {
		this.listParcelaVendaGerar = listParcelaVendaGerar;
	}

	public boolean isDesabilitaParcela() {
		return desabilitaParcela;
	}

	public void setDesabilitaParcela(boolean desabilitaParcela) {
		this.desabilitaParcela = desabilitaParcela;
	}
	
	

}
