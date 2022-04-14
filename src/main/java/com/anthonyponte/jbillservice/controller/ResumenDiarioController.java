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

import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.idao.ISummaryDao;
import com.anthonyponte.jbillservice.model.Empresa;
import com.anthonyponte.jbillservice.model.ResumenDiario;
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

/** @author anthony */
public class ResumenDiarioController {
  private final ResumenDiarioIFrame iFrame;
  private final LoadingDialog dialog;
  private Preferences preferences;
  private SummaryDao summaryDao;
  private ResumenDiario resumenDiario;

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
  }

  private void initComponents() {
    summaryDao = new ISummaryDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());

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
}
