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
public class OtrosCargos {
  private boolean indicador;
  private double total;

  public OtrosCargos() {}

  public OtrosCargos(boolean indicador, double total) {
    this.indicador = indicador;
    this.total = total;
  }

  public boolean isIndicador() {
    return indicador;
  }

  public void setIndicador(boolean indicador) {
    this.indicador = indicador;
  }

  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "OtrosCargos{" + "indicador=" + indicador + ", total=" + total + '}';
  }
}
