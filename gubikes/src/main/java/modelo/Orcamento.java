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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tab_orcamento")
public class Orcamento implements Serializable {
 
		public Orcamento() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_orcamento")
		private Long id;

		
		@Column(nullable = false)
		private boolean status;
		
		 
		private String dataOrcamento;
		
		@Column(precision = 7, scale = 2)
		private BigDecimal valorTotalOrcamento;
		
		
		
		@Column(precision = 7, scale = 2)
		private BigDecimal subTotalOrcamento;
		
		@Column(precision = 7, scale = 2)
		private BigDecimal desconto;
		
		private String situacao;
		
		
		@Column(precision = 7, scale = 2)
		private BigDecimal lucroOrcamento;
		
		private String observacao;
 
		@NotNull(message = "O campo pessoa não pode ser nulo")
		@JoinColumn(name = "id_cliente", nullable = false)
		@ManyToOne
		private Cliente cliente;
		
		@NotNull(message = "O campo funcionario não pode ser nulo")
		@JoinColumn(name = "id_funcionario", nullable = false)
		@ManyToOne
		private Funcionario funcionario;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

 

 

		public String getDataOrcamento() {
			return dataOrcamento;
		}

		public void setDataOrcamento(String dataOrcamento) {
			this.dataOrcamento = dataOrcamento;
		}

		public BigDecimal getValorTotalOrcamento() {
			return valorTotalOrcamento;
		}

		public void setValorTotalOrcamento(BigDecimal valorTotalOrcamento) {
			this.valorTotalOrcamento = valorTotalOrcamento;
		}

		public String getSituacao() {
			return situacao;
		}

		public void setSituacao(String situacao) {
			this.situacao = situacao;
		}

		public BigDecimal getLucroOrcamento() {
			return lucroOrcamento;
		}

		public void setLucroOrcamento(BigDecimal lucroOrcamento) {
			this.lucroOrcamento = lucroOrcamento;
		}

		public String getObservacao() {
			return observacao;
		}

		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}

	 

		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public Funcionario getFuncionario() {
			return funcionario;
		}

		public void setFuncionario(Funcionario funcionario) {
			this.funcionario = funcionario;
		}

		public BigDecimal getDesconto() {
			return desconto;
		}

		public void setDesconto(BigDecimal desconto) {
			this.desconto = desconto;
		}

		public BigDecimal getSubTotalOrcamento() {
			return subTotalOrcamento;
		}

		public void setSubTotalOrcamento(BigDecimal subTotalOrcamento) {
			this.subTotalOrcamento = subTotalOrcamento;
		}

 
		
 

 
		
		
}
