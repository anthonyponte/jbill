/*
 * Copyright (C) 2022 AnthonyPonte
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
import com.anthonyponte.jbill.model.ComunicacionDetalle;

/** @author AnthonyPonte */
public class ComunicacionDetalleTableFormat implements TableFormat<ComunicacionDetalle> {

  @Override
  public int getColumnCount() {
    return 4;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return "Tipo";
      case 1:
        return "Serie";
      case 2:
        return "Numero";
      case 3:
        return "Motivo";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }

  @Override
  public Object getColumnValue(ComunicacionDetalle baseObject, int column) {
    switch (column) {
      case 0:
        return baseObject.getDocumento().getTipo().getDescripcion();
      case 1:
        return baseObject.getDocumento().getSerie();
      case 2:
        return baseObject.getDocumento().getCorrelativo();
      case 3:
        return baseObject.getMotivo();
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }
}
