/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.custom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;

/** @author anthony */
public class MyHsqldbConnection {
  private final String ALIAS = "jbs";
  private final String DATABASE = "jbillservice";
  private final String USER = "SA";
  private final String PASS = "";
  private final String URL = "jdbc:hsqldb:hsql://localhost/" + ALIAS;
  private Connection connection = null;
  private Server server = null;

  public void connect() {
    try {
      HsqlProperties properties = new HsqlProperties();
      properties.setProperty("server.database.0", "./database/" + DATABASE);
      properties.setProperty("server.dbname.0", ALIAS);

      server = new Server();
      server.setProperties(properties);
      server.setTrace(true);
      server.start();

      connection = DriverManager.getConnection(URL, USER, PASS);
    } catch (IOException | ServerAcl.AclFormatException | SQLException ex) {
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyHsqldbConnection.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  public void disconnect() {
    try {
      connection.close();
      server.stop();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(
          null, ex.getMessage(), MyHsqldbConnection.class.getName(), JOptionPane.ERROR_MESSAGE);
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
