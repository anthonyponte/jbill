/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbillservice.controller;

import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.idao.ComunicacionBajaDaoImpl;
import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import com.anthonyponte.jbillservice.model.Documento;
import com.anthonyponte.jbillservice.view.ComunicacionBajaIFrame;
import java.awt.event.ActionEvent;
import java.text.ParseException;
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
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import com.anthonyponte.jbillservice.maindoc.VoidedDocuments;
import com.anthonyponte.jbillservice.model.Empresa;
import java.io.File;
import java.nio.file.Files;
import java.util.prefs.Preferences;
import javax.swing.SwingWorker;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.idao.SummaryDaoImpl;
import com.anthonyponte.jbillservice.dao.ComunicacionBajaDao;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import com.anthonyponte.jbillservice.view.MainFrame;

/** @author anthony */
public class ComunicacionBajaController {

  private final MainFrame frame;
  private final ComunicacionBajaIFrame iFrame;
  private LoadingDialog dialog;
  private ComunicacionBajaDao comunicacionBajaDao;
  private SummaryDao summaryDao;
  private Preferences preferences;
  private ComunicacionBaja comunicacionBaja;

  public ComunicacionBajaController(MainFrame frame, ComunicacionBajaIFrame iFrame) {
    this.frame = frame;
    this.iFrame = iFrame;
    initComponents();
  }

  public void start() {
    iFrame.cbxTipo.addActionListener(
        (ActionEvent arg0) -> {
          if (iFrame.cbxTipo.getSelectedIndex() == 0) {
            comunicacionBaja.setTipo("RA");
            int count = summaryDao.read(comunicacionBaja);
            comunicacionBaja.setCorrelativo(count + 1);

            iFrame.tfCorrelativo.setText(String.valueOf(comunicacionBaja.getCorrelativo()));

            iFrame.cbxDocumentoTipo.setModel(
                new DefaultComboBoxModel<>(
                    new String[] {"Factura", "Nota de crédito", "Nota de débito"}));

            iFrame.cbxDocumentoTipo.setEnabled(true);

            try {
              iFrame.tfDocumentoSerie.setValue("");
              iFrame.tfDocumentoSerie.setFormatterFactory(
                  new DefaultFormatterFactory(new MaskFormatter("FAAA")));
            } catch (ParseException ex) {
              Logger.getLogger(ComunicacionBajaController.class.getName())
                  .log(Level.SEVERE, null, ex);
            }

            iFrame.tabbed.setSelectedIndex(1);
            iFrame.cbxDocumentoTipo.requestFocus();
          } else if (iFrame.cbxTipo.getSelectedIndex() == 1) {
            comunicacionBaja.setTipo("RR");
            int count = summaryDao.read(comunicacionBaja);
            comunicacionBaja.setCorrelativo(count + 1);

            iFrame.tfCorrelativo.setText(String.valueOf(comunicacionBaja.getCorrelativo()));

            iFrame.cbxDocumentoTipo.setModel(
                new DefaultComboBoxModel<>(new String[] {"Comprobante de retención"}));

            iFrame.cbxDocumentoTipo.setEnabled(false);

            try {
              iFrame.tfDocumentoSerie.setValue("");
              iFrame.tfDocumentoSerie.setFormatterFactory(
                  new DefaultFormatterFactory(new MaskFormatter("RAAA")));
            } catch (ParseException ex) {
              Logger.getLogger(ComunicacionBajaController.class.getName())
                  .log(Level.SEVERE, null, ex);
            }

            iFrame.tabbed.setSelectedIndex(1);
            iFrame.tfDocumentoSerie.requestFocus();
          }
        });

    iFrame.dpDocumentoFecha.addActionListener(
        (ActionEvent e) -> {
          Date fecha = iFrame.dpDocumentoFecha.getDate();
          comunicacionBaja.setFechaReferencia(fecha);
        });

    iFrame.btnAgregar.addActionListener(
        (arg0) -> {
          String tipo = iFrame.cbxDocumentoTipo.getSelectedItem().toString();
          String serie = iFrame.tfDocumentoSerie.getText();
          int numero = Integer.parseInt(iFrame.tfDocumentoCorrelativo.getText());
          String motivo = iFrame.tfDocumentoMotivo.getText();

          DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
          model.addRow(new Object[] {tipo, serie, numero, motivo});

          iFrame.tfDocumentoSerie.setText("");
          iFrame.tfDocumentoCorrelativo.setText("");
          iFrame.tfDocumentoMotivo.setText("");

          iFrame.btnGuardar.setEnabled(true);
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
                  comunicacionBaja.setTipo("RA");
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
                  iFrame.tfFecha.setEnabled(true);
                  iFrame.cbxTipo.setEnabled(true);
                  iFrame.tfSerie.setEnabled(true);
                  iFrame.tfCorrelativo.setEnabled(true);
                  iFrame.dpDocumentoFecha.setEnabled(true);
                  iFrame.cbxDocumentoTipo.setEnabled(true);
                  iFrame.tfDocumentoSerie.setEnabled(true);
                  iFrame.tfDocumentoCorrelativo.setEnabled(true);
                  iFrame.tfDocumentoMotivo.setEnabled(true);
                  iFrame.btnAgregar.setEnabled(false);
                  iFrame.btnEliminar.setEnabled(false);
                  iFrame.table.setEnabled(true);
                  iFrame.btnNuevo.setEnabled(false);
                  iFrame.btnGuardar.setEnabled(false);
                  iFrame.btnLimpiar.setEnabled(true);

                  iFrame.tfFecha.setText(MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaEmision()));
                  iFrame.tfSerie.setText(comunicacionBaja.getSerie());
                  iFrame.tfCorrelativo.setText(String.valueOf(comunicacionBaja.getCorrelativo()));
                  iFrame.dpDocumentoFecha.setDate(new Date());

                  iFrame.cbxTipo.setModel(
                      new DefaultComboBoxModel<>(
                          new String[] {"Comunicacion de baja", "Resumen de reversiones"}));
                  iFrame.cbxDocumentoTipo.setModel(
                      new DefaultComboBoxModel<>(
                          new String[] {"Factura", "Nota de crédito", "Nota de débito"}));

                  try {
                    iFrame.tfDocumentoSerie.setFormatterFactory(
                        new DefaultFormatterFactory(new MaskFormatter("FAAA")));
//
//                    iFrame.tfDocumentoCorrelativo.setFormatterFactory(
//                        new DefaultFormatterFactory(
//                            new NumberFormatter(new DecimalFormat("####"))));
                  } catch (ParseException ex) {
                    Logger.getLogger(ComunicacionBajaController.class.getName())
                        .log(Level.SEVERE, null, ex);
                  }

                  iFrame.tabbed.setSelectedIndex(0);
                  iFrame.cbxTipo.requestFocus();

                  dialog.dispose();
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

                        documento.setTipo(getCodigoTipoDocumento(tipo));
                        documento.setSerie(serie);
                        documento.setCorrelativo(correlativo);
                        comunicacionBajaDetalle.setDocumento(documento);

                        comunicacionBajaDetalle.setMotivo(motivo);

                        comunicacionBajaDetalles.add(comunicacionBajaDetalle);
                      }
                      comunicacionBaja.setComunicacionBajaDetalles(comunicacionBajaDetalles);

                      VoidedDocuments voided = new VoidedDocuments();
                      File zip = voided.getStructure(comunicacionBaja);
                      byte[] byteArray = Files.readAllBytes(zip.toPath());
                      comunicacionBaja.setNombreZip(zip.getName());
                      comunicacionBaja.setZip(byteArray);

                      int id = summaryDao.create(comunicacionBaja);
                      comunicacionBajaDao.create(id, comunicacionBajaDetalles);

                      zip.delete();

                      return null;
                    }

                    @Override
                    protected void done() {
                      dialog.dispose();

                      JOptionPane.showMessageDialog(
                          iFrame,
                          comunicacionBaja.getNombreZip() + " guardado",
                          "Guardado",
                          JOptionPane.INFORMATION_MESSAGE);

                      initComponents();
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
          initComponents();
        });

    iFrame.tfDocumentoSerie.getDocument().addDocumentListener(dl);
    iFrame.tfDocumentoCorrelativo.getDocument().addDocumentListener(dl);
    iFrame.tfDocumentoMotivo.getDocument().addDocumentListener(dl);
  }

  private void initComponents() {
    dialog = new LoadingDialog(frame, false);
    comunicacionBajaDao = new ComunicacionBajaDaoImpl();
    summaryDao = new SummaryDaoImpl();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());

    iFrame.show();

    iFrame.tfFecha.setEditable(false);
    iFrame.cbxTipo.setEditable(false);
    iFrame.tfSerie.setEditable(false);
    iFrame.tfCorrelativo.setEditable(false);

    iFrame.tfFecha.setEnabled(false);
    iFrame.cbxTipo.setEnabled(false);
    iFrame.tfSerie.setEnabled(false);
    iFrame.tfCorrelativo.setEnabled(false);
    iFrame.dpDocumentoFecha.setEnabled(false);
    iFrame.cbxDocumentoTipo.setEnabled(false);
    iFrame.tfDocumentoSerie.setEnabled(false);
    iFrame.tfDocumentoCorrelativo.setEnabled(false);
    iFrame.tfDocumentoMotivo.setEnabled(false);
    iFrame.btnAgregar.setEnabled(false);
    iFrame.btnEliminar.setEnabled(false);
    iFrame.table.setEnabled(false);
    iFrame.btnNuevo.setEnabled(true);
    iFrame.btnGuardar.setEnabled(false);
    iFrame.btnLimpiar.setEnabled(false);

    iFrame.tfFecha.setText("");
    iFrame.cbxTipo.removeAllItems();
    iFrame.tfSerie.setText("");
    iFrame.tfCorrelativo.setText("");
    iFrame.dpDocumentoFecha.setDate(null);
    iFrame.cbxDocumentoTipo.removeAllItems();
    iFrame.tfDocumentoSerie.setValue("");
//    iFrame.tfDocumentoCorrelativo.setValue(null);
    iFrame.tfDocumentoMotivo.setText("");
    DefaultTableModel model = (DefaultTableModel) iFrame.table.getModel();
    model.getDataVector().removeAllElements();
    model.fireTableDataChanged();

    iFrame.tfDocumentoSerie.putClientProperty("JTextField.showClearButton", true);
    iFrame.tfDocumentoCorrelativo.putClientProperty("JTextField.showClearButton", true);
    iFrame.tfDocumentoMotivo.putClientProperty("JTextField.showClearButton", true);

    iFrame.tabbed.setSelectedIndex(0);
    iFrame.btnNuevo.requestFocus();
  }

  private final DocumentListener dl =
      new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent arg0) {
          enableBtnAgregar();
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
          enableBtnAgregar();
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
          enableBtnAgregar();
        }
      };

  private void enableBtnAgregar() {
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

    if (descripcion.equalsIgnoreCase("Factura")) {
      codigo = "01";
    } else if (descripcion.equalsIgnoreCase("Nota de crédito")) {
      codigo = "07";
    } else if (descripcion.equalsIgnoreCase("Nota de débito")) {
      codigo = "08";
    } else if (descripcion.equalsIgnoreCase("Comprobante de retención")) {
      codigo = "20";
    }

    return codigo;
  }
}
