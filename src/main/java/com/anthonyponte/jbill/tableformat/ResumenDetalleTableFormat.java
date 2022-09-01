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
import com.anthonyponte.jbill.model.ResumenDetalle;

/**
 * @author AnthonyPonte
 */
public class ResumenDetalleTableFormat implements TableFormat<ResumenDetalle> {

  @Override
  public int getColumnCount() {
    return 24;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return "Serie";
      case 1:
        return "Correlativo";
      case 2:
        return "Tipo";
      case 3:
        return "Adquiriente";
      case 4:
        return "Referencia Serie";
      case 5:
        return "Referencia Correlativo";
      case 6:
        return "Referencia Tipo";
      case 7:
        return "Percepcion Descripcion";
      case 8:
        return "Percepcion Tasa";
      case 9:
        return "Percepcion Monto";
      case 10:
        return "Percepcion Monto Totsl";
      case 11:
        return "Estado";
      case 12:
        return "Importe Total";
      case 13:
        return "Moneda";
      case 14:
        return "Gravadas";
      case 15:
        return "Exoneradas";
      case 16:
        return "Inafectas";
      case 17:
        return "Gratuitas";
      case 18:
        return "Exportacion";
      case 19:
        return "Otros Cargos";
      case 20:
        return "IGV";
      case 21:
        return "ISC";
      case 22:
        return "Otros Tributos";
      case 23:
        return "Bolsas";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }

  @Override
  public Object getColumnValue(ResumenDetalle baseObject, int column) {
    switch (column) {
      case 0:
        return baseObject.getSerie();
      case 1:
        return baseObject.getCorrelativo();
      case 2:
        return baseObject.getTipoDocumento().getDescripcion();
      case 3:
        if (baseObject.getAdquiriente() != null) return baseObject.getAdquiriente().getNumero();
        else return "";
      case 4:
        if (baseObject.getSerieReferencia() != null) return baseObject.getSerieReferencia();
        else return "";
      case 5:
        if (baseObject.getCorrelativoReferencia() > 0) return baseObject.getCorrelativoReferencia();
        else return "";
      case 6:
        if (baseObject.getTipoDocumentoReferencia() != null)
          return baseObject.getTipoDocumentoReferencia().getDescripcion();
        else return "";
      case 7:
        if (baseObject.getPercepcion() != null)
          return baseObject.getPercepcion().getRegimenPercepcion().getDescripcion();
        else return "";
      case 8:
        if (baseObject.getPercepcion() != null)
          return baseObject.getPercepcion().getRegimenPercepcion().getPorcentaje();
        else return "";
      case 9:
        if (baseObject.getPercepcion() != null) return baseObject.getPercepcion().getMonto();
        else return "";
      case 10:
        if (baseObject.getPercepcion() != null) return baseObject.getPercepcion().getMontoTotal();
        else return "";
      case 11:
        return baseObject.getEstado().getDescripcion();
      case 12:
        return baseObject.getImporteTotal();
      case 13:
        return baseObject.getMoneda().getDescripcion();
      case 14:
        if (baseObject.getGravadas() != null) return baseObject.getGravadas().getTotal();
        else return "";
      case 15:
        if (baseObject.getExoneradas() != null) return baseObject.getExoneradas().getTotal();
        else return "";
      case 16:
        if (baseObject.getInafectas() != null) return baseObject.getInafectas().getTotal();
        else return "";
      case 17:
        if (baseObject.getGratuitas() != null) return baseObject.getGratuitas().getTotal();
        else return "";
      case 18:
        if (baseObject.getExportacion() != null) return baseObject.getExportacion().getTotal();
        else return "";
      case 19:
        if (baseObject.getOtrosCargos() != null) return baseObject.getOtrosCargos().getTotal();
        else return "";
      case 20:
        return baseObject.getIgv().getTotal();
      case 21:
        if (baseObject.getIsc() != null) return baseObject.getIsc().getTotal();
        else return "";
      case 22:
        if (baseObject.getOtrosTributos() != null) return baseObject.getOtrosTributos().getTotal();
        else return "";
      case 23:
        if (baseObject.getImpuestoBolsa() != null) return baseObject.getImpuestoBolsa().getTotal();
        else return "";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }
}
