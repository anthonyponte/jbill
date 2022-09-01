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

package com.anthonyponte.jbill.dao;

import com.anthonyponte.jbill.model.ResumenDetalle;
import com.anthonyponte.jbill.model.Summary;
import java.sql.SQLException;
import java.util.List;

/**
 * @author anthony
 */
public interface ResumenDetalleDao {
  public void create(int id, List<ResumenDetalle> detalles) throws SQLException;

  public List<ResumenDetalle> read(Summary summary) throws SQLException;

  public void delete(int id) throws SQLException;
}
