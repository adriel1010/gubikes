package modelo;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "tab_Funcionario")
public class Funcionario implements Serializable {
	 
	public Funcionario() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_funcionario")
	private Long id; 
  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date DataCadastroFuncionario;

	@Column(nullable = false)
	private Boolean statusFuncionario;
	
	private String senha;
	
	private int controleFuncionario;
	
 

	public int getControleFuncionario() {
		return controleFuncionario;
	}

	public void setControleFuncionario(int controleFuncionario) {
		this.controleFuncionario = controleFuncionario;
	}

	@OneToOne
	@JoinColumn(name = "id_pessoa", nullable = false)
	private Pessoas pessoa;
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoas getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoas pessoa) {
		this.pessoa = pessoa;
	}

	public Date getDataCadastroFuncionario() {
		return DataCadastroFuncionario;
	}

	public void setDataCadastroFuncionario(Date dataCadastroFuncionario) {
		DataCadastroFuncionario = dataCadastroFuncionario;
	}

	public Boolean getStatusFuncionario() {
		return statusFuncionario;
	}

	public void setStatusFuncionario(Boolean statusFuncionario) {
		this.statusFuncionario = statusFuncionario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
 

	 
 
	
	

}
