package org.snu.ids.ha.dic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Hashtable;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.util.Timer;
import org.snu.ids.ha.util.Util;

public class UNPDDictionary
{
  private static final float DEFAULT_PROB = -30.0F;
  private static final Hashtable<Character, Float> PROB_AT_NOUN_HASH = new Hashtable();

  static { load("/dic/prob/lnpr_syllable_uni_noun.dic"); }


  public static final void load(String fileName)
  {
    System.out.println("Loading " + fileName);
    Timer timer = new Timer();
    timer.start();

    String line = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(SpacingPDDictionary.class.getResourceAsStream(fileName), "UTF-8"));

      while ((line = br.readLine()) != null)
        if ((Util.valid(line)) && (!line.startsWith("//"))) {
          line = line.trim();
          char ch = line.charAt(0);
          float lnpr = Float.parseFloat(line.substring(1).trim());
          PROB_AT_NOUN_HASH.put(Character.valueOf(ch), Float.valueOf(lnpr));
        }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(line);
      System.err.println("Unable to load probability dictionary!!");
    } finally {
      timer.stop();
      System.out.println(PROB_AT_NOUN_HASH.size() + " values are loaded. (Loading time( " + timer.getInterval() + " secs)");
    }
  }

  private static final float getProbAtNoun(char ch)
  {
    Float lnpr = (Float)PROB_AT_NOUN_HASH.get(Character.valueOf(ch));
    if (lnpr != null) {
      return lnpr.floatValue();
    }
    return -30.0F;
  }

  public static float getProb(String str)
  {
    if (!Util.valid(str)) return 1.4E-45F;
    float prob = 0.0F;
    int i = 0; for (int len = str.length(); i < len; i++) {
      char ch = str.charAt(i);
      prob += getProbAtNoun(ch);
    }

    return prob;
  }

  public static void main(String[] args)
  {
    System.out.println(getProb("속한"));
    System.out.println(PDDictionary.getLnprPosGExp("속하", POSTag.VV));
  }
}