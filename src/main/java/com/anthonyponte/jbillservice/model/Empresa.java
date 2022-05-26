/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author AnthonyPonte */
public class Empresa {
  private int tipo;
  private TipoDocumentoIdentidad tipoDocumentoIdentidad;
  private String numeroDocumentoIdentidad;
  private String nombre;

  public Empresa() {}

  public Empresa(
      int tipo,
      TipoDocumentoIdentidad tipoDocumentoIdentidad,
      String numeroDocumentoIdentidad,
      String nombre) {
    this.tipo = tipo;
    this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
    this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
    this.nombre = nombre;
  }

  public int getTipo() {
    return tipo;
  }

  public void setTipo(int tipo) {
    this.tipo = tipo;
  }

  public TipoDocumentoIdentidad getTipoDocumentoIdentidad() {
    return tipoDocumentoIdentidad;
  }

  public void setTipoDocumentoIdentidad(TipoDocumentoIdentidad tipoDocumentoIdentidad) {
    this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
  }

  public String getNumeroDocumentoIdentidad() {
    return numeroDocumentoIdentidad;
  }

  public void setNumeroDocumentoIdentidad(String numeroDocumentoIdentidad) {
    this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
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
        + "tipo="
        + tipo
        + ", tipoDocumentoIdentidad="
        + tipoDocumentoIdentidad
        + ", numeroDocumentoIdentidad="
        + numeroDocumentoIdentidad
        + ", nombre="
        + nombre
        + '}';
  }
}
