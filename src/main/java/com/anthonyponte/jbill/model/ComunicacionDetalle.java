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
  private Bill documento;
  private String motivo;

  public ComunicacionDetalle() {}

  public ComunicacionDetalle(int id, Summary summary, int numero, Bill documento, String motivo) {
    this.id = id;
    this.summary = summary;
    this.numero = numero;
    this.documento = documento;
    this.motivo = motivo;
  }

  public ComunicacionDetalle(int numero, Bill documento, String motivo) {
    this.numero = numero;
    this.documento = documento;
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

  public Bill getDocumento() {
    return documento;
  }

  public void setDocumento(Bill documento) {
    this.documento = documento;
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
        + ", documento="
        + documento
        + ", motivo="
        + motivo
        + '}';
  }
}
