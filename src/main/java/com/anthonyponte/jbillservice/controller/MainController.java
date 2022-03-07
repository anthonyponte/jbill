/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anthonyponte.jbillservice.controller;

import com.anthonyponte.jbillservice.view.SummaryIFrame;
import com.anthonyponte.jbillservice.view.MainFrame;
import com.anthonyponte.jbillservice.view.UsuarioIFrame;
import com.anthonyponte.jbillservice.view.ComunicacionBajaIFrame;
import com.anthonyponte.jbillservice.view.ComunicacionesBajaIFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/** @author anthony */
public class MainController {

  private final MainFrame frame;
  private UsuarioIFrame usuarioIFrame;
  private ComunicacionBajaIFrame comunicacionBajaIFrame;
  private SummaryIFrame summaryIFrame;
  private ComunicacionesBajaIFrame comunicacionesBajaIFrame;

  public MainController(MainFrame frame) {
    this.frame = frame;
    initComponents();
  }

  public void start() {
    frame.menuEntrar.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(usuarioIFrame)) {
            this.usuarioIFrame = new UsuarioIFrame();
            frame.dpane.add(usuarioIFrame);
            usuarioIFrame.setLocation(centerIFrame(usuarioIFrame));
            new UsuarioController(frame, usuarioIFrame).start();
          } else {
            iframeClosed(usuarioIFrame);
          }
        });

    frame.miComunicacionBaja.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(comunicacionBajaIFrame)) {
            this.comunicacionBajaIFrame = new ComunicacionBajaIFrame();
            frame.dpane.add(comunicacionBajaIFrame);
            comunicacionBajaIFrame.setLocation(centerIFrame(comunicacionBajaIFrame));
            new ComunicacionBajaController(frame, comunicacionBajaIFrame).start();
          } else {
            iframeClosed(comunicacionBajaIFrame);
          }
        });

    frame.miComunicacionesBaja.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(comunicacionesBajaIFrame)) {
            this.comunicacionesBajaIFrame = new ComunicacionesBajaIFrame();
            frame.dpane.add(comunicacionesBajaIFrame);
            comunicacionesBajaIFrame.setLocation(centerIFrame(comunicacionesBajaIFrame));
            new ComunicacionesBajaController(frame, comunicacionesBajaIFrame).start();
          } else {
            iframeClosed(comunicacionesBajaIFrame);
          }
        });

    frame.miSummary.addActionListener(
        (ActionEvent arg0) -> {
          if (isIframeClosed(summaryIFrame)) {
            this.summaryIFrame = new SummaryIFrame();
            frame.dpane.add(summaryIFrame);
            summaryIFrame.setLocation(centerIFrame(summaryIFrame));
            new SummaryController(frame, summaryIFrame).start();
          } else {
            iframeClosed(summaryIFrame);
          }
        });

    frame.menuSalir.addActionListener(
        (ActionEvent arg0) -> {
          int input =
              JOptionPane.showConfirmDialog(
                  frame,
                  "Seguro que desea salir?",
                  "Salir",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE);

          if (input == JOptionPane.YES_OPTION) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
          }
        });
  }

  private void initComponents() {
    frame.setVisible(true);

    if (isIframeClosed(usuarioIFrame)) {
      this.usuarioIFrame = new UsuarioIFrame();
      frame.dpane.add(usuarioIFrame);
      usuarioIFrame.setLocation(centerIFrame(usuarioIFrame));
      new UsuarioController(frame, usuarioIFrame).start();
    } else {
      iframeClosed(usuarioIFrame);
    }

    frame.menuNuevo.setEnabled(false);
    frame.menuVer.setEnabled(false);
    frame.menuBillService.setEnabled(false);
    frame.miComunicacionBaja.setEnabled(false);
    frame.miComunicacionesBaja.setEnabled(false);
    frame.miSummary.setEnabled(false);
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
          Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        iframe.setLocation(centerIFrame(iframe));
      }
    }
  }
}