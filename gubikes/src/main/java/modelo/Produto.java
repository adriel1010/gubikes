package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tab_produto")
public class Produto implements Serializable {
 
		public Produto() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_produto")
		private Long id;

		@NotNull(message = "O campo nome não pode ser nulo")
		@Column(nullable = false)
		private String nome;
		
		@Digits(integer=6, fraction=2 , message="Informe um valor válido para o campo preço compra")
		@Column(nullable=false, precision = 7, scale = 2)
		private BigDecimal valorCompra;
		
		@Digits(integer=6, fraction=2 , message="Informe um valor válido para o campo preço compra")
		@Column(nullable=false, precision = 7, scale = 2)
		private BigDecimal valorVenda;
		
		@Column(nullable=false)
		private Integer quantidade;

		@Column(nullable = false)
		private Boolean status;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public BigDecimal getValorCompra() {
			return valorCompra;
		}

		public void setValorCompra(BigDecimal valorCompra) {
			this.valorCompra = valorCompra;
		}

		public BigDecimal getValorVenda() {
			return valorVenda;
		}

		public void setValorVenda(BigDecimal valorVenda) {
			this.valorVenda = valorVenda;
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

		
		
}
