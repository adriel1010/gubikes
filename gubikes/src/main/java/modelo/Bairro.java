package modelo;

import java.io.Serializable;
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
@Table(name = "tab_bairro")
public class Bairro implements Serializable {
 
		public Bairro() {
			super();
		}

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id_bairro")
		private Long id;

		@NotNull(message = "O campo nome não pode ser nulo")
		@Column(nullable = false)
		private String nome;
		
		 
		@Column(nullable = false)
		private Boolean status;
 
		@NotNull(message = "O campo cidade não pode ser nulo")
		@JoinColumn(name = "id_cidade", nullable = false)
		@ManyToOne
		private Cidade cidade;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		 

		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
		}

	
		public Cidade getCidade() {
			return cidade;
		}

		public void setCidade(Cidade cidade) {
			this.cidade = cidade;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			result = prime * result + ((status == null) ? 0 : status.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Bairro other = (Bairro) obj;
			if (cidade == null) {
				if (other.cidade != null)
					return false;
			} else if (!cidade.equals(other.cidade))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			if (status == null) {
				if (other.status != null)
					return false;
			} else if (!status.equals(other.status))
				return false;
			return true;
		}
 

 
		
		
}
