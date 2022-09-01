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

package com.anthonyponte.jbill.model;

/**
 * @author anthony
 */
public class RegimenPercepcion extends Tipo {
  private double porcentaje;

  public RegimenPercepcion() {}

  public RegimenPercepcion(String codigo, String descripcion) {
    super(codigo, descripcion);
  }

  public RegimenPercepcion(double porcentaje) {
    this.porcentaje = porcentaje;
  }

  public RegimenPercepcion(double porcentaje, String codigo, String descripcion) {
    super(codigo, descripcion);
    this.porcentaje = porcentaje;
  }

  public double getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(double porcentaje) {
    this.porcentaje = porcentaje;
  }

  @Override
  public String toString() {
    return "RegimenPercepcion{" + "porcentaje=" + porcentaje + '}';
  }
}
