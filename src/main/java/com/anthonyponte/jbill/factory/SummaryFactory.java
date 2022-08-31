/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.factory;

import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.idao.IComunicacionDetalleDao;
import com.anthonyponte.jbill.idao.IResumenDetalleDao;
import com.anthonyponte.jbill.idao.ISummaryDao;
import com.anthonyponte.jbill.model.Summary;
import java.sql.SQLException;
import com.anthonyponte.jbill.dao.ResumenDetalleDao;
import com.anthonyponte.jbill.dao.ComunicacionDetalleDao;

/**
 * @author AnthonyPonte
 */
public class SummaryFactory {
  private final SummaryDao summaryDao;
  private final ComunicacionDetalleDao comunicacionDetalleDao;
  private final ResumenDetalleDao resumenDetalleDao;

  public SummaryFactory() {
    summaryDao = new ISummaryDao();
    comunicacionDetalleDao = new IComunicacionDetalleDao();
    resumenDetalleDao = new IResumenDetalleDao();
  }

  public void delete(Summary summary) throws SQLException {
    if (summary.getTipo().getCodigo().equals("RA") || summary.getTipo().getCodigo().equals("RR")) {
      comunicacionDetalleDao.delete(summary.getId());
      summaryDao.delete(summary.getId());
    } else if (summary.getTipo().getCodigo().equals("RC")) {
      resumenDetalleDao.delete(summary.getId());
      summaryDao.delete(summary.getId());
    }
  }
}
