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
@Table(name = "tab_venda")
public class Venda implements Serializable {
 
		public Venda() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_venda")
		private Long id;

		
		@Column(nullable = false)
		private Boolean status;
		
		
		private String mes;
		
		private String ano;
		
		@Column(nullable = false)
		private String situacao;
		
		@Column(precision = 7, scale = 2)
		private BigDecimal entradaValor;
		
		 
		private String situacaoPago;
		
		public String getSituacaoPago() {
			return situacaoPago;
		}

		public void setSituacaoPago(String situacaoPago) {
			this.situacaoPago = situacaoPago;
		}

		 
		private String dataVenda;
 
		@NotNull(message = "O campo orcamento não pode ser nulo")
		@JoinColumn(name = "id_orcamento", nullable = false)
		@OneToOne
		private Orcamento orcamento;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		

		public String getMes() {
			return mes;
		}

		public void setMes(String mes) {
			this.mes = mes;
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

	 
		public String getDataVenda() {
			return dataVenda;
		}

		public void setDataVenda(String dataVenda) {
			this.dataVenda = dataVenda;
		}

		public Orcamento getOrcamento() {
			return orcamento;
		}

		public void setOrcamento(Orcamento orcamento) {
			this.orcamento = orcamento;
		}

		public String getAno() {
			return ano;
		}

		public void setAno(String ano) {
			this.ano = ano;
		}

		public BigDecimal getEntradaValor() {
			return entradaValor;
		}

		public void setEntradaValor(BigDecimal entradaValor) {
			this.entradaValor = entradaValor;
		}


	

 
		
		
}
