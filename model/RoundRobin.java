package model;

import java.util.List;

/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 18/08/2022
 * Ultima alteracao: 18/08/2022
 * Nome: RoundRobin
 * Funcao: Organiza os processos de acordo a ordem de tempo deles
 * e adiciona um novo no final da fila circular
 * **************************************************************
 */
public class RoundRobin extends Agendador{

  public RoundRobin() {
    super(4);
  }

  @Override
  public List<BCP> order(List<BCP> list) {
    return list;
  }

  @Override
  public List<BCP> addElement(List<BCP> list, BCP element) {
    list.add(element);
    return list;
  }
  
}
