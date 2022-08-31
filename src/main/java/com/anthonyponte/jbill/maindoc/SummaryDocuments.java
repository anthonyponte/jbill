/*
 * Copyright (C) 2022 anthony
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

package com.anthonyponte.jbill.maindoc;

import com.anthonyponte.jbill.controller.MainController;
import com.anthonyponte.jbill.controller.UsuarioController;
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.model.Resumen;
import com.anthonyponte.jbill.model.ResumenDetalle;
import java.util.Locale;
import java.util.prefs.Preferences;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * @author anthony
 */
public class SummaryDocuments {

  private static final Preferences PREFS =
      Preferences.userRoot().node(MainController.class.getPackageName());
  private Document document;

  public Document getStructure(Resumen resumenDiario) {

    document = new Document();

    Namespace urn =
        Namespace.getNamespace(
            "urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1");

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

    document.setRootElement(new Element("SummaryDocuments", urn));
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

    Element ublVersionID = new Element("UBLVersionID", cbc).setText(resumenDiario.getUbl());
    document.getRootElement().addContent(ublVersionID);

    Element customizationID =
        new Element("CustomizationID", cbc).setText(resumenDiario.getVersion());
    document.getRootElement().addContent(customizationID);

    Element id =
        new Element("ID", cbc)
            .setText(
                resumenDiario.getTipoDocumento().getCodigo()
                    + "-"
                    + resumenDiario.getSerie()
                    + "-"
                    + resumenDiario.getCorrelativo());
    document.getRootElement().addContent(id);

    Element referenceDate =
        new Element("ReferenceDate", cbc)
            .setText(MyDateFormat.yyyy_MM_dd(resumenDiario.getFechaReferencia()));
    document.getRootElement().addContent(referenceDate);

    Element issueDate =
        new Element("IssueDate", cbc)
            .setText(MyDateFormat.yyyy_MM_dd(resumenDiario.getFechaEmision()));
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
                                    .setText(resumenDiario.getEmisor().getNumero())))
                    .addContent(
                        new Element("PartyName", cac)
                            .addContent(
                                new Element("Name", cbc)
                                    .setText(resumenDiario.getEmisor().getNombre()))))
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
                    .setText(resumenDiario.getEmisor().getNumero()))
            .addContent(
                new Element("AdditionalAccountID", cbc)
                    .setText(String.valueOf(resumenDiario.getEmisor().getTipo())))
            .addContent(
                new Element("Party", cac)
                    .addContent(
                        new Element("PartyLegalEntity", cac)
                            .addContent(
                                new Element("RegistrationName", cbc)
                                    .setText(resumenDiario.getEmisor().getNombre()))));
    document.getRootElement().addContent(accountingSupplierParty);

    for (int i = 0; i < resumenDiario.getResumenDiarioDetalles().size(); i++) {
      ResumenDetalle detalle = resumenDiario.getResumenDiarioDetalles().get(i);

      Element summaryDocumentsLine =
          new Element("SummaryDocumentsLine", sac)
              .addContent(new Element("LineID", cbc).setText(String.valueOf(i + 1)))
              .addContent(
                  new Element("DocumentTypeCode", cbc)
                      .setText(detalle.getDocumento().getTipo().getCodigo()))
              .addContent(
                  new Element("ID", cbc)
                      .setText(
                          detalle.getDocumento().getSerie()
                              + "-"
                              + detalle.getDocumento().getCorrelativo()));

      if (detalle.getAdquiriente() != null) {
        Element accountingCustomerParty =
            new Element("AccountingCustomerParty", cac)
                .addContent(
                    new Element("CustomerAssignedAccountID", cbc)
                        .setText(detalle.getAdquiriente().getNumero()))
                .addContent(
                    new Element("AdditionalAccountID", cbc)
                        .setText(String.valueOf(detalle.getAdquiriente().getTipo())));
        summaryDocumentsLine.addContent(accountingCustomerParty);
      }

      if (detalle.getDocumentoReferencia() != null) {
        Element billingReference =
            new Element("BillingReference", cac)
                .addContent(
                    new Element("InvoiceDocumentReference", cac)
                        .addContent(
                            new Element("ID", cbc)
                                .setText(
                                    detalle.getDocumentoReferencia().getSerie()
                                        + "-"
                                        + detalle.getDocumentoReferencia().getCorrelativo()))
                        .addContent(
                            new Element("DocumentTypeCode", cbc)
                                .setText(detalle.getDocumentoReferencia().getTipo().getCodigo())));
        summaryDocumentsLine.addContent(billingReference);
      }

      if (detalle.getPercepcion() != null) {
        Element sunatPerceptionSummaryDocumentReference =
            new Element("SUNATPerceptionSummaryDocumentReference", sac)
                .addContent(
                    new Element("SUNATPerceptionSystemCode", sac)
                        .setText(detalle.getPercepcion().getRegimenPercepcion().getCodigo()))
                .addContent(
                    new Element("SUNATPerceptionPercent", sac)
                        .setText(
                            String.valueOf(
                                detalle.getPercepcion().getRegimenPercepcion().getPorcentaje())))
                .addContent(
                    new Element("TotalInvoiceAmount", cbc)
                        .setAttribute("currencyID", "PEN")
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getPercepcion().getMonto())))
                .addContent(
                    new Element("SUNATTotalCashed", sac)
                        .setAttribute("currencyID", "PEN")
                        .setText(String.valueOf(detalle.getPercepcion().getMontoTotal())))
                .addContent(
                    new Element("TaxableAmount", cbc)
                        .setAttribute("currencyID", "PEN")
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getPercepcion().getBase())));
        summaryDocumentsLine.addContent(sunatPerceptionSummaryDocumentReference);
      }

      Element status =
          new Element("Status", cac)
              .addContent(
                  new Element("ConditionCode", cbc).setText(detalle.getEstado().getCodigo()));
      summaryDocumentsLine.addContent(status);

      Element totalAmount =
          new Element("TotalAmount", sac)
              .setAttribute("currencyID", detalle.getMoneda().getCodigo())
              .setText(String.format(Locale.ROOT, "%.2f", detalle.getImporteTotal()));
      summaryDocumentsLine.addContent(totalAmount);

      if (detalle.getGravadas() != null) {
        Element billingPayment =
            new Element("BillingPayment", sac)
                .addContent(
                    new Element("PaidAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getGravadas().getTotal())))
                .addContent(
                    new Element("InstructionID", cbc).setText(detalle.getGravadas().getCodigo()));
        summaryDocumentsLine.addContent(billingPayment);
      }

      if (detalle.getExoneradas() != null) {
        Element billingPayment =
            new Element("BillingPayment", sac)
                .addContent(
                    new Element("PaidAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getExoneradas().getTotal())))
                .addContent(
                    new Element("InstructionID", cbc).setText(detalle.getExoneradas().getCodigo()));
        summaryDocumentsLine.addContent(billingPayment);
      }

      if (detalle.getInafectas() != null) {
        Element billingPayment =
            new Element("BillingPayment", sac)
                .addContent(
                    new Element("PaidAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getInafectas().getTotal())))
                .addContent(
                    new Element("InstructionID", cbc).setText(detalle.getInafectas().getCodigo()));
        summaryDocumentsLine.addContent(billingPayment);
      }

      if (detalle.getGratuitas() != null) {
        Element billingPayment =
            new Element("BillingPayment", sac)
                .addContent(
                    new Element("PaidAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(Locale.ROOT, "%.2f", detalle.getGratuitas().getTotal())))
                .addContent(
                    new Element("InstructionID", cbc).setText(detalle.getGratuitas().getCodigo()));
        summaryDocumentsLine.addContent(billingPayment);
      }

      if (detalle.getExportacion() != null) {
        Element billingPayment =
            new Element("BillingPayment", sac)
                .addContent(
                    new Element("PaidAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(
                                Locale.ROOT, "%.2f", detalle.getExportacion().getTotal())))
                .addContent(
                    new Element("InstructionID", cbc)
                        .setText(detalle.getExportacion().getCodigo()));
        summaryDocumentsLine.addContent(billingPayment);
      }

      if (detalle.getOtrosCargos() != null) {
        Element allowanceCharge =
            new Element("AllowanceCharge", cac)
                .addContent(
                    new Element("ChargeIndicator", cbc)
                        .setText(String.valueOf(detalle.getOtrosCargos().isIndicador())))
                .addContent(
                    new Element("Amount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(
                                Locale.ROOT, "%.2f", detalle.getOtrosCargos().getTotal())));
        summaryDocumentsLine.addContent(allowanceCharge);
      }

      Element taxTotal =
          new Element("TaxTotal", cac)
              .addContent(
                  new Element("TaxAmount", cbc)
                      .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                      .setText(String.format(Locale.ROOT, "%.2f", detalle.getIgv().getTotal())))
              .addContent(
                  new Element("TaxSubtotal", cac)
                      .addContent(
                          new Element("TaxAmount", cbc)
                              .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                              .setText(
                                  String.format(Locale.ROOT, "%.2f", detalle.getIgv().getTotal())))
                      .addContent(
                          new Element("TaxCategory", cac)
                              .addContent(
                                  new Element("TaxScheme", cac)
                                      .addContent(
                                          new Element("ID", cbc)
                                              .setText(detalle.getIgv().getCodigo()))
                                      .addContent(
                                          new Element("Name", cbc)
                                              .setText(detalle.getIgv().getDescripcion()))
                                      .addContent(
                                          new Element("TaxTypeCode", cbc)
                                              .setText(
                                                  detalle.getIgv().getCodigoInternacional())))));
      summaryDocumentsLine.addContent(taxTotal);

      if (detalle.getIsc() != null) {
        taxTotal =
            new Element("TaxTotal", cac)
                .addContent(
                    new Element("TaxAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(String.format(Locale.ROOT, "%.2f", detalle.getIsc().getTotal())))
                .addContent(
                    new Element("TaxSubtotal", cac)
                        .addContent(
                            new Element("TaxAmount", cbc)
                                .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                                .setText(
                                    String.format(
                                        Locale.ROOT, "%.2f", detalle.getIsc().getTotal())))
                        .addContent(
                            new Element("TaxCategory", cac)
                                .addContent(
                                    new Element("TaxScheme", cac)
                                        .addContent(
                                            new Element("ID", cbc)
                                                .setText(detalle.getIsc().getCodigo()))
                                        .addContent(
                                            new Element("Name", cbc)
                                                .setText(detalle.getIsc().getDescripcion()))
                                        .addContent(
                                            new Element("TaxTypeCode", cbc)
                                                .setText(
                                                    detalle.getIsc().getCodigoInternacional())))));
        summaryDocumentsLine.addContent(taxTotal);
      }

      if (detalle.getOtrosTributos() != null) {
        taxTotal =
            new Element("TaxTotal", cac)
                .addContent(
                    new Element("TaxAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(
                                Locale.ROOT, "%.2f", detalle.getOtrosTributos().getTotal())))
                .addContent(
                    new Element("TaxSubtotal", cac)
                        .addContent(
                            new Element("TaxAmount", cbc)
                                .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                                .setText(
                                    String.format(
                                        Locale.ROOT,
                                        "%.2f",
                                        detalle.getOtrosTributos().getTotal())))
                        .addContent(
                            new Element("TaxCategory", cac)
                                .addContent(
                                    new Element("TaxScheme", cac)
                                        .addContent(
                                            new Element("ID", cbc)
                                                .setText(detalle.getOtrosTributos().getCodigo()))
                                        .addContent(
                                            new Element("Name", cbc)
                                                .setText(
                                                    detalle.getOtrosTributos().getDescripcion()))
                                        .addContent(
                                            new Element("TaxTypeCode", cbc)
                                                .setText(
                                                    detalle
                                                        .getOtrosTributos()
                                                        .getCodigoInternacional())))));
        summaryDocumentsLine.addContent(taxTotal);
      }

      if (detalle.getImpuestoBolsa() != null) {
        taxTotal =
            new Element("TaxTotal", cac)
                .addContent(
                    new Element("TaxAmount", cbc)
                        .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                        .setText(
                            String.format(
                                Locale.ROOT, "%.2f", detalle.getImpuestoBolsa().getTotal())))
                .addContent(
                    new Element("TaxSubtotal", cac)
                        .addContent(
                            new Element("TaxAmount", cbc)
                                .setAttribute("currencyID", detalle.getMoneda().getCodigo())
                                .setText(
                                    String.format(
                                        Locale.ROOT,
                                        "%.2f",
                                        detalle.getImpuestoBolsa().getTotal())))
                        .addContent(
                            new Element("TaxCategory", cac)
                                .addContent(
                                    new Element("TaxScheme", cac)
                                        .addContent(
                                            new Element("ID", cbc)
                                                .setText(detalle.getImpuestoBolsa().getCodigo()))
                                        .addContent(
                                            new Element("Name", cbc)
                                                .setText(
                                                    detalle.getImpuestoBolsa().getDescripcion()))
                                        .addContent(
                                            new Element("TaxTypeCode", cbc)
                                                .setText(
                                                    detalle
                                                        .getImpuestoBolsa()
                                                        .getCodigoInternacional())))));
        summaryDocumentsLine.addContent(taxTotal);
      }

      document.getRootElement().addContent(summaryDocumentsLine);
    }

    return document;
  }
}
