package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Entrada;
import util.Transacional;

public class EntradaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Entrada> dao;
	
	@Transacional
	public void inserirAlterar(Entrada entidade){
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
