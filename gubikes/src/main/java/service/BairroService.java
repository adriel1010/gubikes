package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Bairro;
import modelo.Cidade;
import modelo.Tipos;
import util.Transacional;

public class BairroService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Bairro> daoBairro;
	
	@Transacional
	public void inserirAlterar(Bairro bairro){
		if(bairro.getId()==null){
			daoBairro.inserir(bairro);
		}else{
			daoBairro.alterar(bairro);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoBairro.update(valor);
	}

}
