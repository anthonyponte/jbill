/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.model;

/**
 * @author AnthonyPonte
 */
public class Empresa {
  private Tipo tipoDocumentoIdentidad;
  private String numero;
  private String nombre;

  public Empresa() {}

  public Empresa(Tipo tipoDocumentoIdentidad, String numero, String nombre) {
    this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
    this.numero = numero;
    this.nombre = nombre;
  }

  public Tipo getTipoDocumentoIdentidad() {
    return tipoDocumentoIdentidad;
  }

  public void setTipoDocumentoIdentidad(Tipo tipoDocumentoIdentidad) {
    this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
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
        + "tipoDocumentoIdentidad="
        + tipoDocumentoIdentidad
        + ", numero="
        + numero
        + ", nombre="
        + nombre
        + '}';
  }
}
