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
public class Operacion extends Tipo {
  private double total;

  public Operacion() {}

  public Operacion(String codigo, String descripcion) {
    super(codigo, descripcion);
  }

  public Operacion(double total) {
    this.total = total;
  }

  public Operacion(double total, String codigo, String descripcion) {
    super(codigo, descripcion);
    this.total = total;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "Operacion{" + "total=" + total + '}';
  }
}
