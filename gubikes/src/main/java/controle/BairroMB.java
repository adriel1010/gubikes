package controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Bairro;
import modelo.Cidade;
import service.BairroService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCadastro;
import dao.GenericDAO;

@ViewScoped
@Named("bairroMB")
public class BairroMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Bairro bairro;
	private List<Bairro> listBairro;
	private List<Cidade> listCidade;

	@Inject
	private GenericDAO<Cidade> daoCidade;

	@Inject
	private GenericDAO<Bairro> daoBairro;

	@Inject
	private BairroService bairroService;

	@Inject
	private ValidaCadastro validaCadastro;

	@PostConstruct
	public void inicializar() {

		bairro = new Bairro();
		listCidade = daoCidade.listaComStatus(Cidade.class);
		listBairro = daoBairro.listaComStatus(Bairro.class);

	}

	public void preencherLista(Bairro t) {
		this.bairro = t;

	}

	public void inativarBairro(Bairro t) {
		bairroService.update(" Bairro set status = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {
		
		bairro.setNome(bairro.getNome().toUpperCase());
		try {
			if (bairro.getId() == null) {
				if (validaCadastro.buscarBairro(bairro)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTRADOCIDADE);
				} else {
					bairro.setStatus(true);
					bairroService.inserirAlterar(bairro);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogBairro();
					carregarLista();
				}
			} else {
				if (validaCadastro.buscarBairro(bairro) && validaCadastro.buscarBairroAlterar(bairro)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTRADOCIDADE);
				} else {
					bairro.setStatus(true);
					bairroService.inserirAlterar(bairro);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogBairro();
					carregarLista();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public void criarNovoObjeto() {
		bairro = new Bairro();

	}

	public void carregarLista() {
		listBairro = daoBairro.listaComStatus(Bairro.class);
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public List<Bairro> getListBairro() {
		return listBairro;
	}

	public void setListBairro(List<Bairro> listBairro) {
		this.listBairro = listBairro;
	}

	public GenericDAO<Bairro> getDaoBairro() {
		return daoBairro;
	}

	public void setDaoBairro(GenericDAO<Bairro> daoBairro) {
		this.daoBairro = daoBairro;
	}

	public List<Cidade> getListCidade() {
		return listCidade;
	}

	public void setListCidade(List<Cidade> listCidade) {
		this.listCidade = listCidade;
	}

}
