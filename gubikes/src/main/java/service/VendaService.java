package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Venda;
import util.Transacional;

public class VendaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Venda> dao;
	
	@Transacional
	public void inserirAlterar(Venda entidade){
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
