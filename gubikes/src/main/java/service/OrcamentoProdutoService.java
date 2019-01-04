package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.OrcamentoProdutos;
import util.Transacional;

public class OrcamentoProdutoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<OrcamentoProdutos> dao;
	
	@Transacional
	public void inserirAlterar(OrcamentoProdutos entidade){
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
