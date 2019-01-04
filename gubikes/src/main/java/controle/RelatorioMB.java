package controle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import util.ChamarRelatorio;
import util.ExibirMensagem;
import util.Mensagem;
import dao.GenericDAO;
import modelo.Cliente;
import modelo.Orcamento;
import modelo.OrcamentoProdutos;
import modelo.ParcelaVenda;
import modelo.Produto;
import modelo.Venda;

@ViewScoped
@Named("relatorioMB")
public class RelatorioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private String data;
	private BigDecimal valorEmPecas;
	private String mesVendas;
	private String mesVendasSemana;

	private String dataFechamento;
	private String dataBuscar;

	private List<OrcamentoProdutos> listOrcamentoProdutos;
	
	private List<OrcamentoProdutos> listOrcamentoProdutosVender;

	@Inject
	private GenericDAO<Venda> daoVenda;

	@Inject
	private GenericDAO<Cliente> daoCliente;
	@Inject
	private GenericDAO<Produto> daoProduto;

	@Inject
	private GenericDAO<OrcamentoProdutos> daoOrcamentoProduto;

	@Inject
	private GenericDAO<Orcamento> daoOrcamento;

	private Cliente cliente;

	@Inject
	private GenericDAO<ParcelaVenda> daoParcelaVenda;

	@Inject
	private EntityManager manager;

	@PostConstruct
	public void inicializar() {
		cliente = new Cliente();
	}

	public void imprimirRelatorioDia() {

		System.out.println("data passada " + data);
		try {
			List<Venda> relatorio = daoVenda.listar(Venda.class, " dataVenda = '" + data + "'");
			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();
				parametro.put("DATAS", data);
				ChamarRelatorio ch = new ChamarRelatorio("vendas.jasper", parametro, "relatório de vendas " + data);
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void buscarVendas() {

		listOrcamentoProdutos = new ArrayList<>();

		List<Venda> listVenda = daoVenda.listar(Venda.class,
				" mes = '" + mesVendas.charAt(0) + "" + mesVendas.charAt(1) + "' and ano ='" + mesVendas.charAt(3) + ""
						+ mesVendas.charAt(4) + "" + mesVendas.charAt(5) + "" + mesVendas.charAt(6) + "'");

		if (listVenda.size() == 0) {
			ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
		} else {
			for (Venda v : listVenda) {

				listOrcamentoProdutos.addAll(daoOrcamentoProduto.listar(OrcamentoProdutos.class,
						" orcamento = '" + v.getOrcamento().getId() + "'"));
			}
		}
		valorEmPecas = new BigDecimal(0);
		for (OrcamentoProdutos o : listOrcamentoProdutos) {
			valorEmPecas = valorEmPecas.add(o.getValorUnitario().multiply(new BigDecimal(o.getQuantidade())));
		}

	}

	public void buscarVendasSemana() {

		listOrcamentoProdutos = new ArrayList<>();
 
		valorEmPecas = new BigDecimal(0);

		listOrcamentoProdutosVender = new ArrayList<>();
		for (int i = 0; i < 7; i++) {

			if (i > 0) {
				
				
				String dia = mesVendasSemana.charAt(0) + "" + mesVendasSemana.charAt(1);
				int data = Integer.parseInt(dia) - i;
				dataBuscar = data+""+mesVendasSemana.charAt(2)+""+mesVendasSemana.charAt(3)+""+mesVendasSemana.charAt(4)
				+""+mesVendasSemana.charAt(5)+""+mesVendasSemana.charAt(6)+""+mesVendasSemana.charAt(7)+""+mesVendasSemana.charAt(8)
				+""+mesVendasSemana.charAt(9);
				 
			}
			else{
				dataBuscar = mesVendasSemana;
			}

			
			System.out.println("busca para o dia "+dataBuscar);
			
			List<Venda> listVenda = daoVenda.listar(Venda.class, " dataVenda = '" + dataBuscar + "'");
			
			
			System.out.println("tamanho da lista para o dia "+dataBuscar + " "+listVenda.size()); 
			if (listVenda.size() > 0) {
				

				for (Venda v : listVenda) {

					System.out.println("busca as vendas "+v.getId() +" id orcamento "+v.getOrcamento().getId());
					
					listOrcamentoProdutosVender.addAll(daoOrcamentoProduto.listar(OrcamentoProdutos.class,
							" orcamento = '" + v.getOrcamento().getId() + "'"));
				}
 
			}
			

		}
		
		
		for (OrcamentoProdutos o : listOrcamentoProdutosVender) {
			valorEmPecas = valorEmPecas.add(o.getValorUnitario().multiply(new BigDecimal(o.getQuantidade())));
		}
		 

	}

	public void imprimirRelatorioContasReceber() {
		try {
			List<ParcelaVenda> relatorio = daoParcelaVenda.listar(ParcelaVenda.class, " situacao != 'PAGO'");
			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();

				ChamarRelatorio ch = new ChamarRelatorio("devendoContas.jasper", parametro,
						"relatório de contas a receber ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioValoresEstoque() {
		try {
			List<Produto> relatorio = daoProduto.listar(Produto.class, " quantidade != 0 ");
			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();

				ChamarRelatorio ch = new ChamarRelatorio("produtosValor.jasper", parametro,
						"relatório valores em estoque");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioReceber() {
		try {
			List<ParcelaVenda> relatorio = daoParcelaVenda.listar(ParcelaVenda.class,
					" venda.status is true and status is true and situacao != 'PAGO'");

			for (ParcelaVenda p : relatorio) {
				System.out.println("pessoas " + p.getVenda().getOrcamento().getCliente().getPessoa().getNome());
			}

			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();

				ChamarRelatorio ch = new ChamarRelatorio("contaRecebe.jasper", parametro, "relatório contas a receber");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioCliente() {

		try {
			List<Orcamento> relatorio = daoOrcamento.listar(Orcamento.class,
					" status is true and cliente='" + cliente.getId() + "'");
			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();
				parametro.put("DATAS", cliente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("vendaClienteTodas.jasper", parametro,
						"relatório de Cliente " + cliente.getPessoa().getNome());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioComprasCliente() {

		try {
			List<Orcamento> relatorio = daoOrcamento.listar(Orcamento.class,
					" status is true and cliente='" + cliente.getId() + "'");
			if (relatorio.size() > 0) {

				HashMap parametro = new HashMap<>();
				parametro.put("DATAS", cliente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("vendaClienteTodasSLucro.jasper", parametro,
						"relatório de Cliente " + cliente.getPessoa().getNome());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioCliente(Venda venda) {

		try {
			HashMap parametro = new HashMap<>();
			parametro.put("IDCLIENTE", venda.getId());
			ChamarRelatorio ch = new ChamarRelatorio("vendasCliente.jasper", parametro,
					"relatório de vendas Cliente :" + venda.getOrcamento().getCliente().getPessoa().getNome());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioClienteParcelado(Venda venda) {

		try {
			HashMap parametro = new HashMap<>();
			parametro.put("IDCLIENTE", venda.getId());
			ChamarRelatorio ch = new ChamarRelatorio("vendasParcela.jasper", parametro,
					"relatório de par Cliente :" + venda.getOrcamento().getCliente().getPessoa().getNome());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioClientePromissoria(Venda venda) {

		try {
			HashMap parametro = new HashMap<>();
			parametro.put("IDORCAMENTOPASS", venda.getId());
			ChamarRelatorio ch = new ChamarRelatorio("vendasParcelaPromissoria.jasper", parametro,
					"relatório de promissórias :" + venda.getOrcamento().getCliente().getPessoa().getNome());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioOrcamento(Orcamento orcamento) {

		try {
			HashMap parametro = new HashMap<>();
			parametro.put("IDORCAMENTO", orcamento.getId());
			ChamarRelatorio ch = new ChamarRelatorio("orcamentoCliente.jasper", parametro,
					"Orçamento Cliente :" + orcamento.getCliente().getPessoa().getNome());
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioFechamentoMes() {

		try {

			List<Venda> relatorio = daoVenda.listar(Venda.class,
					" mes = '" + dataFechamento.charAt(0) + "" + dataFechamento.charAt(1) + "' and ano = '"
							+ dataFechamento.charAt(3) + "" + dataFechamento.charAt(4) + "" + dataFechamento.charAt(5)
							+ "" + dataFechamento.charAt(6) + "'");
			if (relatorio.size() > 0) {
				HashMap parametro = new HashMap<>();
				parametro.put("MES", dataFechamento.charAt(0) + "" + dataFechamento.charAt(1));
				parametro.put("ANO", dataFechamento.charAt(3) + "" + dataFechamento.charAt(4) + ""
						+ dataFechamento.charAt(5) + "" + dataFechamento.charAt(6));
				ChamarRelatorio ch = new ChamarRelatorio("fechamentoMes.jasper", parametro, "Fechamento de mês ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioFechamentoAno() {

		String ano = dataFechamento.charAt(0) + "" + dataFechamento.charAt(1) + "" + dataFechamento.charAt(2) + ""
				+ dataFechamento.charAt(3);

		try {

			List<Venda> relatorio = daoVenda.listar(Venda.class, " ano = '" + dataFechamento.charAt(0) + ""
					+ dataFechamento.charAt(1) + "" + dataFechamento.charAt(2) + "" + dataFechamento.charAt(3) + "'");
			if (relatorio.size() > 0) {
				HashMap parametro = new HashMap<>();
				parametro.put("ANO", ano);
				ChamarRelatorio ch = new ChamarRelatorio("fechamentoAno.jasper", parametro, "Fechamento de Ano ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
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

	public String getData() {
		return data;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(String dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public String getMesVendas() {
		return mesVendas;
	}
	
	

	public String getDataBuscar() {
		return dataBuscar;
	}

	public void setDataBuscar(String dataBuscar) {
		this.dataBuscar = dataBuscar;
	}

	public List<OrcamentoProdutos> getListOrcamentoProdutosVender() {
		return listOrcamentoProdutosVender;
	}

	public void setListOrcamentoProdutosVender(List<OrcamentoProdutos> listOrcamentoProdutosVender) {
		this.listOrcamentoProdutosVender = listOrcamentoProdutosVender;
	}

	public void setMesVendas(String mesVendas) {
		this.mesVendas = mesVendas;
	}

	public List<OrcamentoProdutos> getListOrcamentoProdutos() {
		return listOrcamentoProdutos;
	}

	public void setListOrcamentoProdutos(List<OrcamentoProdutos> listOrcamentoProdutos) {
		this.listOrcamentoProdutos = listOrcamentoProdutos;
	}

	public BigDecimal getValorEmPecas() {
		return valorEmPecas;
	}

	public void setValorEmPecas(BigDecimal valorEmPecas) {
		this.valorEmPecas = valorEmPecas;
	}

	public String getMesVendasSemana() {
		return mesVendasSemana;
	}

	public void setMesVendasSemana(String mesVendasSemana) {
		this.mesVendasSemana = mesVendasSemana;
	}

}
