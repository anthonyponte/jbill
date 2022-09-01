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

/**
 * @author AnthonyPonte
 */
public class Impuesto extends Tipo {
  private double total;
  private String codigoInternacional;

  public Impuesto() {}

  public Impuesto(String codigo, String descripcion) {
    super(codigo, descripcion);
  }

  public Impuesto(double total, String codigoInternacional) {
    this.total = total;
    this.codigoInternacional = codigoInternacional;
  }

  public Impuesto(double total, String codigoInternacional, String codigo, String descripcion) {
    super(codigo, descripcion);
    this.total = total;
    this.codigoInternacional = codigoInternacional;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  public String getCodigoInternacional() {
    return codigoInternacional;
  }

  public void setCodigoInternacional(String codigoInternacional) {
    this.codigoInternacional = codigoInternacional;
  }

  @Override
  public String toString() {
    return "Impuesto{" + "total=" + total + ", codigoInternacional=" + codigoInternacional + '}';
  }
}
