/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import com.anthonyponte.jbillservice.idao.IComunicacionBajaDao;
import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import com.anthonyponte.jbillservice.view.ComunicacionesBajaIFrame;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;
import com.anthonyponte.jbillservice.dao.ComunicacionBajaDao;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/** @author AnthonyPonte */
public class ComunicacionesBajaController {

  private final ComunicacionesBajaIFrame iFrame;
  private final LoadingDialog dialog;
  private ComunicacionBajaDao dao;
  private EventList<ComunicacionBaja> eventList;
  private SortedList<ComunicacionBaja> sortedList;
  private AdvancedListSelectionModel<ComunicacionBaja> selectionModel;
  private AdvancedTableModel<ComunicacionBaja> tableModel;

  public ComunicacionesBajaController(ComunicacionesBajaIFrame iFrame, LoadingDialog dialog) {
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
              if (column == 7 || column == 8) {
                ComunicacionBaja selected = selectionModel.getSelected().get(0);

                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));

                if (column == 7) {
                  chooser.setSelectedFile(new File(selected.getNombreZip()));
                } else if (column == 8) {
                  chooser.setSelectedFile(new File(selected.getNombreContent()));
                }

                int result = chooser.showSaveDialog(iFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                  File file = chooser.getSelectedFile().getAbsoluteFile();
                  try (FileOutputStream fos =
                      new FileOutputStream(file.getParent() + "//" + file.getName())) {

                    if (column == 7) {
                      fos.write(selected.getZip());
                    } else if (column == 8) {
                      fos.write(selected.getContent());
                    }

                    fos.flush();
                  } catch (FileNotFoundException ex) {
                    Logger.getLogger(ComunicacionesBajaController.class.getName())
                        .log(Level.SEVERE, null, ex);
                  } catch (IOException ex) {
                    Logger.getLogger(ComunicacionesBajaController.class.getName())
                        .log(Level.SEVERE, null, ex);
                  }
                }

              } else {
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(iFrame);

                SwingWorker worker =
                    new SwingWorker<List<ComunicacionBajaDetalle>, Void>() {
                      @Override
                      protected List<ComunicacionBajaDetalle> doInBackground() throws Exception {
                        ComunicacionBaja selected = selectionModel.getSelected().get(0);
                        List<ComunicacionBajaDetalle> list = dao.read(selected);
                        return list;
                      }

                      @Override
                      protected void done() {
                        try {
                          dialog.dispose();

                          DefaultTableModel model =
                              (DefaultTableModel) iFrame.tblDetalle.getModel();

                          for (int i = 0; i < model.getRowCount(); i++) {
                            model.removeRow(i);
                          }

                          List<ComunicacionBajaDetalle> list = get();
                          Object[] row = null;
                          for (ComunicacionBajaDetalle next : list) {
                            String tipo = next.getDocumento().getTipo();
                            String serie = next.getDocumento().getSerie();
                            int correlativo = next.getDocumento().getCorrelativo();
                            String motivo = next.getMotivo();

                            row = new Object[] {tipo, serie, correlativo, motivo};
                          }
                          model.addRow(row);
                        } catch (InterruptedException | ExecutionException ex) {
                          Logger.getLogger(ComunicacionesBajaController.class.getName())
                              .log(Level.SEVERE, null, ex);
                        }
                      }
                    };

                worker.execute();
              }
            }
          }
        });
  }

  private void initComponents() {
    dao = new IComunicacionBajaDao();
    eventList = new BasicEventList<>();

    Comparator comparator =
        (Comparator<ComunicacionBaja>)
            (ComunicacionBaja o1, ComunicacionBaja o2) ->
                o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(eventList, comparator.reversed());

    TextFilterator<ComunicacionBaja> filterator =
        (List<String> list, ComunicacionBaja comunicacionBaja) -> {
          list.add(comunicacionBaja.getTipo());
          list.add(String.valueOf(comunicacionBaja.getCorrelativo()));
        };

    MatcherEditor<ComunicacionBaja> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, filterator);

    FilterList<ComunicacionBaja> filterList = new FilterList<>(sortedList, matcherEditor);

    TableFormat<ComunicacionBaja> tableFormat =
        new TableFormat<ComunicacionBaja>() {
          @Override
          public int getColumnCount() {
            return 11;
          }

          @Override
          public String getColumnName(int column) {
            switch (column) {
              case 0:
                return "Tipo";
              case 1:
                return "Serie";
              case 2:
                return "Correlativo";
              case 3:
                return "Fecha Emision";
              case 4:
                return "Fecha Referencia";
              case 5:
                return "RUC";
              case 6:
                return "Razon Social";
              case 7:
                return "Zip";
              case 8:
                return "Ticket";
              case 9:
                return "Status Code";
              case 10:
                return "CDR";
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }

          @Override
          public Object getColumnValue(ComunicacionBaja comunicacionBaja, int column) {
            switch (column) {
              case 0:
                return comunicacionBaja.getTipo();
              case 1:
                return comunicacionBaja.getSerie();
              case 2:
                return String.valueOf(comunicacionBaja.getCorrelativo());
              case 3:
                return MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaEmision());
              case 4:
                return MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaReferencia());
              case 5:
                return comunicacionBaja.getEmisor().getRuc();
              case 6:
                return comunicacionBaja.getEmisor().getRazonSocial();
              case 7:
                return comunicacionBaja.getNombreZip();
              case 8:
                return comunicacionBaja.getTicket();
              case 9:
                return comunicacionBaja.getStatusCode();
              case 10:
                return comunicacionBaja.getNombreContent();
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

    iFrame.show();

    iFrame.dpMesAno.requestFocus();

    Date date = iFrame.dpMesAno.getDate();
    start(date);
  }

  private void start(Date date) {
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(iFrame);

    SwingWorker worker =
        new SwingWorker<List<ComunicacionBaja>, Void>() {
          @Override
          protected List<ComunicacionBaja> doInBackground() throws Exception {
            DateTime dateTime = new DateTime(date);
            List<ComunicacionBaja> list = dao.read(dateTime);
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<ComunicacionBaja> get = get();
              eventList.clear();
              eventList.addAll(get);

              resize(iFrame.tblEncabezado);

              if (!get.isEmpty()) iFrame.tfFiltrar.requestFocus();
              else iFrame.dpMesAno.requestFocus();
            } catch (InterruptedException | ExecutionException ex) {
              Logger.getLogger(ComunicacionesBajaController.class.getName())
                  .log(Level.SEVERE, null, ex);
            }
          }
        };
    worker.execute();
  }

  private void resize(JTable table) {
    TableColumnModel columnModel = table.getColumnModel();
    for (int column = 0; column < table.getColumnCount(); column++) {
      int width = 150;
      for (int row = 0; row < table.getRowCount(); row++) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component comp = table.prepareRenderer(renderer, row, column);
        width = Math.max(comp.getPreferredSize().width + 1, width);
      }
      if (width > 300) width = 300;
      columnModel.getColumn(column).setPreferredWidth(width);
    }
  }
}
