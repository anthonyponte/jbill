/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.anthonyponte.jbill.dao;

import com.anthonyponte.jbill.model.Comunicacion;
import com.anthonyponte.jbill.model.ComunicacionDetalle;
import java.sql.SQLException;
import java.util.List;
import org.joda.time.DateTime;

/**
 * @author anthony
 */
public interface ComunicacionDao {
  public void create(int id, List<ComunicacionDetalle> comunicacionBajaDetalles)
      throws SQLException;

  public List<Comunicacion> read(DateTime dateTime) throws SQLException;

  public List<ComunicacionDetalle> read(Comunicacion comunicacionBaja) throws SQLException;

  public void delete(int id) throws SQLException;
}
