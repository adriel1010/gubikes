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
import util.ValidacoesGerirUsuarios;
import dao.GenericDAO;
import modelo.Tipos;
import service.TipoService;


@ViewScoped
@Named("tipoServidorMB")
public class TipoServidorMB implements Serializable{
	
	private static final long serialVersionUID = 1L;


	private Tipos tipoServidor;
	private List<Tipos> tipoServidorBusca;
	private List<Tipos> listTipoServidor;
	
	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	
	@Inject
	private GenericDAO<Tipos> daoTipo; //faz as buscas
	
	@Inject
	private TipoService tipoService; // inserir no banco
	
	
	@PostConstruct
	public void inicializar() {
	
		tipoServidor = new Tipos();
	
		listTipoServidor = new ArrayList<>();
		listTipoServidor = daoTipo.listaComStatus(Tipos.class);
		tipoServidorBusca = new ArrayList<>();
		
	}

	public void preencherListaTipoServidor(Tipos t) {
		this.tipoServidor = t;

	}

	public void inativarTipoServidor(Tipos t) {
		tipoService.update(" Tipo set status = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {
			
			if (validacoesGerirUsuarios.buscarPermissao(tipoServidor) == true) {
				ExibirMensagem.exibirMensagem(Mensagem.TIPOCADASTRO);
			} else {

				if (tipoServidor.getId() == null) {
					tipoServidor.setStatus(true);
					tipoService.inserirAlterar(tipoServidor);

				} else {
					tipoServidor.setStatus(true);
					tipoService.inserirAlterar(tipoServidor);
				}

				criarNovoObjeto();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogTipoServidor();
				carregarLista();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public void criarNovoObjeto() {
		tipoServidor = new Tipos();
	}

	public void carregarLista() {
		listTipoServidor = daoTipo.listaComStatus(Tipos.class);
	}

	public List<Tipos> getListTipoServidor() {
		return listTipoServidor;
	}

	public void setListTipoServidor(List<Tipos> listTipoServidor) {
		this.listTipoServidor = listTipoServidor;
	}

	public Tipos getTipoServidor() {
		return tipoServidor;
	}

	public void setTipoServidor(Tipos tipoServidor) {
		this.tipoServidor = tipoServidor;
	}

	public List<Tipos> getTipoServidorBusca() {
		return tipoServidorBusca;
	}

	public void setTipoServidorBusca(List<Tipos> tipoServidorBusca) {
		this.tipoServidorBusca = tipoServidorBusca;
	}
}

