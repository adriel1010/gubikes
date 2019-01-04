package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Bairro;
import modelo.Cidade;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Pessoas;
import modelo.Produto;

public class ValidaCadastro implements Serializable{
	
	
	private static final long serialVersionUID = 1L;


	@Inject
	private GenericDAO<Cidade> daoCidade; //faz as buscas
	
	@Inject
	private GenericDAO<Bairro> daoBairro;
	
	@Inject
	private GenericDAO<Pessoas> daoCliente; 
	@Inject
	private GenericDAO<Produto> daoProduto;
	
	@Inject
	private GenericDAO<Funcionario> daoFuncionario;
	

	public Boolean buscarCidade(Cidade cidade) {
		List<Cidade> cidades = new ArrayList<>();
		try {
			cidades = daoCidade.listarCadastro(Cidade.class, " status is true and nome = '" + cidade.getNome() + "' and estado = '"+cidade.getEstado()+"'");
			if (cidades.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean validaExcluirCliente(Long id){
		List<Funcionario> funcionarios = new ArrayList<>();
		try {
			funcionarios = daoFuncionario.listarCadastro(Funcionario.class, " statusFuncionario is true and pessoa = '" +id+"'");
			if (funcionarios.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return false;
	
	}
	
	public boolean validaAdicionaFuncionarioCliente(Long id){
		List<Funcionario> funcionarios = new ArrayList<>();
		try {
			funcionarios = daoFuncionario.listarCadastro(Funcionario.class, " controleFuncionario = 1 and pessoa = '" +id+"'");
			if (funcionarios.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return false;
	
	}
	
	public boolean validaAdicionaClienteFuncionario(Long id){
		List<Cliente> funcionarios = new ArrayList<>();
		try {
			funcionarios = daoFuncionario.listarCadastro(Cliente.class, " controleCliente = 1 and pessoa = '" +id+"'");
			if (funcionarios.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return false;
	
	}
	
	public boolean validaExcluirFuncionario(Long id){
		List<Cliente> funcionarios = new ArrayList<>();
		try {
			funcionarios = daoFuncionario.listarCadastro(Cliente.class, " statusCliente is true and pessoa = '" +id+"'");
			if (funcionarios.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return false;
	
	}
	
	
	public Boolean buscarCidadeAlterar(Cidade cidade) {
		List<Cidade> cidades = new ArrayList<>();
		try {
			cidades = daoCidade.listarCadastro(Cidade.class, " status is true and nome = '" + cidade.getNome() + "' and estado = '"+cidade.getEstado()+"' "
					+ " and id = "+cidade.getId());
			if (cidades.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarCidade");
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean buscarBairro(Bairro bairro) {
		List<Bairro> bairos = new ArrayList<>();
		try {
			bairos = daoBairro.listarCadastro(Bairro.class, " status is true and nome = '" + bairro.getNome() + "' and cidade = "+bairro.getCidade().getId());
			if (bairos.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarBairroAlterar(Bairro bairro) {
		List<Bairro> bairos = new ArrayList<>();
		try {
			bairos = daoBairro.listarCadastro(Bairro.class, " status is true and nome = '" + bairro.getNome() + "' and cidade = "+bairro.getCidade().getId()+" "
					+ " and id = "+bairro.getId());
			if (bairos.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return true;
	}
	
	
	public Boolean buscarClienteCpf(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCodicaoLivre(Pessoas.class, " status is true and cpf = '" +pessoa.getCpf()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarClienteEmail(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCodicaoLivre(Pessoas.class, " status is true and usuario = '" +pessoa.getUsuario()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarFuncionarioEmail(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCadastro(Pessoas.class, " status is true and usuario = '" +pessoa.getUsuario()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarFuncionarioEmailAlterar(Funcionario pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCadastro(Pessoas.class, " status is true and usuario = '" +pessoa.getPessoa().getUsuario()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarFuncionario(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoFuncionario.listarCadastro(Pessoas.class, " status is true and cpf = '"+pessoa.getCpf()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Boolean buscarFuncionarioAtivar(Funcionario pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoFuncionario.listarCadastro(Funcionario.class, " statusFuncionario is false and id = "+pessoa.getId());
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarFuncionarioAlterar(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoFuncionario.listarCadastro(Pessoas.class, " status is true and cpf = '"+pessoa.getCpf()
			+"' and id = "+pessoa.getId());
			if (pes.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean buscarClienteEmailAlterar(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCadastro(Pessoas.class, " status is true and usuario = '" +pessoa.getUsuario()+"' and id = "+pessoa.getId());
			if (pes.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean buscarClienteAlterarCpf(Pessoas pessoa) {
		List<Pessoas> pes = new ArrayList<>();
		try {
			pes = daoCliente.listarCadastro(Pessoas.class, " status is true and cpf = '" +pessoa.getCpf()+"' and id = "+pessoa.getId());
			if (pes.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean buscarProduto(Produto produto) {
		List<Produto> pes = new ArrayList<>();
		try {
			pes = daoProduto.listarCadastro(Produto.class, " status is true and nome = '" +produto.getNome()+"'");
			if (pes.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean buscarProdutoAlterar(Produto produto) {
		List<Produto> pes = new ArrayList<>();
		try {
			pes = daoProduto.listarCadastro(Produto.class, " status is true and nome = '" +produto.getNome()+"' and id = "+produto.getId());
			if (pes.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Erro buscarBairro");
			e.printStackTrace();
		}
		return true;
	}
	
}
