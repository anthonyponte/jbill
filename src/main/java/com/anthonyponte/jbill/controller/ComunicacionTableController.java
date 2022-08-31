/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.controller;

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
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.custom.MyTableResize;
import com.anthonyponte.jbill.idao.IComunicacionDao;
import com.anthonyponte.jbill.model.Comunicacion;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
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
import javax.swing.SwingWorker;
import org.joda.time.DateTime;
import com.anthonyponte.jbill.tableformat.ComunicacionDetalleTableFormat;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.TableIFrame;
import javax.swing.JOptionPane;
import com.anthonyponte.jbill.dao.ComunicacionDao;

/** @author AnthonyPonte */
public class ComunicacionTableController {

  private final TableIFrame iFrame;
  private final LoadingDialog dialog;
  private ComunicacionDao dao;
  private EventList<Comunicacion> elEncabezado;
  private EventList<ComunicacionDetalle> elDetalle;
  private SortedList<Comunicacion> sortedList;
  private AdvancedListSelectionModel<Comunicacion> selectionModel;
  private AdvancedTableModel<Comunicacion> tableModel;

  public ComunicacionTableController(TableIFrame iFrame, LoadingDialog dialog) {
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

    iFrame.tblEncabezado.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              int column = iFrame.tblEncabezado.columnAtPoint(e.getPoint());
              if (column == 8 || column == 11) {
                Comunicacion selected = selectionModel.getSelected().get(0);

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
                    JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        ComunicacionTableController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        ComunicacionTableController.class.getName(),
                        JOptionPane.ERROR_MESSAGE);
                  }
                }

              } else {
                dialog.setVisible(true);
                dialog.setLocationRelativeTo(iFrame);

                SwingWorker worker =
                    new SwingWorker<List<ComunicacionDetalle>, Void>() {
                      @Override
                      protected List<ComunicacionDetalle> doInBackground() throws Exception {
                        Comunicacion selected = selectionModel.getSelected().get(0);
                        List<ComunicacionDetalle> list = dao.read(selected);
                        return list;
                      }

                      @Override
                      protected void done() {
                        try {
                          dialog.dispose();

                          List<ComunicacionDetalle> get = get();

                          elDetalle.clear();
                          elDetalle.addAll(get);

                          MyTableResize.resize(iFrame.tblDetalle);

                        } catch (InterruptedException | ExecutionException ex) {
                          JOptionPane.showMessageDialog(null,
                              ex.getMessage(),
                              ComunicacionTableController.class.getName(),
                              JOptionPane.ERROR_MESSAGE);
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
    dao = new IComunicacionDao();
    elEncabezado = new BasicEventList<>();
    elDetalle = new BasicEventList<>();

    Comparator comparator =
        (Comparator<Comunicacion>)
            (Comunicacion o1, Comunicacion o2) ->
                o1.getFechaEmision().compareTo(o2.getFechaEmision());

    sortedList = new SortedList<>(elEncabezado, comparator.reversed());

    TextFilterator<Comunicacion> filterator =
        (List<String> list, Comunicacion comunicacionBaja) -> {
          list.add(comunicacionBaja.getTipoDocumento().getCodigo());
          list.add(comunicacionBaja.getTipoDocumento().getDescripcion());
          list.add(String.valueOf(comunicacionBaja.getCorrelativo()));
        };

    MatcherEditor<Comunicacion> matcherEditor =
        new TextComponentMatcherEditor<>(iFrame.tfFiltrar, filterator);

    FilterList<Comunicacion> filterList = new FilterList<>(sortedList, matcherEditor);

    TableFormat<Comunicacion> tableFormat =
        new TableFormat<Comunicacion>() {
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
          public Object getColumnValue(Comunicacion comunicacionBaja, int column) {
            switch (column) {
              case 0:
                return comunicacionBaja.getTipoDocumento().getCodigo();
              case 1:
                return comunicacionBaja.getTipoDocumento().getDescripcion();
              case 2:
                return comunicacionBaja.getSerie();
              case 3:
                return String.valueOf(comunicacionBaja.getCorrelativo());
              case 4:
                return MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaEmision());
              case 5:
                return MyDateFormat.d_MMMM_Y(comunicacionBaja.getFechaReferencia());
              case 6:
                return comunicacionBaja.getEmisor().getNumero();
              case 7:
                return comunicacionBaja.getEmisor().getNombre();
              case 8:
                return comunicacionBaja.getNombreZip();
              case 9:
                return comunicacionBaja.getTicket();
              case 10:
                return comunicacionBaja.getStatusCode();
              case 11:
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

    AdvancedTableModel<ComunicacionDetalle> tmDetalle =
        eventTableModelWithThreadProxyList(elDetalle, new ComunicacionDetalleTableFormat());
    iFrame.tblDetalle.setModel(tmDetalle);

    iFrame.show();

    iFrame.dpMesAno.requestFocus();

    Date date = iFrame.dpMesAno.getDate();
    start(date);
  }

  private void start(Date date) {
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(iFrame);

    SwingWorker worker =
        new SwingWorker<List<Comunicacion>, Void>() {
          @Override
          protected List<Comunicacion> doInBackground() throws Exception {
            DateTime dateTime = new DateTime(date);
            List<Comunicacion> list = dao.read(dateTime);
            return list;
          }

          @Override
          protected void done() {
            try {
              dialog.dispose();

              List<Comunicacion> get = get();
              elEncabezado.clear();
              elEncabezado.addAll(get);

              MyTableResize.resize(iFrame.tblEncabezado);

              if (!get.isEmpty()) iFrame.tfFiltrar.requestFocus();
              else iFrame.dpMesAno.requestFocus();
            } catch (InterruptedException | ExecutionException ex) {
              JOptionPane.showMessageDialog(null,
                  ex.getMessage(),
                  ComunicacionTableController.class.getName(),
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };
    worker.execute();
  }
}
