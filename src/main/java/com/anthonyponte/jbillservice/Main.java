/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.anthonyponte.jbillservice;

import com.anthonyponte.jbillservice.controller.MainController;
import com.anthonyponte.jbillservice.view.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import javax.swing.SwingUtilities;

/** @author anthony */
public class Main {
  /** @param args the command line arguments */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          FlatLaf.registerCustomDefaultsSource("com.anthonyponte.jbillservice.theme");
          FlatDarkLaf.setup();
          MainFrame mainFrame = new MainFrame();
          new MainController(mainFrame).init();
        });
  }
}
