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

package com.anthonyponte.jbillservice.tableformat;

import ca.odell.glazedlists.gui.TableFormat;
import com.anthonyponte.jbillservice.model.ResumenDiarioDetalle;

/** @author AnthonyPonte */
public class ResumenDiarioDetalleTableFormat implements TableFormat<ResumenDiarioDetalle> {

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
  public Object getColumnValue(ResumenDiarioDetalle detalle, int column) {
    switch (column) {
      case 0:
        return detalle.getDocumento().getSerie();
      case 1:
        return detalle.getDocumento().getCorrelativo();
      case 2:
        return detalle.getDocumento().getTipoDocumento().getDescripcion();
      case 3:
        if (detalle.getAdquiriente() != null)
          return detalle.getAdquiriente().getNumeroDocumentoIdentidad();
        else return "";
      case 4:
        if (detalle.getDocumentoReferencia() != null)
          return detalle.getDocumentoReferencia().getSerie();
        else return "";
      case 5:
        if (detalle.getDocumentoReferencia() != null)
          return detalle.getDocumentoReferencia().getCorrelativo();
        else return "";
      case 6:
        if (detalle.getDocumentoReferencia() != null)
          return detalle.getDocumentoReferencia().getTipoDocumento().getDescripcion();
        else return "";
      case 7:
        if (detalle.getPercepcion() != null)
          return detalle.getPercepcion().getRegimenPercepcion().getDescripcion();
        else return "";
      case 8:
        if (detalle.getPercepcion() != null)
          return detalle.getPercepcion().getRegimenPercepcion().getPorcentaje();
        else return "";
      case 9:
        if (detalle.getPercepcion() != null) return detalle.getPercepcion().getMonto();
        else return "";
      case 10:
        if (detalle.getPercepcion() != null) return detalle.getPercepcion().getMontoTotal();
        else return "";
      case 11:
        return detalle.getEstado().getDescripcion();
      case 12:
        return detalle.getImporteTotal();
      case 13:
        return detalle.getMoneda().getDescripcion();
      case 14:
        if (detalle.getGravadas() != null) return detalle.getGravadas().getTotal();
        else return "";
      case 15:
        if (detalle.getExoneradas() != null) return detalle.getExoneradas().getTotal();
        else return "";
      case 16:
        if (detalle.getInafectas() != null) return detalle.getInafectas().getTotal();
        else return "";
      case 17:
        if (detalle.getGratuitas() != null) return detalle.getGratuitas().getTotal();
        else return "";
      case 18:
        if (detalle.getExportacion() != null) return detalle.getExportacion().getTotal();
        else return "";
      case 19:
        if (detalle.getOtrosCargos() != null) return detalle.getOtrosCargos().getTotal();
        else return "";
      case 20:
        return detalle.getIgv().getTotal();
      case 21:
        if (detalle.getIsc() != null) return detalle.getIsc().getTotal();
        else return "";
      case 22:
        if (detalle.getOtrosTributos() != null) return detalle.getOtrosTributos().getTotal();
        else return "";
      case 23:
        if (detalle.getImpuestoBolsa() != null) return detalle.getImpuestoBolsa().getTotal();
        else return "";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }
}
