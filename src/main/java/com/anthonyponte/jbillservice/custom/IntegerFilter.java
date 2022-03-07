/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.custom;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** @author anthony */
public class IntegerFilter extends DocumentFilter {
  private final Pattern regexCheck = Pattern.compile("[0-9]+");

  @Override
  public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
      throws BadLocationException {
    if (string == null) {
      return;
    }

    if (regexCheck.matcher(string).matches() && (string.length()) <= 11) {
      super.insertString(fb, offset, string, attr);
    }
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
      throws BadLocationException {
    if (text == null) {
      return;
    }

    if (regexCheck.matcher(text).matches()) {
      fb.replace(offset, length, text, attrs);
    }
  }
}
