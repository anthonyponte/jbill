/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbill.controller;

import com.anthonyponte.jbill.view.BillServiceIFrame;
import com.anthonyponte.jbill.view.MainFrame;
import com.anthonyponte.jbill.view.UsuarioIFrame;
import com.anthonyponte.jbill.view.ComunicacionIFrame;
import com.anthonyponte.jbill.view.LoadingDialog;
import com.anthonyponte.jbill.view.ResumenIFrame;
import com.anthonyponte.jbill.view.SummaryIFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.hsqldb.Server;

/**
 * @author anthony
 */
public class MainController {

  private final MainFrame frame;
  private UsuarioIFrame usuarioIFrame;
  private ComunicacionIFrame comunicacionIFrame;
  private ResumenIFrame resumenIFrame;
  private SummaryIFrame summaryIFrame;
  private BillServiceIFrame billServiceIFrame;
  private LoadingDialog dialog;
  private Server server = null;
  private final String ALIAS = "jb";
  private final String DATABASE = "jbill";

  public MainController(MainFrame frame) {
    this.frame = frame;
    initComponents();
  }

  public void init() {
    frame.miEntrar.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(usuarioIFrame)) {
            usuarioIFrame = new UsuarioIFrame();
            usuarioIFrame.setTitle(frame.miEntrar.getText());
            frame.dpane.add(usuarioIFrame);
            usuarioIFrame.setLocation(centerIFrame(usuarioIFrame));
            new UsuarioController(frame, usuarioIFrame, server.isNotRunning()).init();
          } else {
            iframeClosed(usuarioIFrame);
          }
        });

    frame.miComunicacion.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(comunicacionIFrame)) {
            comunicacionIFrame = new ComunicacionIFrame();
            comunicacionIFrame.setTitle(frame.miComunicacion.getText());
            frame.dpane.add(comunicacionIFrame);
            comunicacionIFrame.setLocation(centerIFrame(comunicacionIFrame));
            new ComunicacionController(comunicacionIFrame, dialog).init();
          } else {
            iframeClosed(comunicacionIFrame);
          }
        });

    frame.miResumen.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(resumenIFrame)) {
            resumenIFrame = new ResumenIFrame();
            resumenIFrame.setTitle(frame.miResumen.getText());
            frame.dpane.add(resumenIFrame);
            resumenIFrame.setLocation(centerIFrame(resumenIFrame));
            new ResumenController(resumenIFrame, dialog).init();
          } else {
            iframeClosed(resumenIFrame);
          }
        });

    frame.miSummary.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(summaryIFrame)) {
            summaryIFrame = new SummaryIFrame();
            summaryIFrame.setTitle(frame.miSummary.getText());
            frame.dpane.add(summaryIFrame);
            summaryIFrame.setLocation(centerIFrame(summaryIFrame));
            new SummaryController(summaryIFrame, dialog).init();
          } else {
            iframeClosed(summaryIFrame);
          }
        });

    frame.miBillService.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(billServiceIFrame)) {
            billServiceIFrame = new BillServiceIFrame();
            billServiceIFrame.setTitle(frame.miBillService.getText());
            frame.dpane.add(billServiceIFrame);
            billServiceIFrame.setLocation(centerIFrame(billServiceIFrame));
            new BillServiceController(billServiceIFrame, dialog).init();
          } else {
            iframeClosed(billServiceIFrame);
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
    server = new Server();
    server.setDatabaseName(0, ALIAS);
    server.setDatabasePath(0, "file:" + System.getProperty("user.dir") + "/hsqldb/" + DATABASE);
    server.setTrace(true);
    server.start();
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
