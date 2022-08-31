/*
 * Copyright (C) 2022 Anthony Ponte
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

package com.anthonyponte.jbill.tableformat;

import ca.odell.glazedlists.gui.TableFormat;
import com.anthonyponte.jbill.custom.MyDateFormat;
import com.anthonyponte.jbill.model.Summary;

/**
 * @author Anthony Ponte
 */
public class SummaryTableFormat implements TableFormat<Summary> {

  @Override
  public int getColumnCount() {
    return 12;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return "Tipo Codigo";
      case 1:
        return "Tipo Descripcion";
      case 2:
        return "Serie";
      case 3:
        return "Correlativo";
      case 4:
        return "Fecha Emision";
      case 5:
        return "Fecha Referencia";
      case 6:
        return "RUC";
      case 7:
        return "Razon Social";
      case 8:
        return "Zip";
      case 9:
        return "Ticket";
      case 10:
        return "Status Code";
      case 11:
        return "CDR";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }

  @Override
  public Object getColumnValue(Summary e, int i) {
    switch (i) {
      case 0:
        return e.getTipoDocumento().getCodigo();
      case 1:
        return e.getTipoDocumento().getDescripcion();
      case 2:
        return e.getSerie();
      case 3:
        return String.valueOf(e.getCorrelativo());
      case 4:
        return MyDateFormat.d_MMMM_Y(e.getFechaEmision());
      case 5:
        return MyDateFormat.d_MMMM_Y(e.getFechaReferencia());
      case 6:
        return e.getEmisor().getNumero();
      case 7:
        return e.getEmisor().getNombre();
      case 8:
        return e.getNombreZip();
      case 9:
        return e.getTicket();
      case 10:
        return e.getStatusCode();
      case 11:
        return e.getNombreContent();
    }
    throw new IllegalStateException("Unexpected column: " + i);
  }
}
