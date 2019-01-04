package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Tipos;
import util.Transacional;

public class TipoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Tipos> daoTipo;
	
	@Transacional
	public void inserirAlterar(Tipos tipo){
		if(tipo.getId()==null){
			daoTipo.inserir(tipo);
		}else{
			daoTipo.alterar(tipo);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoTipo.update(valor);
	}

}
