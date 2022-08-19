package gui;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import model.BCP;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 10/08/2022
 * Ultima alteracao: 14/08/2022
 * Nome: BCPutil
 * Funcao: Classe utilitaria que fornece metodos que ajudam na
 * interface
 * **************************************************************
 */
public abstract class BCPUtil {
  private static int colorindex=-1;

  /**
   * Retorna uma cor para o processo
   * @return
   */
  public static String createColor(){
    colorindex = colorindex >= 4 ? 0 : colorindex + 1;
    String color = "";
    switch (colorindex){
      case 0:
        color = "#FFB3BA";
        break;
      case 1:
        color = "#FFE8B3";
        break;
      case 2:
        color = "#E2FFBA";
        break;
      case 3:
        color = "#b2e2f2";
        break;
      case 4:
        color = "#fdcae1";
        break;
    }
    return color;
  }


  /**
   * Retorna uma cor especifica
   * @param i indice da cor
   * @return
   */
  public static String createColor(int i){
    String color = "";
    switch (i){
      case 5:
        color = "#e4c1f9"; // ROXO
        break;
      case 1:
        color = "#ff99c8"; // VERMELHO
        break;
      case 2:
        color = "#fcf6bd"; // AMARELO
        break;
      case 3:
        color = "#d0f4de"; // VERDE
        break;
      case 4:
        color = "#a9def9"; // AZUL
        break;
    }
    return color;
  }

  /**
   * Publica uma visualizacao de um processo na interface
   * @param processo
   * @param hb
   * @param type
   */
  public static void publishViewProcess(BCP processo, VBox hb, int type){
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(5));
    String css = String.format("-fx-border-color: #9f9f9f; -fx-background-color: %s;",processo.getColor());
    ArrayList<Node> nodes = new ArrayList<>();
    nodes.add(new Label("Processo "+processo.getId()));
    nodes.add(new Separator());
    switch (type){
      case 0:
        nodes.add(new Label("Ordem: "+(processo.getOrdemDeCriacao()+1)));
        break;
      case 1:
        nodes.add(new Label("Priority: "+processo.getPrioridade()));
        break;
      case 2:
        nodes.add(new Label("Time: "+processo.getTempoDeExecucao()+"s"));
        break;
      case 3:
        nodes.add(new Label("To Finish: "+processo.getTempoDeExecucao()+"s"));
        nodes.add(new Label("DeadLine: "+processo.getDeadline()+"s"));
        break;  
      default:
        nodes.add(new Label("Time: "+processo.getTempoDeExecucao()+"s"));
        nodes.add(new Label("Priority: "+processo.getPrioridade()));

        break;
    }
    Label lbl = new Label(((int)(processo.getProgress()*100))+"%");
    nodes.add(lbl);
    processo.setLbl_progress(lbl);
    vbox.setStyle(css);
    vbox.getChildren().addAll(nodes);

    ObservableList<Node> oList = hb.getChildren();
    Platform.runLater(() -> oList.add(vbox));
  }
}
