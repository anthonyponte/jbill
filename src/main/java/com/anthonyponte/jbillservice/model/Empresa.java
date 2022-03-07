/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author AnthonyPonte */
public class Empresa {
  private String ruc;
  private int tipo;
  private String razonSocial;

  public Empresa() {}

  public Empresa(String ruc, int tipo, String razonSocial) {
    this.ruc = ruc;
    this.tipo = tipo;
    this.razonSocial = razonSocial;
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

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  @Override
  public String toString() {
    return "Empresa{" + "ruc=" + ruc + ", tipo=" + tipo + ", razonSocial=" + razonSocial + '}';
  }
}
