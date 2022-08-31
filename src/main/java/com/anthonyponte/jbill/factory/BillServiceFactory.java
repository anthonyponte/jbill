/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.factory;

import com.anthonyponte.jbill.controller.MainController;
import com.anthonyponte.jbill.controller.UsuarioController;
import com.anthonyponte.jbill.idao.IBillService;
import com.anthonyponte.jbill.idao.IBetaBillService;
import com.anthonyponte.jbill.model.StatusResponse;
import jakarta.activation.DataHandler;
import java.util.prefs.Preferences;

/** @author AnthonyPonte */
public class BillServiceFactory {
  private static final Preferences PREFERENCES =
      Preferences.userRoot().node(MainController.class.getPackageName());
  private final boolean WEB_SERVICE =
      PREFERENCES.getBoolean(UsuarioController.SUNAT_WEB_SERVICE, false);
  private final beta.sunat.gob.pe.BillService betaService;
  private final sunat.gob.pe.BillService service;

  public BillServiceFactory() {
    betaService = new IBetaBillService();
    service = new IBillService();
  }

  public String sendSummary(String fileName, DataHandler contentFile, String partyType) {
    String ticket;

    if (WEB_SERVICE) ticket = service.sendSummary(fileName, contentFile, partyType);
    else ticket = betaService.sendSummary(fileName, contentFile, partyType);

    return ticket;
  }

  public StatusResponse getStatus(String ticket) {
    StatusResponse responde;

    if (WEB_SERVICE) {
      sunat.gob.pe.StatusResponse statusResponse = service.getStatus(ticket);

      responde = new StatusResponse();
      responde.setStatusCode(statusResponse.getStatusCode());
      responde.setContent(statusResponse.getContent());

    } else {

      beta.sunat.gob.pe.StatusResponse statusResponse = betaService.getStatus(ticket);

      responde = new StatusResponse();
      responde.setStatusCode(statusResponse.getStatusCode());
      responde.setContent(statusResponse.getContent());
    }

    return responde;
  }
}
