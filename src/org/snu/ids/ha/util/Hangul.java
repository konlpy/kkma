package org.snu.ids.ha.util;

public class Hangul
{
  public static final StringSet MO_POSITIVE_SET = new StringSet(new String[] { "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅗ", "ㅛ", "ㅘ" });

  public static final StringSet MO_NEGATIVE_SET = new StringSet(new String[] { "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅜ", "ㅠ", "ㅝ", 
    "ㅞ", "ㅟ", "ㅚ", "ㅙ" });

  public static final StringSet MO_NEUTRIAL_SET = new StringSet(new String[] { "ㅡ", "ㅣ", "ㅢ" });

  public static final StringSet MO_DOUBLE_SET = new StringSet(new String[] { "ㅘ", "ㅝ", "ㅞ", "ㅟ", "ㅚ", "ㅙ" });

  public char cho = '\000';
  public char jung = '\000';
  public char jong = '\000';

  public String toString()
  {
    return "(" + this.cho + "," + this.jung + "," + this.jong + ")";
  }

  private String get()
  {
    String ret = this.cho;
    ret = ret + (this.jung == 0 ? "" : new StringBuilder(String.valueOf(this.jung)).toString());

    switch (this.jong) {
    case '\000':
      ret = ret;
      break;
    case 'ㄳ':
      ret = ret + "ㄱㅅ";
      break;
    case 'ㄵ':
      ret = ret + "ㄴㅋ";
      break;
    case 'ㄶ':
      ret = ret + "ㄴㅎ";
      break;
    case 'ㄺ':
      ret = ret + "ㄹㄱ";
      break;
    case 'ㄻ':
      ret = ret + "ㄹㅁ";
      break;
    case 'ㄼ':
      ret = ret + "ㄹㅂ";
      break;
    case 'ㄽ':
      ret = ret + "ㄹㅅ";
      break;
    case 'ㄾ':
      ret = ret + "ㄹㅌ";
      break;
    case 'ㄿ':
      ret = ret + "ㄹㅍ";
      break;
    case 'ㅀ':
      ret = ret + "ㄹㅎ";
      break;
    case 'ㅄ':
      ret = ret + "ㅂㅅ";
      break;
    default:
      ret = ret + this.jong;
    }

    return ret;
  }

  public boolean hasCho()
  {
    return this.cho != 0;
  }

  public boolean hasJung()
  {
    return this.jung != 0;
  }

  public boolean hasJong()
  {
    return this.jong != 0;
  }

  protected static final char getCho(int idx)
  {
    char ret = '\000';
    switch (idx) {
    case 0:
      ret = 'ㄱ';
      break;
    case 1:
      ret = 'ㄲ';
      break;
    case 2:
      ret = 'ㄴ';
      break;
    case 3:
      ret = 'ㄷ';
      break;
    case 4:
      ret = 'ㄸ';
      break;
    case 5:
      ret = 'ㄹ';
      break;
    case 6:
      ret = 'ㅁ';
      break;
    case 7:
      ret = 'ㅂ';
      break;
    case 8:
      ret = 'ㅃ';
      break;
    case 9:
      ret = 'ㅅ';
      break;
    case 10:
      ret = 'ㅆ';
      break;
    case 11:
      ret = 'ㅇ';
      break;
    case 12:
      ret = 'ㅈ';
      break;
    case 13:
      ret = 'ㅉ';
      break;
    case 14:
      ret = 'ㅊ';
      break;
    case 15:
      ret = 'ㅋ';
      break;
    case 16:
      ret = 'ㅌ';
      break;
    case 17:
      ret = 'ㅍ';
      break;
    case 18:
      ret = 'ㅎ';
    }

    return ret;
  }

  protected static final int getChoIdx(char ch)
  {
    int ret = -1;
    switch (ch) {
    case 'ㄱ':
      ret = 0;
      break;
    case 'ㄲ':
      ret = 1;
      break;
    case 'ㄴ':
      ret = 2;
      break;
    case 'ㄷ':
      ret = 3;
      break;
    case 'ㄸ':
      ret = 4;
      break;
    case 'ㄹ':
      ret = 5;
      break;
    case 'ㅁ':
      ret = 6;
      break;
    case 'ㅂ':
      ret = 7;
      break;
    case 'ㅃ':
      ret = 8;
      break;
    case 'ㅅ':
      ret = 9;
      break;
    case 'ㅆ':
      ret = 10;
      break;
    case 'ㅇ':
      ret = 11;
      break;
    case 'ㅈ':
      ret = 12;
      break;
    case 'ㅉ':
      ret = 13;
      break;
    case 'ㅊ':
      ret = 14;
      break;
    case 'ㅋ':
      ret = 15;
      break;
    case 'ㅌ':
      ret = 16;
      break;
    case 'ㅍ':
      ret = 17;
      break;
    case 'ㅎ':
      ret = 18;
    case 'ㄳ':
    case 'ㄵ':
    case 'ㄶ':
    case 'ㄺ':
    case 'ㄻ':
    case 'ㄼ':
    case 'ㄽ':
    case 'ㄾ':
    case 'ㄿ':
    case 'ㅀ':
    case 'ㅄ': } return ret;
  }

  protected static final char getJung(int idx)
  {
    char ret = '\000';
    switch (idx) {
    case 0:
      ret = 'ㅏ';
      break;
    case 1:
      ret = 'ㅐ';
      break;
    case 2:
      ret = 'ㅑ';
      break;
    case 3:
      ret = 'ㅒ';
      break;
    case 4:
      ret = 'ㅓ';
      break;
    case 5:
      ret = 'ㅔ';
      break;
    case 6:
      ret = 'ㅕ';
      break;
    case 7:
      ret = 'ㅖ';
      break;
    case 8:
      ret = 'ㅗ';
      break;
    case 9:
      ret = 'ㅘ';
      break;
    case 10:
      ret = 'ㅙ';
      break;
    case 11:
      ret = 'ㅚ';
      break;
    case 12:
      ret = 'ㅛ';
      break;
    case 13:
      ret = 'ㅜ';
      break;
    case 14:
      ret = 'ㅝ';
      break;
    case 15:
      ret = 'ㅞ';
      break;
    case 16:
      ret = 'ㅟ';
      break;
    case 17:
      ret = 'ㅠ';
      break;
    case 18:
      ret = 'ㅡ';
      break;
    case 19:
      ret = 'ㅢ';
      break;
    case 20:
      ret = 'ㅣ';
    }

    return ret;
  }

  protected static final int getJungIdx(char ch)
  {
    int ret = -1;
    switch (ch) {
    case 'ㅏ':
      ret = 0;
      break;
    case 'ㅐ':
      ret = 1;
      break;
    case 'ㅑ':
      ret = 2;
      break;
    case 'ㅒ':
      ret = 3;
      break;
    case 'ㅓ':
      ret = 4;
      break;
    case 'ㅔ':
      ret = 5;
      break;
    case 'ㅕ':
      ret = 6;
      break;
    case 'ㅖ':
      ret = 7;
      break;
    case 'ㅗ':
      ret = 8;
      break;
    case 'ㅘ':
      ret = 9;
      break;
    case 'ㅙ':
      ret = 10;
      break;
    case 'ㅚ':
      ret = 11;
      break;
    case 'ㅛ':
      ret = 12;
      break;
    case 'ㅜ':
      ret = 13;
      break;
    case 'ㅝ':
      ret = 14;
      break;
    case 'ㅞ':
      ret = 15;
      break;
    case 'ㅟ':
      ret = 16;
      break;
    case 'ㅠ':
      ret = 17;
      break;
    case 'ㅡ':
      ret = 18;
      break;
    case 'ㅢ':
      ret = 19;
      break;
    case 'ㅣ':
      ret = 20;
    }

    return ret;
  }

  protected static final char getJong(int idx)
  {
    char ret = '\000';
    switch (idx) {
    case 0:
      ret = '\000';
      break;
    case 1:
      ret = 'ㄱ';
      break;
    case 2:
      ret = 'ㄲ';
      break;
    case 3:
      ret = 'ㄳ';
      break;
    case 4:
      ret = 'ㄴ';
      break;
    case 5:
      ret = 'ㄵ';
      break;
    case 6:
      ret = 'ㄶ';
      break;
    case 7:
      ret = 'ㄷ';
      break;
    case 8:
      ret = 'ㄹ';
      break;
    case 9:
      ret = 'ㄺ';
      break;
    case 10:
      ret = 'ㄻ';
      break;
    case 11:
      ret = 'ㄼ';
      break;
    case 12:
      ret = 'ㄽ';
      break;
    case 13:
      ret = 'ㄾ';
      break;
    case 14:
      ret = 'ㄿ';
      break;
    case 15:
      ret = 'ㅀ';
      break;
    case 16:
      ret = 'ㅁ';
      break;
    case 17:
      ret = 'ㅂ';
      break;
    case 18:
      ret = 'ㅄ';
      break;
    case 19:
      ret = 'ㅅ';
      break;
    case 20:
      ret = 'ㅆ';
      break;
    case 21:
      ret = 'ㅇ';
      break;
    case 22:
      ret = 'ㅈ';
      break;
    case 23:
      ret = 'ㅊ';
      break;
    case 24:
      ret = 'ㅋ';
      break;
    case 25:
      ret = 'ㅌ';
      break;
    case 26:
      ret = 'ㅍ';
      break;
    case 27:
      ret = 'ㅎ';
    }

    return ret;
  }

  protected static final int getJongIdx(char ch)
  {
    int ret = -1;
    switch (ch) {
    case '\000':
      ret = 0;
      break;
    case ' ':
      ret = 0;
      break;
    case 'ㄱ':
      ret = 1;
      break;
    case 'ㄲ':
      ret = 2;
      break;
    case 'ㄳ':
      ret = 3;
      break;
    case 'ㄴ':
      ret = 4;
      break;
    case 'ㄵ':
      ret = 5;
      break;
    case 'ㄶ':
      ret = 6;
      break;
    case 'ㄷ':
      ret = 7;
      break;
    case 'ㄹ':
      ret = 8;
      break;
    case 'ㄺ':
      ret = 9;
      break;
    case 'ㄻ':
      ret = 10;
      break;
    case 'ㄼ':
      ret = 11;
      break;
    case 'ㄽ':
      ret = 12;
      break;
    case 'ㄾ':
      ret = 13;
      break;
    case 'ㄿ':
      ret = 14;
      break;
    case 'ㅀ':
      ret = 15;
      break;
    case 'ㅁ':
      ret = 16;
      break;
    case 'ㅂ':
      ret = 17;
      break;
    case 'ㅄ':
      ret = 18;
      break;
    case 'ㅅ':
      ret = 19;
      break;
    case 'ㅆ':
      ret = 20;
      break;
    case 'ㅇ':
      ret = 21;
      break;
    case 'ㅈ':
      ret = 22;
      break;
    case 'ㅊ':
      ret = 23;
      break;
    case 'ㅋ':
      ret = 24;
      break;
    case 'ㅌ':
      ret = 25;
      break;
    case 'ㅍ':
      ret = 26;
      break;
    case 'ㅎ':
      ret = 27;
    }

    return ret;
  }

  public static Hangul split(char ch)
  {
    Hangul hangul = new Hangul();
    int x = ch & 0xFFFF; int y = 0; int z = 0;
    if ((x >= 44032) && (x <= 55203)) {
      y = x - 44032;
      z = y % 588;
      hangul.cho = getCho(y / 588);
      hangul.jung = getJung(z / 28);
      hangul.jong = getJong(z % 28);
    } else if ((x >= 12593) && (x <= 12643)) {
      if (getChoIdx(ch) > -1)
        hangul.cho = ch;
      else if (getJungIdx(ch) > -1)
        hangul.jung = ch;
      else if (getJongIdx(ch) > -1)
        hangul.jong = ch;
    }
    else {
      hangul.cho = ch;
    }
    return hangul;
  }

  public static String split(String string)
  {
    if (string == null) return null;

    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = string.length(); i < stop; i++) {
      sb.append(split(string.charAt(i)));
    }
    return sb.toString();
  }

  public static char combine(char cho, char jung, char jong)
  {
    return (char)(getChoIdx(cho) * 21 * 28 + getJungIdx(jung) * 28 + getJongIdx(jong) + 44032);
  }

  public static String append(String head, String tail)
  {
    String ret = null;

    Hangul headTail = split(head.charAt(head.length() - 1));
    Hangul tailHead = split(tail.charAt(0));

    if ((tailHead.hasJung()) || (headTail.hasJong())) {
      ret = head + tail;
    } else {
      String headHead = head.substring(0, head.length() - 1);
      String tailTail = tail.substring(1);
      ret = headHead + combine(headTail.cho, headTail.jung, tailHead.cho) + tailTail;
    }
    return ret;
  }

  public static boolean hasJong(char ch)
  {
    return split(ch).hasJong();
  }

  public static boolean hasJong(String string)
  {
    if (!Util.valid(string)) return false;
    return hasJong(string.charAt(string.length() - 1));
  }

  public static String split2(String string)
  {
    if (string == null) return null;
    String ret = "";
    int i = 0; for (int stop = string.length(); i < stop; i++) {
      ret = ret + split(string.charAt(i)).get() + ":";
    }
    return ret;
  }

  public static boolean endsWith(String string, String pattern)
  {
    if ((!Util.valid(string)) || (!Util.valid(pattern))) return false;
    int slen = string.length(); int plen = pattern.length();
    if (slen < plen) return false;
    char sch = '\000'; char pch = '\000';
    for (int i = 0; i < plen; i++) {
      sch = string.charAt(slen - i - 1);
      pch = pattern.charAt(plen - i - 1);
      if (pch != sch) {
        if (i == plen - 1) return endsWith2(sch, pch);
        return false;
      }
    }
    return true;
  }

  public static boolean endsWith(char sch, char pch)
  {
    if (sch == pch) return true;
    return endsWith2(sch, pch);
  }

  private static boolean endsWith2(char sch, char pch)
  {
    String stemp = split(sch).get(); String ptemp = split(pch).get();
    return stemp.endsWith(ptemp);
  }

  public static String removeEnd(String string, String pattern)
  {
    if ((!Util.valid(string)) || (!Util.valid(pattern))) return string;
    int slen = string.length(); int plen = pattern.length();
    if (slen < plen) return string;
    if (string.endsWith(pattern)) return string.substring(0, slen - plen);

    if (!pattern.substring(1).equals(string.substring(slen - plen + 1))) return string;

    String stemp = split(string.charAt(slen - plen)).get();
    String ptemp = split(pattern.charAt(0)).get();
    if (!stemp.endsWith(ptemp)) return string;
    String temp = stemp.substring(0, stemp.length() - ptemp.length());
    char[] ch = new char[3];
    int i = 0; for (int stop = temp.length(); i < stop; i++) {
      ch[i] = temp.charAt(i);
    }
    String ret = slen > plen ? string.substring(0, slen - plen) : "";
    char rch = combine(ch[0], ch[1], ch[2]);
    if (rch == 0) return ret;
    return ret += combine(ch[0], ch[1], ch[2]);
  }

  public static String extractExtraEomi(String string, int len)
  {
    int strlen = string.length();
    if ((!Util.valid(string)) || (strlen < len)) return null;
    Hangul hg = split(string.charAt(strlen - len));
    if (!hg.hasJong()) return null;
    String temp = hg.get();
    return temp.charAt(temp.length() - 1) + string.substring(strlen - len + 1);
  }
}