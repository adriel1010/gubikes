package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Cidade;
import modelo.Funcionario;
import modelo.Pessoas;
import modelo.Tipos;
import util.Transacional;

public class FuncionarioService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Funcionario> daoFuncionario;
	
	@Transacional
	public void inserirAlterar(Funcionario funcionario){
		if(funcionario.getId()==null){
			daoFuncionario.inserir(funcionario);
		}else{
			daoFuncionario.alterar(funcionario);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoFuncionario.update(valor);
	}

}
