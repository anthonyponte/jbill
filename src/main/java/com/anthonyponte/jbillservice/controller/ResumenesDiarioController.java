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

package com.anthonyponte.jbillservice.controller;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.custom.MyTableResize;
import com.anthonyponte.jbillservice.dao.ResumenDiarioDao;
import com.anthonyponte.jbillservice.idao.IResumenDiarioDao;
import com.anthonyponte.jbillservice.model.ResumenDiario;
import com.anthonyponte.jbillservice.model.ResumenDiarioDetalle;
import com.anthonyponte.jbillservice.tableformat.ResumenDiarioDetalleTableFormat;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import com.anthonyponte.jbillservice.view.ResumenesDiarioIFrame;
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

/** @author AnthonyPonte */
public class ResumenesDiarioController {
  private final ResumenesDiarioIFrame iFrame;
  private final LoadingDialog dialog;
  private ResumenDiarioDao dao;
  private EventList<ResumenDiario> eventList;
  private EventList<ResumenDiarioDetalle> elDetalle;
  private SortedList<ResumenDiario> sortedList;
  private AdvancedListSelectionModel<ResumenDiario> selectionModel;
  private AdvancedTableModel<ResumenDiario> tableModel;

  public ResumenesDiarioController(ResumenesDiarioIFrame iFrame, LoadingDialog dialog) {
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
                ResumenDiario selected = selectionModel.getSelected().get(0);

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
                        ResumenesDiarioController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        ResumenesDiarioController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }
              } else {
                try {
                  ResumenDiario selected = selectionModel.getSelected().get(0);
                  List<ResumenDiarioDetalle> get = dao.read(selected);

                  elDetalle.clear();
                  elDetalle.addAll(get);

                  MyTableResize.resize(iFrame.tblEncabezado);
                } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(
                      null,
                      ex.getMessage(),
                      ResumenesDiarioController.class.getName(),
                      JOptionPane.ERROR_MESSAGE);
                }
              }
            }
          }
        });
  }

  private void initComponents() {
    dao = new IResumenDiarioDao();
    eventList = new BasicEventList<>();
    elDetalle = new BasicEventList<>();

    Comparator comparator =
        (Comparator<ResumenDiario>)
            (ResumenDiario o1, ResumenDiario o2) ->
                o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(eventList, comparator.reversed());

    TextFilterator<ResumenDiario> filterator =
        (List<String> list, ResumenDiario resumenDiario) -> {
          list.add(resumenDiario.getTipoDocumento().getCodigo());
          list.add(resumenDiario.getTipoDocumento().getDescripcion());
          list.add(String.valueOf(resumenDiario.getCorrelativo()));
        };

    MatcherEditor<ResumenDiario> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, filterator);

    FilterList<ResumenDiario> filterList = new FilterList<>(sortedList, matcherEditor);

    TableFormat<ResumenDiario> tableFormat =
        new TableFormat<ResumenDiario>() {
          @Override
          public int getColumnCount() {
            return 12;
          }

          @Override
          public String getColumnName(int column) {
            switch (column) {
              case 0:
                return "Tipo Codigo";
              case 1:
                return "Tipo Descripcion";
              case 2:
                return "Serie";
              case 3:
                return "Correlativo";
              case 4:
                return "Fecha Emision";
              case 5:
                return "Fecha Referencia";
              case 6:
                return "RUC";
              case 7:
                return "Razon Social";
              case 8:
                return "Zip";
              case 9:
                return "Ticket";
              case 10:
                return "Status Code";
              case 11:
                return "CDR";
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }

          @Override
          public Object getColumnValue(ResumenDiario resumenDiario, int column) {
            switch (column) {
              case 0:
                return resumenDiario.getTipoDocumento().getCodigo();
              case 1:
                return resumenDiario.getTipoDocumento().getDescripcion();
              case 2:
                return resumenDiario.getSerie();
              case 3:
                return String.valueOf(resumenDiario.getCorrelativo());
              case 4:
                return MyDateFormat.d_MMMM_Y(resumenDiario.getFechaEmision());
              case 5:
                return MyDateFormat.d_MMMM_Y(resumenDiario.getFechaReferencia());
              case 6:
                return resumenDiario.getEmisor().getNumeroDocumentoIdentidad();
              case 7:
                return resumenDiario.getEmisor().getNombre();
              case 8:
                return resumenDiario.getNombreZip();
              case 9:
                return resumenDiario.getTicket();
              case 10:
                return resumenDiario.getStatusCode();
              case 11:
                return resumenDiario.getNombreContent();
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }
        };

    tableModel = eventTableModelWithThreadProxyList(filterList, tableFormat);
    selectionModel = new DefaultEventSelectionModel<>(filterList);

    iFrame.tblEncabezado.setModel(tableModel);
    iFrame.tblEncabezado.setSelectionModel(selectionModel);

    TableComparatorChooser.install(
        iFrame.tblEncabezado, sortedList, TableComparatorChooser.SINGLE_COLUMN);

    AdvancedTableModel<ResumenDiarioDetalle> ttmDetalle =
        eventTableModelWithThreadProxyList(elDetalle, new ResumenDiarioDetalleTableFormat());
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
        new SwingWorker<List<ResumenDiario>, Void>() {
          @Override
          protected List<ResumenDiario> doInBackground() throws Exception {
            DateTime dateTime = new DateTime(date);
            List<ResumenDiario> list = dao.read(dateTime);
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<ResumenDiario> get = get();
              eventList.clear();
              eventList.addAll(get);

              MyTableResize.resize(iFrame.tblEncabezado);

              if (!get.isEmpty()) iFrame.tfFiltrar.requestFocus();
              else iFrame.dpMesAno.requestFocus();
            } catch (InterruptedException | ExecutionException ex) {
              JOptionPane.showMessageDialog(
                  null,
                  ex.getMessage(),
                  ResumenesDiarioController.class.getName(),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };
    worker.execute();
  }
}
