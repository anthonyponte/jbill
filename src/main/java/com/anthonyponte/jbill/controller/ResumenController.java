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

package com.anthonyponte.jbill.controller;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.custom.MyFileCreator;
import com.anthonyponte.jbill.custom.MyTableResize;
import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.filter.IntegerFilter;
import com.anthonyponte.jbill.idao.IResumenDetalleDao;
import com.anthonyponte.jbill.idao.ISummaryDao;
import com.anthonyponte.jbill.maindoc.SummaryDocuments;
import com.anthonyponte.jbill.model.Empresa;
import com.anthonyponte.jbill.model.Impuesto;
import com.anthonyponte.jbill.model.Operacion;
import com.anthonyponte.jbill.model.OtrosCargos;
import com.anthonyponte.jbill.model.Percepcion;
import com.anthonyponte.jbill.model.RegimenPercepcion;
import com.anthonyponte.jbill.model.Resumen;
import com.anthonyponte.jbill.model.ResumenDetalle;
import com.anthonyponte.jbill.model.Tipo;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.ResumenIFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.jdom2.Document;
import org.xml.sax.SAXException;
import com.anthonyponte.jbill.dao.ResumenDetalleDao;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anthony
 */
public class ResumenController {
  private final ResumenIFrame iFrame;
  private final LoadingDialog dialog;
  private Preferences preferences;
  private SummaryDao summaryDao;
  private ResumenDetalleDao resumenDetalleDao;
  private EventList<ResumenDetalle> eventList;
  private AdvancedTableModel<ResumenDetalle> tableModel;
  private AdvancedListSelectionModel<ResumenDetalle> selectionModel;

  public ResumenController(ResumenIFrame iFrame, LoadingDialog dialog) {
    this.iFrame = iFrame;
    this.dialog = dialog;
    initComponents();
  }

  void init() {
    iFrame.btnNuevo.addActionListener(
        (ActionEvent arg0) -> {
          iFrame.cbxTipo.setSelectedIndex(0);
          iFrame.dpFechaGeneracion.setDate(new Date());

          dialog.setVisible(true);
          dialog.setLocationRelativeTo(iFrame);

          SwingWorker worker =
              new SwingWorker<Integer, Void>() {
                @Override
                protected Integer doInBackground() throws Exception {
                  int count = 0;
                  try {
                    Tipo tipoDocumento = (Tipo) iFrame.cbxTipo.getSelectedItem();
                    Date fechaGeneracion = iFrame.dpFechaGeneracion.getDate();

                    count = summaryDao.count(tipoDocumento, fechaGeneracion);
                  } catch (SQLException ex) {
                    Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                  return count;
                }

                @Override
                protected void done() {
                  try {
                    dialog.dispose();

                    iFrame.tabbed.setSelectedIndex(0);
                    iFrame.tbbdDetalle.setSelectedIndex(0);

                    Date fechaGeneracion = iFrame.dpFechaGeneracion.getDate();
                    iFrame.tfSerie.setText(MyDateFormat.yyyyMMdd(fechaGeneracion));

                    int count = get();
                    iFrame.tfCorrelativo.setText(String.valueOf(count));

                    iFrame.dpFechaEmision.setEnabled(true);
                    iFrame.dpFechaEmision.setDate(new Date());
                    iFrame.dpFechaEmision.requestFocus();

                    iFrame.cbxEstado.setEnabled(true);
                    iFrame.cbxEstado.setSelectedIndex(0);

                    iFrame.cbxDocumentoTipo.setEnabled(true);
                    iFrame.cbxDocumentoTipo.setSelectedIndex(0);

                    iFrame.tfDocumentoSerie.setEnabled(true);

                    iFrame.tfDocumentoCorrelativo.setEnabled(true);

                    iFrame.chckPercepcion.setEnabled(true);

                    iFrame.cbxMoneda.setEnabled(true);
                    iFrame.cbxMoneda.setSelectedIndex(0);

                    iFrame.tfImporteTotal.setValue(0);

                    iFrame.tfGravadas.setEnabled(true);
                    iFrame.tfGravadas.setValue(0);

                    iFrame.tfExoneradas.setEnabled(true);
                    iFrame.tfExoneradas.setValue(0);

                    iFrame.tfInafectas.setEnabled(true);
                    iFrame.tfInafectas.setValue(0);

                    iFrame.tfGratuitas.setEnabled(true);
                    iFrame.tfGratuitas.setValue(0);

                    iFrame.tfExportacion.setEnabled(true);
                    iFrame.tfExportacion.setValue(0);

                    iFrame.tfOtrosCargos.setEnabled(true);
                    iFrame.tfOtrosCargos.setValue(0);

                    iFrame.tfIgv.setValue(0);

                    iFrame.tfIsc.setEnabled(true);
                    iFrame.tfIsc.setValue(0);

                    iFrame.tfOtrosTributos.setEnabled(true);
                    iFrame.tfOtrosTributos.setValue(0);

                    iFrame.tfBolsasPlasticas.setEnabled(true);
                    iFrame.tfBolsasPlasticas.setValue(0);

                    iFrame.btnNuevo.setEnabled(false);

                    iFrame.btnGuardar.setEnabled(false);

                    iFrame.btnLimpiar.setEnabled(true);
                  } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }
              };

          worker.execute();
        });

    iFrame.btnGuardar.addActionListener(
        (ActionEvent arg0) -> {
          File jks = new File(preferences.get(UsuarioController.FIRMA_JKS, ""));
          if (jks.exists()) {
            Tipo tipoDocumento = (Tipo) iFrame.cbxTipo.getSelectedItem();
            int input =
                JOptionPane.showOptionDialog(
                    iFrame,
                    "Seguro que desea guardar esta " + tipoDocumento.getDescripcion() + "?",
                    "Guardar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[] {"Guardar", "Cancelar"},
                    "Guardar");

            if (input == JOptionPane.YES_OPTION) {

              dialog.setVisible(true);
              dialog.setLocationRelativeTo(iFrame);

              SwingWorker worker =
                  new SwingWorker<Resumen, Void>() {
                    @Override
                    protected Resumen doInBackground() throws Exception {
                      Resumen resumen = null;
                      try {
                        resumen = new Resumen();
                        resumen.setUbl("2.0");
                        resumen.setVersion("1.1");

                        resumen.setTipoDocumento((Tipo) iFrame.cbxTipo.getSelectedItem());
                        resumen.setSerie(MyDateFormat.yyyyMMdd(iFrame.dpFechaGeneracion.getDate()));
                        resumen.setCorrelativo(Integer.valueOf(iFrame.tfCorrelativo.getText()));

                        resumen.setFechaEmision(iFrame.dpFechaGeneracion.getDate());
                        resumen.setFechaReferencia(iFrame.dpFechaEmision.getDate());

                        Empresa emisor = new Empresa();
                        emisor.setNumero(preferences.get(UsuarioController.RUC, ""));

                        Tipo tipoDocumentoIdentidad = new Tipo();
                        tipoDocumentoIdentidad.setCodigo(
                            preferences.get(UsuarioController.RUC_TIPO, ""));
                        emisor.setTipoDocumentoIdentidad(tipoDocumentoIdentidad);

                        emisor.setNombre(preferences.get(UsuarioController.RAZON_SOCIAL, ""));
                        resumen.setEmisor(emisor);

                        resumen.setDetalles(eventList);

                        SummaryDocuments summaryDocuments = new SummaryDocuments();
                        Document document = summaryDocuments.getStructure(resumen);

                        File xml =
                            MyFileCreator.create(
                                resumen.getTipoDocumento().getCodigo(),
                                resumen.getSerie(),
                                resumen.getCorrelativo(),
                                document);

                        File sign =
                            MyFileCreator.sign(
                                resumen.getTipoDocumento().getCodigo(),
                                resumen.getSerie(),
                                resumen.getCorrelativo(),
                                xml);

                        File zip =
                            MyFileCreator.compress(
                                resumen.getTipoDocumento().getCodigo(),
                                resumen.getSerie(),
                                resumen.getCorrelativo(),
                                sign);

                        byte[] byteArray = Files.readAllBytes(zip.toPath());
                        resumen.setNombreZip(zip.getName());
                        resumen.setZip(byteArray);
                        int id = summaryDao.create(resumen);
                        resumenDetalleDao.create(id, eventList);

                        sign.delete();
                        zip.delete();
                      } catch (IOException
                          | InvalidAlgorithmParameterException
                          | KeyStoreException
                          | NoSuchAlgorithmException
                          | UnrecoverableEntryException
                          | CertificateException
                          | MarshalException
                          | XMLSignatureException
                          | ParserConfigurationException
                          | TransformerException
                          | SAXException
                          | SQLException ex) {
                        cancel(true);

                        Logger.getLogger(ResumenController.class.getName())
                            .log(Level.SEVERE, null, ex);

                        JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            ResumenController.class.getName(),
                            JOptionPane.ERROR_MESSAGE);
                      }
                      return resumen;
                    }

                    @Override
                    protected void done() {
                      dialog.dispose();

                      if (isCancelled()) {
                        start();
                      } else {
                        try {
                          start();
                          Resumen get = get();

                          JOptionPane.showMessageDialog(
                              iFrame,
                              get.getNombreZip() + " guardado",
                              "Guardado",
                              JOptionPane.INFORMATION_MESSAGE);
                        } catch (InterruptedException | ExecutionException ex) {
                          Logger.getLogger(ResumenController.class.getName())
                              .log(Level.SEVERE, null, ex);

                          JOptionPane.showMessageDialog(
                              null,
                              ex.getMessage(),
                              ResumenController.class.getName(),
                              JOptionPane.ERROR_MESSAGE);
                        }
                      }
                    }
                  };

              worker.execute();
            }

          } else {
            JOptionPane.showMessageDialog(
                iFrame,
                "No se encuentra el archivo JKS en la ruta "
                    + preferences.get(UsuarioController.FIRMA_JKS, ""),
                ResumenController.class.getName(),
                JOptionPane.ERROR_MESSAGE);
          }
        });

    iFrame.btnLimpiar.addActionListener(
        (ActionEvent arg0) -> {
          start();
        });

    iFrame.cbxDocumentoTipo.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            if (iFrame.cbxDocumentoTipo.getSelectedIndex() == 0) {
              try {
                iFrame.tbbdDetalle.setEnabledAt(2, false);

                iFrame.cbxDocumentoReferenciaTipo.setEnabled(false);
                iFrame.cbxDocumentoReferenciaTipo.setSelectedIndex(-1);

                iFrame.tfDocumentoReferenciaSerie.setEnabled(false);
                iFrame
                    .tfDocumentoReferenciaSerie
                    .getDocument()
                    .remove(0, iFrame.tfDocumentoReferenciaSerie.getText().length());

                iFrame.tfDocumentoReferenciaCorrelativo.setEnabled(false);
                iFrame
                    .tfDocumentoReferenciaCorrelativo
                    .getDocument()
                    .remove(0, iFrame.tfDocumentoReferenciaCorrelativo.getText().length());

                enabled();
              } catch (BadLocationException ex) {
                Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    ResumenController.class.getName(),
                    JOptionPane.ERROR_MESSAGE);
              }
            } else {
              try {
                iFrame.tbbdDetalle.setEnabledAt(2, true);

                iFrame.cbxDocumentoReferenciaTipo.setEnabled(true);
                iFrame.cbxDocumentoReferenciaTipo.setSelectedIndex(0);

                iFrame.tfDocumentoReferenciaSerie.setEnabled(true);
                iFrame
                    .tfDocumentoReferenciaSerie
                    .getDocument()
                    .remove(0, iFrame.tfDocumentoReferenciaSerie.getText().length());

                iFrame.tfDocumentoReferenciaCorrelativo.setEnabled(true);
                iFrame
                    .tfDocumentoReferenciaCorrelativo
                    .getDocument()
                    .remove(0, iFrame.tfDocumentoReferenciaCorrelativo.getText().length());

                enabled();
              } catch (BadLocationException ex) {
                Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    ResumenController.class.getName(),
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        });

    iFrame.cbxDocumentoIdentidadTipo.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            try {
              AbstractDocument adtfDocumentoIdentidadNumero =
                  (AbstractDocument) iFrame.tfDocumentoIdentidadNumero.getDocument();

              adtfDocumentoIdentidadNumero.remove(
                  0, iFrame.tfDocumentoIdentidadNumero.getText().length());

              if (iFrame.cbxDocumentoIdentidadTipo.getSelectedIndex() == 1)
                adtfDocumentoIdentidadNumero.setDocumentFilter(new IntegerFilter(8));
              if (iFrame.cbxDocumentoIdentidadTipo.getSelectedIndex() == 2)
                adtfDocumentoIdentidadNumero.setDocumentFilter(new IntegerFilter(9));
              if (iFrame.cbxDocumentoIdentidadTipo.getSelectedIndex() == 3)
                adtfDocumentoIdentidadNumero.setDocumentFilter(new IntegerFilter(11));

              iFrame.tfDocumentoIdentidadNumero.requestFocus();
            } catch (BadLocationException ex) {
              Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

              JOptionPane.showMessageDialog(
                  null,
                  ex.getMessage(),
                  ResumenController.class.getName(),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        });

    iFrame.chckPercepcion.addItemListener(
        (ItemEvent e) -> {
          if (e.getStateChange() == ItemEvent.SELECTED) {
            iFrame.tbbdDetalle.setEnabledAt(4, true);
            iFrame.cbxPercepcionRegimen.setEnabled(true);
            iFrame.cbxPercepcionRegimen.setSelectedIndex(0);

            iFrame.tfPercepcionTasa.setEnabled(true);

            iFrame.tfPercepcionMonto.setEnabled(true);
            iFrame.tfPercepcionMonto.setValue(0);

            iFrame.tfPercepcionMontoTotal.setEnabled(true);
            iFrame.tfPercepcionMontoTotal.setValue(0);

            iFrame.tfPercepcionBase.setEnabled(true);
            iFrame.tfPercepcionBase.setValue(0);
          } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            iFrame.tbbdDetalle.setEnabledAt(4, false);

            iFrame.cbxPercepcionRegimen.setEnabled(false);
            iFrame.cbxPercepcionRegimen.setSelectedIndex(-1);

            iFrame.tfPercepcionTasa.setEnabled(false);
            iFrame.tfPercepcionTasa.setValue(null);

            iFrame.tfPercepcionMonto.setEnabled(false);
            iFrame.tfPercepcionMonto.setValue(null);

            iFrame.tfPercepcionMontoTotal.setEnabled(false);
            iFrame.tfPercepcionMontoTotal.setValue(null);

            iFrame.tfPercepcionBase.setEnabled(false);
            iFrame.tfPercepcionBase.setValue(null);
          }
        });

    iFrame.cbxPercepcionRegimen.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            RegimenPercepcion regimenPercepcion =
                (RegimenPercepcion) iFrame.cbxPercepcionRegimen.getSelectedItem();
            iFrame.tfPercepcionTasa.setText(String.valueOf(regimenPercepcion.getPorcentaje()));
          }
        });

    iFrame.btnAgregar.addActionListener(
        (arg0) -> {
          try {
            ResumenDetalle detalle = new ResumenDetalle();

            detalle.setSerie(iFrame.tfDocumentoSerie.getText());
            detalle.setCorrelativo(Integer.valueOf(iFrame.tfDocumentoCorrelativo.getText()));
            detalle.setTipoDocumento((Tipo) iFrame.cbxDocumentoTipo.getSelectedItem());

            if (iFrame.cbxDocumentoIdentidadTipo.getSelectedIndex() >= 0
                && !iFrame.tfDocumentoIdentidadNumero.getText().isEmpty()) {
              Empresa adquiriente = new Empresa();
              adquiriente.setNumero(iFrame.tfDocumentoIdentidadNumero.getText());
              adquiriente.setTipoDocumentoIdentidad(
                  (Tipo) iFrame.cbxDocumentoIdentidadTipo.getSelectedItem());
              detalle.setAdquiriente(adquiriente);
            }

            if (iFrame.cbxDocumentoReferenciaTipo.getSelectedIndex() >= 0
                && !iFrame.tfDocumentoReferenciaSerie.getText().isEmpty()
                && !iFrame.tfDocumentoReferenciaCorrelativo.getText().isEmpty()) {
              detalle.setSerieReferencia(iFrame.tfDocumentoReferenciaSerie.getText());
              detalle.setCorrelativoReferencia(
                  Integer.valueOf(iFrame.tfDocumentoReferenciaCorrelativo.getText()));
              detalle.setTipoDocumentoReferencia(
                  (Tipo) iFrame.cbxDocumentoReferenciaTipo.getSelectedItem());
            }

            if (iFrame.cbxPercepcionRegimen.getSelectedIndex() >= 0) {
              Percepcion percepcion = new Percepcion();
              percepcion.setRegimenPercepcion(
                  (RegimenPercepcion) iFrame.cbxPercepcionRegimen.getSelectedItem());
              percepcion.setMonto((Double) iFrame.tfPercepcionMonto.getValue());
              percepcion.setMontoTotal((Double) iFrame.tfPercepcionMonto.getValue());
              percepcion.setMonto((Double) iFrame.tfPercepcionMontoTotal.getValue());
              percepcion.setBase((Double) iFrame.tfPercepcionBase.getValue());
              detalle.setPercepcion(percepcion);
            }

            detalle.setEstado((Tipo) iFrame.cbxEstado.getSelectedItem());

            Number importeTotal = (Number) iFrame.tfImporteTotal.getValue();
            detalle.setImporteTotal(importeTotal.doubleValue());

            detalle.setMoneda((Tipo) iFrame.cbxMoneda.getSelectedItem());

            Number gravadas = (Number) iFrame.tfGravadas.getValue();
            if (gravadas.doubleValue() > 0) {
              Operacion operacionGravadas = new Operacion();
              operacionGravadas.setCodigo("01");
              operacionGravadas.setDescripcion("Gravado");
              operacionGravadas.setTotal(gravadas.doubleValue());
              detalle.setGravadas(operacionGravadas);
            }

            Number exoneradas = (Number) iFrame.tfExoneradas.getValue();
            if (exoneradas.doubleValue() > 0) {
              Operacion operacionExoneradas = new Operacion();
              operacionExoneradas.setCodigo("02");
              operacionExoneradas.setDescripcion("Exonerado");
              operacionExoneradas.setTotal(exoneradas.doubleValue());
              detalle.setExoneradas(operacionExoneradas);
            }

            Number inafectas = (Number) iFrame.tfInafectas.getValue();
            if (inafectas.doubleValue() > 0) {
              Operacion operacionInafectas = new Operacion();
              operacionInafectas.setCodigo("03");
              operacionInafectas.setDescripcion("Inafecto");
              operacionInafectas.setTotal(inafectas.doubleValue());
              detalle.setInafectas(operacionInafectas);
            }

            Number exportacion = (Number) iFrame.tfExportacion.getValue();
            if (exportacion.doubleValue() > 0) {
              Operacion operacionExportacion = new Operacion();
              operacionExportacion.setCodigo("04");
              operacionExportacion.setDescripcion("Exportación");
              operacionExportacion.setTotal(exportacion.doubleValue());
              detalle.setExportacion(operacionExportacion);
            }

            Number gratuitas = (Number) iFrame.tfGratuitas.getValue();
            if (gratuitas.doubleValue() > 0) {
              Operacion operacionGratuitas = new Operacion();
              operacionGratuitas.setCodigo("05");
              operacionGratuitas.setDescripcion("Gratuitas");
              operacionGratuitas.setTotal(gratuitas.doubleValue());
              detalle.setGratuitas(operacionGratuitas);
            }

            Number otrosCargos = (Number) iFrame.tfOtrosCargos.getValue();
            if (otrosCargos.doubleValue() > 0) {
              OtrosCargos operacionOtrosCargos = new OtrosCargos();
              operacionOtrosCargos.setIndicador(true);
              operacionOtrosCargos.setTotal(otrosCargos.doubleValue());
              detalle.setOtrosCargos(operacionOtrosCargos);
            }

            Number igv = (Number) iFrame.tfIgv.getValue();
            Impuesto impuestoIgv = new Impuesto();
            impuestoIgv.setTotal(igv.doubleValue());
            impuestoIgv.setCodigo("1000");
            impuestoIgv.setDescripcion("IGV");
            impuestoIgv.setCodigoInternacional("VAT");
            detalle.setIgv(impuestoIgv);

            Number isc = (Number) iFrame.tfIsc.getValue();
            if (isc.doubleValue() > 0) {
              Impuesto impuestoIsc = new Impuesto();
              impuestoIsc.setTotal(isc.doubleValue());
              impuestoIsc.setCodigo("2000");
              impuestoIsc.setDescripcion("ISC");
              impuestoIsc.setCodigoInternacional("EXC");
              detalle.setIsc(impuestoIsc);
            }

            Number otrosTributos = (Number) iFrame.tfOtrosTributos.getValue();
            if (otrosTributos.doubleValue() > 0) {
              Impuesto impuestoOtrosTributos = new Impuesto();
              impuestoOtrosTributos.setTotal(otrosTributos.doubleValue());
              impuestoOtrosTributos.setCodigo("9999");
              impuestoOtrosTributos.setDescripcion("Otros tributos");
              impuestoOtrosTributos.setCodigoInternacional("OTH");
              detalle.setOtrosTributos(impuestoOtrosTributos);
            }

            Number bolsas = (Number) iFrame.tfBolsasPlasticas.getValue();
            if (bolsas.doubleValue() > 0) {
              Impuesto impuestoBolsas = new Impuesto();
              impuestoBolsas.setTotal(bolsas.doubleValue());
              impuestoBolsas.setCodigo("7152");
              impuestoBolsas.setDescripcion("Impuesto a la bolsa plastica");
              impuestoBolsas.setCodigoInternacional("OTH");
              detalle.setImpuestoBolsa(impuestoBolsas);
            }

            eventList.add(detalle);

            iFrame.tbbdDetalle.setSelectedIndex(0);

            iFrame.cbxEstado.setSelectedIndex(0);
            iFrame.cbxEstado.requestFocus();

            iFrame.cbxMoneda.setSelectedIndex(0);

            iFrame.cbxDocumentoTipo.setSelectedIndex(0);

            iFrame
                .tfDocumentoSerie
                .getDocument()
                .remove(0, iFrame.tfDocumentoSerie.getText().length());

            iFrame
                .tfDocumentoCorrelativo
                .getDocument()
                .remove(0, iFrame.tfDocumentoCorrelativo.getText().length());

            iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(0);

            iFrame.tfDocumentoIdentidadNumero.setEnabled(false);

            iFrame.chckPercepcion.setSelected(false);

            iFrame.tfImporteTotal.setValue(0);

            iFrame.tfGravadas.setValue(0);

            iFrame.tfExoneradas.setValue(0);

            iFrame.tfInafectas.setValue(0);

            iFrame.tfGratuitas.setValue(0);

            iFrame.tfExportacion.setValue(0);

            iFrame.tfOtrosCargos.setValue(0);

            iFrame.tfIgv.setValue(0);

            iFrame.tfIsc.setValue(0);

            iFrame.tfOtrosTributos.setValue(0);

            iFrame.tfBolsasPlasticas.setValue(0);

            iFrame.cbxPercepcionRegimen.setSelectedIndex(-1);

            iFrame.tfPercepcionTasa.setValue(null);

            iFrame.tfPercepcionMonto.setValue(null);

            iFrame.tfPercepcionMontoTotal.setValue(null);

            iFrame.tfPercepcionBase.setValue(null);

            iFrame.btnGuardar.setEnabled(true);
          } catch (BadLocationException ex) {
            Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                ResumenController.class.getName(),
                JOptionPane.ERROR_MESSAGE);
          }
        });

    iFrame.btnEliminar.addActionListener(
        (ActionEvent ae) -> {
          if (!selectionModel.isSelectionEmpty()) {
            eventList.removeAll(selectionModel.getSelected());
          }
        });

    iFrame
        .table
        .getSelectionModel()
        .addListSelectionListener(
            (ListSelectionEvent arg0) -> {
              int selectedRow = iFrame.table.getSelectedRow();
              if (selectedRow >= 0) {
                iFrame.btnEliminar.setEnabled(true);
              } else {
                iFrame.btnEliminar.setEnabled(false);
              }
            });

    iFrame.tfDocumentoSerie.getDocument().addDocumentListener(dlEnabled);
    iFrame.tfDocumentoCorrelativo.getDocument().addDocumentListener(dlEnabled);
    iFrame.tfDocumentoIdentidadNumero.getDocument().addDocumentListener(dlEnabled);
    iFrame.tfDocumentoReferenciaSerie.getDocument().addDocumentListener(dlEnabled);
    iFrame.tfDocumentoReferenciaCorrelativo.getDocument().addDocumentListener(dlEnabled);

    iFrame.tfGravadas.getDocument().addDocumentListener(dlSum);
    iFrame.tfExoneradas.getDocument().addDocumentListener(dlSum);
    iFrame.tfInafectas.getDocument().addDocumentListener(dlSum);
    iFrame.tfOtrosCargos.getDocument().addDocumentListener(dlSum);
    iFrame.tfOtrosCargos.getDocument().addDocumentListener(dlSum);
    iFrame.tfIsc.getDocument().addDocumentListener(dlSum);
    iFrame.tfOtrosTributos.getDocument().addDocumentListener(dlSum);
    iFrame.tfBolsasPlasticas.getDocument().addDocumentListener(dlSum);
  }

  private void initComponents() {
    summaryDao = new ISummaryDao();
    resumenDetalleDao = new IResumenDetalleDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());
    eventList = new BasicEventList<>();

    TableFormat<ResumenDetalle> tableFormat =
        new TableFormat<ResumenDetalle>() {
          @Override
          public int getColumnCount() {
            return 24;
          }

          @Override
          public String getColumnName(int column) {
            switch (column) {
              case 0:
                return "Serie";
              case 1:
                return "Correlativo";
              case 2:
                return "Tipo";
              case 3:
                return "Adquiriente";
              case 4:
                return "Referencia Serie";
              case 5:
                return "Referencia Correlativo";
              case 6:
                return "Referencia Tipo";
              case 7:
                return "Percepcion Descripcion";
              case 8:
                return "Percepcion Tasa";
              case 9:
                return "Percepcion Monto";
              case 10:
                return "Percepcion Monto Totsl";
              case 11:
                return "Estado";
              case 12:
                return "Importe Total";
              case 13:
                return "Moneda";
              case 14:
                return "Gravadas";
              case 15:
                return "Exoneradas";
              case 16:
                return "Inafectas";
              case 17:
                return "Gratuitas";
              case 18:
                return "Exportacion";
              case 19:
                return "Otros Cargos";
              case 20:
                return "IGV";
              case 21:
                return "ISC";
              case 22:
                return "Otros Tributos";
              case 23:
                return "Bolsas";
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }

          @Override
          public Object getColumnValue(ResumenDetalle detalle, int column) {
            switch (column) {
              case 0:
                return detalle.getSerie();
              case 1:
                return detalle.getCorrelativo();
              case 2:
                return detalle.getTipoDocumento().getDescripcion();
              case 3:
                if (detalle.getAdquiriente() != null) return detalle.getAdquiriente().getNumero();
                else return "";
              case 4:
                if (detalle.getSerieReferencia() != null) return detalle.getSerieReferencia();
                else return "";
              case 5:
                if (detalle.getCorrelativoReferencia() > 0)
                  return detalle.getCorrelativoReferencia();
                else return "";
              case 6:
                if (detalle.getTipoDocumentoReferencia() != null)
                  return detalle.getTipoDocumentoReferencia().getDescripcion();
                else return "";
              case 7:
                if (detalle.getPercepcion() != null)
                  return detalle.getPercepcion().getRegimenPercepcion().getDescripcion();
                else return "";
              case 8:
                if (detalle.getPercepcion() != null)
                  return detalle.getPercepcion().getRegimenPercepcion().getPorcentaje();
                else return "";
              case 9:
                if (detalle.getPercepcion() != null) return detalle.getPercepcion().getMonto();
                else return "";
              case 10:
                if (detalle.getPercepcion() != null) return detalle.getPercepcion().getMontoTotal();
                else return "";
              case 11:
                return detalle.getEstado().getDescripcion();
              case 12:
                return detalle.getImporteTotal();
              case 13:
                return detalle.getMoneda().getDescripcion();
              case 14:
                if (detalle.getGravadas() != null) return detalle.getGravadas().getTotal();
                else return "";
              case 15:
                if (detalle.getExoneradas() != null) return detalle.getExoneradas().getTotal();
                else return "";
              case 16:
                if (detalle.getInafectas() != null) return detalle.getInafectas().getTotal();
                else return "";
              case 17:
                if (detalle.getGratuitas() != null) return detalle.getGratuitas().getTotal();
                else return "";
              case 18:
                if (detalle.getExportacion() != null) return detalle.getExportacion().getTotal();
                else return "";
              case 19:
                if (detalle.getOtrosCargos() != null) return detalle.getOtrosCargos().getTotal();
                else return "";
              case 20:
                return detalle.getIgv().getTotal();
              case 21:
                if (detalle.getIsc() != null) return detalle.getIsc().getTotal();
                else return "";
              case 22:
                if (detalle.getOtrosTributos() != null)
                  return detalle.getOtrosTributos().getTotal();
                else return "";
              case 23:
                if (detalle.getImpuestoBolsa() != null)
                  return detalle.getImpuestoBolsa().getTotal();
                else return "";
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }
        };

    tableModel = eventTableModelWithThreadProxyList(eventList, tableFormat);
    selectionModel = new DefaultEventSelectionModel<>(eventList);
    iFrame.table.setSelectionModel(selectionModel);
    iFrame.table.setModel(tableModel);
    MyTableResize.resize(iFrame.table);

    iFrame.show();

    iFrame.btnNuevo.requestFocus();
  }

  private void start() {
    try {
      iFrame.tabbed.setSelectedIndex(0);

      iFrame.cbxTipo.setEnabled(false);
      iFrame.cbxTipo.setSelectedIndex(-1);

      iFrame.tfSerie.setEnabled(false);
      iFrame.tfSerie.setText("");

      iFrame.tfCorrelativo.setEnabled(false);
      iFrame.tfCorrelativo.setText("");

      iFrame.dpFechaGeneracion.setEnabled(false);
      iFrame.dpFechaGeneracion.setDate(null);

      iFrame.dpFechaEmision.setEnabled(false);
      iFrame.dpFechaEmision.setDate(null);

      iFrame.tbbdDetalle.setSelectedIndex(0);
      iFrame.tbbdDetalle.setEnabledAt(1, false);
      iFrame.tbbdDetalle.setEnabledAt(2, false);
      iFrame.tbbdDetalle.setEnabledAt(4, false);

      iFrame.cbxEstado.setEnabled(false);
      iFrame.cbxEstado.setSelectedIndex(-1);

      iFrame.cbxDocumentoTipo.setEnabled(false);
      iFrame.cbxDocumentoTipo.setSelectedIndex(-1);

      iFrame.tfDocumentoSerie.setEnabled(false);
      iFrame.tfDocumentoSerie.getDocument().remove(0, iFrame.tfDocumentoSerie.getText().length());

      iFrame.tfDocumentoCorrelativo.setEnabled(false);
      iFrame
          .tfDocumentoCorrelativo
          .getDocument()
          .remove(0, iFrame.tfDocumentoCorrelativo.getText().length());

      iFrame.cbxDocumentoIdentidadTipo.setEnabled(false);
      iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(-1);

      iFrame.tfDocumentoIdentidadNumero.setEnabled(false);
      iFrame
          .tfDocumentoIdentidadNumero
          .getDocument()
          .remove(0, iFrame.tfDocumentoIdentidadNumero.getText().length());

      iFrame.cbxDocumentoReferenciaTipo.setEnabled(false);
      iFrame.cbxDocumentoReferenciaTipo.setSelectedIndex(-1);

      iFrame.tfDocumentoReferenciaSerie.setEnabled(false);
      iFrame
          .tfDocumentoReferenciaSerie
          .getDocument()
          .remove(0, iFrame.tfDocumentoReferenciaSerie.getText().length());

      iFrame.tfDocumentoReferenciaCorrelativo.setEnabled(false);
      iFrame
          .tfDocumentoReferenciaCorrelativo
          .getDocument()
          .remove(0, iFrame.tfDocumentoReferenciaCorrelativo.getText().length());

      iFrame.chckPercepcion.setSelected(false);
      iFrame.chckPercepcion.setEnabled(false);

      iFrame.cbxMoneda.setEnabled(false);
      iFrame.cbxMoneda.setSelectedIndex(-1);

      iFrame.tfImporteTotal.setEnabled(false);
      iFrame.tfImporteTotal.setValue(null);

      iFrame.tfGravadas.setEnabled(false);
      iFrame.tfGravadas.setValue(null);

      iFrame.tfExoneradas.setEnabled(false);
      iFrame.tfExoneradas.setValue(null);

      iFrame.tfInafectas.setEnabled(false);
      iFrame.tfInafectas.setValue(null);

      iFrame.tfGratuitas.setEnabled(false);
      iFrame.tfGratuitas.setValue(null);

      iFrame.tfExportacion.setEnabled(false);
      iFrame.tfExportacion.setValue(null);

      iFrame.tfOtrosCargos.setEnabled(false);
      iFrame.tfOtrosCargos.setValue(null);

      iFrame.tfIgv.setEnabled(false);
      iFrame.tfIgv.setValue(null);

      iFrame.tfIsc.setEnabled(false);
      iFrame.tfIsc.setValue(null);

      iFrame.tfOtrosTributos.setEnabled(false);
      iFrame.tfOtrosTributos.setValue(null);

      iFrame.tfBolsasPlasticas.setEnabled(false);
      iFrame.tfBolsasPlasticas.setValue(null);

      iFrame.cbxPercepcionRegimen.setEnabled(false);
      iFrame.cbxPercepcionRegimen.setSelectedIndex(-1);

      iFrame.tfPercepcionTasa.setEnabled(false);
      iFrame.tfPercepcionTasa.setValue(null);

      iFrame.tfPercepcionMonto.setEnabled(false);
      iFrame.tfPercepcionMonto.setValue(null);

      iFrame.tfPercepcionMontoTotal.setEnabled(false);
      iFrame.tfPercepcionMontoTotal.setValue(null);

      iFrame.tfPercepcionBase.setEnabled(false);
      iFrame.tfPercepcionBase.setValue(null);

      eventList.clear();
      selectionModel.clearSelection();

      iFrame.btnNuevo.setEnabled(true);
      iFrame.btnNuevo.requestFocus();

      iFrame.btnGuardar.setEnabled(false);

      iFrame.btnLimpiar.setEnabled(false);
    } catch (BadLocationException ex) {
      Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

      JOptionPane.showMessageDialog(
          null, ex.getMessage(), ResumenController.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  private final DocumentListener dlEnabled =
      new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent arg0) {
          enabled();
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
          enabled();
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
          enabled();
        }
      };

  private void enabled() {
    if (iFrame.cbxDocumentoTipo.getSelectedIndex() == 0) {
      if (iFrame.tbbdDetalle.isEnabledAt(1)) {
        if (iFrame.tfDocumentoSerie.getText().isEmpty()
            || iFrame.tfDocumentoCorrelativo.getText().isEmpty()
            || iFrame.tfDocumentoIdentidadNumero.getText().isEmpty()) {
          iFrame.btnAgregar.setEnabled(false);
        } else {
          iFrame.btnAgregar.setEnabled(true);
        }
      } else {
        if (iFrame.tfDocumentoSerie.getText().isEmpty()
            || iFrame.tfDocumentoCorrelativo.getText().isEmpty()) {
          iFrame.btnAgregar.setEnabled(false);
        } else {
          iFrame.btnAgregar.setEnabled(true);
        }
      }
    } else {
      if (iFrame.tbbdDetalle.isEnabledAt(1)) {
        if (iFrame.tfDocumentoSerie.getText().isEmpty()
            || iFrame.tfDocumentoCorrelativo.getText().isEmpty()
            || iFrame.tfDocumentoIdentidadNumero.getText().isEmpty()
            || iFrame.tfDocumentoReferenciaSerie.getText().isEmpty()
            || iFrame.tfDocumentoReferenciaCorrelativo.getText().isEmpty()) {
          iFrame.btnAgregar.setEnabled(false);
        } else {
          iFrame.btnAgregar.setEnabled(true);
        }
      } else {
        if (iFrame.tfDocumentoSerie.getText().isEmpty()
            || iFrame.tfDocumentoCorrelativo.getText().isEmpty()
            || iFrame.tfDocumentoReferenciaSerie.getText().isEmpty()
            || iFrame.tfDocumentoReferenciaCorrelativo.getText().isEmpty()) {
          iFrame.btnAgregar.setEnabled(false);
        } else {
          iFrame.btnAgregar.setEnabled(true);
        }
      }
    }
  }

  private final DocumentListener dlSum =
      new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent arg0) {
          sum();
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
          sum();
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
          sum();
        }
      };

  private void sum() {
    double igv = 0;
    double importeTotal = 0;

    if (iFrame.tfGravadas.getValue() != null) {
      double gravadas = ((Number) iFrame.tfGravadas.getValue()).doubleValue();
      if (gravadas > 0) {
        igv = gravadas * 0.18;
        importeTotal += gravadas;
      }
    }

    if (iFrame.tfExoneradas.getValue() != null) {
      double exoneradas = ((Number) iFrame.tfExoneradas.getValue()).doubleValue();
      if (exoneradas > 0) {
        importeTotal += exoneradas;
      }
    }

    if (iFrame.tfInafectas.getValue() != null) {
      double inafectas = ((Number) iFrame.tfInafectas.getValue()).doubleValue();
      if (inafectas > 0) {
        importeTotal += inafectas;
      }
    }

    if (iFrame.tfExportacion.getValue() != null) {
      double exportacion = ((Number) iFrame.tfExportacion.getValue()).doubleValue();
      if (exportacion > 0) {
        importeTotal += exportacion;
      }
    }

    if (iFrame.tfOtrosCargos.getValue() != null) {
      double otrosCargos = ((Number) iFrame.tfOtrosCargos.getValue()).doubleValue();
      if (otrosCargos > 0) {
        importeTotal += otrosCargos;
      }
    }

    if (igv > 0) {
      importeTotal += igv;
    }

    if (iFrame.tfIsc.getValue() != null) {
      double isc = ((Number) iFrame.tfIsc.getValue()).doubleValue();
      if (isc > 0) {
        importeTotal += isc;
      }
    }

    if (iFrame.tfOtrosTributos.getValue() != null) {
      double otrosTributos = ((Number) iFrame.tfOtrosTributos.getValue()).doubleValue();
      if (otrosTributos > 0) {
        importeTotal += otrosTributos;
      }
    }
    if (iFrame.tfBolsasPlasticas.getValue() != null) {
      double bolsas = ((Number) iFrame.tfBolsasPlasticas.getValue()).doubleValue();
      if (bolsas > 0) {
        importeTotal += bolsas;
      }
    }

    iFrame.tfIgv.setValue(igv);

    iFrame.tfImporteTotal.setValue(importeTotal);

    if (importeTotal > 700) {
      iFrame.tbbdDetalle.setEnabledAt(1, true);

      iFrame.cbxDocumentoIdentidadTipo.setEnabled(true);
      iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(0);

      iFrame.tfDocumentoIdentidadNumero.setEnabled(true);

      enabled();
    } else {
      try {
        iFrame.tbbdDetalle.setEnabledAt(1, false);

        iFrame.cbxDocumentoIdentidadTipo.setEnabled(false);
        iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(-1);

        iFrame.tfDocumentoIdentidadNumero.setEnabled(false);
        AbstractDocument adtfDocumentoIdentidadNumero =
            (AbstractDocument) iFrame.tfDocumentoIdentidadNumero.getDocument();
        adtfDocumentoIdentidadNumero.remove(
            0, iFrame.tfDocumentoIdentidadNumero.getText().length());

        enabled();
      } catch (BadLocationException ex) {
        Logger.getLogger(ResumenController.class.getName()).log(Level.SEVERE, null, ex);

        JOptionPane.showMessageDialog(
            null, ex.getMessage(), ResumenController.class.getName(), JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
