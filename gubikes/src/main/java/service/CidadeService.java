package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Cidade;
import modelo.Tipos;
import util.Transacional;

public class CidadeService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Cidade> daoCidade;
	
	@Transacional
	public void inserirAlterar(Cidade cidade){
		if(cidade.getId()==null){
			daoCidade.inserir(cidade);
		}else{
			daoCidade.alterar(cidade);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoCidade.update(valor);
	}

}
