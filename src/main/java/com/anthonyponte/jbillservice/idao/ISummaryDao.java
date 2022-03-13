/*
 * Copyright (C) 2022 AnthonyPonte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.anthonyponte.jbillservice.idao;

import com.anthonyponte.jbillservice.custom.MyHsqldbConnection;
import com.anthonyponte.jbillservice.dao.SummaryDao;
import com.anthonyponte.jbillservice.model.Empresa;
import com.anthonyponte.jbillservice.model.Summary;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/** @author AnthonyPonte */
public class ISummaryDao implements SummaryDao {

  private final MyHsqldbConnection database;

  public ISummaryDao() {
    this.database = new MyHsqldbConnection();
  }

  @Override
  public int create(Summary summary) {
    int result = 0;
    try {
      database.connect();

      String query =
          "INSERT INTO SUMMARY"
              + "(UBL, VERSION, TIPO, SERIE, CORRELATIVO, FECHA_EMISION, FECHA_REFERENCIA, RUC, TIPO_RUC, RAZON_SOCIAL, NOMBRE_ZIP, ZIP) "
              + "VALUES"
              + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      try (PreparedStatement ps =
          database
              .getConnection()
              .prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, summary.getUbl());
        ps.setString(2, summary.getVersion());
        ps.setString(3, summary.getTipo());
        ps.setString(4, summary.getSerie());
        ps.setInt(5, summary.getCorrelativo());
        ps.setDate(6, new Date(summary.getFechaEmision().getTime()));
        ps.setDate(7, new Date(summary.getFechaReferencia().getTime()));
        ps.setString(8, summary.getEmisor().getRuc());
        ps.setInt(9, summary.getEmisor().getTipo());
        ps.setString(10, summary.getEmisor().getRazonSocial());
        ps.setString(11, summary.getNombreZip());
        ps.setBytes(12, summary.getZip());

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
          while (rs.next()) {
            result = rs.getInt(1);
          }
        }
      }

      database.disconnect();

    } catch (SQLException ex) {
      Logger.getLogger(ISummaryDao.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null,
          ex.getErrorCode() + " - " + ex.getMessage(),
          ISummaryDao.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }

    return result;
  }

  @Override
  public List<Summary> read() {
    List<Summary> list = null;
    try {
      database.connect();

      String query =
          "SELECT "
              + "ID, FECHA_EMISION, RUC, TIPO, SERIE, CORRELATIVO, NOMBRE_ZIP, ZIP "
              + "FROM SUMMARY "
              + "WHERE TICKET IS NULL  AND STATUS_CODE IS NULL AND NOMBRE_CONTENT IS NULL AND CONTENT IS NULL "
              + "ORDER BY FECHA_EMISION DESC";

      try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          list = new ArrayList<>();
          while (rs.next()) {
            Summary summary = new Summary();
            summary.setId(rs.getInt(1));
            summary.setFechaEmision(rs.getDate(2));
            Empresa emisor = new Empresa();
            emisor.setRuc(rs.getString(3));
            summary.setEmisor(emisor);
            summary.setTipo(rs.getString(4));
            summary.setSerie(rs.getString(5));
            summary.setCorrelativo(rs.getInt(6));
            summary.setNombreZip(rs.getString(7));
            summary.setZip(rs.getBytes(8));
            list.add(summary);
          }
        }
      }
      database.disconnect();
    } catch (SQLException ex) {
      Logger.getLogger(ISummaryDao.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null,
          ex.getErrorCode() + " - " + ex.getMessage(),
          ISummaryDao.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }
    return list;
  }

  @Override
  public int read(Summary summary) {
    int count = 0;
    try {
      database.connect();

      String query =
          "SELECT TOP 1 CORRELATIVO "
              + "FROM SUMMARY "
              + "WHERE TIPO = ? AND FECHA_EMISION = ? "
              + "ORDER BY CORRELATIVO DESC";

      try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
        ps.setString(1, summary.getTipo());
        ps.setDate(2, new Date(summary.getFechaEmision().getTime()));

        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            count = rs.getInt(1);
          }
        }
      }
      database.disconnect();

    } catch (SQLException ex) {
      Logger.getLogger(ISummaryDao.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null,
          ex.getErrorCode() + " - " + ex.getMessage(),
          ISummaryDao.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }

    return count;
  }

  @Override
  public void update(int id, Summary summary) {
    try {
      database.connect();

      String query =
          "UPDATE SUMMARY SET "
              + "TICKET = ?, STATUS_CODE = ?, NOMBRE_CONTENT = ?, CONTENT = ? "
              + "WHERE ID = ?";

      try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {

        ps.setString(1, summary.getTicket());
        ps.setString(2, summary.getStatusCode());
        ps.setString(3, summary.getNombreContent());
        ps.setBytes(4, summary.getContent());
        ps.setInt(5, id);

        ps.executeUpdate();
      }

      database.disconnect();

    } catch (SQLException ex) {
      Logger.getLogger(ISummaryDao.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null,
          ex.getErrorCode() + " - " + ex.getMessage(),
          ISummaryDao.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void delete(int id) {
    try {
      database.connect();

      String query = "DELETE FROM SUMMARY WHERE ID = ?";

      try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
        ps.setInt(1, id);
        ps.executeUpdate();
      }

      database.disconnect();

    } catch (SQLException ex) {
      Logger.getLogger(ISummaryDao.class.getName()).log(Level.SEVERE, null, ex);
      JOptionPane.showMessageDialog(
          null,
          ex.getErrorCode() + " - " + ex.getMessage(),
          ISummaryDao.class.getName(),
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
