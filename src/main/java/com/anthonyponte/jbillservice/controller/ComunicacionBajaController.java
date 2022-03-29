/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbillservice.controller;

import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.custom.MyFileCreator;
import com.anthonyponte.jbillservice.idao.IComunicacionBajaDao;
import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import com.anthonyponte.jbillservice.model.Documento;
import com.anthonyponte.jbillservice.view.ComunicacionBajaIFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import com.anthonyponte.jbillservice.maindoc.VoidedDocuments;
import com.anthonyponte.jbillservice.model.Empresa;
import java.io.File;
import java.nio.file.Files;
import java.util.prefs.Preferences;
import javax.swing.SwingWorker;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.idao.ISummaryDao;
import com.anthonyponte.jbillservice.dao.ComunicacionBajaDao;
import com.anthonyponte.jbillservice.filter.SerieFilter;
import com.anthonyponte.jbillservice.model.TipoDocumento;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.jdom2.Document;
import org.xml.sax.SAXException;

/** @author anthony */
public class ComunicacionBajaController {

  private final ComunicacionBajaIFrame iFrame;
  private final LoadingDialog dialog;
  private ComunicacionBajaDao comunicacionBajaDao;
  private SummaryDao summaryDao;
  private Preferences preferences;
  private ComunicacionBaja comunicacionBaja;

  public ComunicacionBajaController(ComunicacionBajaIFrame iFrame, LoadingDialog dialog) {
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
                new SwingWorker<Void, Void>() {
                  @Override
                  protected Void doInBackground() throws Exception {

                    TipoDocumento tipoDocumento = new TipoDocumento();
                    tipoDocumento.setDescripcion(iFrame.cbxTipo.getSelectedItem().toString());
                    comunicacionBaja.setTipoDocumento(tipoDocumento);

                    int count = summaryDao.read(comunicacionBaja);
                    comunicacionBaja.setCorrelativo(count + 1);

                    return null;
                  }

                  @Override
                  protected void done() {
                    try {
                      dialog.dispose();

                      iFrame.tfCorrelativo.setText(
                          String.valueOf(comunicacionBaja.getCorrelativo()));

                      iFrame
                          .tfDocumentoSerie
                          .getDocument()
                          .remove(0, iFrame.tfDocumentoSerie.getText().length());

                      if (iFrame.cbxTipo.getSelectedIndex() == 0) {
                        iFrame.cbxDocumentoTipo.setEnabled(true);
                        iFrame.cbxDocumentoTipo.setModel(
                            new DefaultComboBoxModel<>(
                                new String[] {"Factura", "Nota de crédito", "Nota de débito"}));

                        AbstractDocument docSerie =
                            (AbstractDocument) iFrame.tfDocumentoSerie.getDocument();
                        docSerie.setDocumentFilter(new SerieFilter('F'));
                      } else if (iFrame.cbxTipo.getSelectedIndex() == 1) {
                        iFrame.cbxDocumentoTipo.setEnabled(false);
                        iFrame.cbxDocumentoTipo.setModel(
                            new DefaultComboBoxModel<>(new String[] {"Comprobante de retención"}));

                        AbstractDocument docSerie =
                            (AbstractDocument) iFrame.tfDocumentoSerie.getDocument();
                        docSerie.setDocumentFilter(new SerieFilter('R'));
                      }
                    } catch (BadLocationException ex) {
                      Logger.getLogger(ComunicacionBajaController.class.getName())
                          .log(Level.SEVERE, null, ex);

                      JOptionPane.showMessageDialog(
                          null,
                          ex.getMessage(),
                          ComunicacionBajaController.class.getName(),
                          JOptionPane.ERROR_MESSAGE);
                    }
                  }
                };
            worker.execute();
          }
        });

    iFrame.dpDocumentoFecha.addActionListener(
        (ActionEvent e) -> {
          Date fecha = iFrame.dpDocumentoFecha.getDate();
          comunicacionBaja.setFechaReferencia(fecha);
        });

    iFrame.btnAgregar.addActionListener(
        (arg0) -> {
          try {
            String tipo = iFrame.cbxDocumentoTipo.getSelectedItem().toString();
            String serie = iFrame.tfDocumentoSerie.getText();
            int numero = Integer.parseInt(iFrame.tfDocumentoCorrelativo.getText());
            String motivo = iFrame.tfDocumentoMotivo.getText();

            DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
            model.addRow(new Object[] {tipo, serie, numero, motivo});

            iFrame.tfDocumentoSerie.setText("");
            iFrame
                .tfDocumentoCorrelativo
                .getDocument()
                .remove(0, iFrame.tfDocumentoCorrelativo.getText().length());
            iFrame.tfDocumentoMotivo.setText("");

            iFrame.btnGuardar.setEnabled(true);
          } catch (BadLocationException ex) {
            Logger.getLogger(ComunicacionBajaController.class.getName())
                .log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                ComunicacionBajaController.class.getName(),
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
          dialog.setVisible(true);
          dialog.setLocationRelativeTo(iFrame);

          SwingWorker worker =
              new SwingWorker<ComunicacionBaja, Void>() {
                @Override
                protected ComunicacionBaja doInBackground() throws Exception {
                  comunicacionBaja = new ComunicacionBaja();
                  comunicacionBaja.setUbl("2.0");
                  comunicacionBaja.setVersion("1.0");

                  TipoDocumento tipoDocumento = new TipoDocumento();
                  tipoDocumento.setDescripcion(iFrame.cbxTipo.getSelectedItem().toString());
                  comunicacionBaja.setTipoDocumento(tipoDocumento);

                  comunicacionBaja.setSerie(MyDateFormat.yyyyMMdd(new Date()));
                  comunicacionBaja.setFechaEmision(new Date());
                  comunicacionBaja.setFechaReferencia(new Date());

                  int count = summaryDao.read(comunicacionBaja);
                  comunicacionBaja.setCorrelativo(count + 1);

                  Empresa emisor = new Empresa();
                  emisor.setRuc(preferences.get(UsuarioController.RUC, ""));
                  emisor.setTipo(preferences.getInt(UsuarioController.RUC_TIPO, 0));
                  emisor.setRazonSocial(preferences.get(UsuarioController.RAZON_SOCIAL, ""));
                  comunicacionBaja.setEmisor(emisor);

                  return comunicacionBaja;
                }

                @Override
                protected void done() {
                  dialog.dispose();

                  iFrame.tabbed.setSelectedIndex(0);

                  iFrame.tfFecha.setEnabled(true);
                  iFrame.tfFecha.setText(MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaEmision()));

                  iFrame.cbxTipo.setEnabled(true);
                  iFrame.cbxTipo.setSelectedIndex(0);
                  iFrame.cbxTipo.requestFocus();

                  iFrame.tfSerie.setEnabled(true);
                  iFrame.tfSerie.setText(comunicacionBaja.getSerie());

                  iFrame.tfCorrelativo.setEnabled(true);
                  iFrame.tfCorrelativo.setText(String.valueOf(comunicacionBaja.getCorrelativo()));

                  iFrame.dpDocumentoFecha.setEnabled(true);
                  iFrame.dpDocumentoFecha.setDate(new Date());

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
                }
              };

          worker.execute();
        });

    iFrame.btnGuardar.addActionListener(
        (arg0) -> {
          File jks = new File(preferences.get(UsuarioController.FIRMA_JKS, ""));
          if (jks.exists()) {
            int input =
                JOptionPane.showOptionDialog(
                    iFrame,
                    "Seguro que desea guardar esta "
                        + iFrame.cbxTipo.getSelectedItem().toString()
                        + "?",
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
                  new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                      DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
                      List<ComunicacionBajaDetalle> comunicacionBajaDetalles = new ArrayList<>();
                      for (int i = 0; i < model.getRowCount(); i++) {
                        ComunicacionBajaDetalle comunicacionBajaDetalle =
                            new ComunicacionBajaDetalle();
                        Documento documento = new Documento();
                        String tipo = model.getValueAt(i, 0).toString();
                        String serie = model.getValueAt(i, 1).toString();
                        int correlativo = Integer.parseInt(model.getValueAt(i, 2).toString());
                        String motivo = model.getValueAt(i, 3).toString();
                        comunicacionBajaDetalle.setNumero(i + 1);

                        TipoDocumento tipoDocumento = new TipoDocumento();
                        tipoDocumento.setDescripcion(tipo);
                        documento.setTipoDocumento(tipoDocumento);
                        
                        documento.setSerie(serie);
                        documento.setCorrelativo(correlativo);
                        comunicacionBajaDetalle.setDocumento(documento);
                        comunicacionBajaDetalle.setMotivo(motivo);
                        comunicacionBajaDetalles.add(comunicacionBajaDetalle);
                      }
                      comunicacionBaja.setComunicacionBajaDetalles(comunicacionBajaDetalles);

                      VoidedDocuments voided = new VoidedDocuments();
                      Document document = voided.getStructure(comunicacionBaja);

                      try {
                        File xml =
                            MyFileCreator.create(
                                comunicacionBaja.getTipoDocumento().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                document);

                        File sign =
                            MyFileCreator.sign(
                                comunicacionBaja.getTipoDocumento().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                xml);

                        File zip =
                            MyFileCreator.compress(
                                comunicacionBaja.getTipoDocumento().getCodigo(),
                                comunicacionBaja.getSerie(),
                                comunicacionBaja.getCorrelativo(),
                                sign);

                        byte[] byteArray = Files.readAllBytes(zip.toPath());
                        comunicacionBaja.setNombreZip(zip.getName());
                        comunicacionBaja.setZip(byteArray);
                        int id = summaryDao.create(comunicacionBaja);
                        comunicacionBajaDao.create(id, comunicacionBajaDetalles);

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
                            ComunicacionBajaController.class.getName(),
                            JOptionPane.ERROR_MESSAGE);
                      }

                      return null;
                    }

                    @Override
                    protected void done() {
                      dialog.dispose();

                      if (isCancelled()) {
                        start();
                      } else {
                        start();

                        JOptionPane.showMessageDialog(
                            iFrame,
                            comunicacionBaja.getNombreZip() + " guardado",
                            "Guardado",
                            JOptionPane.INFORMATION_MESSAGE);
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
                ComunicacionBajaController.class.getName(),
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
    comunicacionBajaDao = new IComunicacionBajaDao();
    summaryDao = new ISummaryDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());

    iFrame.show();

    iFrame.btnNuevo.requestFocus();
  }

  private void start() {
    try {
      iFrame.tabbed.setSelectedIndex(0);

      iFrame.tfFecha.setEnabled(false);
      iFrame.tfFecha.setText("");

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
      DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
      model.getDataVector().removeAllElements();
      model.fireTableDataChanged();

      iFrame.btnNuevo.setEnabled(true);
      iFrame.btnNuevo.requestFocus();

      iFrame.btnGuardar.setEnabled(false);

      iFrame.btnLimpiar.setEnabled(false);
    } catch (BadLocationException ex) {
      Logger.getLogger(ComunicacionBajaController.class.getName()).log(Level.SEVERE, null, ex);

      JOptionPane.showMessageDialog(
          null,
          ex.getMessage(),
          ComunicacionBajaController.class.getName(),
          JOptionPane.ERROR_MESSAGE);
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

  private String getCodigoTipoDocumento(String descripcion) {
    String codigo = "";
    if (descripcion.equalsIgnoreCase("Factura")) codigo = "01";
    else if (descripcion.equalsIgnoreCase("Nota de crédito")) codigo = "07";
    else if (descripcion.equalsIgnoreCase("Nota de débito")) codigo = "08";
    else if (descripcion.equalsIgnoreCase("Comprobante de retención")) codigo = "20";
    return codigo;
  }
}
