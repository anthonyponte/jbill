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

package com.anthonyponte.jbill.idao;

import com.anthonyponte.jbill.custom.MyHsqldbConnection;
import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.model.Empresa;
import com.anthonyponte.jbill.model.Summary;
import com.anthonyponte.jbill.model.Tipo;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * @author AnthonyPonte
 */
public class ISummaryDao implements SummaryDao {

  private final MyHsqldbConnection database;

  public ISummaryDao() {
    this.database = new MyHsqldbConnection();
  }

  @Override
  public int create(Summary summary) throws SQLException {
    int result = 0;

    database.connect();

    String query =
        "INSERT INTO SUMMARY"
            + "(UBL, VERSION, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, FECHA_EMISION, FECHA_REFERENCIA, RUC, RUC_TIPO, RAZON_SOCIAL, ZIP_NOMBRE, ZIP) "
            + "VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps =
        database.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, summary.getUbl());
      ps.setString(2, summary.getVersion());
      ps.setString(3, summary.getTipoDocumento().getCodigo());
      ps.setString(4, summary.getTipoDocumento().getDescripcion());
      ps.setString(5, summary.getSerie());
      ps.setInt(6, summary.getCorrelativo());
      ps.setDate(7, new Date(summary.getFechaEmision().getTime()));
      ps.setDate(8, new Date(summary.getFechaReferencia().getTime()));
      ps.setString(9, summary.getEmisor().getNumero());
      ps.setString(10, summary.getEmisor().getTipoDocumentoIdentidad().getCodigo());
      ps.setString(11, summary.getEmisor().getNombre());
      ps.setString(12, summary.getNombreZip());
      ps.setBytes(13, summary.getZip());

      ps.executeUpdate();

      try (ResultSet rs = ps.getGeneratedKeys()) {
        while (rs.next()) {
          result = rs.getInt(1);
        }
      }
    }

    database.disconnect();

    return result;
  }

  @Override
  public List<Summary> read() throws SQLException {
    List<Summary> list = new ArrayList<>();

    database.connect();

    String query =
        "SELECT "
            + "ID, FECHA_EMISION, RUC, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, ZIP_NOMBRE, ZIP "
            + "FROM SUMMARY "
            + "WHERE TICKET IS NULL  AND STATUS_CODE IS NULL AND CONTENT_NOMBRE IS NULL AND CONTENT IS NULL "
            + "ORDER BY FECHA_EMISION DESC";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Summary summary = new Summary();
          summary.setId(rs.getInt(1));
          summary.setFechaEmision(rs.getDate(2));

          Empresa emisor = new Empresa();
          emisor.setNumero(rs.getString(3));
          summary.setEmisor(emisor);

          Tipo tipo = new Tipo();
          tipo.setCodigo(rs.getString(4));
          tipo.setDescripcion(rs.getString(5));
          summary.setTipoDocumento(tipo);

          summary.setSerie(rs.getString(6));
          summary.setCorrelativo(rs.getInt(7));
          summary.setNombreZip(rs.getString(8));
          summary.setZip(rs.getBytes(9));
          list.add(summary);
        }
      }
    }
    database.disconnect();

    return list;
  }

  @Override
  public List<Summary> read(DateTime fecha) throws SQLException {
    List<Summary> list = new ArrayList<>();

    database.connect();

    String query =
        "SELECT "
            + "ID, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, FECHA_EMISION, "
            + "FECHA_REFERENCIA, RUC, RAZON_SOCIAL, ZIP_NOMBRE, ZIP, TICKET,  STATUS_CODE, "
            + "CONTENT_NOMBRE, CONTENT "
            + "FROM SUMMARY "
            + "WHERE MONTH(FECHA_EMISION) = ? AND YEAR(FECHA_EMISION) = ? AND TICKET IS NOT NULL "
            + "AND STATUS_CODE IS NOT NULL AND CONTENT_NOMBRE IS NOT NULL AND CONTENT IS NOT NULL "
            + "ORDER BY FECHA_EMISION DESC";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      ps.setInt(1, fecha.getMonthOfYear());
      ps.setInt(2, fecha.getYear());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Summary summary = new Summary();
          summary.setId(rs.getInt(1));

          Tipo tipo = new Tipo();
          tipo.setCodigo(rs.getString(2));
          tipo.setDescripcion(rs.getString(3));
          summary.setTipoDocumento(tipo);

          summary.setSerie(rs.getString(4));
          summary.setCorrelativo(rs.getInt(5));
          summary.setFechaEmision(rs.getDate(6));
          summary.setFechaReferencia(rs.getDate(7));

          Empresa emisor = new Empresa();
          emisor.setNumero(rs.getString(8));
          emisor.setNombre(rs.getString(9));
          summary.setEmisor(emisor);

          summary.setNombreZip(rs.getString(10));
          summary.setZip(rs.getBytes(11));
          summary.setTicket(rs.getString(12));
          summary.setStatusCode(rs.getString(13));
          summary.setNombreCdr(rs.getString(14));
          summary.setCdr(rs.getBytes(15));
          list.add(summary);
        }
      }
    }

    database.disconnect();

    return list;
  }

  @Override
  public void update(int id, Summary summary) throws SQLException {
    database.connect();

    String query =
        "UPDATE SUMMARY SET "
            + "TICKET = ?, STATUS_CODE = ?, CONTENT_NOMBRE = ?, CONTENT = ? "
            + "WHERE ID = ?";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {

      ps.setString(1, summary.getTicket());
      ps.setString(2, summary.getStatusCode());
      ps.setString(3, summary.getNombreCdr());
      ps.setBytes(4, summary.getCdr());
      ps.setInt(5, id);

      ps.executeUpdate();
    }

    database.disconnect();
  }

  @Override
  public void delete(int id) throws SQLException {
    database.connect();

    String query = "DELETE FROM SUMMARY WHERE ID = ?";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }

    database.disconnect();
  }

  @Override
  public int count(Tipo tipoDocumento, java.util.Date fechaEmision) throws SQLException {
    int count = 0;

    database.connect();

    String query =
        "SELECT TOP 1 CORRELATIVO "
            + "FROM SUMMARY "
            + "WHERE TIPO_CODIGO = ? AND FECHA_EMISION = ? "
            + "ORDER BY CORRELATIVO DESC";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      ps.setString(1, tipoDocumento.getCodigo());
      ps.setDate(2, new Date(fechaEmision.getTime()));

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          count = rs.getInt(1);
        }
      }

      count = count + 1;
    }
    database.disconnect();

    return count;
  }
}
