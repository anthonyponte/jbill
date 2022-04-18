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

package com.anthonyponte.jbillservice.controller;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.idao.ISummaryDao;
import com.anthonyponte.jbillservice.model.Empresa;
import com.anthonyponte.jbillservice.model.ResumenDiario;
import com.anthonyponte.jbillservice.model.ResumenDiarioDetalle;
import com.anthonyponte.jbillservice.model.TipoDocumento;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import com.anthonyponte.jbillservice.view.ResumenDiarioIFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/** @author anthony */
public class ResumenDiarioController {
  private final ResumenDiarioIFrame iFrame;
  private final LoadingDialog dialog;
  private Preferences preferences;
  private SummaryDao summaryDao;
  private ResumenDiario resumenDiario;
  private EventList<ResumenDiarioDetalle> eventList;
  private AdvancedTableModel<ResumenDiarioDetalle> tableModel;
  private AdvancedListSelectionModel<ResumenDiarioDetalle> selectionModel;

  public ResumenDiarioController(ResumenDiarioIFrame iFrame, LoadingDialog dialog) {
    this.iFrame = iFrame;
    this.dialog = dialog;
    initComponents();
  }

  void init() {
    iFrame.btnNuevo.addActionListener(
        (ActionEvent arg0) -> {
          dialog.setVisible(true);
          dialog.setLocationRelativeTo(iFrame);

          SwingWorker worker =
              new SwingWorker<ResumenDiario, Void>() {
                @Override
                protected ResumenDiario doInBackground() throws Exception {
                  ResumenDiario resumen = null;
                  try {
                    resumen = new ResumenDiario();

                    resumen.setUbl("2.0");
                    resumen.setVersion("1.0");
                    resumen.setSerie(MyDateFormat.yyyyMMdd(new Date()));

                    TipoDocumento tipoDocumento = new TipoDocumento();
                    tipoDocumento.setDescripcion("Resumen diario");
                    resumen.setTipoDocumento(tipoDocumento);

                    resumen.setFechaEmision(new Date());
                    resumen.setFechaReferencia(new Date());

                    Empresa emisor = new Empresa();
                    emisor.setRuc(preferences.get(UsuarioController.RUC, ""));
                    emisor.setTipo(preferences.getInt(UsuarioController.RUC_TIPO, 0));
                    emisor.setRazonSocial(preferences.get(UsuarioController.RAZON_SOCIAL, ""));
                    resumen.setEmisor(emisor);

                    int count = summaryDao.read(resumen);
                    resumen.setCorrelativo(count + 1);
                  } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenDiarioController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                  return resumen;
                }

                @Override
                protected void done() {
                  try {
                    dialog.dispose();

                    resumenDiario = get();

                    iFrame.tabbed.setSelectedIndex(0);

                    iFrame.tfTipo.setEnabled(true);
                    iFrame.tfTipo.setText(resumenDiario.getTipoDocumento().getDescripcion());

                    iFrame.tfSerie.setEnabled(true);
                    iFrame.tfSerie.setText(resumenDiario.getSerie());

                    iFrame.tfCorrelativo.setEnabled(true);
                    iFrame.tfCorrelativo.setText(String.valueOf(resumenDiario.getCorrelativo()));

                    iFrame.tfFechaGeneracion.setEnabled(true);
                    iFrame.tfFechaGeneracion.setText(
                        MyDateFormat.d_MMMM_Y(resumenDiario.getFechaEmision()));

                    iFrame.dpFechaEmision.setEnabled(true);
                    iFrame.dpFechaEmision.setDate(new Date());
                    iFrame.dpFechaEmision.requestFocus();

                    iFrame.cbxEstado.setEnabled(true);
                    iFrame.cbxEstado.setSelectedIndex(0);

                    iFrame.cbxMoneda.setEnabled(true);
                    iFrame.cbxMoneda.setSelectedIndex(0);

                    iFrame.cbxDocumentoTipo.setEnabled(true);
                    iFrame.cbxDocumentoTipo.setSelectedIndex(0);

                    iFrame.tfDocumentoSerie.setEnabled(true);

                    iFrame.tfDocumentoCorrelativo.setEnabled(true);

                    iFrame.cbxDocumentoIdentidadTipo.setEnabled(true);
                    iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(0);

                    iFrame.tfDocumentoIdentidadNumero.setEnabled(true);

                    iFrame.tfImporteTotal.setEnabled(true);
                    iFrame.tfImporteTotal.setText("0.00");

                    iFrame.tfGravadas.setEnabled(true);
                    iFrame.tfGravadas.setText("0.00");

                    iFrame.tfExoneradas.setEnabled(true);
                    iFrame.tfExoneradas.setText("0.00");

                    iFrame.tfInafectas.setEnabled(true);
                    iFrame.tfInafectas.setText("0.00");

                    iFrame.tfGratuitas.setEnabled(true);
                    iFrame.tfGratuitas.setText("0.00");

                    iFrame.tfExportacion.setEnabled(true);
                    iFrame.tfExportacion.setText("0.00");

                    iFrame.tfOtrosCargos.setEnabled(true);
                    iFrame.tfOtrosCargos.setText("0.00");

                    iFrame.tfIsc.setEnabled(true);
                    iFrame.tfIsc.setText("0.00");

                    iFrame.tfIgv.setEnabled(true);
                    iFrame.tfIgv.setText("0.00");

                    iFrame.tfIsc.setEnabled(true);
                    iFrame.tfIsc.setText("0.00");

                    iFrame.tfOtrosTributos.setEnabled(true);
                    iFrame.tfOtrosTributos.setText("0.00");

                    iFrame.tfBolsasPlasticas.setEnabled(true);
                    iFrame.tfBolsasPlasticas.setText("0.00");

                    iFrame.cbxPercepcionRegimen.setEnabled(true);
                    iFrame.cbxPercepcionRegimen.setSelectedIndex(0);

                    iFrame.tfPercepcionTasa.setEnabled(true);

                    iFrame.tfPercepcionMonto.setEnabled(true);

                    iFrame.tfPercepcionMontoTotal.setEnabled(true);

                    iFrame.btnNuevo.setEnabled(false);

                    iFrame.btnGuardar.setEnabled(false);

                    iFrame.btnLimpiar.setEnabled(true);
                  } catch (InterruptedException | ExecutionException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenDiarioController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }
              };

          worker.execute();
        });

    iFrame.btnGuardar.addActionListener((ActionEvent arg0) -> {});

    iFrame.btnLimpiar.addActionListener(
        (ActionEvent arg0) -> {
          start();
        });

    iFrame.cbxDocumentoTipo.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            if (iFrame.cbxDocumentoTipo.getSelectedIndex() == 0) {
              iFrame.tbbdDetalle.setEnabledAt(2, false);

              iFrame.cbxReferenciaTipo.setEnabled(false);
              iFrame.cbxReferenciaTipo.setSelectedIndex(-1);

              iFrame.tfReferenciaSerie.setEnabled(false);

              iFrame.tfReferenciaCorrelativo.setEnabled(false);
            } else {
              iFrame.tbbdDetalle.setEnabledAt(2, true);

              iFrame.cbxReferenciaTipo.setEnabled(true);
              iFrame.cbxReferenciaTipo.setSelectedIndex(0);

              iFrame.tfReferenciaSerie.setEnabled(true);

              iFrame.tfReferenciaCorrelativo.setEnabled(true);
            }
          }
        });

    iFrame.btnAgregar.addActionListener(
        (arg0) -> {
          ResumenDiarioDetalle detalle = new ResumenDiarioDetalle();

          iFrame.btnGuardar.setEnabled(true);
        });

    iFrame.tfGravadas.getDocument().addDocumentListener(dl);

    iFrame.tfExoneradas.getDocument().addDocumentListener(dl);

    iFrame.tfInafectas.getDocument().addDocumentListener(dl);

    iFrame.tfExportacion.getDocument().addDocumentListener(dl);

    iFrame.tfOtrosCargos.getDocument().addDocumentListener(dl);

    iFrame.tfIsc.getDocument().addDocumentListener(dl);

    iFrame.tfOtrosTributos.getDocument().addDocumentListener(dl);

    iFrame.tfBolsasPlasticas.getDocument().addDocumentListener(dl);
  }

  private void initComponents() {
    summaryDao = new ISummaryDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());
    eventList = new BasicEventList<>();

    TableFormat<ResumenDiarioDetalle> tableFormat =
        new TableFormat<ResumenDiarioDetalle>() {
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
          public Object getColumnValue(ResumenDiarioDetalle detalle, int column) {
            switch (column) {
              case 0:
                return detalle.getDocumento().getSerie();
              case 1:
                return detalle.getDocumento().getCorrelativo();
              case 2:
                return detalle.getDocumento().getTipoDocumento().getDescripcion();
              case 3:
                if (detalle.getRemitente() != null) return detalle.getRemitente().getRuc();
                else return "";
              case 4:
                if (detalle.getDocumentoReferencia() != null)
                  return detalle.getDocumentoReferencia().getSerie();
                else return "";
              case 5:
                if (detalle.getDocumentoReferencia() != null)
                  return detalle.getDocumentoReferencia().getCorrelativo();
                else return "";
              case 6:
                if (detalle.getDocumentoReferencia() != null)
                  return detalle.getDocumentoReferencia().getTipoDocumento().getDescripcion();
                else return "";
              case 7:
                if (detalle.getPercepcion() != null)
                  return detalle.getPercepcion().getDescripcion();
                else return "";
              case 8:
                if (detalle.getPercepcion() != null) return detalle.getPercepcion().getTasa();
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
    iFrame.table.setModel(tableModel);
    iFrame.table.setSelectionModel(selectionModel);

    iFrame.show();

    iFrame.btnNuevo.requestFocus();
  }

  private void start() {
    iFrame.tabbed.setSelectedIndex(0);

    iFrame.tfTipo.setEnabled(false);
    iFrame.tfTipo.setText("");

    iFrame.tfSerie.setEnabled(false);
    iFrame.tfSerie.setText("");

    iFrame.tfCorrelativo.setEnabled(false);
    iFrame.tfCorrelativo.setText("");

    iFrame.tfFechaGeneracion.setEnabled(false);
    iFrame.tfFechaGeneracion.setText("");

    iFrame.dpFechaEmision.setEnabled(false);
    iFrame.dpFechaEmision.setDate(null);

    iFrame.tbbdDetalle.setSelectedIndex(0);
    iFrame.tbbdDetalle.setEnabledAt(2, false);

    iFrame.cbxEstado.setEnabled(false);
    iFrame.cbxEstado.setSelectedIndex(-1);

    iFrame.cbxMoneda.setEnabled(false);
    iFrame.cbxMoneda.setSelectedIndex(-1);

    iFrame.cbxDocumentoTipo.setEnabled(false);
    iFrame.cbxDocumentoTipo.setSelectedIndex(-1);

    iFrame.tfDocumentoSerie.setEnabled(false);

    iFrame.tfDocumentoCorrelativo.setEnabled(false);

    iFrame.cbxDocumentoIdentidadTipo.setEnabled(false);
    iFrame.cbxDocumentoIdentidadTipo.setSelectedIndex(-1);

    iFrame.tfDocumentoIdentidadNumero.setEnabled(false);

    iFrame.tfImporteTotal.setEnabled(false);
    iFrame.tfImporteTotal.setText("");

    iFrame.tfGravadas.setEnabled(false);
    iFrame.tfGravadas.setText("");

    iFrame.tfExoneradas.setEnabled(false);
    iFrame.tfExoneradas.setText("");

    iFrame.tfInafectas.setEnabled(false);
    iFrame.tfInafectas.setText("");

    iFrame.tfGratuitas.setEnabled(false);
    iFrame.tfGratuitas.setText("");

    iFrame.tfExportacion.setEnabled(false);
    iFrame.tfExportacion.setText("");

    iFrame.tfOtrosCargos.setEnabled(false);
    iFrame.tfOtrosCargos.setText("");

    iFrame.tfIsc.setEnabled(false);
    iFrame.tfIsc.setText("");

    iFrame.tfIgv.setEnabled(false);
    iFrame.tfIgv.setText("");

    iFrame.tfIsc.setEnabled(false);
    iFrame.tfIsc.setText("");

    iFrame.tfOtrosTributos.setEnabled(false);
    iFrame.tfOtrosTributos.setText("");

    iFrame.tfBolsasPlasticas.setEnabled(false);
    iFrame.tfBolsasPlasticas.setText("");

    iFrame.cbxPercepcionRegimen.setEnabled(false);
    iFrame.cbxPercepcionRegimen.setSelectedIndex(-1);

    iFrame.tfPercepcionTasa.setEnabled(false);
    iFrame.tfPercepcionTasa.setText("");

    iFrame.tfPercepcionMonto.setEnabled(false);
    iFrame.tfPercepcionMonto.setText("");

    iFrame.tfPercepcionMontoTotal.setEnabled(false);
    iFrame.tfPercepcionMontoTotal.setText("");

    iFrame.btnNuevo.setEnabled(true);
    iFrame.btnNuevo.requestFocus();

    iFrame.btnGuardar.setEnabled(false);

    iFrame.btnLimpiar.setEnabled(false);
  }

  private final DocumentListener dl =
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

        private void sum() {
          if (!iFrame.tfGravadas.getText().isEmpty()
              && !iFrame.tfExoneradas.getText().isEmpty()
              && !iFrame.tfInafectas.getText().isEmpty()
              && !iFrame.tfExportacion.getText().isEmpty()
              && !iFrame.tfOtrosCargos.getText().isEmpty()
              && !iFrame.tfIsc.getText().isEmpty()
              && !iFrame.tfOtrosTributos.getText().isEmpty()
              && !iFrame.tfBolsasPlasticas.getText().isEmpty()) {
            double gravadas = Double.parseDouble(iFrame.tfGravadas.getText());
            double exoneradas = Double.parseDouble(iFrame.tfExoneradas.getText());
            double inafectas = Double.parseDouble(iFrame.tfInafectas.getText());
            double exportacion = Double.parseDouble(iFrame.tfExportacion.getText());
            double otrosCargos = Double.parseDouble(iFrame.tfOtrosCargos.getText());
            double igv = gravadas * 0.18;
            double isc = Double.parseDouble(iFrame.tfIsc.getText());
            double otrosTributos = Double.parseDouble(iFrame.tfOtrosTributos.getText());
            double bolsas = Double.parseDouble(iFrame.tfBolsasPlasticas.getText());
            double importeTotal =
                gravadas
                    + exoneradas
                    + inafectas
                    + exportacion
                    + otrosCargos
                    + igv
                    + isc
                    + otrosTributos
                    + bolsas;

            iFrame.tfIgv.setValue(igv);
            iFrame.tfImporteTotal.setValue(importeTotal);
          }
        }
      };
}
