/*
 * Copyright (C) 2022 anthony
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

package com.anthonyponte.jbillservice.model;

/** @author anthony */
public class TipoDocumento {
  private String codigo;
  private String descripcion;

  public TipoDocumento() {}

  public TipoDocumento(String codigo, String descripcion) {
    this.codigo = codigo;
    this.descripcion = descripcion;
  }

  public String getCodigo() {
    switch (descripcion) {
      case "Factura":
        return codigo = "01";
      case "Nota de crédito":
        return codigo = "07";
      case "Nota de débito":
        return codigo = "08";
      case "Comprobante de retención":
        return codigo = "20";
      case "Comunicación de baja":
        return codigo = "RA";
      case "Resumen de reversiones":
        return codigo = "RR";
      case "Resumen diario":
        return codigo = "RC";
      default:
        return codigo;
    }
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDescripcion() {
    switch (codigo) {
      case "RA":
        return descripcion = "Comunicación de baja";
      case "RR":
        return descripcion = "Resumen de reversiones";
      case "RC":
        return descripcion = "Resumen diario";
      default:
        return descripcion;
    }
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  @Override
  public String toString() {
    return "TipoDocumento{" + "codigo=" + codigo + ", descripcion=" + descripcion + '}';
  }
}
