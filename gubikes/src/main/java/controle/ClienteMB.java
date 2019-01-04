package controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelo.Bairro;
import modelo.Cidade;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Permissoes;
import modelo.Pessoas;
import modelo.Tipos;
import service.BairroService;
import service.ClienteService;
import service.FuncionarioService;
import service.PermissaoService;
import service.PessoaService;
import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCadastro;
import dao.GenericDAO;

@ViewScoped
@Named("clienteMB")
public class ClienteMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoas clientePessoa;
	private Permissoes permissao;
	private Funcionario funcionario;
	private Tipos tipos;
	private Cliente cliente;
	private List<Bairro> listBairro;
	private List<Cliente> listCliente;
	private List<Cliente> listClienteInativo;
	private List<Cidade> listCidade;
	private Cidade cidade;
	private List<Tipos> listPermissoes;
	private boolean controleBairro = true;
	private boolean controleAdd = false;
	private boolean controleAddTipo = true;

	@Inject
	private GenericDAO<Cidade> daoCidade;

	@Inject
	private GenericDAO<Cliente> daoCliente;

	@Inject
	private GenericDAO<Bairro> daoBairro;

	@Inject
	private GenericDAO<Tipos> daoTipos;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private ClienteService clientService;

	@Inject
	private FuncionarioService funcionarioService;

	@Inject
	private PermissaoService permissaoService;

	@Inject
	private ValidaCadastro validaCadastro;

	@PostConstruct
	public void inicializar() {

		clientePessoa = new Pessoas();
		funcionario = new Funcionario();
		permissao = new Permissoes();
		tipos = new Tipos();
		cliente = new Cliente();
		listPermissoes = new ArrayList<>();
		buscarInativo();
		listCidade = daoCidade.listaComStatus(Cidade.class);
		listCliente = daoCliente.listarCodicaoLivre(Cliente.class, " statusCliente is true and pessoa.status is true");
		cidade = new Cidade();

	}

	public void buscarInativo() {
		listClienteInativo = daoCliente.listarCodicaoLivre(Cliente.class,
				" statusCliente is false and controleCliente = 1 and pessoa.status is true");
	}

	public void preencherLista(Cliente t) {
		this.clientePessoa = t.getPessoa();
		this.cliente = t;
		cidade = t.getPessoa().getBairro().getCidade();
		buscarBairro();
		controleAdd = true;
	}

	public void removePermissao(Tipos t) {
		listPermissoes.remove(t);
	}

	public void adicionarPermissao() {

		if (listPermissoes.size() == 0) {
			listPermissoes.add(tipos);
			tipos = new Tipos();
		} else {
			if (validaPermissaoAdiciona()) {
				listPermissoes.add(tipos);
				tipos = new Tipos();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.ADDPERMISSAO);
			}
		}

	}

	public boolean validaPermissaoAdiciona() {

		for (Tipos t : listPermissoes) {
			if (t.getDescricao().equals(tipos.getDescricao())) {
				return false;
			}
		}
		return true;
	}

	public void controle() {
		controleAddTipo = false;
	}

	public void ativarCliente(Cliente t) {
		clientService.update(" Cliente set statusCliente = true where id = " + t.getId());
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

		carregarLista();
		buscarInativo();
	}

	public void verificaTornaCliente() {
		if (validaCadastro.validaAdicionaFuncionarioCliente(clientePessoa.getId()) == false) {
			FecharDialog.abrirDialogCliente();
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.ATVFUNC);
		}
	}

	public void tornarFuncionario() {

		if (listPermissoes.size() == 0) {
			ExibirMensagem.exibirMensagem(Mensagem.PERMISSAO);
		} else {
			funcionario.setSenha(CriptografiaSenha.criptografar(funcionario.getSenha()));
			funcionario.setDataCadastroFuncionario(new Date());
			funcionario.setControleFuncionario(1);
			funcionario.setStatusFuncionario(true);
			funcionario.setPessoa(clientePessoa);
			funcionarioService.inserirAlterar(funcionario);
			for (Tipos t : listPermissoes) {
				permissao.setDataInclusao(new Date());
				permissao.setStatus(true);
				permissao.setFuncionario(funcionario);
				permissao.setTipo(t);
				permissaoService.inserirAlterar(permissao);
				permissao = new Permissoes();
			}
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativo();
			FecharDialog.fecharDialogCliente();
			FecharDialog.fecharDialogClienteSenha();
		}
	}

	public List<Tipos> completarTipos(String str) {
		List<Tipos> pess = new ArrayList<>();
		pess = daoTipos.listaComStatus(Tipos.class);
		List<Tipos> pessoasSelecionados = new ArrayList<>();
		for (Tipos at : pess) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				pessoasSelecionados.add(at);
			}
		}
		return pessoasSelecionados;
	}

	public void inativarCliente(Cliente t) {
		clientService.update(" Cliente set statusCliente = false where id = " + t.getId());
		buscarInativo();
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void excluirCliente(Cliente t) {
		// fazer uma verificação se não ta usando em funcionário

		if (validaCadastro.validaExcluirCliente(t.getPessoa().getId()) == false) {
			clientService.update(" Cliente set statusCliente = false, controleCliente = 0 where id = " + t.getId());
			pessoaService.update(" Pessoas set status = false, controle = 0 where id = " + t.getPessoa().getId());
			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativo();
		} else {
			clientService.update(" Cliente set statusCliente = false, controleCliente = 0 where id = " + t.getId());
			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativo();
		}

	}

	public void buscarBairro() {
		listBairro = daoBairro.listar(Bairro.class, " cidade = '" + cidade.getId() + "'");
		controleBairro = false;
	}

	public void salvar() {
		clientePessoa.setNome(clientePessoa.getNome().toUpperCase());
	 
		try {
			if (clientePessoa.getId() == null) {
//				if (validaCadastro.buscarClienteCpf(clientePessoa)) {
//					ExibirMensagem.exibirMensagem(Mensagem.CADASTRO);
//				} else {
//					if (validaCadastro.buscarClienteEmail(clientePessoa)) {
//					 
//						ExibirMensagem.exibirMensagem(Mensagem.CADASTROEMAIL);
//					} else {
					 
						
						clientePessoa.setDataCadastroPessoa(new Date());
						clientePessoa.setStatus(true);
						
						clientePessoa.setControle(1);
						pessoaService.inserirAlterar(clientePessoa);

						cliente.setDataCadastroCliente(new Date());
						cliente.setStatusCliente(true);
						cliente.setControleCliente(1);
						cliente.setPessoa(clientePessoa);
						clientService.inserirAlterar(cliente);

						criarNovoObjeto();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						FecharDialog.fecharDialogCliente();
						carregarLista();
				//	}
			//	}
			} else {
//				if (validaCadastro.buscarClienteCpf(clientePessoa)
//						&& validaCadastro.buscarClienteAlterarCpf(clientePessoa)) {
//					ExibirMensagem.exibirMensagem(Mensagem.CADASTRO);
//				} else {
//					if (validaCadastro.buscarClienteEmail(clientePessoa)
//							&& validaCadastro.buscarClienteEmailAlterar(clientePessoa)) {
//						ExibirMensagem.exibirMensagem(Mensagem.CADASTROEMAIL);
//					} else {
						pessoaService.inserirAlterar(clientePessoa);
						clientService.inserirAlterar(cliente);
						criarNovoObjeto();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						FecharDialog.fecharDialogCliente();
						carregarLista();
				//	}
			//	}
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void criarNovoObjeto() {
		cliente = new Cliente();
		clientePessoa = new Pessoas();
		funcionario = new Funcionario();
		controleAdd = false;
		permissao = new Permissoes();

	}

	public void carregarLista() {
		listCliente = daoCliente.listarCodicaoLivre(Cliente.class, " statusCliente is true and pessoa.status is true");
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
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

	public boolean isControleBairro() {
		return controleBairro;
	}

	public void setControleBairro(boolean controleBairro) {
		this.controleBairro = controleBairro;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public List<Cliente> getListClienteInativo() {
		return listClienteInativo;
	}

	public void setListClienteInativo(List<Cliente> listClienteInativo) {
		this.listClienteInativo = listClienteInativo;
	}

	public Pessoas getClientePessoa() {
		return clientePessoa;
	}

	public void setClientePessoa(Pessoas clientePessoa) {
		this.clientePessoa = clientePessoa;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public boolean isControleAdd() {
		return controleAdd;
	}

	public void setControleAdd(boolean controleAdd) {
		this.controleAdd = controleAdd;
	}

	public Tipos getTipos() {
		return tipos;
	}

	public void setTipos(Tipos tipos) {
		this.tipos = tipos;
	}

	public List<Tipos> getListPermissoes() {
		return listPermissoes;
	}

	public void setListPermissoes(List<Tipos> listPermissoes) {
		this.listPermissoes = listPermissoes;
	}

	public boolean isControleAddTipo() {
		return controleAddTipo;
	}

	public void setControleAddTipo(boolean controleAddTipo) {
		this.controleAddTipo = controleAddTipo;
	}

}
