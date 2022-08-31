/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbill.model;

/**
 * @author anthony
 */
public class ComunicacionDetalle {
  private int id;
  private Summary summary;
  private int numero;
  private Tipo tipoDocumento;
  private String serie;
  private int correlativo;
  private String motivo;

  public ComunicacionDetalle() {}

  public ComunicacionDetalle(
      int id,
      Summary summary,
      int numero,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      String motivo) {
    this.id = id;
    this.summary = summary;
    this.numero = numero;
    this.tipoDocumento = tipoDocumento;
    this.serie = serie;
    this.correlativo = correlativo;
    this.motivo = motivo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Summary getSummary() {
    return summary;
  }

  public void setSummary(Summary summary) {
    this.summary = summary;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public Tipo getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(Tipo tipoDocumento) {
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

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  @Override
  public String toString() {
    return "ComunicacionDetalle{"
        + "id="
        + id
        + ", summary="
        + summary
        + ", numero="
        + numero
        + ", tipoDocumento="
        + tipoDocumento
        + ", serie="
        + serie
        + ", correlativo="
        + correlativo
        + ", motivo="
        + motivo
        + '}';
  }
}
