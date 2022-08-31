/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbill.controller;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.custom.MyFileCreator;
import com.anthonyponte.jbill.custom.MyTableResize;
import com.anthonyponte.jbill.idao.IComunicacionDetalleDao;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import com.anthonyponte.jbill.model.Bill;
import com.anthonyponte.jbill.view.ComunicacionIFrame;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import com.anthonyponte.jbill.maindoc.VoidedDocuments;
import java.io.File;
import java.nio.file.Files;
import java.util.prefs.Preferences;
import javax.swing.SwingWorker;
import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.idao.ISummaryDao;
import com.anthonyponte.jbill.filter.SerieFilter;
import com.anthonyponte.jbill.model.Comunicacion;
import com.anthonyponte.jbill.model.Empresa;
import com.anthonyponte.jbill.model.Tipo;
import com.anthonyponte.jbill.tableformat.ComunicacionDetalleTableFormat;
import com.anthonyponte.jbill.view.LoadingDialog;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.jdom2.Document;
import org.xml.sax.SAXException;
import com.anthonyponte.jbill.dao.ComunicacionDetalleDao;

/**
 * @author anthony
 */
public class ComunicacionController {

  private final ComunicacionIFrame iFrame;
  private final LoadingDialog dialog;
  private Preferences preferences;
  private SummaryDao summaryDao;
  private ComunicacionDetalleDao comunicacionBajaDao;
  private EventList<ComunicacionDetalle> eventList;
  private AdvancedTableModel<ComunicacionDetalle> tableModel;
  private AdvancedListSelectionModel<ComunicacionDetalle> selectionModel;

  public ComunicacionController(ComunicacionIFrame iFrame, LoadingDialog dialog) {
    this.iFrame = iFrame;
    this.dialog = dialog;
    initComponents();
  }

  public void init() {
    iFrame.cbxTipo.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(iFrame);

            SwingWorker worker =
                new SwingWorker<Integer, Void>() {
                  @Override
                  protected Integer doInBackground() throws Exception {
                    int count = 0;
                    try {
                      Tipo tipoDocumento = (Tipo) iFrame.cbxTipo.getSelectedItem();

                      Date fechaGeneracion = iFrame.dpFecha.getDate();

                      count = summaryDao.count(tipoDocumento, fechaGeneracion);
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(
                          null,
                          ex.getMessage(),
                          ComunicacionController.class.getName(),
                          JOptionPane.ERROR_MESSAGE);
                    }
                    return count;
                  }

                  @Override
                  protected void done() {
                    try {
                      dialog.dispose();

                      Date fecha = iFrame.dpFecha.getDate();
                      iFrame.tfSerie.setText(MyDateFormat.yyyyMMdd(fecha));

                      int count = get();
                      iFrame.tfCorrelativo.setText(String.valueOf(count));

                      iFrame
                          .tfDocumentoSerie
                          .getDocument()
                          .remove(0, iFrame.tfDocumentoSerie.getText().length());

                      if (iFrame.cbxTipo.getSelectedIndex() == 0) {
                        iFrame.cbxDocumentoTipo.setEnabled(true);
                        iFrame.cbxDocumentoTipo.setModel(
                            new DefaultComboBoxModel(
                                new Tipo[] {
                                  new Tipo("01", "Factura"),
                                  new Tipo("07", "Nota de crédito"),
                                  new Tipo("08", "Nota de débito")
                                }));

                        AbstractDocument docSerie =
                            (AbstractDocument) iFrame.tfDocumentoSerie.getDocument();
                        docSerie.setDocumentFilter(new SerieFilter('F'));
                      } else if (iFrame.cbxTipo.getSelectedIndex() == 1) {
                        iFrame.cbxDocumentoTipo.setEnabled(false);
                        iFrame.cbxDocumentoTipo.setModel(
                            new DefaultComboBoxModel(
                                new Tipo[] {new Tipo("20", "Comprobante de retención")}));

                        AbstractDocument docSerie =
                            (AbstractDocument) iFrame.tfDocumentoSerie.getDocument();
                        docSerie.setDocumentFilter(new SerieFilter('R'));
                      }
                    } catch (BadLocationException | InterruptedException | ExecutionException ex) {
                      JOptionPane.showMessageDialog(
                          null,
                          ex.getMessage(),
                          ComunicacionController.class.getName(),
                          JOptionPane.ERROR_MESSAGE);
                    }
                  }
                };
            worker.execute();
          }
        });

    iFrame.btnAgregar.addActionListener(
        (arg0) -> {
          try {
            Bill documento = new Bill();
            documento.setTipo((Tipo) iFrame.cbxDocumentoTipo.getSelectedItem());
            documento.setSerie(iFrame.tfDocumentoSerie.getText());
            documento.setCorrelativo(Integer.parseInt(iFrame.tfDocumentoCorrelativo.getText()));

            ComunicacionDetalle detalle = new ComunicacionDetalle();
            detalle.setDocumento(documento);
            detalle.setMotivo(iFrame.tfDocumentoMotivo.getText());

            eventList.add(detalle);

            MyTableResize.resize(iFrame.table);

            iFrame.cbxDocumentoTipo.setSelectedIndex(0);

            iFrame
                .tfDocumentoSerie
                .getDocument()
                .remove(0, iFrame.tfDocumentoSerie.getText().length());

            iFrame
                .tfDocumentoCorrelativo
                .getDocument()
                .remove(0, iFrame.tfDocumentoCorrelativo.getText().length());

            iFrame.tfDocumentoMotivo.setText("");

            iFrame.btnGuardar.setEnabled(true);
          } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                ComunicacionController.class.getName(),
                JOptionPane.ERROR_MESSAGE);
          }
        });

    iFrame.btnEliminar.addActionListener(
        (arg0) -> {
          DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
          int selectedRow = iFrame.table.getSelectedRow();
          model.removeRow(selectedRow);

          int rowCount = model.getRowCount();
          if (rowCount > 0) {
            iFrame.btnGuardar.setEnabled(true);
          } else {
            iFrame.btnGuardar.setEnabled(false);
          }
        });

    iFrame.btnNuevo.addActionListener(
        (ActionEvent arg0) -> {
          iFrame.dpFecha.setDate(new Date());

          iFrame.dpDocumentoFecha.setDate(new Date());

          iFrame.cbxTipo.setEnabled(true);
          iFrame.cbxTipo.setSelectedIndex(0);
          iFrame.cbxTipo.requestFocus();

          iFrame.tfSerie.setEnabled(true);

          iFrame.tfCorrelativo.setEnabled(true);

          iFrame.dpDocumentoFecha.setEnabled(true);

          iFrame.cbxDocumentoTipo.setEnabled(true);
          iFrame.cbxDocumentoTipo.setSelectedIndex(0);

          iFrame.tfDocumentoSerie.setEnabled(true);

          iFrame.tfDocumentoCorrelativo.setEnabled(true);

          iFrame.tfDocumentoMotivo.setEnabled(true);

          iFrame.btnAgregar.setEnabled(false);

          iFrame.btnEliminar.setEnabled(false);

          iFrame.table.setEnabled(true);

          iFrame.btnNuevo.setEnabled(false);

          iFrame.btnGuardar.setEnabled(false);

          iFrame.btnLimpiar.setEnabled(true);
        });

    iFrame.btnGuardar.addActionListener(
        (arg0) -> {
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
                  new SwingWorker<Comunicacion, Void>() {
                    @Override
                    protected Comunicacion doInBackground() throws Exception {
                      Comunicacion comunicacionBaja = new Comunicacion();
                      comunicacionBaja.setUbl("2.0");
                      comunicacionBaja.setVersion("1.0");
                      comunicacionBaja.setTipo((Tipo) iFrame.cbxTipo.getSelectedItem());
                      comunicacionBaja.setSerie(MyDateFormat.yyyyMMdd(iFrame.dpFecha.getDate()));
                      comunicacionBaja.setCorrelativo(
                          Integer.valueOf(iFrame.tfCorrelativo.getText()));

                      comunicacionBaja.setFechaEmision(iFrame.dpFecha.getDate());
                      comunicacionBaja.setFechaReferencia(iFrame.dpDocumentoFecha.getDate());

                      Empresa emisor = new Empresa();
                      emisor.setNumero(preferences.get(UsuarioController.RUC, ""));

                      Tipo tipo = new Tipo();
                      tipo.setCodigo(preferences.get(UsuarioController.RUC_TIPO, ""));
                      emisor.setTipo(tipo);

                      emisor.setNombre(preferences.get(UsuarioController.RAZON_SOCIAL, ""));
                      comunicacionBaja.setEmisor(emisor);

                      comunicacionBaja.setDetalles(eventList);

                      VoidedDocuments voided = new VoidedDocuments();
                      Document document = voided.getStructure(comunicacionBaja);

                      try {
                        File xml =
                            MyFileCreator.create(
                                comunicacionBaja.getTipo().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                document);

                        File sign =
                            MyFileCreator.sign(
                                comunicacionBaja.getTipo().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                xml);

                        File zip =
                            MyFileCreator.compress(
                                comunicacionBaja.getTipo().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                sign);

                        byte[] byteArray = Files.readAllBytes(zip.toPath());
                        comunicacionBaja.setNombreZip(zip.getName());
                        comunicacionBaja.setZip(byteArray);
                        int id = summaryDao.create(comunicacionBaja);
                        comunicacionBajaDao.create(id, eventList);

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

                        JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            ComunicacionController.class.getName(),
                            JOptionPane.ERROR_MESSAGE);
                      }

                      return comunicacionBaja;
                    }

                    @Override
                    protected void done() {
                      dialog.dispose();

                      if (isCancelled()) {
                        start();
                      } else {
                        try {
                          start();

                          Comunicacion get = get();

                          JOptionPane.showMessageDialog(
                              iFrame,
                              get.getNombreZip() + " guardado",
                              "Guardado",
                              JOptionPane.INFORMATION_MESSAGE);
                        } catch (InterruptedException | ExecutionException ex) {
                          JOptionPane.showMessageDialog(
                              null,
                              ex.getMessage(),
                              ComunicacionController.class.getName(),
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
                ComunicacionController.class.getName(),
                JOptionPane.ERROR_MESSAGE);
          }
        });

    iFrame.btnLimpiar.addActionListener(
        (arg0) -> {
          start();
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

    iFrame.tfDocumentoSerie.getDocument().addDocumentListener(dl);
    iFrame.tfDocumentoCorrelativo.getDocument().addDocumentListener(dl);
    iFrame.tfDocumentoMotivo.getDocument().addDocumentListener(dl);
  }

  private void initComponents() {
    comunicacionBajaDao = new IComunicacionDetalleDao();
    summaryDao = new ISummaryDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());
    eventList = new BasicEventList<>();
    tableModel =
        eventTableModelWithThreadProxyList(eventList, new ComunicacionDetalleTableFormat());
    selectionModel = new DefaultEventSelectionModel<>(eventList);

    iFrame.table.setModel(tableModel);
    iFrame.table.setSelectionModel(selectionModel);

    iFrame.show();

    iFrame.btnNuevo.requestFocus();
  }

  private void start() {
    try {
      iFrame.tabbed.setSelectedIndex(0);

      iFrame.dpFecha.setEnabled(false);
      iFrame.dpFecha.setDate(null);

      iFrame.cbxTipo.setEnabled(false);
      iFrame.cbxTipo.setSelectedIndex(-1);

      iFrame.tfSerie.setEnabled(false);
      iFrame.tfSerie.setText("");

      iFrame.tfCorrelativo.setEnabled(false);
      iFrame.tfCorrelativo.setText("");

      iFrame.dpDocumentoFecha.setEnabled(false);
      iFrame.dpDocumentoFecha.setDate(null);

      iFrame.cbxDocumentoTipo.setEnabled(false);
      iFrame.cbxDocumentoTipo.setSelectedIndex(-1);

      iFrame.tfDocumentoSerie.setEnabled(false);
      iFrame.tfDocumentoSerie.getDocument().remove(0, iFrame.tfDocumentoSerie.getText().length());
      AbstractDocument docSerie = (AbstractDocument) iFrame.tfDocumentoSerie.getDocument();
      docSerie.setDocumentFilter(new SerieFilter('F'));

      iFrame.tfDocumentoCorrelativo.setEnabled(false);
      iFrame
          .tfDocumentoCorrelativo
          .getDocument()
          .remove(0, iFrame.tfDocumentoCorrelativo.getText().length());

      iFrame.tfDocumentoMotivo.setEnabled(false);
      iFrame.tfDocumentoMotivo.setText("");

      iFrame.btnAgregar.setEnabled(false);

      iFrame.btnEliminar.setEnabled(false);

      iFrame.table.setEnabled(false);
      eventList.clear();

      iFrame.btnNuevo.setEnabled(true);
      iFrame.btnNuevo.requestFocus();

      iFrame.btnGuardar.setEnabled(false);

      iFrame.btnLimpiar.setEnabled(false);
    } catch (BadLocationException ex) {
      Logger.getLogger(ComunicacionController.class.getName()).log(Level.SEVERE, null, ex);

      JOptionPane.showMessageDialog(
          null, ex.getMessage(), ComunicacionController.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  private final DocumentListener dl =
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
    if (iFrame.tfDocumentoSerie.getText().length() < 4
        || iFrame.tfDocumentoCorrelativo.getText().isEmpty()
        || iFrame.tfDocumentoMotivo.getText().length() < 15) {
      iFrame.btnAgregar.setEnabled(false);
    } else {
      iFrame.btnAgregar.setEnabled(true);
    }
  }
}
