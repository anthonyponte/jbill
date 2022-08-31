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

/**
 * @author AnthonyPonte
 */
public class ResumenDetalle {
  private int id;
  private Summary summary;
  private int numero;
  private Tipo tipoDocumento;
  private String serie;
  private int correlativo;
  private Empresa adquiriente;
  private Tipo tipoDocumentoReferencia;
  private String serieReferencia;
  private int correlativoReferencia;
  private Percepcion percepcion;
  private Tipo estado;
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
      Summary summary,
      int numero,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      Empresa adquiriente,
      Tipo tipoDocumentoReferencia,
      String serieReferencia,
      int correlativoReferencia,
      Percepcion percepcion,
      Tipo estado,
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
    this.summary = summary;
    this.numero = numero;
    this.tipoDocumento = tipoDocumento;
    this.serie = serie;
    this.correlativo = correlativo;
    this.adquiriente = adquiriente;
    this.tipoDocumentoReferencia = tipoDocumentoReferencia;
    this.serieReferencia = serieReferencia;
    this.correlativoReferencia = correlativoReferencia;
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

  public Summary getSummary() {
    return summary;
  }

  public void setSummary(Summary summary) {
    this.summary = summary;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public Tipo getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(Tipo tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getSerie() {
    return serie;
  }

  public void setSerie(String serie) {
    this.serie = serie;
  }

  public int getCorrelativo() {
    return correlativo;
  }

  public void setCorrelativo(int correlativo) {
    this.correlativo = correlativo;
  }

  public Empresa getAdquiriente() {
    return adquiriente;
  }

  public void setAdquiriente(Empresa adquiriente) {
    this.adquiriente = adquiriente;
  }

  public Tipo getTipoDocumentoReferencia() {
    return tipoDocumentoReferencia;
  }

  public void setTipoDocumentoReferencia(Tipo tipoDocumentoReferencia) {
    this.tipoDocumentoReferencia = tipoDocumentoReferencia;
  }

  public String getSerieReferencia() {
    return serieReferencia;
  }

  public void setSerieReferencia(String serieReferencia) {
    this.serieReferencia = serieReferencia;
  }

  public int getCorrelativoReferencia() {
    return correlativoReferencia;
  }

  public void setCorrelativoReferencia(int correlativoReferencia) {
    this.correlativoReferencia = correlativoReferencia;
  }

  public Percepcion getPercepcion() {
    return percepcion;
  }

  public void setPercepcion(Percepcion percepcion) {
    this.percepcion = percepcion;
  }

  public Tipo getEstado() {
    return estado;
  }

  public void setEstado(Tipo estado) {
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
    return "ResumenDetalle{"
        + "id="
        + id
        + ", summary="
        + summary
        + ", numero="
        + numero
        + ", tipoDocumento="
        + tipoDocumento
        + ", serie="
        + serie
        + ", correlativo="
        + correlativo
        + ", adquiriente="
        + adquiriente
        + ", tipoDocumentoReferencia="
        + tipoDocumentoReferencia
        + ", serieReferencia="
        + serieReferencia
        + ", correlativoReferencia="
        + correlativoReferencia
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
