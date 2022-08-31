/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbill.controller;

import com.anthonyponte.jbill.view.SummaryIFrame;
import com.anthonyponte.jbill.view.MainFrame;
import com.anthonyponte.jbill.view.UsuarioIFrame;
import com.anthonyponte.jbill.view.ComunicacionIFrame;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.ResumenIFrame;
import com.anthonyponte.jbill.view.TableIFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;

/**
 * @author anthony
 */
public class MainController {

  private final MainFrame frame;
  private UsuarioIFrame usuarioIFrame;
  private ComunicacionIFrame comunicacionBajaIFrame;
  private ResumenIFrame resumenDiarioIFrame;
  private SummaryIFrame summaryIFrame;
  private TableIFrame comunicacionTableIFrame;
  private TableIFrame resumenTableIFrame;
  private LoadingDialog dialog;
  private Server server = null;
  private final String ALIAS = "jbs";
  private final String DATABASE = "jbillservice";

  public MainController(MainFrame frame) {
    this.frame = frame;
    initComponents();
  }

  public void init() {
    frame.menuEntrar.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(usuarioIFrame)) {
            usuarioIFrame = new UsuarioIFrame();
            frame.dpane.add(usuarioIFrame);
            usuarioIFrame.setLocation(centerIFrame(usuarioIFrame));
            new UsuarioController(frame, usuarioIFrame, server.isNotRunning()).init();
          } else {
            iframeClosed(usuarioIFrame);
          }
        });

    frame.miComunicacionBaja.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(comunicacionBajaIFrame)) {
            comunicacionBajaIFrame = new ComunicacionIFrame();
            frame.dpane.add(comunicacionBajaIFrame);
            comunicacionBajaIFrame.setLocation(centerIFrame(comunicacionBajaIFrame));
            new ComunicacionController(comunicacionBajaIFrame, dialog).init();
          } else {
            iframeClosed(comunicacionBajaIFrame);
          }
        });

    frame.miResumenDiario.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(resumenDiarioIFrame)) {
            resumenDiarioIFrame = new ResumenIFrame();
            frame.dpane.add(resumenDiarioIFrame);
            resumenDiarioIFrame.setLocation(centerIFrame(resumenDiarioIFrame));
            new ResumenController(resumenDiarioIFrame, dialog).init();
          } else {
            iframeClosed(resumenDiarioIFrame);
          }
        });

    frame.miComunicaciones.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(comunicacionTableIFrame)) {
            comunicacionTableIFrame = new TableIFrame();
            frame.dpane.add(comunicacionTableIFrame);
            comunicacionTableIFrame.setLocation(centerIFrame(comunicacionTableIFrame));
            new ComunicacionTableController(comunicacionTableIFrame, dialog).init();
          } else {
            iframeClosed(comunicacionTableIFrame);
          }
        });

    frame.miResumenes.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(resumenTableIFrame)) {
            resumenTableIFrame = new TableIFrame();
            frame.dpane.add(resumenTableIFrame);
            resumenTableIFrame.setLocation(centerIFrame(resumenTableIFrame));
            new ResumenTableController(resumenTableIFrame, dialog).init();
          } else {
            iframeClosed(resumenTableIFrame);
          }
        });

    frame.miSummary.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(summaryIFrame)) {
            summaryIFrame = new SummaryIFrame();
            frame.dpane.add(summaryIFrame);
            summaryIFrame.setLocation(centerIFrame(summaryIFrame));
            new SummaryController(summaryIFrame, dialog).init();
          } else {
            iframeClosed(summaryIFrame);
          }
        });

    frame.menuSalir.addActionListener(
        (ActionEvent arg0) -> {
          finnish();
        });

    frame.addWindowListener(
        new WindowListener() {
          @Override
          public void windowOpened(WindowEvent e) {}

          @Override
          public void windowClosing(WindowEvent e) {
            finnish();
          }

          @Override
          public void windowClosed(WindowEvent e) {
            server.stop();
          }

          @Override
          public void windowIconified(WindowEvent e) {}

          @Override
          public void windowDeiconified(WindowEvent e) {}

          @Override
          public void windowActivated(WindowEvent e) {}

          @Override
          public void windowDeactivated(WindowEvent e) {}
        });
  }

  private void initComponents() {
    dialog = new LoadingDialog(frame, false);

    frame.setVisible(true);

    start();
  }

  private void start() {
    try {
      HsqlProperties properties = new HsqlProperties();
      properties.setProperty("server.database.0", "file:./hsqldb/" + DATABASE);
      properties.setProperty("server.dbname.0", ALIAS);

      server = new Server();
      server.setProperties(properties);
      server.setTrace(true);
      server.start();
    } catch (IOException | ServerAcl.AclFormatException ex) {
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MainController.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  private void finnish() {
    int input =
        JOptionPane.showConfirmDialog(
            frame,
            "Seguro que desea salir?",
            "Salir",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (input == JOptionPane.YES_OPTION) frame.dispose();
  }

  private Point centerIFrame(JInternalFrame jif) {
    Point point = new Point(0, 0);
    Dimension dimenDPane = frame.dpane.getSize();
    Dimension dimenIFrame = jif.getSize();
    point.x = (dimenDPane.width - dimenIFrame.width) / 2;
    point.y = (dimenDPane.height - dimenIFrame.height) / 2;
    return point;
  }

  private boolean isIframeClosed(Object jif) {
    JInternalFrame[] iframes = frame.dpane.getAllFrames();
    boolean isClosed = true;
    int i = 0;
    while (i < iframes.length && isClosed) {
      if (iframes[i] == jif) {
        isClosed = false;
      }
      i++;
    }
    return isClosed;
  }

  private void iframeClosed(JInternalFrame iframe) {
    int input =
        JOptionPane.showConfirmDialog(
            iframe,
            "La ventana '" + iframe.getTitle() + "' ya esta abierta",
            iframe.getTitle(),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (input == JOptionPane.OK_OPTION) {
      if (iframe.isIcon()) {
        try {
          iframe.setIcon(false);
        } catch (PropertyVetoException ex) {
          JOptionPane.showMessageDialog(
              null, ex.getMessage(), MainController.class.getName(), JOptionPane.ERROR_MESSAGE);
        }
      } else {
        iframe.setLocation(centerIFrame(iframe));
      }
    }
  }
}
