package gui;

import javafx.scene.layout.VBox;
/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 10/08/2022
 * Ultima alteracao: 19/08/2022
 * Nome: WrapperGui
 * Funcao: Reune todas os paines de rolagem da interface
 * **************************************************************
 */
public class WrapperGui {
  private final VBox vb_inicio;
  private final VBox vb_pronto;
  private final VBox vb_exec;
  private final VBox vb_bloq;
  private final VBox vb_final;

  public WrapperGui(VBox vb_inicio, VBox vb_pronto, VBox vb_exec, VBox vb_bloq, VBox vb_final) {
    this.vb_inicio = vb_inicio;
    this.vb_pronto = vb_pronto;
    this.vb_exec = vb_exec;
    this.vb_bloq = vb_bloq;
    this.vb_final = vb_final;
  }

  public VBox getVb_inicio() {
    return vb_inicio;
  }

  public VBox getVb_pronto() {
    return vb_pronto;
  }

  public VBox getVb_exec() {
    return vb_exec;
  }

  public VBox getVb_bloq() {
    return vb_bloq;
  }

  public VBox getVb_final() {
    return vb_final;
  }
}
