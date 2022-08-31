/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.custom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/** @author anthony */
public class MyHsqldbConnection {
  private final String ALIAS = "jbs";
  private final String USER = "SA";
  private final String PASS = "";
  private final String URL = "jdbc:hsqldb:hsql://localhost/" + ALIAS;
//  private final String URL = "jdbc:hsqldb:file:/com/anthonyponte/jbillservice/hsqldb/" + ALIAS;
  private Connection connection = null;

  public void connect() {
    try {

      connection = DriverManager.getConnection(URL, USER, PASS);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyHsqldbConnection.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  public void disconnect() {
    try {
      connection.close();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyHsqldbConnection.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
