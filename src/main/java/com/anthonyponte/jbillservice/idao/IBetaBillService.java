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

package com.anthonyponte.jbillservice.idao;

import beta.sunat.gob.pe.BillService;
import beta.sunat.gob.pe.BillService_Service;
import beta.sunat.gob.pe.StatusResponse;
import beta.sunat.gob.pe.StatusResponseAR;
import com.anthonyponte.jbillservice.controller.MainController;
import com.anthonyponte.jbillservice.controller.UsuarioController;
import jakarta.activation.DataHandler;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/** @author AnthonyPonte */
public class IBetaBillService implements BillService {
  private static final Preferences PREFERENCES =
      Preferences.userRoot().node(MainController.class.getPackageName());
  private final String RUC = PREFERENCES.get(UsuarioController.RUC, "");
  private final String USUARIO = PREFERENCES.get(UsuarioController.CLAVE_SOL_USUARIO, "");
  private final String CONTRASENA = PREFERENCES.get(UsuarioController.CLAVE_SOL_CONTRASENA, "");

  @Override
  public StatusResponse getStatus(String ticket) {
    StatusResponse statusResponse = null;

    try {
      BillService_Service service = new BillService_Service();
      BillService port = service.getBillServicePort();
      BindingProvider binding = (BindingProvider) port;

      @SuppressWarnings("rawtypes")
      List<Handler> handlers = new ArrayList<>();
      SOAPHandler<SOAPMessageContext> handler = new ISOAPHanlder(RUC + USUARIO, CONTRASENA);
      handlers.add(handler);
      binding.getBinding().setHandlerChain(handlers);

      statusResponse = port.getStatus(ticket);
    } catch (Exception ex) {
      Logger.getLogger(IBillService.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(), ticket, JOptionPane.ERROR_MESSAGE);
    }

    return statusResponse;
  }

  @Override
  public StatusResponseAR getStatusAR(
      String rucComprobante,
      String tipoComprobante,
      String serieComprobante,
      String numeroComprobante) {
    StatusResponseAR statusResponse = null;

    try {
      BillService_Service service = new BillService_Service();
      BillService port = service.getBillServicePort();
      BindingProvider binding = (BindingProvider) port;

      @SuppressWarnings("rawtypes")
      List<Handler> handlers = new ArrayList<>();
      SOAPHandler<SOAPMessageContext> handler = new ISOAPHanlder(RUC + USUARIO, CONTRASENA);
      handlers.add(handler);
      binding.getBinding().setHandlerChain(handlers);

      statusResponse =
          port.getStatusAR(rucComprobante, tipoComprobante, serieComprobante, numeroComprobante);
    } catch (Exception ex) {
      Logger.getLogger(IBillService.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), IBetaBillService.class.getName(), JOptionPane.ERROR_MESSAGE);
    }

    return statusResponse;
  }

  @Override
  public byte[] sendBill(String fileName, DataHandler contentFile, String partyType) {
    byte[] content = null;

    try {
      BillService_Service service = new BillService_Service();
      BillService port = service.getBillServicePort();
      BindingProvider binding = (BindingProvider) port;

      @SuppressWarnings("rawtypes")
      List<Handler> handlers = new ArrayList<>();
      SOAPHandler<SOAPMessageContext> handler = new ISOAPHanlder(RUC + USUARIO, CONTRASENA);
      handlers.add(handler);
      binding.getBinding().setHandlerChain(handlers);

      content = port.sendBill(fileName, contentFile, partyType);
    } catch (Exception ex) {
      Logger.getLogger(IBillService.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(), fileName, JOptionPane.ERROR_MESSAGE);
    }

    return content;
  }

  @Override
  public String sendPack(String fileName, DataHandler contentFile, String partyType) {
    String ticket = null;

    try {
      BillService_Service service = new BillService_Service();
      BillService port = service.getBillServicePort();
      BindingProvider binding = (BindingProvider) port;

      @SuppressWarnings("rawtypes")
      List<Handler> handlers = new ArrayList<>();
      SOAPHandler<SOAPMessageContext> handler = new ISOAPHanlder(RUC + USUARIO, CONTRASENA);
      handlers.add(handler);
      binding.getBinding().setHandlerChain(handlers);

      ticket = port.sendPack(fileName, contentFile, partyType);
    } catch (Exception ex) {
      Logger.getLogger(IBillService.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(), fileName, JOptionPane.ERROR_MESSAGE);
    }

    return ticket;
  }

  @Override
  public String sendSummary(String fileName, DataHandler contentFile, String partyType) {
    String ticket = null;

    try {
      BillService_Service service = new BillService_Service();
      BillService port = service.getBillServicePort();
      BindingProvider binding = (BindingProvider) port;

      @SuppressWarnings("rawtypes")
      List<Handler> handlers = new ArrayList<>();
      SOAPHandler<SOAPMessageContext> handler = new ISOAPHanlder(RUC + USUARIO, CONTRASENA);
      handlers.add(handler);
      binding.getBinding().setHandlerChain(handlers);

      ticket = port.sendSummary(fileName, contentFile, partyType);
    } catch (Exception ex) {
      Logger.getLogger(IBillService.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(null, ex.getMessage(), fileName, JOptionPane.ERROR_MESSAGE);
    }

    return ticket;
  }
}
