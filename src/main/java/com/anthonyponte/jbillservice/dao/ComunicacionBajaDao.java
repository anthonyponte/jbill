/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.anthonyponte.jbillservice.dao;

import com.anthonyponte.jbillservice.model.ComunicacionBaja;
import com.anthonyponte.jbillservice.model.ComunicacionBajaDetalle;
import java.util.List;
import org.joda.time.DateTime;

/** @author anthony */
public interface ComunicacionBajaDao {
  public void create(int id, List<ComunicacionBajaDetalle> comunicacionBajaDetalles);

  public List<ComunicacionBaja> read(DateTime dateTime);

  public List<ComunicacionBajaDetalle> read(ComunicacionBaja comunicacionBaja);

  public void delete(int id);
}
