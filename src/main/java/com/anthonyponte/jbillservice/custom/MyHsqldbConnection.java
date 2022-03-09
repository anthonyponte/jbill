/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.custom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
      HsqlProperties hsqlProperties = new HsqlProperties();
      hsqlProperties.setProperty("server.database.0", "./database/" + DATABASE);
      hsqlProperties.setProperty("server.dbname.0", ALIAS);

      server = new Server();
      server.setProperties(hsqlProperties);
      server.setTrace(true);
      server.start();

      connection = DriverManager.getConnection(URL, USER, PASS);
    } catch (IOException | ServerAcl.AclFormatException | SQLException ex) {
      Logger.getLogger(MyHsqldbConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void disconnect() {
    try {
      connection.close();
      server.stop();
    } catch (SQLException ex) {
      Logger.getLogger(MyHsqldbConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Connection getConnection() {
    return connection;
  }
}
