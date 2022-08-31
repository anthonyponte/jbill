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

/** @author AnthonyPonte */
public class Impuesto {
  private double total;
  private String codigo;
  private String descripcion;
  private String codigoInternacional;

  public Impuesto() {}

  public Impuesto(double total, String codigo, String descripcion, String codigoInternacional) {
    this.total = total;
    this.codigo = codigo;
    this.descripcion = descripcion;
    this.codigoInternacional = codigoInternacional;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getCodigoInternacional() {
    return codigoInternacional;
  }

  public void setCodigoInternacional(String codigoInternacional) {
    this.codigoInternacional = codigoInternacional;
  }

  @Override
  public String toString() {
    return "Impuesto{"
        + "total="
        + total
        + ", codigo="
        + codigo
        + ", descripcion="
        + descripcion
        + ", codigoInternacional="
        + codigoInternacional
        + '}';
  }
}
