package com.chaosdev.ngpad.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
  public static String capitilizeStr(String data) {

    String pattern = "(?m)^([a-z])(.*)$";
    Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(data);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, m.group(1).toUpperCase() + m.group(2));
    }
    m.appendTail(sb);
    String capitalizedData = sb.toString();
    System.out.println(capitalizedData);

    return capitalizedData;
  }
}
