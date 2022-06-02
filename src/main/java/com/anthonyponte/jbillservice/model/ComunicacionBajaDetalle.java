/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class ComunicacionBajaDetalle {

  private int id;
  private ComunicacionBaja comunicacionBaja;
  private int numero;
  private Bill documento;
  private String motivo;

  public ComunicacionBajaDetalle() {}

  public ComunicacionBajaDetalle(
      int id, ComunicacionBaja comunicacionBaja, int numero, Bill documento, String motivo) {
    this.id = id;
    this.comunicacionBaja = comunicacionBaja;
    this.numero = numero;
    this.documento = documento;
    this.motivo = motivo;
  }

  public ComunicacionBajaDetalle(int numero, Bill documento, String motivo) {
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

  public ComunicacionBaja getComunicacionBaja() {
    return comunicacionBaja;
  }

  public void setComunicacionBaja(ComunicacionBaja comunicacionBaja) {
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
