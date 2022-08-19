package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gui.BCPUtil;
/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 18/08/2022
 * Ultima alteracao: 18/08/2022
 * Nome: MultiplasFilas
 * Funcao: Organiza os processos dividindo em duas filas, a primeira
 * utiliza a prioridade e a segunda o tempo
 * **************************************************************
 */
public class MultiplasFilas extends Agendador {

  public MultiplasFilas() {
    super(5);
  }

  @Override
  public List<BCP> order(List<BCP> list) {
    // SEPARO A MINHA LISTA EM VARIAS LISTAS DE ACORDO A SUA PRIORIDADE
    Map<Integer,List<BCP>> map = list.stream().collect(Collectors.groupingBy(BCP::getPrioridade));
    ArrayList<BCP> orderList = new ArrayList<>();
    // ORGANIZO CADA LISTA PELO TEMPO DE EXECUCAO
    map.forEach((prioridade, lista) -> {
      lista = lista.stream()
      .map((t) -> {
        t.setColor(BCPUtil.createColor(prioridade));
        return t;
      })
      .sorted(Comparator.comparing(BCP::getTempoDeExecucao))
      .collect(Collectors.toList());
      orderList.addAll(lista);
    });
    return orderList;
  }

  @Override
  public List<BCP> addElement(List<BCP> list, BCP element) {
    Map<Integer,List<BCP>> map = list.stream().collect(Collectors.groupingBy(BCP::getPrioridade));
    ArrayList<BCP> orderList = new ArrayList<>();
    if(map.containsKey(element.getPrioridade())){
      map.get(element.getPrioridade()).add(element);
    }else{
      map.put(element.getPrioridade(), Arrays.asList(element));
    }
    map.forEach((prioridade, lista) -> {
      lista = lista.stream()
      .sorted(Comparator.comparing(BCP::getTempoDeExecucao))
      .collect(Collectors.toList());
      orderList.addAll(lista);
    });
    return orderList;
  }
  
}
