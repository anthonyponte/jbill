/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author AnthonyPonte */
public class Empresa {
  private String ruc;
  private int tipo;
  private DocumentoIdentidad documentoIdentidad;
  private int documentoIdentidadNumero;
  private String razonSocial;
  private String nombre;

  public Empresa() {}

  public Empresa(
      DocumentoIdentidad documentoIdentidad, int documentoIdentidadNumero, String nombre) {
    this.documentoIdentidad = documentoIdentidad;
    this.documentoIdentidadNumero = documentoIdentidadNumero;
    this.nombre = nombre;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public int getTipo() {
    return tipo;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }

  public DocumentoIdentidad getDocumentoIdentidad() {
    return documentoIdentidad;
  }

  public void setDocumentoIdentidad(DocumentoIdentidad documentoIdentidad) {
    this.documentoIdentidad = documentoIdentidad;
  }

  public int getDocumentoIdentidadNumero() {
    return documentoIdentidadNumero;
  }

  public void setDocumentoIdentidadNumero(int documentoIdentidadNumero) {
    this.documentoIdentidadNumero = documentoIdentidadNumero;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Override
  public String toString() {
    return "Empresa{"
        + "ruc="
        + ruc
        + ", tipo="
        + tipo
        + ", documentoIdentidad="
        + documentoIdentidad
        + ", documentoIdentidadNumero="
        + documentoIdentidadNumero
        + ", razonSocial="
        + razonSocial
        + ", nombre="
        + nombre
        + '}';
  }
}
