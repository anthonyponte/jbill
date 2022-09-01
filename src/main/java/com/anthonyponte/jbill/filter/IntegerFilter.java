/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbill.filter;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** @author anthony */
public class IntegerFilter extends DocumentFilter {
  private final Pattern regexCheck = Pattern.compile("[0-9]+");

  private final int maxLength;

  public IntegerFilter(int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
      throws BadLocationException {
    if (fb.getDocument().getLength() + string.length() <= maxLength) {
      if (regexCheck.matcher(string).matches()) {
        super.insertString(fb, offset, string, attr);
      }
    }
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
      throws BadLocationException {
    int documentLength = fb.getDocument().getLength();
    if (documentLength - length + text.length() <= maxLength) {
      if (regexCheck.matcher(text).matches()) {
        super.replace(fb, offset, length, text.toUpperCase(), attrs);
      }
    }
  }
}
