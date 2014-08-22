package org.snu.ids.ha.util;

public class Util
{
  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public static boolean valid(String str)
  {
    if ((str == null) || (str.trim().equals(""))) return false;
    return true;
  }

  public static int getTabCnt(String str)
  {
    int cnt = 0;

    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if ((ch == ' ') || (ch == '\t')) cnt++;
    }
    return cnt;
  }

  private static String getTab(int cnt)
  {
    String tab = "";
    for (int i = 0; i < cnt; i++)
      tab = tab + "\t";
    return tab;
  }

  public static String getTabbedString(String string, int tabSize, int width)
  {
    int cnt = 0;
    if (string == null) {
      return getTab((width - cnt) / tabSize);
    }

    int i = 0; for (int len = string.length(); i < len; i++) {
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(string.charAt(i));
      if ((ub == Character.UnicodeBlock.HANGUL_SYLLABLES) || (ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO))
        cnt += 2;
      else {
        cnt++;
      }
    }

    String ret = string + getTab((width - cnt) / tabSize);
    if (cnt % tabSize != 0) ret = ret + "\t";
    return ret;
  }

  public static final String rplcXMLSpclChar(String src)
  {
    return src.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\\"", "&quot;").replaceAll("'", "&apos;");
  }
}