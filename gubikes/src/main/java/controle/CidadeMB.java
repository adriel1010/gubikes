package controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
import service.CidadeService;

@ViewScoped
@Named("cidadeMB")
public class CidadeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cidade cidade;
	private List<Cidade> listCidade;

	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	@Inject
	private GenericDAO<Cidade> daoCidade; // faz as buscas

	@Inject
	private CidadeService cidadeService; // inserir no banco

	@Inject
	private ValidaCadastro validaCadastro;

	@PostConstruct
	public void inicializar() {

		cidade = new Cidade();

		listCidade = daoCidade.listaComStatus(Cidade.class);

	}

	public void preencherLista(Cidade t) {
		this.cidade = t;

	}

	public void inativarCidade(Cidade t) {
		cidadeService.update(" Cidade set status = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {
		cidade.setNome(cidade.getNome().toUpperCase());
		try {
			if (cidade.getId() == null) {
				if (validaCadastro.buscarCidade(cidade)) {
					ExibirMensagem.exibirMensagem(Mensagem.CIDADE);
				} else {
					cidade.setStatus(true);
					cidadeService.inserirAlterar(cidade);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogCidade();
					carregarLista();
				}
			} else {
				if (validaCadastro.buscarCidade(cidade) && validaCadastro.buscarCidadeAlterar(cidade)) {
					ExibirMensagem.exibirMensagem(Mensagem.CIDADE);
				} else {
					cidade.setStatus(true);
					cidadeService.inserirAlterar(cidade);
					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogCidade();
					carregarLista();
				}

			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public void criarNovoObjeto() {
		cidade = new Cidade();
	}

	public void carregarLista() {
		listCidade = daoCidade.listaComStatus(Cidade.class);
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<Cidade> getListCidade() {
		return listCidade;
	}

	public void setListCidade(List<Cidade> listCidade) {
		this.listCidade = listCidade;
	}

	public GenericDAO<Cidade> getDaoCidade() {
		return daoCidade;
	}

	public void setDaoCidade(GenericDAO<Cidade> daoCidade) {
		this.daoCidade = daoCidade;
	}

}
