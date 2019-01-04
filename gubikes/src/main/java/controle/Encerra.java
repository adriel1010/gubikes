package controle;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCadastro;
import util.ValidacoesGerirUsuarios;
import dao.GenericDAO;
import modelo.Cidade;
import modelo.ControleVencimento;
import modelo.Produto;
import service.CidadeService;
import service.ControleVencimentoService;

@ViewScoped
@Named("encerraMB")
public class Encerra implements Serializable {

	private static final long serialVersionUID = 1L;

	 
	@PostConstruct
	public void inicializar() {

		 

	}

	public void encerra() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../login.jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
