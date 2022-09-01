/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.idao;

import com.anthonyponte.jbill.custom.MyHsqldbConnection;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.anthonyponte.jbill.model.Tipo;
import com.anthonyponte.jbill.dao.ComunicacionDetalleDao;
import com.anthonyponte.jbill.model.Summary;

/**
 * @author anthony
 */
public class IComunicacionDetalleDao implements ComunicacionDetalleDao {

  private final MyHsqldbConnection database;

  public IComunicacionDetalleDao() {
    this.database = new MyHsqldbConnection();
  }

  @Override
  public void create(int id, List<ComunicacionDetalle> detalles) throws SQLException {
    database.connect();

    String queryDetalle =
        "INSERT INTO COMUNICACION_BAJA_DETALLE"
            + "(SUMMARY_ID, NUMERO, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, MOTIVO) "
            + "VALUES"
            + "(?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = database.getConnection().prepareStatement(queryDetalle)) {

      for (int i = 0; i < detalles.size(); i++) {
        ComunicacionDetalle get = detalles.get(i);
        ps.setInt(1, id);
        ps.setInt(2, i + 1);
        ps.setString(3, get.getTipoDocumento().getCodigo());
        ps.setString(4, get.getTipoDocumento().getDescripcion());
        ps.setString(5, get.getSerie());
        ps.setInt(6, get.getCorrelativo());
        ps.setString(7, get.getMotivo());
        ps.addBatch();
      }

      ps.executeBatch();
    }

    database.disconnect();
  }

  @Override
  public List<ComunicacionDetalle> read(Summary summary) throws SQLException {
    List<ComunicacionDetalle> list = new ArrayList<>();

    database.connect();

    String query =
        "SELECT "
            + "ID, NUMERO, TIPO_CODIGO, TIPO_DESCRIPCION, SERIE, CORRELATIVO, MOTIVO "
            + "FROM COMUNICACION_BAJA_DETALLE "
            + "WHERE SUMMARY_ID = ?";

    try (PreparedStatement ps = database.getConnection().prepareStatement(query)) {

      ps.setInt(1, summary.getId());

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ComunicacionDetalle detalle = new ComunicacionDetalle();

          detalle.setSummary(summary);

          detalle.setId(rs.getInt(1));
          detalle.setNumero(rs.getInt(2));

          Tipo tipoDocumento = new Tipo();
          tipoDocumento.setCodigo(rs.getString(3));
          tipoDocumento.setDescripcion(rs.getString(4));
          detalle.setTipoDocumento(tipoDocumento);

          detalle.setSerie(rs.getString(5));
          detalle.setCorrelativo(rs.getInt(6));

          detalle.setMotivo(rs.getString(7));

          list.add(detalle);
        }
      }
    }

    database.disconnect();

    return list;
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
