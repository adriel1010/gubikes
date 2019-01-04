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
@Table(name = "tab_entrada")
public class Entrada implements Serializable {
 
		public Entrada() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_produto")
		private Long id;

 
		private String localCompra;
		
		@Digits(integer=6, fraction=2 , message="Informe um valor válido para o campo preço compra")
		@Column(nullable=false, precision = 7, scale = 2)
		private BigDecimal valorCompra;

		private Date dataCompra;
		
		private String mes;
		
		private String ano;
		
		 
		@Column(nullable = false)
		private Boolean status;


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getLocalCompra() {
			return localCompra;
		}


		public void setLocalCompra(String localCompra) {
			this.localCompra = localCompra;
		}


		public BigDecimal getValorCompra() {
			return valorCompra;
		}


		public void setValorCompra(BigDecimal valorCompra) {
			this.valorCompra = valorCompra;
		}


		public Date getDataCompra() {
			return dataCompra;
		}


		public void setDataCompra(Date dataCompra) {
			this.dataCompra = dataCompra;
		}


		public String getMes() {
			return mes;
		}


		public void setMes(String mes) {
			this.mes = mes;
		}


		public String getAno() {
			return ano;
		}


		public void setAno(String ano) {
			this.ano = ano;
		}


		public Boolean getStatus() {
			return status;
		}


		public void setStatus(Boolean status) {
			this.status = status;
		}

	 

		
		
}
