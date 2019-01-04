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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tab_parcela_venda")
public class ParcelaVenda implements Serializable {
 
		public ParcelaVenda() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_parcela_venda")
		private Long id;

		@Column(precision = 7, scale = 2)
		private BigDecimal valorTotalVenda;
		
		@Column(precision = 7, scale = 2)
		private BigDecimal jurosParcela;
		
		@Column(precision = 7, scale = 2)
		private BigDecimal valorParcela;
		
		@Column(nullable = false)
		private Boolean status;
		
		@Column(nullable = false)
		private String situacao;
		
		@Column(nullable = false)
		private Integer quantidadeParcelas;
		
		@Column(nullable = false)
		private Integer numeroParcela;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(nullable = false)
		private Date data;
		
		@Temporal(TemporalType.TIMESTAMP) 
		private Date dataPagamento;
 
		@NotNull(message = "O campo venda não pode ser nulo")
		@JoinColumn(name = "id_venda", nullable = false)
		@ManyToOne
		private Venda venda;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public BigDecimal getValorTotalVenda() {
			return valorTotalVenda;
		}

		public void setValorTotalVenda(BigDecimal valorTotalVenda) {
			this.valorTotalVenda = valorTotalVenda;
		}

		public BigDecimal getJurosParcela() {
			return jurosParcela;
		}

		public void setJurosParcela(BigDecimal jurosParcela) {
			this.jurosParcela = jurosParcela;
		}

		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
		}

		public String getSituacao() {
			return situacao;
		}

		public void setSituacao(String situacao) {
			this.situacao = situacao;
		}

		public Integer getQuantidadeParcelas() {
			return quantidadeParcelas;
		}

		public void setQuantidadeParcelas(Integer quantidadeParcelas) {
			this.quantidadeParcelas = quantidadeParcelas;
		}

		public Integer getNumeroParcela() {
			return numeroParcela;
		}

		public void setNumeroParcela(Integer numeroParcela) {
			this.numeroParcela = numeroParcela;
		}

		public Date getData() {
			return data;
		}

		public void setData(Date data) {
			this.data = data;
		}

		public Venda getVenda() {
			return venda;
		}

		public void setVenda(Venda venda) {
			this.venda = venda;
		}

		public BigDecimal getValorParcela() {
			return valorParcela;
		}

		public void setValorParcela(BigDecimal valorParcela) {
			this.valorParcela = valorParcela;
		}

		public Date getDataPagamento() {
			return dataPagamento;
		}

		public void setDataPagamento(Date dataPagamento) {
			this.dataPagamento = dataPagamento;
		}

	
	

 
		
		
}
