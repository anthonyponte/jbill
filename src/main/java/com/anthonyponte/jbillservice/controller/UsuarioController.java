package com.anthonyponte.jbillservice.controller;

import com.anthonyponte.jbillservice.filter.IntegerFilter;
import com.anthonyponte.jbillservice.filter.UpperCaseFilter;
import com.anthonyponte.jbillservice.view.MainFrame;
import com.anthonyponte.jbillservice.view.UsuarioIFrame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;

public class UsuarioController {

  private final MainFrame frame;
  private final UsuarioIFrame iFrame;
  public static final String FIRMA_JKS = "FIRMA_JKS";
  public static final String FIRMA_USUARIO = "FIRMA_USUARIO";
  public static final String FIRMA_CONTRASENA = "FIRMA_CONTRASENA";
  public static final String RUC = "RUC";
  public static final String RUC_TIPO = "RUC_TIPO";
  public static final String RAZON_SOCIAL = "RAZON_SOCIAL";
  public static final String CLAVE_SOL_USUARIO = "CLAVE_SOL_USUARIO";
  public static final String CLAVE_SOL_CONTRASENA = "CLAVE_SOL_CONTRASENA";
  private Preferences preferences;
  private String firmaJks;
  private String firmaUsuario;
  private String firmaContrasena;
  private String ruc;
  private String razonSocial;
  private String claveSolUsuario;
  private String claveSolContrasena;

  public UsuarioController(MainFrame frame, UsuarioIFrame iFrame) {
    this.frame = frame;
    this.iFrame = iFrame;
    initComponents();
  }

  public void init() {
    iFrame.tabbed.addChangeListener(
        (ChangeEvent ce) -> {
          if (iFrame.tabbed.getSelectedIndex() == 0)
            if (isEmpty()) iFrame.btnFirmaJks.requestFocus();
            else iFrame.btnEntrar.requestFocus();
          else if (iFrame.tabbed.getSelectedIndex() == 1)
            if (isEmpty()) iFrame.tfRuc.requestFocus();
            else iFrame.btnEntrar.requestFocus();
        });

    iFrame.btnFirmaJks.addActionListener(
        (ActionEvent arg0) -> {
          JFileChooser chooser = new JFileChooser();
          chooser.setDialogTitle("Importar Firma");
          chooser.setApproveButtonText("Importar");
          chooser.setAcceptAllFileFilterUsed(false);
          chooser.addChoosableFileFilter(new FileNameExtensionFilter("JKS", "jks"));
          chooser.setCurrentDirectory(new File("."));
          int result = chooser.showOpenDialog(iFrame);
          if (result == JFileChooser.APPROVE_OPTION) {
            File jks = chooser.getSelectedFile();
            iFrame.tfFirmaJks.setText(jks.getAbsolutePath());
            iFrame.tfRuc.requestFocus();
          }
        });

    iFrame.btnEntrar.addActionListener(
        (ActionEvent arg0) -> {
          try {
            if (iFrame.cbRecordar.isSelected()) {
              preferences.put(FIRMA_JKS, iFrame.tfFirmaJks.getText());
              preferences.put(FIRMA_USUARIO, iFrame.tfFirmaUsuario.getText());
              preferences.put(
                  FIRMA_CONTRASENA, String.valueOf(iFrame.tfFirmaContrasena.getPassword()));
              preferences.put(RUC, iFrame.tfRuc.getText());
              preferences.putInt(RUC_TIPO, 6);
              preferences.put(RAZON_SOCIAL, iFrame.tfRazonSocial.getText());
              preferences.put(CLAVE_SOL_USUARIO, iFrame.tfClaveSolUsuario.getText());
              preferences.put(
                  CLAVE_SOL_CONTRASENA, String.valueOf(iFrame.tfClaveSolContrasena.getPassword()));
            } else {
              preferences.clear();
            }
            iFrame.dispose();
            frame.menuNuevo.setEnabled(true);
            frame.menuVer.setEnabled(true);
            frame.menuBillService.setEnabled(true);
            frame.miComunicacionBaja.setEnabled(true);
            frame.miResumenDiario.setEnabled(true);
            frame.miComunicacionesBaja.setEnabled(true);
            frame.miResumenesDiario.setEnabled(true);
            frame.miSummary.setEnabled(true);
          } catch (BackingStoreException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
          }
        });

    iFrame.tfFirmaJks.getDocument().addDocumentListener(dl);
    iFrame.tfFirmaUsuario.getDocument().addDocumentListener(dl);
    iFrame.tfFirmaContrasena.getDocument().addDocumentListener(dl);
    iFrame.tfRuc.getDocument().addDocumentListener(dl);
    iFrame.tfRazonSocial.getDocument().addDocumentListener(dl);
    iFrame.tfClaveSolUsuario.getDocument().addDocumentListener(dl);
    iFrame.tfClaveSolContrasena.getDocument().addDocumentListener(dl);
  }

  private void initComponents() {
    preferences = Preferences.userRoot().node(MainController.class.getPackageName());
    firmaJks = preferences.get(FIRMA_JKS, "");
    firmaUsuario = preferences.get(FIRMA_USUARIO, "");
    firmaContrasena = preferences.get(FIRMA_CONTRASENA, "");
    ruc = preferences.get(RUC, "");
    razonSocial = preferences.get(RAZON_SOCIAL, "");
    claveSolUsuario = preferences.get(CLAVE_SOL_USUARIO, "");
    claveSolContrasena = preferences.get(CLAVE_SOL_CONTRASENA, "");

    iFrame.show();

    AbstractDocument docRuc = (AbstractDocument) iFrame.tfRuc.getDocument();
    docRuc.setDocumentFilter(new IntegerFilter(11));

    AbstractDocument douRazonSocial = (AbstractDocument) iFrame.tfRazonSocial.getDocument();
    douRazonSocial.setDocumentFilter(new UpperCaseFilter());

    AbstractDocument docClaveSolUsuario = (AbstractDocument) iFrame.tfClaveSolUsuario.getDocument();
    docClaveSolUsuario.setDocumentFilter(new UpperCaseFilter());

    if (isEmpty()) {
      iFrame.tfFirmaJks.requestFocus();
      iFrame.cbRecordar.setSelected(false);
      iFrame.btnEntrar.setEnabled(false);
    } else {
      File file = new File(firmaJks);
      if (file.exists()) iFrame.tfFirmaJks.setText(firmaJks);
      iFrame.tfFirmaUsuario.setText(firmaUsuario);
      iFrame.tfFirmaContrasena.setText(firmaContrasena);
      iFrame.tfRuc.setText(ruc);
      iFrame.tfRazonSocial.setText(razonSocial);
      iFrame.tfClaveSolUsuario.setText(claveSolUsuario);
      iFrame.tfClaveSolContrasena.setText(claveSolContrasena);
      iFrame.cbRecordar.setSelected(true);
      iFrame.btnEntrar.setEnabled(true);
      iFrame.btnEntrar.requestFocus();
    }
  }

  private final DocumentListener dl =
      new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent arg0) {
          enabled();
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
          enabled();
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
          enabled();
        }
      };

  private void enabled() {
    if (iFrame.tfFirmaJks.getText().isEmpty()
        || iFrame.tfFirmaUsuario.getText().length() < 6
        || iFrame.tfFirmaContrasena.getPassword().length < 7
        || iFrame.tfRuc.getText().length() < 11
        || iFrame.tfRazonSocial.getText().length() < 11
        || iFrame.tfClaveSolUsuario.getText().length() < 7
        || iFrame.tfClaveSolContrasena.getPassword().length < 7) {
      iFrame.btnEntrar.setEnabled(false);
    } else {
      iFrame.btnEntrar.setEnabled(true);
    }
  }

  private boolean isEmpty() {
    boolean isEmpty =
        firmaJks.isEmpty()
            && firmaUsuario.isEmpty()
            && ruc.isEmpty()
            && razonSocial.isEmpty()
            && claveSolUsuario.isEmpty();
    return isEmpty;
  }
}
