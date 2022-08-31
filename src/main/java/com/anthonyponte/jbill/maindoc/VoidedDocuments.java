/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.maindoc;

import com.anthonyponte.jbill.controller.MainController;
import com.anthonyponte.jbill.controller.UsuarioController;
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.model.Comunicacion;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import com.anthonyponte.jbill.model.Summary;
import java.util.prefs.Preferences;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * @author AnthonyPonte
 */
public class VoidedDocuments {

  private static final Preferences PREFS =
      Preferences.userRoot().node(MainController.class.getPackageName());
  private Document document;

  public Document getStructure(Comunicacion comunicacion) {

    document = new Document();

    Namespace urn =
        Namespace.getNamespace(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1");

    Namespace cac =
        Namespace.getNamespace(
            "cac", "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");

    Namespace cbc =
        Namespace.getNamespace(
            "cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");

    Namespace ds = Namespace.getNamespace("ds", "http://www.w3.org/2000/09/xmldsig#");

    Namespace ext =
        Namespace.getNamespace(
            "ext", "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");

    Namespace sac =
        Namespace.getNamespace(
            "sac", "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");

    Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    document.setRootElement(new Element("VoidedDocuments", urn));
    document.getRootElement().addNamespaceDeclaration(urn);
    document.getRootElement().addNamespaceDeclaration(cac);
    document.getRootElement().addNamespaceDeclaration(cbc);
    document.getRootElement().addNamespaceDeclaration(ds);
    document.getRootElement().addNamespaceDeclaration(ext);
    document.getRootElement().addNamespaceDeclaration(sac);
    document.getRootElement().addNamespaceDeclaration(xsi);

    Element ublExtensions =
        new Element("UBLExtensions", ext)
            .addContent(
                new Element("UBLExtension", ext).addContent(new Element("ExtensionContent", ext)));
    document.getRootElement().addContent(ublExtensions);

    Element ublVersionID = new Element("UBLVersionID", cbc).setText(comunicacion.getUbl());
    document.getRootElement().addContent(ublVersionID);

    Element customizationID =
        new Element("CustomizationID", cbc).setText(comunicacion.getVersion());
    document.getRootElement().addContent(customizationID);

    Element id =
        new Element("ID", cbc)
            .setText(
                comunicacion.getTipo().getCodigo()
                    + "-"
                    + comunicacion.getSerie()
                    + "-"
                    + comunicacion.getCorrelativo());
    document.getRootElement().addContent(id);

    Element referenceDate =
        new Element("ReferenceDate", cbc)
            .setText(MyDateFormat.yyyy_MM_dd(comunicacion.getFechaReferencia()));
    document.getRootElement().addContent(referenceDate);

    Element issueDate =
        new Element("IssueDate", cbc)
            .setText(MyDateFormat.yyyy_MM_dd(comunicacion.getFechaEmision()));
    document.getRootElement().addContent(issueDate);

    Element signature =
        new Element("Signature", cac)
            .addContent(
                new Element("ID", cbc)
                    .setText("SIGN-" + PREFS.get(UsuarioController.FIRMA_USUARIO, "")))
            .addContent(
                new Element("SignatoryParty", cac)
                    .addContent(
                        new Element("PartyIdentification", cac)
                            .addContent(
                                new Element("ID", cbc)
                                    .setText(comunicacion.getEmisor().getNumero())))
                    .addContent(
                        new Element("PartyName", cac)
                            .addContent(
                                new Element("Name", cbc)
                                    .setText(comunicacion.getEmisor().getNombre()))))
            .addContent(
                new Element("DigitalSignatureAttachment", cac)
                    .addContent(
                        new Element("ExternalReference", cac)
                            .addContent(
                                new Element("URI", cbc)
                                    .setText(
                                        "#SIGN-"
                                            + PREFS.get(UsuarioController.FIRMA_USUARIO, "")))));
    document.getRootElement().addContent(signature);

    Element accountingSupplierParty =
        new Element("AccountingSupplierParty", cac)
            .addContent(
                new Element("CustomerAssignedAccountID", cbc)
                    .setText(comunicacion.getEmisor().getNumero()))
            .addContent(
                new Element("AdditionalAccountID", cbc)
                    .setText(comunicacion.getEmisor().getTipo().getCodigo()))
            .addContent(
                new Element("Party", cac)
                    .addContent(
                        new Element("PartyLegalEntity", cac)
                            .addContent(
                                new Element("RegistrationName", cbc)
                                    .setText(comunicacion.getEmisor().getNombre()))));
    document.getRootElement().addContent(accountingSupplierParty);

    for (int i = 0; i < comunicacion.getDetalles().size(); i++) {
      ComunicacionDetalle detalle = comunicacion.getDetalles().get(i);

      Element voidedDocumentsLine =
          new Element("VoidedDocumentsLine", sac)
              .addContent(new Element("LineID", cbc).setText(String.valueOf(i + 1)))
              .addContent(
                  new Element("DocumentTypeCode", cbc)
                      .setText(detalle.getDocumento().getTipo().getCodigo()))
              .addContent(
                  new Element("DocumentSerialID", sac).setText(detalle.getDocumento().getSerie()))
              .addContent(
                  new Element("DocumentNumberID", sac)
                      .setText(String.valueOf(detalle.getDocumento().getCorrelativo())))
              .addContent(new Element("VoidReasonDescription", sac).setText(detalle.getMotivo()));

      document.getRootElement().addContent(voidedDocumentsLine);
    }

    return document;
  }
}
