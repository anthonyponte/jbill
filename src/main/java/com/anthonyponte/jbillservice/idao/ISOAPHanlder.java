package com.anthonyponte.jbillservice.idao;

import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;

public class ISOAPHanlder implements SOAPHandler<SOAPMessageContext> {

  private final String username;
  private final String password;

  public ISOAPHanlder(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public Set<QName> getHeaders() {
    return null;
  }

  @Override
  public boolean handleMessage(SOAPMessageContext c) {
    Boolean isRequest = (Boolean) c.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (isRequest) {
      try {
        SOAPEnvelope envelope = c.getMessage().getSOAPPart().getEnvelope();

        SOAPHeader header = envelope.getHeader();
        if (header == null) {
          header = envelope.addHeader();
        }

        SOAPElement tagSecurity =
            header.addChildElement(
                "Security",
                "wsse",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPElement tagToken = tagSecurity.addChildElement("UsernameToken", "wsse");
        SOAPElement tagUsername = tagToken.addChildElement("Username", "wsse");
        SOAPElement tagPassword = tagToken.addChildElement("Password", "wsse");

        tagUsername.addTextNode(this.username);
        tagPassword.addTextNode(this.password);
      } catch (SOAPException ex) {
        Logger.getLogger(ISOAPHanlder.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, ex.getMessage(), ISOAPHanlder.class.getName(), JOptionPane.ERROR_MESSAGE);
      }
    }

    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (isRequest) {
      SOAPMessage message = context.getMessage();
      JOptionPane.showMessageDialog(null,
          message.getContentDescription(),
          ISOAPHanlder.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }
    return true;
  }

  @Override
  public void close(MessageContext mc) {}
}
