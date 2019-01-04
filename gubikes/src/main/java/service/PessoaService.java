package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Cidade;
import modelo.Pessoas;
import modelo.Tipos;
import util.Transacional;

public class PessoaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Pessoas> daoPessoas;
	
	@Transacional
	public void inserirAlterar(Pessoas pessoas){
		if(pessoas.getId()==null){
			daoPessoas.inserir(pessoas);
		}else{
			daoPessoas.alterar(pessoas);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoPessoas.update(valor);
	}

}
