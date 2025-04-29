package com.chaosdev.ngpad.utils;

import android.os.Build;
import android.text.Html;
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
    
    public static String escapSpacialCharacter(String input) {
        return
          Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
              ? Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY).toString()
              : Html.fromHtml(input).toString();
    }
}
