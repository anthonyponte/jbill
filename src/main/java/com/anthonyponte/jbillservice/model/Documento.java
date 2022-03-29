/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class Documento {

  private TipoDocumento tipoDocumento;
  private String serie;
  private int correlativo;

  public Documento() {}

  public Documento(TipoDocumento tipoDocumento, String serie, int correlativo) {
    this.tipoDocumento = tipoDocumento;
    this.serie = serie;
    this.correlativo = correlativo;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(TipoDocumento tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
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
        + "tipoDocumento="
        + tipoDocumento
        + ", serie="
        + serie
        + ", correlativo="
        + correlativo
        + '}';
  }
}
