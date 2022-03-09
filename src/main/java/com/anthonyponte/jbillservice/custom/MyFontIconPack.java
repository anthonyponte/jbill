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

package com.anthonyponte.jbillservice.custom;

import java.awt.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.swing.FontIcon;

/** @author AnthonyPonte */
public class MyFontIconPack {

  private int size = 16;
  private Color color = new Color(255, 255, 255);

  public MyFontIconPack() {}

  public MyFontIconPack(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public FontIcon getIcon(Ikon ikon) {
    FontIcon icon = FontIcon.of(ikon, size, color);
    return icon;
  }
}
