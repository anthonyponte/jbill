/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.custom;

import com.anthonyponte.jbillservice.controller.MainController;
import com.anthonyponte.jbillservice.controller.UsuarioController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/** @author AnthonyPonte */
public class MyFileCreator {

  private static final Preferences PREFS =
      Preferences.userRoot().node(MainController.class.getPackageName());

  public static File create(String tipo, String serie, int correlativo, Document estructura) {
    File xml = null;

    try {
      xml =
          new File(
              PREFS.get(UsuarioController.RUC, "")
                  + "-"
                  + tipo
                  + "-"
                  + serie
                  + "-"
                  + correlativo
                  + ".xml");
      try (FileOutputStream fos = new FileOutputStream(xml, false)) {
        XMLOutputter outputter = new XMLOutputter();
        Format format = Format.getRawFormat();
        format.setEncoding("ISO-8859-1");
        outputter.setFormat(format);
        outputter.output(estructura, fos);
        fos.flush();
      }

      return xml;
    } catch (FileNotFoundException ex) {
      Logger.getLogger(MyFileCreator.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyFileCreator.class.getName(), JOptionPane.ERROR_MESSAGE);
    } catch (IOException ex) {
      Logger.getLogger(MyFileCreator.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyFileCreator.class.getName(), JOptionPane.ERROR_MESSAGE);
    }

    return xml;
  }

  public static File sign(String tipo, String serie, int correlativo, File file) {
    File jks = new File(PREFS.get(UsuarioController.FIRMA_JKS, ""));
    try {
      XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

      Reference ref =
          fac.newReference(
              "",
              fac.newDigestMethod(DigestMethod.SHA1, null),
              Collections.singletonList(
                  fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
              null,
              null);

      SignedInfo si =
          fac.newSignedInfo(
              fac.newCanonicalizationMethod(
                  CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
              fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
              Collections.singletonList(ref));

      KeyStore ks = KeyStore.getInstance("JKS");
      KeyStore.PrivateKeyEntry keyEntry;
      FileInputStream ksfis = new FileInputStream(PREFS.get(UsuarioController.FIRMA_JKS, ""));
      ks.load(ksfis, PREFS.get(UsuarioController.FIRMA_CONTRASENA, "").toCharArray());
      keyEntry =
          (KeyStore.PrivateKeyEntry)
              ks.getEntry(
                  PREFS.get(UsuarioController.FIRMA_USUARIO, ""),
                  new KeyStore.PasswordProtection(
                      PREFS.get(UsuarioController.FIRMA_CONTRASENA, "").toCharArray()));
      X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
      KeyInfoFactory kif = fac.getKeyInfoFactory();
      ArrayList<Object> x509Content = new ArrayList<>();
      x509Content.add(cert.getSubjectX500Principal().getName());
      x509Content.add(cert);
      X509Data xd = kif.newX509Data(x509Content);
      KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      org.w3c.dom.Document doc;
      FileInputStream fis = new FileInputStream(file);
      doc = (org.w3c.dom.Document) dbf.newDocumentBuilder().parse(fis);

      DOMSignContext dsc =
          new DOMSignContext(
              keyEntry.getPrivateKey(),
              (Node) doc.getDocumentElement().getFirstChild().getLastChild().getLastChild());
      dsc.setDefaultNamespacePrefix("ds");
      XMLSignature xMLSignature =
          fac.newXMLSignature(
              si, ki, null, "SIGN-" + PREFS.get(UsuarioController.FIRMA_USUARIO, ""), null);
      xMLSignature.sign(dsc);

      try (OutputStream os = new FileOutputStream(file, false)) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource((Node) doc), new StreamResult(os));
        os.flush();
      }

    } catch (NoSuchAlgorithmException
        | InvalidAlgorithmParameterException
        | KeyStoreException
        | IOException
        | ParserConfigurationException
        | SAXException
        | TransformerException
        | MarshalException
        | XMLSignatureException
        | CertificateException
        | UnrecoverableEntryException ex) {
      Logger.getLogger(MyFileCreator.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyFileCreator.class.getName(), JOptionPane.ERROR_MESSAGE);
    }

    return file;
  }

  public static File compress(String tipo, String serie, int correlativo, File file) {
    File zipFile =
        new File(
            PREFS.get(UsuarioController.RUC, "")
                + "-"
                + tipo
                + "-"
                + serie
                + "-"
                + correlativo
                + ".zip");

    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
      zos.putNextEntry(new ZipEntry(file.getName()));
      try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
      }
      zos.flush();
    } catch (IOException ex) {
      Logger.getLogger(MyFileCreator.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyFileCreator.class.getName(), JOptionPane.ERROR_MESSAGE);
    }

    return zipFile;
  }
}
