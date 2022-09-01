/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.anthonyponte.jbill.dao;

import com.anthonyponte.jbill.model.ComunicacionDetalle;
import com.anthonyponte.jbill.model.Summary;
import java.sql.SQLException;
import java.util.List;

/**
 * @author anthony
 */
public interface ComunicacionDetalleDao {
  public void create(int id, List<ComunicacionDetalle> detalles) throws SQLException;

  public List<ComunicacionDetalle> read(Summary summary) throws SQLException;

  public void delete(int id) throws SQLException;
}
