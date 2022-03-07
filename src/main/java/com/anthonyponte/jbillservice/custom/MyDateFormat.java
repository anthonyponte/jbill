/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anthonyponte.jbillservice.custom;

import java.text.SimpleDateFormat;
import java.util.Date;

/** @author AnthonyPonte */
public class MyDateFormat {

  private static final SimpleDateFormat d_MMMM_Y = new SimpleDateFormat("d MMMM y");
  private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

  public static String d_MMMM_Y(Date fecha) {
    return d_MMMM_Y.format(fecha);
  }

  public static String yyyyMMdd(Date fecha) {
    return yyyyMMdd.format(fecha);
  }
  
  
  public static String yyyy_MM_dd(Date fecha) {
    return yyyy_MM_dd.format(fecha);
  }
}
