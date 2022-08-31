/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.model;

/**
 * @author AnthonyPonte
 */
public class Empresa {
  private Tipo tipo;
  private String numero;
  private String nombre;

  public Empresa() {}

  public Empresa(Tipo tipo, String numero, String nombre) {
    this.tipo = tipo;
    this.numero = numero;
    this.nombre = nombre;
  }

  public Tipo getTipo() {
    return tipo;
  }

  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
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
    return "Empresa{" + "tipo=" + tipo + ", numero=" + numero + ", nombre=" + nombre + '}';
  }
}
