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
import com.anthonyponte.jbill.idao.IResumenDao;
import com.anthonyponte.jbill.model.Resumen;
import com.anthonyponte.jbill.model.ResumenDetalle;
import com.anthonyponte.jbill.tableformat.ResumenDetalleTableFormat;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.TableIFrame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.joda.time.DateTime;
import com.anthonyponte.jbill.dao.ResumenDao;
import com.anthonyponte.jbill.tableformat.SummaryTableFormat;

/**
 * @author AnthonyPonte
 */
public class ResumenTableController {
  private final TableIFrame iFrame;
  private final LoadingDialog dialog;
  private ResumenDao dao;
  private EventList<Resumen> eventList;
  private EventList<ResumenDetalle> elDetalle;
  private SortedList<Resumen> sortedList;
  private AdvancedListSelectionModel<Resumen> selectionModel;
  private AdvancedTableModel<Resumen> tableModel;

  public ResumenTableController(TableIFrame iFrame, LoadingDialog dialog) {
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
                Resumen selected = selectionModel.getSelected().get(0);

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
                        ResumenTableController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenTableController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }
              } else {
                try {
                  Resumen selected = selectionModel.getSelected().get(0);
                  List<ResumenDetalle> get = dao.read(selected);

                  elDetalle.clear();
                  elDetalle.addAll(get);

                  MyTableResize.resize(iFrame.tblDetalle);
                } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(
                      null,
                      ex.getMessage(),
                      ResumenTableController.class.getName(),
                      JOptionPane.ERROR_MESSAGE);
                }
              }
            }
          }
        });
  }

  private void initComponents() {
    dao = new IResumenDao();
    eventList = new BasicEventList<>();
    elDetalle = new BasicEventList<>();

    Comparator comparator =
        (Comparator<Resumen>)
            (Resumen o1, Resumen o2) -> o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(eventList, comparator.reversed());

    TextFilterator<Resumen> filterator =
        (List<String> list, Resumen resumenDiario) -> {
          list.add(resumenDiario.getTipoDocumento().getCodigo());
          list.add(resumenDiario.getTipoDocumento().getDescripcion());
          list.add(String.valueOf(resumenDiario.getCorrelativo()));
        };

    MatcherEditor<Resumen> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, filterator);

    FilterList<Resumen> filterList = new FilterList<>(sortedList, matcherEditor);

    tableModel = eventTableModelWithThreadProxyList(filterList, new SummaryTableFormat());
    selectionModel = new DefaultEventSelectionModel<>(filterList);

    iFrame.tblEncabezado.setModel(tableModel);
    iFrame.tblEncabezado.setSelectionModel(selectionModel);

    TableComparatorChooser.install(
        iFrame.tblEncabezado, sortedList, TableComparatorChooser.SINGLE_COLUMN);

    AdvancedTableModel<ResumenDetalle> ttmDetalle =
        eventTableModelWithThreadProxyList(elDetalle, new ResumenDetalleTableFormat());
    iFrame.tblDetalle.setModel(ttmDetalle);

    iFrame.show();

    iFrame.dpMesAno.requestFocus();

    Date date = iFrame.dpMesAno.getDate();
    start(date);
  }

  private void start(Date date) {
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(iFrame);

    SwingWorker worker =
        new SwingWorker<List<Resumen>, Void>() {
          @Override
          protected List<Resumen> doInBackground() throws Exception {
            DateTime dateTime = new DateTime(date);
            List<Resumen> list = dao.read(dateTime);
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<Resumen> get = get();
              eventList.clear();
              eventList.addAll(get);

              MyTableResize.resize(iFrame.tblEncabezado);

              if (!get.isEmpty()) iFrame.tfFiltrar.requestFocus();
              else iFrame.dpMesAno.requestFocus();
            } catch (InterruptedException | ExecutionException ex) {
              JOptionPane.showMessageDialog(
                  null,
                  ex.getMessage(),
                  ResumenTableController.class.getName(),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };
    worker.execute();
  }
}
