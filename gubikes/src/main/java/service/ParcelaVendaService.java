package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;  
import modelo.ParcelaVenda;
import util.Transacional;

public class ParcelaVendaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<ParcelaVenda> dao;
	
	@Transacional
	public void inserirAlterar(ParcelaVenda entidade){
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
