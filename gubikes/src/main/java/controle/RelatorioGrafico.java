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

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import dao.GenericDAO;
import modelo.Orcamento;
import modelo.OrcamentoProdutos;
import modelo.Venda;
import util.ExibirMensagem;

@ViewScoped
@Named("relatorioGraficoMB")
public class RelatorioGrafico implements Serializable {

	private static final long serialVersionUID = 1L;

	private LineChartModel areaModel;
	private String dataFechamento;
	private boolean sumirGrafico = false;
	List<Venda> listaVendas = new ArrayList<>();
	BigDecimal valorJaneiro = new BigDecimal(0);
	BigDecimal valorFev = new BigDecimal(0);
	BigDecimal valorMar = new BigDecimal(0);
	BigDecimal valorAbri = new BigDecimal(0);
	BigDecimal valorMaio = new BigDecimal(0);
	BigDecimal valorJui = new BigDecimal(0);
	BigDecimal valorJul = new BigDecimal(0);
	BigDecimal valorAgo = new BigDecimal(0);
	BigDecimal valorSet = new BigDecimal(0);
	BigDecimal valorOut = new BigDecimal(0);
	BigDecimal valorNov = new BigDecimal(0);
	BigDecimal valorDez = new BigDecimal(0);
	double totalPrazoJaneiro = 0., totalVistaJaneiro = 0., qtdJan = 0.;
	double totalPrazoFev = 0., totalVistaFev = 0., qtdFev = 0.;
	double totalPrazoMar = 0., totalVistaMar = 0., qtdMar = 0.;
	double totalPrazoAbr = 0., totalVistaAbr = 0., qtdAbr = 0.;
	double totalPrazoMai = 0., totalVistaMai = 0., qtdMai = 0.;
	double totalPrazoJui = 0., totalVistaJui = 0., qtdJui = 0.;
	double totalPrazoJul = 0., totalVistaJul = 0., qtdJul = 0.;
	double totalPrazoAgo = 0., totalVistaAgo = 0., qtdAgo = 0.;
	double totalPrazoSet = 0., totalVistaSet = 0., qtdSet = 0.;
	double totalPrazoOut = 0., totalVistaOut = 0., qtdOut = 0.;
	double totalPrazoNov = 0., totalVistaNov = 0., qtdNov = 0.;
	double totalPrazoDez = 0., totalVistaDez = 0., qtdDez = 0.;
	double porcentAvistaJan = 0., porcentAprazoJan = 0.;
	double porcentAvistaFev = 0., porcentAprazoFev = 0.;
	double porcentAvistaMar = 0., porcentAprazoMar = 0.;
	double porcentAvistaAbr = 0., porcentAprazoAbr = 0.;
	double porcentAvistaMai = 0., porcentAprazoMai = 0.;
	double porcentAvistaJui = 0., porcentAprazoJui = 0.;
	double porcentAvistaJul = 0., porcentAprazoJul = 0.;
	double porcentAvistaAgo = 0., porcentAprazoAgo = 0.;
	double porcentAvistaSet = 0., porcentAprazoSet = 0.;
	double porcentAvistaOut = 0., porcentAprazoOut = 0.;
	double porcentAvistaNov = 0., porcentAprazoNov = 0.;
	double porcentAvistaDez = 0., porcentAprazoDez = 0.;

	private String data;
	private List<OrcamentoProdutos> listOrcamentoProdutosVender;

	@Inject
	private GenericDAO<Venda> daoVenda;

	@Inject
	private EntityManager manager;

	@PostConstruct
	public void inicializar() {

		verificaMontagem();

	}

	public void limpar() {
		listaVendas.clear();
		valorJaneiro = new BigDecimal(0);
		valorFev = new BigDecimal(0);
		valorMar = new BigDecimal(0);
		valorAbri = new BigDecimal(0);
		valorMaio = new BigDecimal(0);
		valorJui = new BigDecimal(0);
		valorJul = new BigDecimal(0);
		valorAgo = new BigDecimal(0);
		valorSet = new BigDecimal(0);
		valorOut = new BigDecimal(0);
		valorNov = new BigDecimal(0);
		valorDez = new BigDecimal(0);
		totalPrazoJaneiro = 0.;
		totalVistaJaneiro = 0.;
		qtdJan = 0.;
		totalPrazoFev = 0.;
		totalVistaFev = 0.;
		qtdFev = 0.;
		totalPrazoMar = 0.;
		totalVistaMar = 0.;
		qtdMar = 0.;
		totalPrazoAbr = 0.;
		totalVistaAbr = 0.;
		qtdAbr = 0.;
		totalPrazoMai = 0.;
		totalVistaMai = 0.;
		qtdMai = 0.;
		totalPrazoJui = 0.;
		totalVistaJui = 0.;
		qtdJui = 0.;
		totalPrazoJul = 0.;
		totalVistaJul = 0.;
		qtdJul = 0.;
		totalPrazoAgo = 0.;
		totalVistaAgo = 0.;
		qtdAgo = 0.;
		totalPrazoSet = 0.;
		totalVistaSet = 0.;
		qtdSet = 0.;
		totalPrazoOut = 0.;
		totalVistaOut = 0.;
		qtdOut = 0.;
		totalPrazoNov = 0.;
		totalVistaNov = 0.;
		qtdNov = 0.;
		totalPrazoDez = 0.;
		totalVistaDez = 0.;
		qtdDez = 0.;
		porcentAvistaJan = 0.;
		porcentAprazoJan = 0.;
		porcentAvistaFev = 0.;
		porcentAprazoFev = 0.;
		porcentAvistaMar = 0.;
		porcentAprazoMar = 0.;
		porcentAvistaAbr = 0.;
		porcentAprazoAbr = 0.;
		porcentAvistaMai = 0.;
		porcentAprazoMai = 0.;
		porcentAvistaJui = 0.;
		porcentAprazoJui = 0.;
		porcentAvistaJul = 0.;
		porcentAprazoJul = 0.;
		porcentAvistaAgo = 0.;
		porcentAprazoAgo = 0.;
		porcentAvistaSet = 0.;
		porcentAprazoSet = 0.;
		porcentAvistaOut = 0.;
		porcentAprazoOut = 0.;
		porcentAvistaNov = 0.;
		porcentAprazoNov = 0.;
		porcentAvistaDez = 0.;
		porcentAprazoDez = 0.;
	}

	public void verificaMontagem() {

		if (dataFechamento == null) {
			sumirGrafico = false; 
		} else {
			sumirGrafico = true;
			limpar();
			listaVendas = daoVenda.listar(Venda.class, " ano = '" + dataFechamento + "'");
 
			
			if (listaVendas.size() > 0) {

				for (Venda v : listaVendas) {

					if (v.getMes().equals("01")) {
						valorJaneiro = valorJaneiro.add(v.getOrcamento().getSubTotalOrcamento());
						qtdJan += 1;
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoJaneiro += 1;
						} else {
							totalVistaJaneiro += 1;
						}
					}

					else if (v.getMes().equals("02")) {
						valorFev = valorFev.add(v.getOrcamento().getSubTotalOrcamento());
						qtdFev += 1;
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoFev += 1;
						} else {
							totalVistaFev += 1;
						}
					}

					else if (v.getMes().equals("03")) {
						valorMar = valorMar.add(v.getOrcamento().getSubTotalOrcamento());
						qtdMar += 1;
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoMar += 1;
						} else {
							totalVistaMar += 1;
						}
					}

					else if (v.getMes().equals("04")) {
						qtdAbr += 1;
						valorAbri = valorAbri.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoAbr += 1;
						} else {
							totalVistaAbr += 1;
						}
					}

					else if (v.getMes().equals("05")) {
						qtdMai += 1;
						valorMaio = valorMaio.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoMai += 1;
						} else {
							totalVistaMai += 1;
						}
					}

					else if (v.getMes().equals("06")) {
						qtdJui += 1;
						valorJui = valorJui.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoJui += 1;
						} else {
							totalVistaJui += 1;
						}
					}

					else if (v.getMes().equals("07")) {
						qtdJul += 1;
						valorJul = valorJul.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoJul += 1;
						} else {
							totalVistaJul += 1;
						}
					}

					else if (v.getMes().equals("08")) {
						qtdAgo += 1;
						valorAgo = valorAgo.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoAgo += 1;
						} else {
							totalVistaAgo += 1;
						}
					}

					else if (v.getMes().equals("09")) {
						qtdSet += 1;
						valorSet = valorSet.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoSet += 1;
						} else {
							totalVistaSet += 1;
						}
					}

					else if (v.getMes().equals("10")) {
						qtdOut += 1;
						valorOut = valorOut.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoOut += 1;
						} else {
							totalVistaOut += 1;
						}
					}

					else if (v.getMes().equals("11")) {
						qtdNov += 1;
						valorNov = valorNov.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoNov += 1;
						} else {
							totalVistaNov += 1;
						}
					}

					else if (v.getMes().equals("12")) {
						qtdDez += 1;
						valorDez = valorDez.add(v.getOrcamento().getSubTotalOrcamento());
						if (v.getSituacao().equals("A Prazo")) {
							totalPrazoDez += 1;
						} else {
							totalVistaDez += 1;
						}
					}

				}

				if (qtdJan > 0) {
					porcentAprazoJan = (100 * totalPrazoJaneiro) / qtdJan;
					porcentAvistaJan = (100 * totalVistaJaneiro) / qtdJan;
				 
				}
				if (qtdFev > 0) {

					porcentAprazoFev = (100 * totalPrazoFev) / qtdFev;
					porcentAvistaFev = (100 * totalVistaFev) / qtdFev;
					 
				}

				if (qtdMar > 0) {
					porcentAprazoMar = (100 * totalPrazoMar) / qtdMar;
					porcentAvistaMar = (100 * totalVistaMar) / qtdMar;
					 

				}

				if (qtdAbr > 0) {
					porcentAprazoAbr = (100 * totalPrazoAbr) / qtdAbr;
					porcentAvistaAbr = (100 * totalVistaAbr) / qtdAbr;

					 

				}

				if (qtdMai > 0) {
					porcentAprazoMai = (100 * totalPrazoMai) / qtdMai;
					porcentAvistaMai = (100 * totalVistaMai) / qtdMai;

					 
				}

				if (qtdJui > 0) {
					porcentAprazoJui = (100 * totalPrazoJui) / qtdJui;
					porcentAvistaJui = (100 * totalVistaJui) / qtdJui;

					 
				}

				if (qtdJul > 0) {
					porcentAprazoJul = (100 * totalPrazoJul) / qtdJul;
					porcentAvistaJul = (100 * totalVistaJul) / qtdJul;

					 

				}
				if (qtdAgo > 0) {

					porcentAprazoAgo = (100 * totalPrazoAgo) / qtdAgo;
					porcentAvistaAgo = (100 * totalVistaAgo) / qtdAgo;

				 

				}

				if (qtdSet > 0) {
					porcentAprazoSet = (100 * totalPrazoSet) / qtdSet;
					porcentAvistaSet = (100 * totalVistaSet) / qtdSet;

				}

				if (qtdOut > 0) {

					porcentAprazoOut = (100 * totalPrazoOut) / qtdOut;
					porcentAvistaOut = (100 * totalVistaOut) / qtdOut;

				}

				if (qtdNov > 0) {

					porcentAprazoNov = (100 * totalPrazoNov) / qtdNov;
					porcentAvistaNov = (100 * totalVistaNov) / qtdNov;

				}

				if (qtdDez > 0) {
					porcentAprazoDez = (100 * totalPrazoDez) / qtdDez;
					porcentAvistaDez = (100 * totalVistaDez) / qtdDez;

				}

				createAreaModel();

			}

			else {
				sumirGrafico = false;
				ExibirMensagem.exibirMensagem("Vendas não encontradas");
			}

		}

	}

	public void createAreaModel() {

		areaModel = new LineChartModel();

		LineChartSeries aprazo = new LineChartSeries();
		aprazo.setFill(true);
		aprazo.setLabel("a Prazo");
		aprazo.set("Jan - " + valorJaneiro, porcentAprazoJan);
		aprazo.set("Fev - " + valorFev, porcentAprazoFev);
		aprazo.set("Mar  - " + valorMar, porcentAprazoMar);
		aprazo.set("Abr - " + valorAbri, porcentAprazoAbr);
		aprazo.set("Mai - " + valorMaio, porcentAprazoMai);
		aprazo.set("Jun - " + valorJui, porcentAprazoJui);
		aprazo.set("Jul - " + valorJul, porcentAprazoJul);
		aprazo.set("Ago - " + valorAgo, porcentAprazoAgo);
		aprazo.set("Set - " + valorSet, porcentAprazoSet);
		aprazo.set("Out - " + valorOut, porcentAprazoOut);
		aprazo.set("Nov - " + valorNov, porcentAprazoNov);
		aprazo.set("Dez - " + valorDez, porcentAprazoDez);

		LineChartSeries avista = new LineChartSeries();

		avista.setFill(true);
		avista.setLabel("a Vista");
		avista.set("Jan - " + valorJaneiro, porcentAvistaJan);
		avista.set("Fev - " + valorFev, porcentAvistaFev);
		avista.set("Mar - " + valorMar, porcentAvistaMar);
		avista.set("Abr - " + valorAbri, porcentAvistaAbr);
		avista.set("Mai - " + valorMaio, porcentAvistaMai);
		avista.set("Jun - " + valorJui, porcentAvistaJui);
		avista.set("Jul - " + valorJul, porcentAvistaJul);
		avista.set("Ago - " + valorAgo, porcentAvistaAgo);
		avista.set("Set - " + valorSet, porcentAvistaSet);
		avista.set("Out - " + valorOut, porcentAvistaOut);
		avista.set("Nov - " + valorNov, porcentAvistaNov);
		avista.set("Dez - " + valorDez, porcentAvistaDez);

		areaModel.addSeries(aprazo);
		areaModel.addSeries(avista);

		areaModel.setTitle("Porcentagem de vendas a prazo e a vista");
		areaModel.setLegendPosition("ne");
		areaModel.setStacked(true);
		areaModel.setShowPointLabels(true);
		areaModel.setShowDatatip(true);
		areaModel.setAnimate(true); 

		Axis xAxis = new CategoryAxis(dataFechamento);
		areaModel.getAxes().put(AxisType.X, xAxis);
		Axis yAxis = areaModel.getAxis(AxisType.Y);
		yAxis.setLabel("TOTAL");
		yAxis.setMin(0);
		yAxis.setMax(150);

	}

	public LineChartModel getAreaModel() {
		return areaModel;
	}

	public String getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(String dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public boolean isSumirGrafico() {
		return sumirGrafico;
	}

	public void setSumirGrafico(boolean sumirGrafico) {
		this.sumirGrafico = sumirGrafico;
	}

	public String getDatatipFormatIntegers() {
		return "<span style=\"display:none;\">%s</span><span>%s</span>";
	}

}
