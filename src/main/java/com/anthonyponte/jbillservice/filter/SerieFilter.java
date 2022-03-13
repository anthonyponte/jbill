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

package com.anthonyponte.jbillservice.filter;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** @author anthony */
public class SerieFilter extends DocumentFilter {
  private final Pattern regexCheck = Pattern.compile("[A-Za-z0-9]+");

  @Override
  public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
      throws BadLocationException {
    if (fb.getDocument().getLength() + string.length() <= 4) {
      if (regexCheck.matcher(string).matches()) {
        super.insertString(fb, offset, string.toUpperCase(), attr);
      }
    }
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
      throws BadLocationException {
    int documentLength = fb.getDocument().getLength();
    if (documentLength - length + text.length() <= 4) {
      if (regexCheck.matcher(text).matches()) {
        super.replace(fb, offset, length, text.toUpperCase(), attrs);
      }
    }
  }
}
