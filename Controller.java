import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import gui.BCPUtil;
import gui.WrapperGui;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.*;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 13/07/2022
 * Ultima alteracao: 21/07/2022
 * Nome: Controller
 * Funcao: Controla as alteracoes da interface
 * **************************************************************
 */
public class Controller implements Initializable {

  public VBox vbox_inicia;
  public VBox vbox_pronto;
  public VBox vbox_execu;
  public VBox vbox_bloq;
  public VBox vbox_final;
  public TextField tf_nome;
  public Slider sld_tempodeexec;
  public Slider sld_prioridade;
  public CheckBox check_auto;
  public Button btn_criar;
  public Slider sld_slicing;
  public Button btn_iniciar;
  public Button btn_parar;
  public Button btn_limpar;
  public TitledPane tp_processo;
  public TitledPane tp_inicia;
  public MenuButton cbx_escal;
  public CheckMenuItem menuItem_Fifo;
  public CheckMenuItem menuItem_Prio;
  public CheckMenuItem menuItem_SJF;
  public CheckMenuItem menuItem_Garan;
  public CheckMenuItem menuItem_MultiF;
  public CheckMenuItem menuItem_RoundR;
  public Text txt_info;
  private ArrayList<BCP> processos;
  private CheckMenuItem[] items;

  private ThreadManager threadManager;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    processos = new ArrayList<>();
    items = new CheckMenuItem[] {menuItem_Fifo, menuItem_Prio, menuItem_SJF, menuItem_Garan, menuItem_MultiF, menuItem_RoundR};

    EventHandler<ActionEvent> eventCriarProcesso = e -> {
      BCP novoProcesso;
      if(check_auto.isSelected()){
        Random random = new Random();
        novoProcesso = new BCP("", 1 + random.nextInt(5), 1 + random.nextInt(10));
      }else{
        novoProcesso = new BCP("", (int) sld_prioridade.getValue(), (int) sld_tempodeexec.getValue());
      }
      novoProcesso.setTimeslicing(sld_slicing);
      BCPUtil.publishViewProcess(novoProcesso, vbox_inicia, 4);
      processos.add(novoProcesso);
      if(threadManager==null||!threadManager.isAlive()){
        if (btn_limpar.isDisable()) {
          btn_limpar.setDisable(false);
        }
        if(btn_iniciar.isDisable())
          btn_iniciar.setDisable(false);
      }
    };
    EventHandler<ActionEvent> eventClickOnCheck = e -> {
      sld_prioridade.setDisable(check_auto.isSelected());
      sld_tempodeexec.setDisable(check_auto.isSelected());
    };
    EventHandler<ActionEvent> eventSelectAgendador = e -> {
      for (CheckMenuItem item : items) {
        item.setSelected(false);
      }
      CheckMenuItem itemSelecionado = (CheckMenuItem) e.getSource();
      itemSelecionado.setSelected(true);
      cbx_escal.setText(itemSelecionado.getText());
      if(itemSelecionado.getText().equals("Multiplas Filas")) {
        txt_info.setVisible(true);
        txt_info.setText("Na simulacao desse tipo de escalonamento os processos de uma mesma fila serao representados por uma cor em comum. As filas sao ordenadas por prioridade enquanto os processos com menor tempo de execucao sao colocados no inicio.");
      } else if(itemSelecionado.getText().equals("Garantido")){
        txt_info.setVisible(true);
        txt_info.setText("Nesse cenario, o algoritmo de escalonamento organiza os processos para que sejam executados antes de atingir um certo deadline, entretando nem sempre eh possivel executar todos antes do prazo. Logo os processos que podem ser executados recebem a cor verde enquanto que os que nao, recebem a cor vermelha");
      }else txt_info.setVisible(false);
    };
    EventHandler<ActionEvent> eventDispararGerenciador = e -> {
      cbx_escal.setDisable(true);
      int type = getChoiceBoxSelectedIndex();
      Agendador agendador = getAgendador(type);
      processos = (ArrayList<BCP>) agendador.order(processos);

      WrapperGui gui = new WrapperGui(vbox_inicia, vbox_pronto, vbox_execu, vbox_bloq, vbox_final);
      // PEGAR O PRIMEIRO DA LISTA E PASSAR PARA EXECUTANDO
      Runnable onPostExecute = () -> {
        btn_limpar.setDisable(false);
        btn_parar.setDisable(true);
        System.out.println("Processo finalizado");
      };
      threadManager = new ThreadManager(sld_slicing, processos, agendador, gui, onPostExecute);
      threadManager.start();

      btn_iniciar.setDisable(true);
      btn_limpar.setDisable(true);
      btn_parar.setDisable(false);
    };
    EventHandler<ActionEvent> eventPararGerenciador = e -> {
      threadManager.stop();
      if (threadManager.isAlive())
        threadManager.interrupt();
      btn_limpar.setDisable(false);
      btn_parar.setDisable(true);
    };
    EventHandler<ActionEvent> eventApagarProcessos = e -> {
      processos.forEach(bcp -> {
        if (bcp.isAlive())
          bcp.interrupt();
      });
      processos.clear();
      btn_limpar.setDisable(true);
      btn_iniciar.setDisable(true);
      cbx_escal.setDisable(false);
      Platform.runLater(() -> {
        vbox_inicia.getChildren().clear();
        vbox_pronto.getChildren().clear();
        vbox_bloq.getChildren().clear();
        vbox_execu.getChildren().clear();
        vbox_final.getChildren().clear();
      });
      BCP.resetOrdemDeCriacao();
    };

    btn_criar.setOnAction(eventCriarProcesso);
    check_auto.setOnAction(eventClickOnCheck);
    btn_iniciar.setOnAction(eventDispararGerenciador);
    btn_parar.setOnAction(eventPararGerenciador);
    btn_limpar.setOnAction(eventApagarProcessos);

    for (CheckMenuItem item : items) item.setOnAction(eventSelectAgendador);
  }

  /**
   * Pega o indice do item selecionado para
   * saber qual protocolo usar
   * 
   * @return
   */
  public int getChoiceBoxSelectedIndex() {
    int i = 0;
    for (CheckMenuItem item : items) {
      if (item.isSelected())
        return i;
      i++;
    }
    return 0;
  }

  /**
   * Retorna o protocolo de acordo um indice
   * 
   * @param i
   * @return
   */
  public Agendador getAgendador(int i) {
    switch (i) {
      case 1:
        return new Prioridade();
      case 2:
        return new ShortedJobFirst();
      case 3:
        return new Garantido();  
      case 4:
        return new MultiplasFilas();
      case 5:
        return new RoundRobin();  
      default:
        return new Fifo();
    }
  }

}
