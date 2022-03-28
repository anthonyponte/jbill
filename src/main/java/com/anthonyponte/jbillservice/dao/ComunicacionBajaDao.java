/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.anthonyponte.jbillservice.dao;

import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import java.sql.SQLException;
import java.util.List;
import org.joda.time.DateTime;

/** @author anthony */
public interface ComunicacionBajaDao {
  public void create(int id, List<ComunicacionBajaDetalle> comunicacionBajaDetalles)
      throws SQLException;

  public List<ComunicacionBaja> read(DateTime dateTime) throws SQLException;

  public List<ComunicacionBajaDetalle> read(ComunicacionBaja comunicacionBaja) throws SQLException;

  public void delete(int id) throws SQLException;
}
