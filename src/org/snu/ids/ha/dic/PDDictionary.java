package org.snu.ids.ha.dic;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.util.Timer;
import org.snu.ids.ha.util.Util;

public final class PDDictionary
{
  private static final Hashtable<Long, Float> LNPR_POS = new Hashtable(50);
  private static final Hashtable<String, Float> LNPR_MORP = new Hashtable(80000);
  private static final Hashtable<String, Float> LNPR_POS_G_EXP = new Hashtable(70000);
  private static final Hashtable<String, Float> LNPR_POS_G_MORP_INTRA = new Hashtable(60000);
  private static final Hashtable<String, Float> LNPR_POS_G_MORP_INTER = new Hashtable(520000);
  private static final float MIN_LNPR_POS = -9.0F;
  private static final float MIN_LNPR_MORP = -18.0F;

  static
  {
    System.out.println("Prob Dic Loading!");
    Timer timer = new Timer();
    timer.start();
    loadLnprPos("/dic/prob/lnpr_pos.dic");
    System.out.println(LNPR_POS.size() + " loaded!");
    loadLnprMorp("/dic/prob/lnpr_morp.dic");
    System.out.println(LNPR_MORP.size() + " loaded!");
    loadLnprPosGExp("/dic/prob/lnpr_pos_g_exp.dic");
    System.out.println(LNPR_POS_G_EXP.size() + " loaded!");
    loadLnprPosGMorp("/dic/prob/lnpr_pos_g_morp_intra.dic", LNPR_POS_G_MORP_INTRA);
    System.out.println(LNPR_POS_G_MORP_INTRA.size() + " loaded!");
    loadLnprPosGMorp("/dic/prob/lnpr_pos_g_morp_inter.dic", LNPR_POS_G_MORP_INTER);
    System.out.println(LNPR_POS_G_MORP_INTER.size() + " loaded!");
    timer.stop();
    System.out.println("(Loading time : " + timer.getInterval() + " secs!");
  }

  private static final void loadLnprPos(String fileName)
  {
    ProbDicReader dr = null;
    try {
      dr = new ProbDicReader(fileName);
      String[] arr = (String[])null;
      while ((arr = dr.read()) != null) {
        long pos = POSTag.getTagNum(arr[0]);
        float lnpr = Float.parseFloat(arr[1]);
        LNPR_POS.put(Long.valueOf(pos), Float.valueOf(lnpr));
      }
      dr.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(dr.line);
      System.err.println("Loading error: " + fileName);
    }
  }

  private static final void loadLnprMorp(String fileName)
  {
    ProbDicReader dr = null;
    try {
      dr = new ProbDicReader(fileName);
      String[] arr = (String[])null;
      while ((arr = dr.read()) != null) {
        String exp = arr[0];
        long pos = POSTag.getTagNum(arr[1]);
        float lnpr = Float.parseFloat(arr[2]);
        LNPR_MORP.put(exp + ":" + pos, Float.valueOf(lnpr));
      }
      dr.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(dr.line);
      System.err.println("Loading error: " + fileName);
    }
  }

  private static final void loadLnprPosGExp(String fileName)
  {
    ProbDicReader dr = null;
    try {
      dr = new ProbDicReader(fileName);
      String[] arr = (String[])null;
      while ((arr = dr.read()) != null) {
        String exp = arr[0];
        long pos = POSTag.getTagNum(arr[1]);
        float lnpr = Float.parseFloat(arr[2]);
        LNPR_POS_G_EXP.put(pos + "|" + exp, Float.valueOf(lnpr));
      }
      dr.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(dr.line);
      System.err.println("Loading error: " + fileName);
    }
  }

  private static final void loadLnprPosGMorp(String fileName, Hashtable<String, Float> probMap)
  {
    ProbDicReader dr = null;
    try {
      dr = new ProbDicReader(fileName);
      String[] arr = (String[])null;
      while ((arr = dr.read()) != null)
      {
        if (arr.length == 4) {
          long prevPos = POSTag.getTagNum(arr[0]);
          String exp = arr[1];
          long pos = POSTag.getTagNum(arr[2]);
          float lnpr = Float.parseFloat(arr[3]);
          probMap.put(getKey(prevPos, exp, pos), Float.valueOf(lnpr));
        }
        else if (arr.length == 3) {
          long prevPos = POSTag.getTagNum(arr[0]);
          long pos = POSTag.getTagNum(arr[1]);
          float lnpr = Float.parseFloat(arr[2]);
          probMap.put(getKey(prevPos, null, pos), Float.valueOf(lnpr));
        }
      }

      dr.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(dr.line);
      System.err.println("Loading error: " + fileName);
    }
  }

  static final String getKey(long prevPos, String exp, long pos)
  {
    return prevPos + "|" + exp + ":" + pos;
  }

  public static float getLnprPos(long pos)
  {
    Float lnpr = (Float)LNPR_POS.get(Long.valueOf(getPrTag(pos)));
    if (lnpr == null) {
      return -9.0F;
    }
    return lnpr.floatValue();
  }

  private static float getLnprMorp(String exp, long pos)
  {
    Float lnpr = (Float)LNPR_MORP.get(exp + ":" + getPrTag(pos));
    if (lnpr == null) {
      return -18.0F;
    }
    return lnpr.floatValue();
  }

  public static float getLnprPosGExp(String exp, long pos)
  {
    Float lnpr = (Float)LNPR_POS_G_EXP.get(getPrTag(pos) + "|" + exp);
    if (lnpr == null) {
      return getLnprPos(pos);
    }
    return lnpr.floatValue();
  }

  public static float getLnprPosGMorpIntra(long prevPos, String exp, long pos)
  {
    return getLnprPosGMorp(LNPR_POS_G_MORP_INTRA, prevPos, exp, pos);
  }

  public static float getLnprPosGMorpInter(long prevPos, String exp, long pos)
  {
    return getLnprPosGMorp(LNPR_POS_G_MORP_INTER, prevPos, exp, pos);
  }

  private static float getLnprPosGMorp(Hashtable<String, Float> lnprMap, long prevPos, String exp, long pos)
  {
    Float lnpr = (Float)lnprMap.get(getKey(getPrTag(prevPos), exp, getPrTag(pos)));
    if ((lnpr == null) && (getLnprMorp(exp, pos) < -14.0F)) {
      lnpr = (Float)lnprMap.get(getKey(getPrTag(prevPos), null, getPrTag(pos)));
    }
    if (lnpr == null) return -18.0F;
    return lnpr.floatValue();
  }

  public static long getPrTag(long tag)
  {
    if (((POSTag.NNA | POSTag.UN) & tag) > 0L)
      return POSTag.NNA;
    if (((POSTag.NNM | POSTag.NNB) & tag) > 0L)
      return POSTag.NNB;
    if ((POSTag.VX & tag) > 0L)
      return POSTag.VX;
    if ((POSTag.MD & tag) > 0L)
      return POSTag.MD;
    if ((POSTag.EP & tag) > 0L)
      return POSTag.EP;
    if ((POSTag.EF & tag) > 0L)
      return POSTag.EF;
    if ((POSTag.EC & tag) > 0L) {
      return POSTag.EC;
    }
    return tag;
  }

  public static float getLnpr(String morps)
  {
    float lnpr = 0.0F;
    String[] arr = morps.trim().split("[+]");

    ArrayList mpList = new ArrayList();
    for (String temp : arr) {
      if (temp.equals(" ")) {
        mpList.add(new Morpheme(" ", POSTag.S));
      } else {
        String[] arr2 = temp.split("[/]");
        mpList.add(new Morpheme(arr2[1], POSTag.getTagNum(arr2[2])));
      }
    }

    Morpheme preMp = null;
    boolean spacing = false;
    System.out.println(morps);
    for (??? = mpList.iterator(); ((Iterator)???).hasNext(); ) { Morpheme curMp = (Morpheme)((Iterator)???).next();

      if (curMp.getString().equals(" ")) {
        spacing = true;
      }
      else
      {
        float lnprPosGExp = getLnprPosGExp(curMp.getString(), curMp.getTagNum());
        float lnprPosGMorp = 0.0F;
        float lnprPos = 0.0F;
        if (preMp != null) {
          if (spacing)
            lnprPosGMorp = getLnprPosGMorpInter(preMp.getTagNum(), curMp.getString(), curMp.getTagNum());
          else {
            lnprPosGMorp = getLnprPosGMorpIntra(preMp.getTagNum(), curMp.getString(), curMp.getTagNum());
          }
          lnprPos = getLnprPos(preMp.getTagNum());
        }

        lnpr += lnprPosGExp;
        lnpr += lnprPosGMorp;

        System.out.println("\t" + Util.getTabbedString(curMp.getSmplStr(), 4, 8) + String.format("%10.3f%10s%10.3f%10.3f", new Object[] { Float.valueOf(lnprPosGExp), Boolean.valueOf(spacing), Float.valueOf(lnprPosGMorp), Float.valueOf(lnprPos) }));

        spacing = false;
        preMp = curMp;
      }
    }
    return lnpr;
  }

  public static void main(String[] args)
  {
    System.out.println(getLnpr("42/무리/NNG+ +44/일수/NNG+ +46/있/VV+47/다/EFN"));
    System.out.println(getLnpr("42/무리/NNG+44/이/VCP+45/ㄹ/ETD+ +45/수/NNB+ +46/있/VV+47/다/EFN"));
  }
}