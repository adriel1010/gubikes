package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;


@Entity
@Table(name = "tab_orcamentoProduto")
public class OrcamentoProdutos implements Serializable {

	public OrcamentoProdutos() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_orcamentoProduto")
	private Long id;
	
 
     @Column(precision = 7, scale = 2)
	private BigDecimal valorUnitario;
	
	@Column(nullable=false)
    private Integer quantidade;
	
	@Column(nullable=false)
    private Boolean status;
	
	@Column(precision = 7, scale = 2)
	private BigDecimal lucroUnitario;
	
	 
	@JoinColumn(name = "id_produto", nullable = false)
	@ManyToOne
	private Produto produto;
	
	@JoinColumn(name = "id_orcamento")
	@ManyToOne
	private Orcamento orcamento;
	 
	@Column(nullable=false, precision = 7, scale = 2)
	private BigDecimal subTotal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public BigDecimal getLucroUnitario() {
		return lucroUnitario;
	}

	public void setLucroUnitario(BigDecimal lucroUnitario) {
		this.lucroUnitario = lucroUnitario;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

 
	

}
