/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class Bill {
  private Empresa emisor;
  private TipoDocumento tipoDocumento;
  private String serie;
  private int correlativo;
  private String statusCode;
  private String statusMessage;
  private byte[] content;
  private String cdrStatusCode;
  private String cdrStatusMessage;
  private byte[] cdrContent;

  public Bill() {}

  public Bill(Empresa emisor, TipoDocumento tipoDocumento, String serie, int correlativo) {
    this.emisor = emisor;
    this.tipoDocumento = tipoDocumento;
    this.serie = serie;
    this.correlativo = correlativo;
  }

  public Empresa getEmisor() {
    return emisor;
  }

  public void setEmisor(Empresa emisor) {
    this.emisor = emisor;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(TipoDocumento tipoDocumento) {
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

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getCdrStatusCode() {
    return cdrStatusCode;
  }

  public void setCdrStatusCode(String cdrStatusCode) {
    this.cdrStatusCode = cdrStatusCode;
  }

  public String getCdrStatusMessage() {
    return cdrStatusMessage;
  }

  public void setCdrStatusMessage(String cdrStatusMessage) {
    this.cdrStatusMessage = cdrStatusMessage;
  }

  public byte[] getCdrContent() {
    return cdrContent;
  }

  public void setCdrContent(byte[] cdrContent) {
    this.cdrContent = cdrContent;
  }

  @Override
  public String toString() {
    return "Bill{"
        + "emisor="
        + emisor
        + ", tipoDocumento="
        + tipoDocumento
        + ", serie="
        + serie
        + ", correlativo="
        + correlativo
        + ", statusCode="
        + statusCode
        + ", statusMessage="
        + statusMessage
        + ", content="
        + content
        + ", cdrStatusCode="
        + cdrStatusCode
        + ", cdrStatusMessage="
        + cdrStatusMessage
        + ", cdrContent="
        + cdrContent
        + '}';
  }
}
