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
import static ca.odell.glazedlists.swing.GlazedListsSwing.swingThreadProxyList;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.anthonyponte.jbillservice.custom.MyDateFormat;
import com.anthonyponte.jbillservice.dao.ComunicacionBajaDao;
import com.anthonyponte.jbillservice.view.SummaryIFrame;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import pe.gob.sunat.BillService;
import pe.gob.sunat.StatusResponse;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.idao.IBillService;
import com.anthonyponte.jbillservice.idao.IComunicacionBajaDao;
import com.anthonyponte.jbillservice.idao.ISummaryDao;
import com.anthonyponte.jbillservice.model.Summary;
import com.anthonyponte.jbillservice.view.LoadingDialog;
import com.google.common.util.concurrent.Uninterruptibles;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/** @author anthony */
public class SummaryController {

  private final SummaryIFrame iFrame;
  private final LoadingDialog dialog;
  private SummaryDao summaryDao;
  private ComunicacionBajaDao comunicacionBajaDao;
  private BillService service;
  private EventList<Summary> eventList;
  private EventList<Summary> proxyList;
  private SortedList<Summary> sortedList;
  private AdvancedListSelectionModel<Summary> selectionModel;
  private AdvancedTableModel<Summary> tableModel;

  public SummaryController(SummaryIFrame iFrame, LoadingDialog dialog) {
    this.iFrame = iFrame;
    this.dialog = dialog;
    initComponents();
  }

  public void init() {
    iFrame.btnEnviar.addActionListener(
        (var e) -> {
          int seleccionados = selectionModel.getSelected().size();
          int input =
              JOptionPane.showOptionDialog(
                  iFrame,
                  "Seguro que desea enviar estos " + seleccionados + " archivos?",
                  "Enviar",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE,
                  null,
                  new Object[] {"Enviar", "Cancelar"},
                  "Enviar");

          if (input == JOptionPane.YES_OPTION) {
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(iFrame);

            SwingWorker worker =
                new SwingWorker<Integer, Void>() {
                  @Override
                  protected Integer doInBackground() throws Exception {
                    int enviados = 0;
                    if (!selectionModel.isSelectionEmpty()) {
                      EventList<Summary> selected = selectionModel.getSelected();
                      List<Summary> list = new ArrayList<>();
                      for (Summary next : selected) {
                        DataSource source =
                            new ByteArrayDataSource(next.getZip(), "application/zip");
                        DataHandler handler = new DataHandler(source);

                        String ticket =
                            service.sendSummary(next.getNombreZip(), handler, next.getTipo());

                        if (ticket != null) {
                          Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
                          StatusResponse response = service.getStatus(ticket);

                          if (response.getStatusCode().equals("0")) {
                            next.setTicket(ticket);
                            next.setStatusCode(response.getStatusCode());
                            next.setNombreContent("R-" + next.getNombreZip());
                            next.setContent(response.getContent());

                            summaryDao.update(next.getId(), next);

                            list.add(next);
                            enviados++;
                          }
                        }
                      }
                      eventList.removeAll(list);
                    }
                    return enviados;
                  }

                  @Override
                  protected void done() {
                    try {
                      dialog.dispose();

                      int eliminados = get();
                      JOptionPane.showMessageDialog(
                          iFrame,
                          eliminados + " archivos enviados",
                          "Enviados",
                          JOptionPane.INFORMATION_MESSAGE);
                    } catch (InterruptedException | ExecutionException ex) {
                      Logger.getLogger(SummaryController.class.getName())
                          .log(Level.SEVERE, null, ex);
                    }
                  }
                };
            worker.execute();
          }
        });

    iFrame
        .table
        .getSelectionModel()
        .addListSelectionListener(
            (ListSelectionEvent arg0) -> {
              if (selectionModel.isSelectionEmpty()) {
                iFrame.btnEnviar.setEnabled(false);
              } else {
                iFrame.btnEnviar.setEnabled(true);
              }
            });

    iFrame
        .table
        .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK), "DELETE");

    iFrame
        .table
        .getActionMap()
        .put(
            "DELETE",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent ae) {
                if (!selectionModel.isSelectionEmpty()) {
                  int seleccionados = selectionModel.getSelected().size();

                  int input =
                      JOptionPane.showOptionDialog(
                          iFrame,
                          "Seguro que desea eliminar estos " + seleccionados + " archivos?",
                          "Eliminar",
                          JOptionPane.YES_NO_OPTION,
                          JOptionPane.QUESTION_MESSAGE,
                          null,
                          new Object[] {"Eliminar", "Cancelar"},
                          "Eliminar");

                  if (input == JOptionPane.YES_OPTION) {
                    dialog.setVisible(true);
                    dialog.setLocationRelativeTo(iFrame);

                    SwingWorker worker =
                        new SwingWorker<Integer, Void>() {
                          @Override
                          protected Integer doInBackground() throws Exception {
                            int eliminados = 0;
                            EventList<Summary> selected = selectionModel.getSelected();
                            List<Summary> list = new ArrayList<>();
                            for (int i = 0; i < selected.size(); i++) {
                              Summary get = selected.get(i);
                              comunicacionBajaDao.delete(get.getId());
                              summaryDao.delete(get.getId());
                              list.add(get);
                              eliminados++;
                            }
                            eventList.removeAll(list);
                            return eliminados;
                          }

                          @Override
                          protected void done() {
                            try {
                              dialog.dispose();

                              int eliminados = get();
                              JOptionPane.showMessageDialog(
                                  iFrame,
                                  eliminados + " archivos eliminados",
                                  "Eliminados",
                                  JOptionPane.INFORMATION_MESSAGE);
                            } catch (InterruptedException | ExecutionException ex) {
                              Logger.getLogger(SummaryController.class.getName())
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
    summaryDao = new ISummaryDao();
    comunicacionBajaDao = new IComunicacionBajaDao();
    service = new IBillService();
    eventList = new BasicEventList<>();
    proxyList = swingThreadProxyList(eventList);

    Comparator comparator =
        (Comparator<Summary>)
            (Summary o1, Summary o2) -> o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(proxyList, comparator.reversed());

    TextFilterator<Summary> textFilterator =
        (List<String> list, Summary summary) -> {
          list.add(summary.getTipo());
          list.add(String.valueOf(summary.getCorrelativo()));
        };

    MatcherEditor<Summary> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, textFilterator);

    FilterList<Summary> filterList = new FilterList<>(sortedList, matcherEditor);

    TableFormat<Summary> tableFormat =
        new TableFormat<Summary>() {
          @Override
          public int getColumnCount() {
            return 6;
          }

          @Override
          public String getColumnName(int column) {
            switch (column) {
              case 0:
                return "Fecha Emision";
              case 1:
                return "RUC";
              case 2:
                return "Tipo";
              case 3:
                return "Serie";
              case 4:
                return "Correlativo";
              case 5:
                return "Zip";
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }

          @Override
          public Object getColumnValue(Summary summary, int column) {
            switch (column) {
              case 0:
                return MyDateFormat.d_MMMM_Y(summary.getFechaEmision());
              case 1:
                return summary.getEmisor().getRuc();
              case 2:
                return getDescripcionDocumento(summary.getTipo());
              case 3:
                return summary.getSerie();
              case 4:
                return String.valueOf(summary.getCorrelativo());
              case 5:
                return summary.getNombreZip();
            }
            throw new IllegalStateException("Unexpected column: " + column);
          }
        };

    tableModel = eventTableModelWithThreadProxyList(filterList, tableFormat);
    selectionModel = new DefaultEventSelectionModel<>(filterList);

    iFrame.table.setModel(tableModel);
    iFrame.table.setSelectionModel(selectionModel);

    TableComparatorChooser.install(iFrame.table, sortedList, TableComparatorChooser.SINGLE_COLUMN);

    iFrame.show();

    iFrame.btnEnviar.requestFocus();
    start();
  }

  private void start() {
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(iFrame);

    SwingWorker worker =
        new SwingWorker<List<Summary>, Void>() {
          @Override
          protected List<Summary> doInBackground() throws Exception {
            List<Summary> list = summaryDao.read();
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<Summary> get = get();
              proxyList.clear();
              proxyList.addAll(get);
            } catch (InterruptedException | ExecutionException ex) {
              Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        };
    worker.execute();
  }

  private String getDescripcionDocumento(String codigo) {
    String descripcion = "";
    if (codigo.equalsIgnoreCase("RA")) {
      descripcion = "Comunicacion de baja";
    } else if (codigo.equalsIgnoreCase("RR")) {
      descripcion = "Resumen de reversiones";
    } else if (codigo.equalsIgnoreCase("RC")) {
      descripcion = "Resumen diario";
    }
    return descripcion;
  }
}
