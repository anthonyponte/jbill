/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.model;

/** @author AnthonyPonte */
public class StatusResponse {
  protected byte[] content;
  protected String statusCode;

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public String toString() {
    return "StatusResponde{" + "content=" + content + ", statusCode=" + statusCode + '}';
  }
}
