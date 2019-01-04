package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.ControleVencimento;
import util.Transacional;

public class ControleVencimentoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<ControleVencimento> dao;
	
	@Transacional
	public void inserirAlterar(ControleVencimento entidade){
		if(entidade.getId()==null){
			dao.inserir(entidade);
		}else{
			dao.alterar(entidade);
		}
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
