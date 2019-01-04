package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Permissoes;
import modelo.Tipos;
import util.Transacional;

public class PermissaoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Permissoes> daoPermissoes;
	
	@Transacional
	public void inserirAlterar(Permissoes tipo){
		if(tipo.getId()==null){
			daoPermissoes.inserir(tipo);
		}else{
			daoPermissoes.alterar(tipo);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoPermissoes.update(valor);
	}

}
