/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.model;

import java.util.Date;
import java.util.List;

/** @author anthony */
public class Comunicacion extends Summary {

  private List<ComunicacionDetalle> comunicacionBajaDetalles;

  public Comunicacion() {}

  public Comunicacion(List<ComunicacionDetalle> comunicacionBajaDetalles) {
    this.comunicacionBajaDetalles = comunicacionBajaDetalles;
  }

  public Comunicacion(
      List<ComunicacionDetalle> comunicacionBajaDetalles,
      int id,
      String ubl,
      String version,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      Date fechaEmision,
      Date fechaReferencia,
      Empresa emisor,
      String nombreZip,
      byte[] zip) {
    super(
        id,
        ubl,
        version,
        tipoDocumento,
        serie,
        correlativo,
        fechaEmision,
        fechaReferencia,
        emisor,
        nombreZip,
        zip);
    this.comunicacionBajaDetalles = comunicacionBajaDetalles;
  }

  public Comunicacion(
      List<ComunicacionDetalle> comunicacionBajaDetalles,
      String ubl,
      String version,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      Date fechaEmision,
      Date fechaReferencia,
      Empresa emisor,
      String nombreZip,
      byte[] zip) {
    super(
        ubl,
        version,
        tipoDocumento,
        serie,
        correlativo,
        fechaEmision,
        fechaReferencia,
        emisor,
        nombreZip,
        zip);
    this.comunicacionBajaDetalles = comunicacionBajaDetalles;
  }

  public List<ComunicacionDetalle> getComunicacionBajaDetalles() {
    return comunicacionBajaDetalles;
  }

  public void setComunicacionBajaDetalles(List<ComunicacionDetalle> comunicacionBajaDetalles) {
    this.comunicacionBajaDetalles = comunicacionBajaDetalles;
  }

  @Override
  public String toString() {
    return super.toString()
        + " ComunicacionBaja{"
        + "comunicacionBajaDetalles="
        + comunicacionBajaDetalles
        + '}';
  }
  
  
}
