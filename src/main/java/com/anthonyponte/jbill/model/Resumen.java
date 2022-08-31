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

package com.anthonyponte.jbill.model;

import java.util.Date;
import java.util.List;

/** @author AnthonyPonte */
public class Resumen extends Summary {
  private List<ResumenDetalle> resumenDiarioDetalles;

  public Resumen() {}

  public Resumen(
      int id,
      String ubl,
      String version,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      Date fechaEmision,
      Date fechaReferencia,
      Empresa emisor,
      String nombreZip,
      byte[] zip) {
    super(
        id,
        ubl,
        version,
        tipoDocumento,
        serie,
        correlativo,
        fechaEmision,
        fechaReferencia,
        emisor,
        nombreZip,
        zip);
  }

  public Resumen(
      String ubl,
      String version,
      Tipo tipoDocumento,
      String serie,
      int correlativo,
      Date fechaEmision,
      Date fechaReferencia,
      Empresa emisor,
      String nombreZip,
      byte[] zip) {
    super(
        ubl,
        version,
        tipoDocumento,
        serie,
        correlativo,
        fechaEmision,
        fechaReferencia,
        emisor,
        nombreZip,
        zip);
  }

  public List<ResumenDetalle> getResumenDiarioDetalles() {
    return resumenDiarioDetalles;
  }

  public void setResumenDiarioDetalles(List<ResumenDetalle> resumenDiarioDetalles) {
    this.resumenDiarioDetalles = resumenDiarioDetalles;
  }

  @Override
  public String toString() {
    return super.toString()
        + " ResumenDiario{"
        + "resumenDiarioDetalles="
        + resumenDiarioDetalles
        + '}';
  }
}
