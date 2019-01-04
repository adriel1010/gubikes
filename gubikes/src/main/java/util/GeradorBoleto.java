package util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;

 
public class GeradorBoleto {

        public static void main(String[] args) {

                /*
                 * INFORMANDO DADOS SOBRE O CEDENTE.
                 */
                Cedente cedente = new Cedente("Gubikes", "144.537.692-02");

                /*
                 * INFORMANDO DADOS SOBRE O SACADO.
                 */
                Sacado sacado = new Sacado("Leandro Carlos Dias", "117.578.219-02");

                // Informando o endereço do sacado.
                Endereco enderecoSac = new Endereco();
                enderecoSac.setUF(UnidadeFederativa.PR);
                enderecoSac.setLocalidade("Paranavaí");
                enderecoSac.setCep(new CEP("87703030"));
                enderecoSac.setBairro("Jardim Morumbi");
                enderecoSac.setLogradouro("Avenida Domingos Sanches");
                enderecoSac.setNumero("818");
                sacado.addEndereco(enderecoSac);

                /*
                 * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
                 */
//                SacadorAvalista sacadorAvalista = new SacadorAvalista("JRimum Enterprise", "00.000.000/0001-91");
//
//                // Informando o endereço do sacador avalista.
//                Endereco enderecoSacAval = new Endereco();
//                enderecoSacAval.setUF(UnidadeFederativa.DF);
//                enderecoSacAval.setLocalidade("Brasília");
//                enderecoSacAval.setCep(new CEP("59000-000"));
//                enderecoSacAval.setBairro("Grande Centro");
//                enderecoSacAval.setLogradouro("Rua Eternamente Principal");
//                enderecoSacAval.setNumero("001");
//                sacadorAvalista.addEndereco(enderecoSacAval);

                /*
                 * INFORMANDO OS DADOS SOBRE O TÍTULO.
                 */
                
                // Informando dados sobre a conta bancária do título.
                ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_DO_BRASIL.create());
                contaBancaria.setNumeroDaConta(new NumeroDaConta(54607, "0"));
                contaBancaria.setCarteira(new Carteira(17));
                contaBancaria.setAgencia(new Agencia(381, "6"));
                
//                Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
                Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
                titulo.setNumeroDoDocumento("123456");
                titulo.setNossoNumero("99345678912");
                titulo.setDigitoDoNossoNumero("5");
                titulo.setValor(BigDecimal.valueOf(5.00));
                titulo.setDataDoDocumento(new Date());
                titulo.setDataDoVencimento(new Date());
                titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
                titulo.setAceite(EnumAceite.A);
                titulo.setDesconto(new BigDecimal(0.00));
                titulo.setDeducao(BigDecimal.ZERO);
                titulo.setMora(BigDecimal.ZERO);
                titulo.setAcrecimo(BigDecimal.ZERO);
                titulo.setValorCobrado(BigDecimal.ZERO);

                /*
                 * INFORMANDO OS DADOS SOBRE O BOLETO.
                 */
                Boleto boleto = new Boleto(titulo);
                
                boleto.setLocalPagamento("qualquer Banco até o Vencimento.");  
                boleto.setInstrucao8("Não receber após o vencimento");

                /*
                 * GERANDO O BOLETO BANCÁRIO.
                 */
                // Instanciando um objeto "BoletoViewer", classe responsável pela
                // geração do boleto bancário.
                BoletoViewer boletoViewer = new BoletoViewer(boleto);

                // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
                // pasta do projeto. Outros exemplos:
                // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
                // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
                File arquivoPdf = boletoViewer.getPdfAsFile("C:/boleto/MeuBoletoLeandro3.pdf");

                // Mostrando o boleto gerado na tela.
             //   mostreBoletoNaTela(arquivoPdf);
        }

       
//        private static void mostreBoletoNaTela(File arquivoBoleto) {
//
//                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
//                
//                try {
//                        desktop.open(arquivoBoleto);
//                } catch (IOException e) {
//                        e.printStackTrace();
//                }
//        }
}