/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.idao;

import com.anthonyponte.jbill.custom.MyHsqldbConnection;
import com.anthonyponte.jbill.model.Comunicacion;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.anthonyponte.jbill.model.Bill;
import com.anthonyponte.jbill.model.Empresa;
import java.util.ArrayList;
import org.joda.time.DateTime;
import com.anthonyponte.jbill.model.Tipo;
import com.anthonyponte.jbill.dao.ComunicacionDao;

/**
 * @author anthony
 */
public class IComunicacionDao implements ComunicacionDao {

  private final MyHsqldbConnection database;

  public IComunicacionDao() {
    this.database = new MyHsqldbConnection();
  }

  @Override
  public void create(int id, List<ComunicacionDetalle> comunicacionBajaDetalles)
      throws SQLException {
    database.connect();

    String queryDetalle =
        "INSERT INTO COMUNICACION_BAJA_DETALLE"
            + "(SUMMARY_ID, NUMERO, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, MOTIVO) "
            + "VALUES"
            + "(?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = database.getConnection().prepareStatement(queryDetalle)) {

      for (int i = 0; i < comunicacionBajaDetalles.size(); i++) {
        ComunicacionDetalle get = comunicacionBajaDetalles.get(i);
        ps.setInt(1, id);
        ps.setInt(2, i + 1);
        ps.setString(3, get.getDocumento().getTipo().getCodigo());
        ps.setString(4, get.getDocumento().getTipo().getDescripcion());
        ps.setString(5, get.getDocumento().getSerie());
        ps.setInt(6, get.getDocumento().getCorrelativo());
        ps.setString(7, get.getMotivo());
        ps.addBatch();
      }

      ps.executeBatch();
    }

    database.disconnect();
  }

  @Override
  public List<Comunicacion> read(DateTime dateTime) throws SQLException {
    List<Comunicacion> list = new ArrayList<>();

    database.connect();

    String query =
        "SELECT "
            + "ID, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, FECHA_EMISION, "
            + "FECHA_REFERENCIA, RUC, RAZON_SOCIAL, ZIP_NOMBRE, ZIP, TICKET,  STATUS_CODE, "
            + "CONTENT_NOMBRE, CONTENT "
            + "FROM SUMMARY "
            + "WHERE MONTH(FECHA_EMISION) = ? AND YEAR(FECHA_EMISION) = ? AND TICKET IS NOT NULL "
            + "AND STATUS_CODE IS NOT NULL AND CONTENT_NOMBRE IS NOT NULL AND CONTENT IS NOT NULL "
            + "AND (TIPO_CODIGO = 'RA' OR TIPO_CODIGO = 'RR') "
            + "ORDER BY FECHA_EMISION DESC";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      ps.setInt(1, dateTime.getMonthOfYear());
      ps.setInt(2, dateTime.getYear());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Comunicacion comunicacionBaja = new Comunicacion();
          comunicacionBaja.setId(rs.getInt(1));

          Tipo tipoDocumento = new Tipo();
          tipoDocumento.setCodigo(rs.getString(2));
          tipoDocumento.setDescripcion(rs.getString(3));
          comunicacionBaja.setTipoDocumento(tipoDocumento);

          comunicacionBaja.setSerie(rs.getString(4));
          comunicacionBaja.setCorrelativo(rs.getInt(5));
          comunicacionBaja.setFechaEmision(rs.getDate(6));
          comunicacionBaja.setFechaReferencia(rs.getDate(7));

          Empresa emisor = new Empresa();
          emisor.setNumero(rs.getString(8));
          emisor.setNombre(rs.getString(9));
          comunicacionBaja.setEmisor(emisor);

          comunicacionBaja.setNombreZip(rs.getString(10));
          comunicacionBaja.setZip(rs.getBytes(11));
          comunicacionBaja.setTicket(rs.getString(12));
          comunicacionBaja.setStatusCode(rs.getString(13));
          comunicacionBaja.setNombreContent(rs.getString(14));
          comunicacionBaja.setContent(rs.getBytes(15));
          list.add(comunicacionBaja);
        }
      }
    }

    database.disconnect();

    return list;
  }

  @Override
  public List<ComunicacionDetalle> read(Comunicacion comunicacionBaja) throws SQLException {
    List<ComunicacionDetalle> comunicacionBajaDetalles = new ArrayList<>();

    database.connect();

    String query =
        "SELECT "
            + "ID, NUMERO, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, MOTIVO "
            + "FROM COMUNICACION_BAJA_DETALLE "
            + "WHERE SUMMARY_ID = ?";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {

      ps.setInt(1, comunicacionBaja.getId());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ComunicacionDetalle comunicacionBajaDetalle = new ComunicacionDetalle();

          comunicacionBajaDetalle.setComunicacionBaja(comunicacionBaja);

          comunicacionBajaDetalle.setId(rs.getInt(1));
          comunicacionBajaDetalle.setNumero(rs.getInt(2));

          Bill documento = new Bill();

          Tipo tipo = new Tipo();
          tipo.setCodigo(rs.getString(3));
          tipo.setDescripcion(rs.getString(4));
          documento.setTipo(tipo);

          documento.setSerie(rs.getString(5));
          documento.setCorrelativo(rs.getInt(6));
          comunicacionBajaDetalle.setDocumento(documento);

          comunicacionBajaDetalle.setMotivo(rs.getString(7));

          comunicacionBajaDetalles.add(comunicacionBajaDetalle);
        }
      }
    }

    database.disconnect();

    return comunicacionBajaDetalles;
  }

  @Override
  public void delete(int id) throws SQLException {
    database.connect();

    String query = "DELETE FROM COMUNICACION_BAJA_DETALLE WHERE SUMMARY_ID = ?";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }

    database.disconnect();
  }
}
