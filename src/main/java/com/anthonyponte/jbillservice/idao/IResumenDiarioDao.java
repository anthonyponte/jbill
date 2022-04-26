/*
 * Copyright (C) 2022 anthony
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
import com.anthonyponte.jbillservice.dao.ResumenDiarioDao;
import com.anthonyponte.jbillservice.model.ResumenDiario;
import com.anthonyponte.jbillservice.model.ResumenDiarioDetalle;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.joda.time.DateTime;

/** @author anthony */
public class IResumenDiarioDao implements ResumenDiarioDao {

  private final MyHsqldbConnection database;

  public IResumenDiarioDao() {
    this.database = new MyHsqldbConnection();
  }

  @Override
  public void create(int id, List<ResumenDiarioDetalle> resumenDiarioDetalles) throws SQLException {
    database.connect();

    String queryDetalle =
        "INSERT INTO RESUMEN_DIARIO_DETALLE"
            + "(SUMMARY_ID, NUMERO, SERIE, CORRELATIVO, TIPO_CODIGO, TIPO_DESCRIPCION, "
            + "DOCUMENTO_IDENTIDAD, DOCUMENTO_IDENTIDAD_TIPO, DOCUMENTO_IDENTIDAD_DESCRIPCION, "
            + "REFERENCIA_SERIE, REFERENCIA_CORRELATIVO, REFERENCIA_TIPO_CODIGO, "
            + "REFERENCIA_TIPO_DESCRIPCION, PERCEPCION_REGIMEN_CODIGO, "
            + "PERCEPCION_REGIMEN_DESCRIPCION, PERCEPCION_REGIMEN_PORCENTAJE, PERCEPCION_MONTO, "
            + "PERCEPCION_MONTO_TOTAL, PERCEPCION_BASE, ESTADO_CODIGO, ESTADO_DESCRIPCION, "
            + "IMPORTE_TOTAL, MONEDA_CODIGO, MONEDA_DESCRIPCION, GRAVADAS_TOTAL, GRAVADAS_CODIGO, "
            + "GRAVADAS_DESCRIPCION, EXONERADAS_TOTAL, EXONERADAS_CODIGO, EXONERADAS_DESCRIPCION, "
            + "INAFECTAS_TOTAL, INAFECTAS_CODIGO, INAFECTAS_DESCRIPCION, GRATUITAS_TOTAL, "
            + "GRATUITAS_CODIGO, GRATUITAS_DESCRIPCION, EXPORTACION_TOTAL, EXPORTACION_CODIGO, "
            + "EXPORTACION_DESCRIPCION, OTROS_CARGOS_INDICADOR, OTROS_CARGOS_TOTAL, IGV_TOTAL, "
            + "IGV_CODIGO, IGV_DESCRIPCION, IGV_CODIGO_INTERNACIONAL, ISC_TOTAL, ISC_CODIGO, "
            + "ISC_DESCRIPCION, ISC_CODIGO_INTERNACIONAL, OTRO_TRIBUTOS_TOTAL, OTRO_TRIBUTOS_CODIGO, "
            + "OTRO_TRIBUTOS_DESCRIPCION, OTRO_TRIBUTOS_CODIGO_INTERNACIONAL, IMPUESTO_BOLSA_TOTAL, "
            + "IMPUESTO_BOLSA_CODIGO, IMPUESTO_BOLSA_DESCRIPCION, IMPUESTO_BOLSA_CODIGO_INTERNACIONAL) "
            + "VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?)";

    try (PreparedStatement ps = database.getConnection().prepareStatement(queryDetalle)) {

      for (int i = 0; i < resumenDiarioDetalles.size(); i++) {
        ResumenDiarioDetalle get = resumenDiarioDetalles.get(i);
        ps.setInt(1, id);
        ps.setInt(2, i + 1);
        ps.setString(3, get.getDocumento().getSerie());
        ps.setInt(4, get.getDocumento().getCorrelativo());
        ps.setString(5, get.getDocumento().getTipoDocumento().getCodigo());
        ps.setString(6, get.getDocumento().getTipoDocumento().getDescripcion());

        if (get.getAdquiriente() != null) {
          ps.setInt(7, get.getAdquiriente().getDocumentoIdentidadNumero());
          ps.setString(8, get.getAdquiriente().getDocumentoIdentidad().getCodigo());
          ps.setString(9, get.getAdquiriente().getDocumentoIdentidad().getDescripcion());
        } else {
          ps.setNull(7, Types.INTEGER);
          ps.setNull(8, Types.VARCHAR);
          ps.setNull(9, Types.VARCHAR);
        }

        if (get.getDocumentoReferencia() != null) {
          ps.setString(10, get.getDocumentoReferencia().getSerie());
          ps.setInt(11, get.getDocumentoReferencia().getCorrelativo());
          ps.setString(12, get.getDocumentoReferencia().getTipoDocumento().getCodigo());
          ps.setString(13, get.getDocumentoReferencia().getTipoDocumento().getDescripcion());
        } else {
          ps.setNull(10, Types.VARCHAR);
          ps.setNull(11, Types.INTEGER);
          ps.setNull(12, Types.VARCHAR);
          ps.setNull(13, Types.VARCHAR);
        }

        if (get.getPercepcion() != null) {
          ps.setString(14, get.getPercepcion().getRegimenPercepcion().getCodigo());
          ps.setString(15, get.getPercepcion().getRegimenPercepcion().getDescripcion());
          ps.setDouble(16, get.getPercepcion().getRegimenPercepcion().getPorcentaje());
          ps.setDouble(17, get.getPercepcion().getMonto());
          ps.setDouble(18, get.getPercepcion().getMontoTotal());
          ps.setDouble(19, get.getPercepcion().getBase());
        } else {
          ps.setNull(14, Types.VARCHAR);
          ps.setNull(15, Types.VARCHAR);
          ps.setNull(16, Types.DOUBLE);
          ps.setNull(17, Types.DOUBLE);
          ps.setNull(18, Types.DOUBLE);
          ps.setNull(19, Types.DOUBLE);
        }

        ps.setString(20, get.getEstado().getCodigo());
        ps.setString(21, get.getEstado().getDescripcion());

        ps.setDouble(22, get.getImporteTotal());

        ps.setString(23, get.getMoneda().getCodigo());
        ps.setString(24, get.getMoneda().getDescripcion());

        if (get.getGravadas() != null) {
          ps.setDouble(25, get.getGravadas().getTotal());
          ps.setString(26, get.getGravadas().getCodigo());
          ps.setString(27, get.getGravadas().getDescripcion());
        } else {
          ps.setNull(25, Types.DOUBLE);
          ps.setNull(26, Types.VARCHAR);
          ps.setNull(27, Types.VARCHAR);
        }

        if (get.getExoneradas() != null) {
          ps.setDouble(28, get.getExoneradas().getTotal());
          ps.setString(29, get.getExoneradas().getCodigo());
          ps.setString(30, get.getExoneradas().getDescripcion());
        } else {
          ps.setNull(28, Types.DOUBLE);
          ps.setNull(29, Types.VARCHAR);
          ps.setNull(30, Types.VARCHAR);
        }

        if (get.getInafectas() != null) {
          ps.setDouble(31, get.getInafectas().getTotal());
          ps.setString(32, get.getInafectas().getCodigo());
          ps.setString(33, get.getInafectas().getDescripcion());
        } else {
          ps.setNull(31, Types.DOUBLE);
          ps.setNull(32, Types.VARCHAR);
          ps.setNull(33, Types.VARCHAR);
        }

        if (get.getGratuitas() != null) {
          ps.setDouble(34, get.getGratuitas().getTotal());
          ps.setString(35, get.getGratuitas().getCodigo());
          ps.setString(36, get.getGratuitas().getDescripcion());
        } else {
          ps.setNull(34, Types.DOUBLE);
          ps.setNull(35, Types.VARCHAR);
          ps.setNull(36, Types.VARCHAR);
        }

        if (get.getExportacion() != null) {
          ps.setDouble(37, get.getExportacion().getTotal());
          ps.setString(38, get.getExportacion().getCodigo());
          ps.setString(39, get.getExportacion().getDescripcion());
        } else {
          ps.setNull(37, Types.DOUBLE);
          ps.setNull(38, Types.VARCHAR);
          ps.setNull(39, Types.VARCHAR);
        }

        if (get.getOtrosCargos() != null) {
          ps.setBoolean(40, get.getOtrosCargos().isIndicador());
          ps.setDouble(41, get.getOtrosCargos().getTotal());
        } else {
          ps.setNull(40, Types.BOOLEAN);
          ps.setNull(41, Types.DOUBLE);
        }

        ps.setDouble(42, get.getIgv().getTotal());
        ps.setString(43, get.getIgv().getCodigo());
        ps.setString(44, get.getIgv().getDescripcion());
        ps.setString(45, get.getIgv().getCodigoInternacional());

        if (get.getIsc() != null) {
          ps.setDouble(46, get.getIsc().getTotal());
          ps.setString(47, get.getIsc().getCodigo());
          ps.setString(48, get.getIsc().getDescripcion());
          ps.setString(49, get.getIsc().getCodigoInternacional());
        } else {
          ps.setNull(46, Types.DOUBLE);
          ps.setNull(47, Types.VARCHAR);
          ps.setNull(48, Types.VARCHAR);
          ps.setNull(49, Types.VARCHAR);
        }

        if (get.getOtrosTributos() != null) {
          ps.setDouble(50, get.getOtrosTributos().getTotal());
          ps.setString(51, get.getOtrosTributos().getCodigo());
          ps.setString(52, get.getOtrosTributos().getDescripcion());
          ps.setString(53, get.getOtrosTributos().getCodigoInternacional());
        } else {
          ps.setNull(50, Types.DOUBLE);
          ps.setNull(51, Types.VARCHAR);
          ps.setNull(52, Types.VARCHAR);
          ps.setNull(53, Types.VARCHAR);
        }

        if (get.getImpuestoBolsa() != null) {
          ps.setDouble(54, get.getImpuestoBolsa().getTotal());
          ps.setString(55, get.getImpuestoBolsa().getCodigo());
          ps.setString(56, get.getImpuestoBolsa().getDescripcion());
          ps.setString(57, get.getImpuestoBolsa().getCodigoInternacional());
        } else {
          ps.setNull(54, Types.DOUBLE);
          ps.setNull(55, Types.VARCHAR);
          ps.setNull(56, Types.VARCHAR);
          ps.setNull(57, Types.VARCHAR);
        }

        ps.addBatch();
      }

      ps.executeBatch();
    }

    database.disconnect();
  }

  @Override
  public List<ResumenDiario> read(DateTime dateTime) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<ResumenDiarioDetalle> read(ResumenDiario resumenDiario) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void delete(int id) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
