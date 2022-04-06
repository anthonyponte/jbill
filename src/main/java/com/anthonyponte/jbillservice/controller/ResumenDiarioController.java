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

                    iFrame.cbxDocumentoTipo.setEnabled(true);
                    iFrame.cbxDocumentoTipo.setEditable(true);
                    iFrame.cbxDocumentoTipo.setSelectedIndex(0);

                    iFrame.tfDocumentoSerie.setEnabled(true);
                    iFrame.tfDocumentoSerie.setEditable(true);

                    iFrame.tfDocumentoCorrelativo.setEnabled(true);
                    iFrame.tfDocumentoCorrelativo.setEditable(true);

                    iFrame.cbxDocumentoIdentidadTipo.setEnabled(true);
                    iFrame.cbxDocumentoIdentidadTipo.setEditable(true);

                    iFrame.tfDocumentoIdentidadNumero.setEnabled(true);
                    iFrame.tfDocumentoIdentidadNumero.setEditable(true);

                    iFrame.tfImporteTotal.setEnabled(true);

                    iFrame.tfGravadas.setEnabled(true);
                    iFrame.tfGravadas.setEditable(true);

                    iFrame.tfExoneradas.setEnabled(true);
                    iFrame.tfExoneradas.setEditable(true);

                    iFrame.tfInafectas.setEnabled(true);
                    iFrame.tfInafectas.setEditable(true);

                    iFrame.tfGratuitas.setEnabled(true);
                    iFrame.tfGratuitas.setEditable(true);

                    iFrame.tfExportacion.setEnabled(true);
                    iFrame.tfExportacion.setEditable(true);

                    iFrame.tfIsc.setEnabled(true);
                    iFrame.tfIsc.setEditable(true);

                    iFrame.tfIgv.setEnabled(true);

                    iFrame.tfIsc.setEnabled(true);
                    iFrame.tfIsc.setEditable(true);

                    iFrame.tfOtrosTributos.setEnabled(true);
                    iFrame.tfOtrosTributos.setEditable(true);

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

    iFrame.cbxDocumentoTipo.addItemListener(
        (ItemEvent ie) -> {
          if (ie.getStateChange() == ItemEvent.SELECTED) {
            if (iFrame.cbxDocumentoTipo.getSelectedIndex() == 0) {
              iFrame.tbbdDetalle.setEnabledAt(2, false);

              iFrame.cbxReferenciaTipo.setEnabled(false);
              iFrame.cbxReferenciaTipo.setEditable(false);
              iFrame.cbxReferenciaTipo.setSelectedIndex(-1);

              iFrame.tfReferenciaSerie.setEnabled(false);
              iFrame.tfReferenciaSerie.setEditable(false);

              iFrame.tfReferenciaCorrelativo.setEnabled(false);
              iFrame.tfReferenciaCorrelativo.setEditable(false);
            } else {
              iFrame.tbbdDetalle.setEnabledAt(2, true);

              iFrame.cbxReferenciaTipo.setEnabled(true);
              iFrame.cbxReferenciaTipo.setEditable(true);
              iFrame.cbxReferenciaTipo.setSelectedIndex(0);

              iFrame.tfReferenciaSerie.setEnabled(true);
              iFrame.tfReferenciaSerie.setEditable(true);

              iFrame.tfReferenciaCorrelativo.setEnabled(true);
              iFrame.tfReferenciaCorrelativo.setEditable(true);
            }
          }
        });
  }

  private void initComponents() {
    summaryDao = new ISummaryDao();
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());

    iFrame.show();
    iFrame.tbbdDetalle.setEnabledAt(2, false);

    iFrame.btnNuevo.requestFocus();
  }
}
