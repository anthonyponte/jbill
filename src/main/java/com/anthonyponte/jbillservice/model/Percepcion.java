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

package com.anthonyponte.jbillservice.model;

/** @author AnthonyPonte */
public class Percepcion {
  private String codigo;
  private String descripcion;
  private double tasa;
  private double monto;
  private double montoTotal;
  private double base;

  public Percepcion() {}

  public Percepcion(
      String codigo,
      String descripcion,
      double tasa,
      double monto,
      double montoTotal,
      double base) {
    this.codigo = codigo;
    this.descripcion = descripcion;
    this.tasa = tasa;
    this.monto = monto;
    this.montoTotal = montoTotal;
    this.base = base;
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

  public double getTasa() {
    return tasa;
  }

  public void setTasa(double tasa) {
    this.tasa = tasa;
  }

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public double getMontoTotal() {
    return montoTotal;
  }

  public void setMontoTotal(double montoTotal) {
    this.montoTotal = montoTotal;
  }

  public double getBase() {
    return base;
  }

  public void setBase(double base) {
    this.base = base;
  }

  @Override
  public String toString() {
    return "Percepcion{"
        + "codigo="
        + codigo
        + ", descripcion="
        + descripcion
        + ", tasa="
        + tasa
        + ", monto="
        + monto
        + ", montoTotal="
        + montoTotal
        + ", base="
        + base
        + '}';
  }
}
