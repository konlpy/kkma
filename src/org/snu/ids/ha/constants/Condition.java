package org.snu.ids.ha.constants;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import org.snu.ids.ha.util.Hangul;

public class Condition extends Hangul
{
  public static final String[] COND_ARR = { 
    "ㅣ겹", 
    "모음", 
    "자음", 
    "양성", 
    "음성", 
    "사오", 
    "사옵", 
    "시오", 
    "오", 
    "으라", 
    "으리", 
    "으시", 
    "아", 
    "었", 
    "겠", 
    "려", 
    "ㄴ", 
    "ㄹ", 
    "ㅁ", 
    "ㅂ", 
    "-ㅂ", 
    "-ㄹ", 
    "-ㅎ", 
    "-ㅅ", 
    "하", 
    "가다", 
    "오다", 
    "ENG", 
    "체언", 
    "관형어", 
    "부사어", 
    "서술어", 
    "EC", 
    "F", 
    "생략" };

  public static final Hashtable<String, Long> COND_HASH = new Hashtable();
  public static final Hashtable<Long, String> COND_NUM_HASH = new Hashtable();

  public static final long YI_DB = getCondNum("ㅣ겹");
  public static final long MOEUM = getCondNum("모음");
  public static final long JAEUM = getCondNum("자음");
  public static final long YANGSEONG = getCondNum("양성");
  public static final long EUMSEONG = getCondNum("음성");

  public static final long SAO = getCondNum("사오");
  public static final long SAOP = getCondNum("사옵");
  public static final long SIO = getCondNum("시오");
  public static final long OH = getCondNum("오");
  public static final long ERA = getCondNum("으라");
  public static final long ERI = getCondNum("으리");
  public static final long ESI = getCondNum("으시");
  public static final long AH = getCondNum("아");
  public static final long EUT = getCondNum("었");
  public static final long GET = getCondNum("겠");
  public static final long LYEO = getCondNum("려");

  public static final long NIEUN = getCondNum("ㄴ");
  public static final long MIEUM = getCondNum("ㅁ");
  public static final long LIEUL = getCondNum("ㄹ");

  public static final long BIEUB = getCondNum("ㅂ");

  public static final long MINUS_BIEUB = getCondNum("-ㅂ");

  public static final long MINUS_LIEUL = getCondNum("-ㄹ");
  public static final long MINUS_HIEUT = getCondNum("-ㅎ");
  public static final long MINUS_SIOT = getCondNum("-ㅅ");

  public static final long HA = getCondNum("하");
  public static final long GADA = getCondNum("가다");
  public static final long ODA = getCondNum("오다");
  public static final long ENG = getCondNum("ENG");

  public static final long N = getCondNum("체언");
  public static final long D = getCondNum("관형어");
  public static final long A = getCondNum("부사어");
  public static final long V = getCondNum("서술어");

  public static final long EC = getCondNum("EC");

  public static final long F = getCondNum("F");
  public static final long SHORTEN = getCondNum("생략");

  public static final long MINUS_JA_SET = MINUS_LIEUL | MINUS_HIEUT | MINUS_SIOT;
  public static final long SET_FOR_UN = JAEUM | MOEUM | YANGSEONG | EUMSEONG;

  static
  {
    long conditionNum = 0L;

    int i = 0; for (int stop = COND_ARR.length; i < stop; i++) {
      conditionNum = 1L << i;
      COND_HASH.put(COND_ARR[i], new Long(conditionNum));
      COND_NUM_HASH.put(new Long(conditionNum), COND_ARR[i]);
    }
  }

  private static final long getCondNum(int i)
  {
    return 1L << i;
  }

  public static long getCondNum(String cond)
  {
    try
    {
      return ((Long)COND_HASH.get(cond)).longValue();
    } catch (Exception e) {
      System.err.println("[" + cond + "] 정의되지 않은 조건입니다.");
    }
    return 0L;
  }

  public static long getCondNum(String[] conds)
  {
    long l = 0L;
    int i = 0; for (int size = conds == null ? 0 : conds.length; i < size; i++) {
      l |= getCondNum(conds[i]);
    }
    return l;
  }

  public static String getCond(long condNum)
  {
    return condNum == 0L ? null : (String)COND_NUM_HASH.get(new Long(condNum));
  }

  public static List<String> getCondList(long encCondNum)
  {
    List ret = new ArrayList();
    int i = 0; for (int stop = COND_ARR.length; i < stop; i++) {
      if ((encCondNum & getCondNum(i)) > 0L) ret.add(COND_ARR[i]);
    }
    return ret;
  }

  public static String getCondStr(long encCondNum)
  {
    StringBuffer sb = new StringBuffer();
    List condList = getCondList(encCondNum);
    int i = 0; for (int size = condList.size(); i < size; i++) {
      if (i > 0) sb.append(",");
      sb.append((String)condList.get(i));
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  public static final List<String> getCondList()
  {
    List condList = new ArrayList();
    List condNumList = new ArrayList(COND_NUM_HASH.keySet());
    Collections.sort(condNumList);
    int i = 0; for (int size = condNumList.size(); i < size; i++) {
      condList.add((String)COND_NUM_HASH.get(condNumList.get(i)));
    }
    return condList;
  }

  public static final boolean checkAnd(long havingCond, long checkingCond)
  {
    return (havingCond & checkingCond) == checkingCond;
  }

  public static final boolean checkOr(long havingCond, long checkingCond)
  {
    return (havingCond & checkingCond) > 0L;
  }
}