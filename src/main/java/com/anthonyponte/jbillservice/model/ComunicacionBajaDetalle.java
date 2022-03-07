/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class ComunicacionBajaDetalle {

  private ComunicacionBaja comunicacion;
  private int id;
  private int numero;
  private Documento documento;
  private String motivo;

  public ComunicacionBajaDetalle() {}

  public ComunicacionBajaDetalle(int numero, Documento documento, String motivo) {
    this.numero = numero;
    this.documento = documento;
    this.motivo = motivo;
  }

  public ComunicacionBajaDetalle(
      ComunicacionBaja comunicacion, int id, int numero, Documento documento, String motivo) {
    this.comunicacion = comunicacion;
    this.id = id;
    this.numero = numero;
    this.documento = documento;
    this.motivo = motivo;
  }

  public ComunicacionBaja getComunicacion() {
    return comunicacion;
  }

  public void setComunicacion(ComunicacionBaja comunicacion) {
    this.comunicacion = comunicacion;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public Documento getDocumento() {
    return documento;
  }

  public void setDocumento(Documento documento) {
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
        + "comunicacion="
        + comunicacion
        + ", id="
        + id
        + ", numero="
        + numero
        + ", documento="
        + documento
        + ", motivo="
        + motivo
        + '}';
  }
}
