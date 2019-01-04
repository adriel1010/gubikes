package modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;
//import org.springframework.validation.annotation.Validated;

 
@Entity
@Table(name = "tab_pessoa") 
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoas implements Serializable {
 
	public Pessoas() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_pessoa")
	private Long id;

	@NotNull(message = "O campo nome não pode ser nulo")
	@Column(nullable = false)
	private String nome;
 
 
	private String usuario;
   

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dataCadastroPessoa;

	@Column(nullable = false)
	private Boolean status;
	
	
	private int controle;
 
	private String cpf;
	  
 
 
	private String rg;
	  
	private String rua;
	  
	private String numero; 
	 
	private String endereco;
	 
	@NotNull(message = "O campo bairro não pode ser nulo")
	@JoinColumn(name = "id_bairro", nullable = false)
	@ManyToOne
	private Bairro bairro;
	
 
	private String celular;
	
	private String telefone;
 
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public int getControle() {
		return controle;
	}

	public void setControle(int controle) {
		this.controle = controle;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

 
	 

	public Date getDataCadastroPessoa() {
		return dataCadastroPessoa;
	}

	public void setDataCadastroPessoa(Date dataCadastroPessoa) {
		this.dataCadastroPessoa = dataCadastroPessoa;
	}
 

 

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}


 
	 
 
	
	
	

}
