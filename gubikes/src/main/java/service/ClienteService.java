package service;

import java.io.Serializable;

import javax.inject.Inject;
 
import dao.GenericDAO;
import modelo.Cidade;
import modelo.Cliente;
import modelo.Pessoas;
import modelo.Tipos;
import util.Transacional;

public class ClienteService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Cliente> daoCliente;
	
	@Transacional
	public void inserirAlterar(Cliente pessoas){
		if(pessoas.getId()==null){
			daoCliente.inserir(pessoas);
		}else{
			daoCliente.alterar(pessoas);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoCliente.update(valor);
	}

}
