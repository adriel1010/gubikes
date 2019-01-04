package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO; 
import modelo.Orcamento; 
import util.Transacional;

public class OrcamentoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Orcamento> dao;
	
	@Transacional
	public void inserirAlterar(Orcamento entidade){
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
