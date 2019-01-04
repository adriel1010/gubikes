package util;

import org.primefaces.context.RequestContext;

public class FecharDialog {

	
	public static void fecharDialogTipoServidor() {
		RequestContext.getCurrentInstance().execute("PF('dlgTipoServidor').hide();");
	}
	public static void fecharDialogCidade() {
		RequestContext.getCurrentInstance().execute("PF('dlgCidade').hide();");
	}
	public static void fecharDialogBairro() {
		RequestContext.getCurrentInstance().execute("PF('dlgBairro').hide();");
	}
	
	public static void fecharDialogCliente() {
		RequestContext.getCurrentInstance().execute("PF('dlgCliente').hide();");
	}
	
	public static void abrirDialogCliente() {
		RequestContext.getCurrentInstance().execute("PF('dlgFuncionarioSenha').show();");
	}
	public static void abrirDialogFinalizaOrcamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgOrcamentoConfirma').show();");
	}
	
	public static void abrirDialogParcela() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelamento').show();");
	}
	
	public static void fechaDialogParcela() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelamento').hide();");
	}	
	public static void fechaDialogParcelaCorrecao() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelamentoReparcela').hide();");
	}	
	
	public static void fechaDialogParcelaCorrecaoVer() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelamentoDetalhe').hide();");
	}	
	
	public static void fechaDialoAddValor() {
		RequestContext.getCurrentInstance().execute("PF('dlgValor').hide();");
	}	
	
	public static void abrirDialogObra() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelasmaoObra').show();");
	}
	
	public static void fechaDialogObra() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelasmaoObra').hide();");
	}
	
	 
	public static void fechaDialogFinalizaOrcamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgOrcamentoConfirma').hide();");
	}
	
	public static void fechaDialogBaixa() {
		RequestContext.getCurrentInstance().execute("PF('dlgParcelamentoPagamento').hide();");
	}
	
	 
	
	public static void fechaDialogOrcamento() {
		RequestContext.getCurrentInstance().execute("PF('dlgOrcamento').hide();");
	}
	public static void fecharDialogClienteSenha() {
		RequestContext.getCurrentInstance().execute("PF('dlgFuncionarioSenha').hide();");
	}
	
	public static void fecharDialogFuncionarioSenha() {
		RequestContext.getCurrentInstance().execute("PF('dlgClienteSenha').hide();");
	}
	
	public static void fecharDialogProduto() {
		RequestContext.getCurrentInstance().execute("PF('dlgProduto').hide();");
	}
	
	 

}
