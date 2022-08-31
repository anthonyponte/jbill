/*
 * Copyright (C) 2022 AnthonyPonte
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
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.anthonyponte.jbill.custom.MyTableResize;
import com.anthonyponte.jbill.dao.ComunicacionDetalleDao;
import com.anthonyponte.jbill.dao.ResumenDetalleDao;
import com.anthonyponte.jbill.model.ResumenDetalle;
import com.anthonyponte.jbill.tableformat.ResumenDetalleTableFormat;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.SummaryIFrame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.joda.time.DateTime;
import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.idao.IComunicacionDetalleDao;
import com.anthonyponte.jbill.idao.IResumenDetalleDao;
import com.anthonyponte.jbill.idao.ISummaryDao;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import com.anthonyponte.jbill.model.Summary;
import com.anthonyponte.jbill.tableformat.ComunicacionDetalleTableFormat;
import com.anthonyponte.jbill.tableformat.SummaryTableFormat;
import java.sql.SQLException;

/**
 * @author AnthonyPonte
 */
public class SummaryController {
  private final SummaryIFrame iFrame;
  private final LoadingDialog dialog;
  private SummaryDao summaryDao;
  private ComunicacionDetalleDao comunicacionDetalleDao;
  private ResumenDetalleDao resumenDetalleDao;
  private EventList<Summary> summarys;
  private EventList<ComunicacionDetalle> comunicacionDetalles;
  private EventList<ResumenDetalle> resumenDetalles;
  private SortedList<Summary> sortedList;
  private AdvancedListSelectionModel<Summary> selectionModel;
  private AdvancedTableModel<Summary> modelSummary;
  private AdvancedTableModel<ComunicacionDetalle> modelComunicacionDetalle;
  private AdvancedTableModel<ResumenDetalle> modelResumenDetalle;

  public SummaryController(SummaryIFrame iFrame, LoadingDialog dialog) {
    this.iFrame = iFrame;
    this.dialog = dialog;
    initComponents();
  }

  public void init() {
    iFrame.dpMesAno.addActionListener(
        (ActionEvent e) -> {
          Date date = iFrame.dpMesAno.getDate();
          start(date);
        });

    iFrame.tblEncabezado.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              int column = iFrame.tblEncabezado.columnAtPoint(e.getPoint());
              if (column == 8 || column == 11) {
                Summary selected = selectionModel.getSelected().get(0);

                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));

                if (column == 8) {
                  chooser.setSelectedFile(new File(selected.getNombreZip()));
                } else if (column == 11) {
                  chooser.setSelectedFile(new File(selected.getNombreContent()));
                }

                int result = chooser.showSaveDialog(iFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                  File file = chooser.getSelectedFile().getAbsoluteFile();
                  try (FileOutputStream fos =
                      new FileOutputStream(file.getParent() + "//" + file.getName())) {

                    if (column == 8) {
                      fos.write(selected.getZip());
                    } else if (column == 11) {
                      fos.write(selected.getContent());
                    }

                    fos.flush();
                  } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        SummaryController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        SummaryController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }
              } else {
                try {
                  Summary selected = selectionModel.getSelected().get(0);

                  if (selected.getTipo().getCodigo().equals("RA")
                      || selected.getTipo().getCodigo().equals("RR")) {
                    List<ComunicacionDetalle> detalles = comunicacionDetalleDao.read(selected);

                    iFrame.tblDetalle.setModel(modelComunicacionDetalle);

                    comunicacionDetalles.clear();
                    comunicacionDetalles.addAll(detalles);
                  } else if (selected.getTipo().getCodigo().equals("RC")) {
                    List<ResumenDetalle> detalles = resumenDetalleDao.read(selected);

                    iFrame.tblDetalle.setModel(modelResumenDetalle);

                    resumenDetalles.clear();
                    resumenDetalles.addAll(detalles);
                  }
                  MyTableResize.resize(iFrame.tblDetalle);
                } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(
                      null,
                      ex.getMessage(),
                      SummaryController.class.getName(),
                      JOptionPane.ERROR_MESSAGE);
                }
              }
            }
          }
        });
  }

  private void initComponents() {
    summaryDao = new ISummaryDao();
    comunicacionDetalleDao = new IComunicacionDetalleDao();
    resumenDetalleDao = new IResumenDetalleDao();

    summarys = new BasicEventList<>();
    resumenDetalles = new BasicEventList<>();
    comunicacionDetalles = new BasicEventList<>();

    Comparator comparator =
        (Comparator<Summary>)
            (Summary o1, Summary o2) -> o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(summarys, comparator.reversed());

    TextFilterator<Summary> filterator =
        (List<String> list, Summary summary) -> {
          list.add(summary.getTipo().getCodigo());
          list.add(summary.getTipo().getDescripcion());
          list.add(String.valueOf(summary.getCorrelativo()));
        };

    MatcherEditor<Summary> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, filterator);

    FilterList<Summary> filterList = new FilterList<>(sortedList, matcherEditor);

    modelSummary = eventTableModelWithThreadProxyList(filterList, new SummaryTableFormat());
    selectionModel = new DefaultEventSelectionModel<>(filterList);

    iFrame.tblEncabezado.setModel(modelSummary);
    iFrame.tblEncabezado.setSelectionModel(selectionModel);

    TableComparatorChooser.install(
        iFrame.tblEncabezado, sortedList, TableComparatorChooser.SINGLE_COLUMN);

    modelResumenDetalle =
        eventTableModelWithThreadProxyList(resumenDetalles, new ResumenDetalleTableFormat());

    modelComunicacionDetalle =
        eventTableModelWithThreadProxyList(
            comunicacionDetalles, new ComunicacionDetalleTableFormat());

    iFrame.show();

    iFrame.dpMesAno.requestFocus();

    Date date = iFrame.dpMesAno.getDate();
    start(date);
  }

  private void start(Date date) {
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(iFrame);

    SwingWorker worker =
        new SwingWorker<List<Summary>, Void>() {
          @Override
          protected List<Summary> doInBackground() throws Exception {
            DateTime fecha = new DateTime(date);
            List<Summary> list = summaryDao.read(fecha);
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<Summary> get = get();
              summarys.clear();
              summarys.addAll(get);

              MyTableResize.resize(iFrame.tblEncabezado);

              if (!get.isEmpty()) iFrame.tfFiltrar.requestFocus();
              else iFrame.dpMesAno.requestFocus();
            } catch (InterruptedException | ExecutionException ex) {
              JOptionPane.showMessageDialog(
                  null,
                  ex.getMessage(),
                  SummaryController.class.getName(),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };
    worker.execute();
  }
}
