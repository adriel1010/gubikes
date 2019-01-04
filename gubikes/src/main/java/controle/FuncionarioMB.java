package controle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
@Named("funcionarioMB")
public class FuncionarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Funcionario funcionario;
	private Permissoes permissao;
	private Tipos tipos;
	private Cliente cliente;
	private Pessoas funcionarioPessoa;
	private Pessoas p;
	private List<Bairro> listBairro;
	private List<Funcionario> listFuncionario;
	private List<Tipos> listPermissoes;
	private List<Funcionario> listFuncionarioInativos;
	private List<Cidade> listCidade;
	private Cidade cidade;
	private boolean controleBairro = true;
	private boolean senha = true;
	private boolean cadastro = true;
	private boolean cadastroExistente = true;
	private int controle = 0;
	private boolean controleAdd = false;
	private boolean controleAddTipo = true;

	public boolean isControleAddTipo() {
		return controleAddTipo;
	}

	public void setControleAddTipo(boolean controleAddTipo) {
		this.controleAddTipo = controleAddTipo;
	}

	@Inject
	private GenericDAO<Cidade> daoCidade;

	@Inject
	private GenericDAO<Funcionario> daoFuncionario;

	@Inject
	private GenericDAO<Bairro> daoBairro;

	@Inject
	private GenericDAO<Pessoas> daoPessoas;

	@Inject
	private GenericDAO<Tipos> daoTipos;

	@Inject
	private GenericDAO<Permissoes> daoPermissoes;

	@Inject
	private FuncionarioService funcionarioService;

	@Inject
	private PessoaService funcionarioPessoaService;

	@Inject
	private PermissaoService permissaoService;

	@Inject
	private ClienteService clienteService;

	@Inject
	private ValidaCadastro validaCadastro;

	@PostConstruct
	public void inicializar() {

		funcionario = new Funcionario();
		permissao = new Permissoes();
		listPermissoes = new ArrayList<>();
		cliente = new Cliente();
		funcionarioPessoa = new Pessoas();
		listCidade = daoCidade.listaComStatus(Cidade.class);
		listFuncionario = daoFuncionario.listarCodicaoLivre(Funcionario.class,
				" statusFuncionario is true and pessoa.status is true");
		cidade = new Cidade();
		tipos = new Tipos();
		p = new Pessoas();
		buscarInativos();

	}

	public void buscarInativos() {
		listFuncionarioInativos = daoFuncionario.listarCodicaoLivre(Funcionario.class,
				" statusFuncionario is false and controleFuncionario = 1 and pessoa.status is true");
	}

	public void removePermissao(Tipos t) {
		if(funcionario.getId() == null){
			listPermissoes.remove(t);
		}else{
			Permissoes permissaoInativar = new Permissoes();
		    permissaoInativar = daoPermissoes.buscarCondicao(Permissoes.class, " status is true and funcionario = "+funcionario.getId() +
		    		" and tipo = "+t.getId());
		  
		    funcionarioService.update("Permissoes set status = false where id = " + permissaoInativar.getId());
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			buscaPermissaoFuncionario();

		}
		
	}

	public void adicionarPermissao() {

		if (funcionario.getId() == null) {
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
		} else {
			if (validaPermissaoAdicionaCadastrado()) {
				permissao.setDataInclusao(new Date());
				permissao.setStatus(true);
				permissao.setFuncionario(funcionario);
				permissao.setTipo(tipos);

				permissaoService.inserirAlterar(permissao);

				permissao = new Permissoes();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				buscaPermissaoFuncionario();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.ADDPERMISSAO);
			}
		}
	}
	
	public void buscaPermissaoFuncionario(){
		listPermissoes.clear();
		for (Permissoes p : daoPermissoes.listar(Permissoes.class, " funcionario = " + funcionario.getId())) {
			listPermissoes.add(p.getTipo());
		}
	}

	public boolean validaPermissaoAdicionaCadastrado() {
		List<Permissoes> listPermissAdd = daoPermissoes.listar(Permissoes.class,
				" funcionario = " + funcionario.getId());
		for (Permissoes t : listPermissAdd) {
			if (t.getTipo().getDescricao().equals(tipos.getDescricao())) {
				return false;
			}
		}
		return true;
	}

	public boolean validaPermissaoAdiciona() {

		for (Tipos t : listPermissoes) {
			if (t.getDescricao().equals(tipos.getDescricao())) {
				return false;
			}
		}
		return true;
	}

	public void buscar() {
		funcionario = new Funcionario();
		cadastro = true;
		cadastroExistente = false;
		controle = 1;
	}

	public void controle() {
		controleAddTipo = false;
	}

	public void preencherLista(Funcionario t) {

		this.funcionario = t;
		funcionarioPessoa = t.getPessoa();
 
		senha = false;
		cidade = t.getPessoa().getBairro().getCidade();
		controleAdd = true;

		buscarBairro();
		buscaPermissaoFuncionario();
	}

	public void inativarFuncionario(Funcionario t) {
		funcionarioService.update("Funcionario set statusFuncionario = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
		buscarInativos();
	}

	public void ativarFuncionario(Funcionario t) {
		funcionarioService.update("Funcionario set statusFuncionario = true where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
		buscarInativos();
	}

	public void tornarCliente() {
		// validar se já não esta cadastrado

		if (validaCadastro.validaAdicionaClienteFuncionario(funcionarioPessoa.getId()) == false) {
			cliente.setControleCliente(1);
			cliente.setDataCadastroCliente(new Date());
			cliente.setStatusCliente(true);
			cliente.setPessoa(funcionarioPessoa);
			clienteService.inserirAlterar(cliente);
			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativos();
			FecharDialog.fecharDialogCliente();
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.ATVFUNCCLIENTE);
		}
	}

	public void excluirFuncionario(Funcionario t) {
		// validar se só inativa aqui ou inativa os dois

		if (validaCadastro.validaExcluirFuncionario(t.getPessoa().getId()) == false) {
			funcionarioService.update(
					" Funcionario set statusFuncionario = false, controleFuncionario = 0 where id = " + t.getId());
			funcionarioPessoaService
					.update(" Pessoas set status = false, controle = 0 where id = " + t.getPessoa().getId());
			
			inativarPermissoes(t.getId());
			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativos();
		} else {
			funcionarioService.update(
					" Funcionario set statusFuncionario = false, controleFuncionario = 0 where id = " + t.getId());
			inativarPermissoes(t.getId());
			criarNovoObjeto();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregarLista();
			buscarInativos();
		}

	}
	
	public void inativarPermissoes(Long id){
		List<Permissoes> listPerInativa = daoPermissoes.listar(Permissoes.class,
				" funcionario = " + id);
		for (Permissoes t : listPerInativa) {
			funcionarioService.update("Permissoes set status = false where id = " + t.getId());
		}
		    
	}

	public void buscarBairro() {
		listBairro = daoBairro.listar(Bairro.class, " cidade = '" + cidade.getId() + "'");
		controleBairro = false;
	}

	public void salvar() {
		funcionarioPessoa.setNome(funcionarioPessoa.getNome().toUpperCase());
		try {

			if (listPermissoes.size() > 0) {
				if (funcionario.getId() == null) {

					
						if (validaCadastro.buscarFuncionarioEmail(funcionarioPessoa)) {
							ExibirMensagem.exibirMensagem(Mensagem.CADASTROEMAIL);
						} else {

							funcionarioPessoa.setDataCadastroPessoa(new Date());
							funcionarioPessoa.setStatus(true);
							funcionarioPessoa.setControle(1);
							funcionarioPessoaService.inserirAlterar(funcionarioPessoa);

							funcionario.setSenha(CriptografiaSenha.criptografar(funcionario.getSenha()));
							funcionario.setDataCadastroFuncionario(new Date());
							funcionario.setPessoa(funcionarioPessoa);
							funcionario.setControleFuncionario(1);
							funcionario.setStatusFuncionario(true);
							funcionarioService.inserirAlterar(funcionario);

							for (Tipos t : listPermissoes) {
								permissao.setDataInclusao(new Date());
								permissao.setStatus(true);
								permissao.setFuncionario(funcionario);
								permissao.setTipo(t);

								permissaoService.inserirAlterar(permissao);

								permissao = new Permissoes();
							}

							criarNovoObjeto();
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							FecharDialog.fecharDialogCliente();
							carregarLista();
							buscarInativos();
							senha = true;
						}
					

				} else {
					
						funcionarioPessoaService.inserirAlterar(funcionarioPessoa);

						funcionarioService.inserirAlterar(funcionario);
						criarNovoObjeto();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						FecharDialog.fecharDialogCliente();
						carregarLista();
						buscarInativos();
						senha = true;

				
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.PERMISSAO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}
	
//	public void salvar() {
//		funcionarioPessoa.setNome(funcionarioPessoa.getNome().toUpperCase());
//		try {
//
//			if (listPermissoes.size() > 0) {
//				if (funcionario.getId() == null) {
//
//					if (validaCadastro.buscarFuncionario(funcionarioPessoa)) {
//						ExibirMensagem.exibirMensagem(Mensagem.CADASTRO);
//					} else {
//						if (validaCadastro.buscarFuncionarioEmail(funcionarioPessoa)) {
//							ExibirMensagem.exibirMensagem(Mensagem.CADASTROEMAIL);
//						} else {
//
//							funcionarioPessoa.setDataCadastroPessoa(new Date());
//							funcionarioPessoa.setStatus(true);
//							funcionarioPessoa.setControle(1);
//							funcionarioPessoaService.inserirAlterar(funcionarioPessoa);
//
//							funcionario.setSenha(CriptografiaSenha.criptografar(funcionario.getSenha()));
//							funcionario.setDataCadastroFuncionario(new Date());
//							funcionario.setPessoa(funcionarioPessoa);
//							funcionario.setControleFuncionario(1);
//							funcionario.setStatusFuncionario(true);
//							funcionarioService.inserirAlterar(funcionario);
//
//							for (Tipos t : listPermissoes) {
//								permissao.setDataInclusao(new Date());
//								permissao.setStatus(true);
//								permissao.setFuncionario(funcionario);
//								permissao.setTipo(t);
//
//								permissaoService.inserirAlterar(permissao);
//
//								permissao = new Permissoes();
//							}
//
//							criarNovoObjeto();
//							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
//							FecharDialog.fecharDialogCliente();
//							carregarLista();
//							buscarInativos();
//							senha = true;
//						}
//					}
//
//				} else {
//					if (validaCadastro.buscarFuncionario(funcionarioPessoa)
//							&& validaCadastro.buscarFuncionarioAlterar(funcionarioPessoa)) {
//						ExibirMensagem.exibirMensagem(Mensagem.CADASTRO);
//					} else {
//						funcionarioPessoaService.inserirAlterar(funcionarioPessoa);
//
//						funcionarioService.inserirAlterar(funcionario);
//						criarNovoObjeto();
//						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
//						FecharDialog.fecharDialogCliente();
//						carregarLista();
//						buscarInativos();
//						senha = true;
//
//					}
//				}
//			} else {
//				ExibirMensagem.exibirMensagem(Mensagem.PERMISSAO);
//			}
//		} catch (Exception e) {
//			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
//			e.printStackTrace();
//		}
//	}

	public void salvarExistente() {
		try {
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public List<Pessoas> completarPessoa(String str) {
		List<Pessoas> pess = new ArrayList<>();
		pess = daoPessoas.listarCodicaoLivre(Pessoas.class, " status is true and controle = 1");
		List<Pessoas> pessoasSelecionados = new ArrayList<>();
		for (Pessoas at : pess) {
			if (at.getNome().toLowerCase().startsWith(str)) {
				pessoasSelecionados.add(at);
			}
		}
		return pessoasSelecionados;
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

	public void salvarSenha() {
		funcionario.setSenha(CriptografiaSenha.criptografar(funcionario.getSenha()));
		funcionarioService.inserirAlterar(funcionario);
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogFuncionarioSenha();
		carregarLista();
	}

	public void criarNovoObjeto() {
		permissao = new Permissoes();
		funcionario = new Funcionario();
		funcionarioPessoa = new Pessoas();
		senha = true;
		cadastro = false;
		cadastroExistente = true;
		controle = 0;
		controleAdd = false;
		listPermissoes = new ArrayList<>();
	}

	public void carregarLista() {
		listFuncionario = daoFuncionario.listarCodicaoLivre(Funcionario.class,
				" statusFuncionario is true and pessoa.status is true");
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

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getListFuncionario() {
		return listFuncionario;
	}

	public void setListFuncionario(List<Funcionario> listFuncionario) {
		this.listFuncionario = listFuncionario;
	}

	public boolean isSenha() {
		return senha;
	}

	public void setSenha(boolean senha) {
		this.senha = senha;
	}

	public boolean isCadastro() {
		return cadastro;
	}

	public void setCadastro(boolean cadastro) {
		this.cadastro = cadastro;
	}

	public boolean isCadastroExistente() {
		return cadastroExistente;
	}

	public void setCadastroExistente(boolean cadastroExistente) {
		this.cadastroExistente = cadastroExistente;
	}

	public Pessoas getP() {
		return p;
	}

	public void setP(Pessoas p) {
		this.p = p;
	}

	public Pessoas getFuncionarioPessoa() {
		return funcionarioPessoa;
	}

	public void setFuncionarioPessoa(Pessoas funcionarioPessoa) {
		this.funcionarioPessoa = funcionarioPessoa;
	}

	public List<Funcionario> getListFuncionarioInativos() {
		return listFuncionarioInativos;
	}

	public void setListFuncionarioInativos(List<Funcionario> listFuncionarioInativos) {
		this.listFuncionarioInativos = listFuncionarioInativos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean isControleAdd() {
		return controleAdd;
	}

	public void setControleAdd(boolean controleAdd) {
		this.controleAdd = controleAdd;
	}

	public Permissoes getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissoes permissao) {
		this.permissao = permissao;
	}

	public List<Tipos> getListPermissoes() {
		return listPermissoes;
	}

	public void setListPermissoes(List<Tipos> listPermissoes) {
		this.listPermissoes = listPermissoes;
	}

	public Tipos getTipos() {
		return tipos;
	}

	public void setTipos(Tipos tipos) {
		this.tipos = tipos;
	}

}
