package util;
 
import dao.UsuarioDAO;
import modelo.Funcionario;
import modelo.Pessoas;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@SessionScoped
@Named("usuarioSessaoMB")
public class UsuarioSessaoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Pessoas pessoa; 
	private Funcionario funcionario;


	@Inject
	private UsuarioDAO daoUsuario;

	
	 public UsuarioSessaoMB() { 
		 
		pessoa = new Pessoas();
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof Authentication) {
				pessoa.setUsuario(((User) authentication.getPrincipal()).getUsername());
			}
		}
		
	}

  

	public Funcionario recuperarFuncionairo() {
		System.out.println("email passadpo "+ pessoa.getUsuario());
		
		funcionario = new Funcionario();
		
		try {
			funcionario = (Funcionario) daoUsuario.retornarLogado(Funcionario.class, pessoa.getUsuario()).get(0);
			
		} catch (Exception e) {
		}
		return funcionario;
		
	}

	public Pessoas getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoas pessoa) {
		this.pessoa = pessoa;
	}
 

 

}
