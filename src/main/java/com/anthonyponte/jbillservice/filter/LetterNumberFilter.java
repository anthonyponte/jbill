/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.filter;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** @author AnthonyPonte */
public class LetterNumberFilter extends DocumentFilter {
  private final Pattern regexCheck = Pattern.compile("[a-zA-Z0-9 ]+");

  @Override
  public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
      throws BadLocationException {
    if (regexCheck.matcher(string).matches()) super.insertString(fb, offset, string, attr);
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
      throws BadLocationException {
    if (regexCheck.matcher(text).matches()) super.replace(fb, offset, length, text, attrs);
  }
}
