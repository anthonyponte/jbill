/*
 * Copyright (C) 2022 AnthonyPonte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.anthonyponte.jbill.model;

/** @author AnthonyPonte */
public class ResumenDetalle {
  private int id;
  private Resumen resumenDiario;
  private int numero;
  private Bill documento;
  private Empresa adquiriente;
  private Bill documentoReferencia;
  private Percepcion percepcion;
  private Estado estado;
  private double importeTotal;
  private Tipo moneda;
  private Operacion gravadas;
  private Operacion exoneradas;
  private Operacion inafectas;
  private Operacion gratuitas;
  private Operacion exportacion;
  private OtrosCargos otrosCargos;
  private Impuesto igv;
  private Impuesto isc;
  private Impuesto otrosTributos;
  private Impuesto impuestoBolsa;

  public ResumenDetalle() {}

  public ResumenDetalle(
      int id,
      Resumen resumenDiario,
      int numero,
      Bill documento,
      Empresa adquiriente,
      Bill documentoReferencia,
      Percepcion percepcion,
      Estado estado,
      double importeTotal,
      Tipo moneda,
      Operacion gravadas,
      Operacion exoneradas,
      Operacion inafectas,
      Operacion gratuitas,
      Operacion exportacion,
      OtrosCargos otrosCargos,
      Impuesto igv,
      Impuesto isc,
      Impuesto otrosTributos,
      Impuesto impuestoBolsa) {
    this.id = id;
    this.resumenDiario = resumenDiario;
    this.numero = numero;
    this.documento = documento;
    this.adquiriente = adquiriente;
    this.documentoReferencia = documentoReferencia;
    this.percepcion = percepcion;
    this.estado = estado;
    this.importeTotal = importeTotal;
    this.moneda = moneda;
    this.gravadas = gravadas;
    this.exoneradas = exoneradas;
    this.inafectas = inafectas;
    this.gratuitas = gratuitas;
    this.exportacion = exportacion;
    this.otrosCargos = otrosCargos;
    this.igv = igv;
    this.isc = isc;
    this.otrosTributos = otrosTributos;
    this.impuestoBolsa = impuestoBolsa;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Resumen getResumenDiario() {
    return resumenDiario;
  }

  public void setResumenDiario(Resumen resumenDiario) {
    this.resumenDiario = resumenDiario;
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

  public Empresa getAdquiriente() {
    return adquiriente;
  }

  public void setAdquiriente(Empresa adquiriente) {
    this.adquiriente = adquiriente;
  }

  public Bill getDocumentoReferencia() {
    return documentoReferencia;
  }

  public void setDocumentoReferencia(Bill documentoReferencia) {
    this.documentoReferencia = documentoReferencia;
  }

  public Percepcion getPercepcion() {
    return percepcion;
  }

  public void setPercepcion(Percepcion percepcion) {
    this.percepcion = percepcion;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }

  public double getImporteTotal() {
    return importeTotal;
  }

  public void setImporteTotal(double importeTotal) {
    this.importeTotal = importeTotal;
  }

  public Tipo getMoneda() {
    return moneda;
  }

  public void setMoneda(Tipo moneda) {
    this.moneda = moneda;
  }

  public Operacion getGravadas() {
    return gravadas;
  }

  public void setGravadas(Operacion gravadas) {
    this.gravadas = gravadas;
  }

  public Operacion getExoneradas() {
    return exoneradas;
  }

  public void setExoneradas(Operacion exoneradas) {
    this.exoneradas = exoneradas;
  }

  public Operacion getInafectas() {
    return inafectas;
  }

  public void setInafectas(Operacion inafectas) {
    this.inafectas = inafectas;
  }

  public Operacion getGratuitas() {
    return gratuitas;
  }

  public void setGratuitas(Operacion gratuitas) {
    this.gratuitas = gratuitas;
  }

  public Operacion getExportacion() {
    return exportacion;
  }

  public void setExportacion(Operacion exportacion) {
    this.exportacion = exportacion;
  }

  public OtrosCargos getOtrosCargos() {
    return otrosCargos;
  }

  public void setOtrosCargos(OtrosCargos otrosCargos) {
    this.otrosCargos = otrosCargos;
  }

  public Impuesto getIgv() {
    return igv;
  }

  public void setIgv(Impuesto igv) {
    this.igv = igv;
  }

  public Impuesto getIsc() {
    return isc;
  }

  public void setIsc(Impuesto isc) {
    this.isc = isc;
  }

  public Impuesto getOtrosTributos() {
    return otrosTributos;
  }

  public void setOtrosTributos(Impuesto otrosTributos) {
    this.otrosTributos = otrosTributos;
  }

  public Impuesto getImpuestoBolsa() {
    return impuestoBolsa;
  }

  public void setImpuestoBolsa(Impuesto impuestoBolsa) {
    this.impuestoBolsa = impuestoBolsa;
  }

  @Override
  public String toString() {
    return "ResumenDiarioDetalle{"
        + "id="
        + id
        + ", resumenDiario="
        + resumenDiario
        + ", numero="
        + numero
        + ", documento="
        + documento
        + ", remitente="
        + adquiriente
        + ", documentoReferencia="
        + documentoReferencia
        + ", percepcion="
        + percepcion
        + ", estado="
        + estado
        + ", importeTotal="
        + importeTotal
        + ", moneda="
        + moneda
        + ", gravadas="
        + gravadas
        + ", exoneradas="
        + exoneradas
        + ", inafectas="
        + inafectas
        + ", gratuitas="
        + gratuitas
        + ", exportacion="
        + exportacion
        + ", otrosCargos="
        + otrosCargos
        + ", igv="
        + igv
        + ", isc="
        + isc
        + ", otrosTributos="
        + otrosTributos
        + ", impuestoBolsa="
        + impuestoBolsa
        + '}';
  }
}
