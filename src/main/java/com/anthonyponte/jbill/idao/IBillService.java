/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.idao;

import com.anthonyponte.jbill.controller.MainController;
import com.anthonyponte.jbill.controller.UsuarioController;
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
import sunat.gob.pe.BillService;
import sunat.gob.pe.BillService_Service;
import sunat.gob.pe.StatusResponse;

/**
 * @author anthony
 */
public class IBillService implements BillService {

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
