/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbill.model;

/** @author anthony */
public class ComunicacionDetalle {

  private int id;
  private Comunicacion comunicacionBaja;
  private int numero;
  private Bill documento;
  private String motivo;

  public ComunicacionDetalle() {}

  public ComunicacionDetalle(
      int id, Comunicacion comunicacionBaja, int numero, Bill documento, String motivo) {
    this.id = id;
    this.comunicacionBaja = comunicacionBaja;
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

  public Comunicacion getComunicacionBaja() {
    return comunicacionBaja;
  }

  public void setComunicacionBaja(Comunicacion comunicacionBaja) {
    this.comunicacionBaja = comunicacionBaja;
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
    return "ComunicacionBajaDetalle{"
        + "id="
        + id
        + ", comunicacionBaja="
        + comunicacionBaja
        + ", numero="
        + numero
        + ", documento="
        + documento
        + ", motivo="
        + motivo
        + '}';
  }
}
