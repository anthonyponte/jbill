/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.maindoc;

import com.anthonyponte.jbillservice.controller.MainController;
import com.anthonyponte.jbillservice.controller.UsuarioController;
import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.custom.MyFileCreator;
import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import java.io.File;
import java.util.prefs.Preferences;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/** @author AnthonyPonte */
public class VoidedDocuments {

  private static final Preferences PREFS =
      Preferences.userRoot().node(MainController.class.getPackageName());
  private Document document;

  public File getStructure(ComunicacionBaja comunicacion) {

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
                comunicacion.getTipo()
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
                                new Element("ID", cbc).setText(comunicacion.getEmisor().getRuc())))
                    .addContent(
                        new Element("PartyName", cac)
                            .addContent(
                                new Element("Name", cbc)
                                    .setText(comunicacion.getEmisor().getRazonSocial()))))
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
                    .setText(comunicacion.getEmisor().getRuc()))
            .addContent(
                new Element("AdditionalAccountID", cbc)
                    .setText(String.valueOf(comunicacion.getEmisor().getTipo())))
            .addContent(
                new Element("Party", cac)
                    .addContent(
                        new Element("PartyLegalEntity", cac)
                            .addContent(
                                new Element("RegistrationName", cbc)
                                    .setText(comunicacion.getEmisor().getRazonSocial()))));
    document.getRootElement().addContent(accountingSupplierParty);

    for (int i = 0; i < comunicacion.getComunicacionBajaDetalles().size(); i++) {
      ComunicacionBajaDetalle detalle = comunicacion.getComunicacionBajaDetalles().get(i);

      Element voidedDocumentsLine =
          new Element("VoidedDocumentsLine", sac)
              .addContent(new Element("LineID", cbc).setText(String.valueOf(detalle.getNumero())))
              .addContent(
                  new Element("DocumentTypeCode", cbc).setText(detalle.getDocumento().getTipo()))
              .addContent(
                  new Element("DocumentSerialID", sac).setText(detalle.getDocumento().getSerie()))
              .addContent(
                  new Element("DocumentNumberID", sac)
                      .setText(String.valueOf(detalle.getDocumento().getCorrelativo())))
              .addContent(new Element("VoidReasonDescription", sac).setText(detalle.getMotivo()));

      document.getRootElement().addContent(voidedDocumentsLine);
    }

    File xml =
        MyFileCreator.create(
            comunicacion.getTipo(),
            comunicacion.getSerie(),
            comunicacion.getCorrelativo(),
            document);

    File signedXml =
        MyFileCreator.sign(
            comunicacion.getTipo(), comunicacion.getSerie(), comunicacion.getCorrelativo(), xml);

    File zip =
        MyFileCreator.compress(
            comunicacion.getTipo(),
            comunicacion.getSerie(),
            comunicacion.getCorrelativo(),
            signedXml);

    signedXml.delete();

    return zip;
  }
}
