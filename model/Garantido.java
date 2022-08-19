package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import gui.BCPUtil;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 18/08/2022
 * Ultima alteracao: 18/08/2022
 * Nome: Garantido
 * Funcao: Organiza os processos de acordo com a possibilidade 
 * deles serem executados dentro de um certo deadline
 * **************************************************************
 */
public class Garantido extends Agendador {

  public Garantido() {
    super(3);
  }

  @Override
  public List<BCP> order(List<BCP> list) {
    BCP processoComMaiorDL = list.stream()
    .filter((t) -> t.getProgress()<1.0f)
    .reduce((t, u) -> (t.getDeadline()-t.getTempoDecorrido()) > (u.getDeadline()-t.getTempoDecorrido()) ? t : u).get();
    int maiorDeadLine = (processoComMaiorDL.getDeadline()-processoComMaiorDL.getTempoDecorrido());
    int somaDosTemposDeExec = processoComMaiorDL.getTempoDeExecucao();
    List<BCP> processados = new ArrayList<>();
    List<BCP> mortos = new ArrayList<>();

    for (BCP bcp : list) {
      if(bcp.equals(processoComMaiorDL)) continue;
      if(bcp.getProgress() == 1f){
        bcp.setColor(BCPUtil.createColor(1));
        mortos.add(bcp);
        continue;
      }
      somaDosTemposDeExec+=(bcp.getTempoDeExecucao()-bcp.getTempoEmExecucao());
      if(somaDosTemposDeExec<maiorDeadLine){
        bcp.setColor(BCPUtil.createColor(3));
        processados.add(bcp);
      }else{
        bcp.setColor(BCPUtil.createColor(1));
        bcp.setProgressComplete();
        mortos.add(bcp);
        somaDosTemposDeExec-=(bcp.getTempoDeExecucao()-bcp.getTempoEmExecucao());
      }
    }
    processoComMaiorDL.setColor(BCPUtil.createColor(3));
    processados.add(processoComMaiorDL);
  
    processados = processados.stream()
      .sorted(Comparator.comparing(BCP::getDeadline))
      .collect(Collectors.toList());
    processados.addAll(mortos);
    return processados;
  }

  @Override
  public List<BCP> addElement(List<BCP> list, BCP element) {
    list.add(0,element);
    return order(list);
  }
  
}
