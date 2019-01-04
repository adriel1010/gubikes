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
@Table(name = "tab_Cliente")
public class Cliente implements Serializable {
	 
	public Cliente() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_cliente")
	private Long id;
  
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dataCadastroCliente;

	@Column(nullable = false)
	private Boolean statusCliente;
	
	private int controleCliente;
	
	public int getControleCliente() {
		return controleCliente;
	}

	public void setControleCliente(int controleCliente) {
		this.controleCliente = controleCliente;
	}

	@OneToOne
	@JoinColumn(name = "id_pessoa", nullable = false)
	private Pessoas pessoa;

	public Date getDataCadastroCliente() {
		return dataCadastroCliente;
	}

	public void setDataCadastroCliente(Date dataCadastroCliente) {
		this.dataCadastroCliente = dataCadastroCliente;
	}

	public Boolean getStatusCliente() {
		return statusCliente;
	}

	public void setStatusCliente(Boolean statusCliente) {
		this.statusCliente = statusCliente;
	}

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
 

	 
 
	
	

}
