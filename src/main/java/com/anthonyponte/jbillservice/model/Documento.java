/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class Documento {

  private String tipo;
  private String serie;
  private int correlativo;

  public Documento() {}

  public Documento(String tipo, String serie, int correlativo) {
    this.tipo = tipo;
    this.serie = serie;
    this.correlativo = correlativo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getSerie() {
    return serie;
  }

  public void setSerie(String serie) {
    this.serie = serie;
  }

  public int getCorrelativo() {
    return correlativo;
  }

  public void setCorrelativo(int correlativo) {
    this.correlativo = correlativo;
  }

  @Override
  public String toString() {
    return "Documento{"
        + "tipo="
        + tipo
        + ", serie="
        + serie
        + ", correlativo="
        + correlativo
        + '}';
  }
}
