/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.factory;

import com.anthonyponte.jbill.dao.SummaryDao;
import com.anthonyponte.jbill.idao.IComunicacionDao;
import com.anthonyponte.jbill.idao.IResumenDao;
import com.anthonyponte.jbill.idao.ISummaryDao;
import com.anthonyponte.jbill.model.Summary;
import java.sql.SQLException;
import com.anthonyponte.jbill.dao.ComunicacionDao;
import com.anthonyponte.jbill.dao.ResumenDao;

/** @author AnthonyPonte */
public class SummaryFactory {
  private final SummaryDao summaryDao;
  private final ComunicacionDao comunicacionBajaDao;
  private final ResumenDao resumenDiarioDao;

  public SummaryFactory() {
    summaryDao = new ISummaryDao();
    comunicacionBajaDao = new IComunicacionDao();
    resumenDiarioDao = new IResumenDao();
  }

  public void delete(Summary summary) throws SQLException {
    if (summary.getTipoDocumento().getCodigo().equals("RA")
        || summary.getTipoDocumento().getCodigo().equals("RR")) {
      comunicacionBajaDao.delete(summary.getId());
      summaryDao.delete(summary.getId());
    } else if (summary.getTipoDocumento().getCodigo().equals("RC")) {
      resumenDiarioDao.delete(summary.getId());
      summaryDao.delete(summary.getId());
    }
  }
}
