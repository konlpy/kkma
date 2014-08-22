package org.snu.ids.ha.dic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.snu.ids.ha.constants.Condition;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.ma.MCandidate;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.util.Hangul;
import org.snu.ids.ha.util.StringSet;
import org.snu.ids.ha.util.Timer;
import org.snu.ids.ha.util.Util;

public class Dictionary
{
  private static Dictionary dictionary = null;

  static boolean isLoading = false;

  private final Hashtable<String, MExpression> table = new Hashtable(530000);
  private List<MExpression> meList = null;
  private final Hashtable<String, String[]> compNounTable = new Hashtable();
  private final HashSet<String> verbStemSet = new HashSet();
  private int maxLen = 0;

  static final StringSet MO_SET1 = new StringSet(new String[] { "ㅏ", "ㅓ", "ㅐ", "ㅔ" });
  static final StringSet MO_SET2 = new StringSet(new String[] { "ㅗ", "ㅜ", "ㅡ" });

  public static final synchronized Dictionary getInstance()
  {
    if ((!isLoading) && 
      (dictionary == null)) {
      isLoading = true;
      dictionary = new Dictionary();
      isLoading = false;
    }

    return dictionary;
  }

  protected Dictionary()
  {
    Timer timer = new Timer();
    try {
      timer.start();
      loadDic();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      timer.stop();
      timer.printMsg("Dictionary Loading Time");
      System.out.println("Loaded Item " + this.table.size());
    }
  }

  public static void reload()
  {
    if ((!isLoading) && (dictionary != null)) {
      Timer timer = new Timer();
      try {
        System.out.println("reloading");
        timer.start();
        dictionary.clear();
        dictionary.loadDic();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        timer.stop();
        timer.printMsg("Dictionary Loading Time");
        System.out.println("Loaded Item " + dictionary.table.size());
      }
    }
  }

  public static void reload(List<DicReader> dicReadList)
  {
    if ((!isLoading) && (dictionary != null)) {
      Timer timer = new Timer();
      try {
        System.out.println("reloading");
        timer.start();
        dictionary.clear();
        for (int i = 0; i < dicReadList.size(); i++)
          dictionary.load((DicReader)dicReadList.get(i));
      }
      catch (Exception e) {
        e.printStackTrace();
      } finally {
        timer.stop();
        timer.printMsg("Dictionary Loading Time");
        System.out.println("Loaded Item " + dictionary.table.size());
      }
    }
  }

  public void clear()
  {
    this.table.clear();
    this.compNounTable.clear();
    this.verbStemSet.clear();
    this.maxLen = 0;
  }

  public static MCandidate getVerbBasicMC(String string, String posTag)
    throws Exception
  {
    String stem = null;
    if (string.charAt(string.length() - 1) == 45796)
      stem = string.substring(0, string.length() - 1);
    else {
      stem = string;
    }

    String exp = stem;
    MCandidate mCandidate = new MCandidate(exp, posTag);
    mCandidate.setCandDicLen((byte)exp.length());
    mCandidate.setExp(exp);

    return mCandidate;
  }

  public static List<MCandidate> getVerbExtendedMC(MCandidate mCandidate)
  {
    List ret = new ArrayList();

    String stem = mCandidate.getExp();
    int stemLen = stem.length();
    String preStem = stem.substring(0, stemLen - 1);

    char lastCh = stem.charAt(stemLen - 1); char preLastCh = '\000'; char mo = '\000';
    Hangul lastHg = Hangul.split(lastCh); Hangul preLastHg = null;
    if (stemLen > 1) {
      preLastCh = stem.charAt(stemLen - 2);
      preLastHg = Hangul.split(preLastCh);
    } else {
      preLastCh = '\000';
    }

    String exp = null;
    MCandidate mCandidateClone = null;

    if ((stem.length() == 1) && (!lastHg.hasJong()) && (lastHg.cho != 'ㅎ')) {
      exp = stem;
      if (lastHg.jung == 'ㅏ') {
        mCandidateClone = mCandidate.copy();
        mCandidateClone.add(new Morpheme("아", POSTag.ECS));
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.addHavingCond(Condition.AH);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      } else if (lastHg.jung == 'ㅓ') {
        mCandidateClone = mCandidate.copy();
        mCandidateClone.add(new Morpheme("어", POSTag.ECS));
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.addHavingCond(Condition.AH);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      }

    }

    if ((lastCh == 52270) || (lastCh == 51094)) {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㄴ');
      mCandidateClone.setExp(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      mCandidateClone.setExp(exp);
      ret.add(mCandidateClone);
    }

    if (lastCh == 54616)
    {
      mCandidateClone = mCandidate.copy();
      exp = preStem + "했";

      mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + "해";

      mCandidateClone.add(new Morpheme("어", POSTag.ECS));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + "해";

      mCandidateClone.add(new Morpheme("어", POSTag.EFN));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      if (mCandidate.isTagOf(POSTag.VA | POSTag.VXA)) {
        mCandidateClone = mCandidate.copy();
        exp = preStem + "치";
        mCandidateClone.add(new Morpheme("지", POSTag.ECS));
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      }

    }
    else if ((!lastHg.hasJong()) && (lastHg.jung == 'ㅣ'))
    {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, 'ㅕ', 'ㅆ');
      mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, 'ㅕ', ' ');
      mCandidateClone.add(new Morpheme("어", POSTag.ECS));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.MOEUM | Condition.EUMSEONG | Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ((!lastHg.hasJong()) && (MO_SET1.contains(lastHg.jung))) {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㅆ');
      mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if (lastCh == 47476)
    {
      mCandidateClone = mCandidate.copy();
      mCandidateClone.clearHavingCondition();
      if (preLastCh == 46384) {
        exp = preStem + "랐";
        mCandidateClone.add(new Morpheme("았", POSTag.EPT));
        mCandidateClone.addHavingCond(Condition.EUT);
      } else if (preLastCh == 54392) {
        exp = stem + "렀";
        mCandidateClone.add(new Morpheme("었", POSTag.EPT));
        mCandidateClone.addHavingCond(Condition.EUT);
      } else {
        mo = getMoeum(lastHg, preLastHg);
        exp = stem.substring(0, stemLen - 2) + 
          Hangul.combine(preLastHg.cho, preLastHg.jung, 'ㄹ') + 
          Hangul.combine(lastHg.cho, mo, 'ㅆ');
        if (mo == 'ㅏ')
          mCandidateClone.add(new Morpheme("았", POSTag.EPT));
        else {
          mCandidateClone.add(new Morpheme("었", POSTag.EPT));
        }
        mCandidateClone.addHavingCond(Condition.EUT);
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setCandDicLen((byte)exp.length());

      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      mCandidateClone.clearHavingCondition();
      if (preLastCh == 46384) {
        exp = preStem + "라";
        mCandidateClone.add(new Morpheme("아", POSTag.ECS));
        mCandidateClone.addHavingCond(Condition.AH);
      } else if (preLastCh == 54392) {
        exp = stem + "러";
        mCandidateClone.add(new Morpheme("어", POSTag.ECS));
        mCandidateClone.addHavingCond(Condition.AH);
      } else {
        mo = getMoeum(lastHg, preLastHg);
        exp = stem.substring(0, stemLen - 2) + 
          Hangul.combine(preLastHg.cho, preLastHg.jung, 'ㄹ') + 
          Hangul.combine(lastHg.cho, mo, ' ');
        if (mo == 'ㅏ') {
          mCandidateClone.add(new Morpheme("아", POSTag.ECS));
          mCandidateClone.addHavingCond(Condition.AH);
        } else {
          mCandidateClone.add(new Morpheme("어", POSTag.ECS));
          mCandidateClone.addHavingCond(Condition.AH);
        }
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ((!lastHg.hasJong()) && (lastHg.jung == 'ㅡ'))
    {
      mo = getMoeum(lastHg, preLastHg);
      mCandidateClone = mCandidate.copy();
      mCandidateClone.clearHavingCondition();
      exp = preStem + Hangul.combine(lastHg.cho, mo, 'ㅆ');
      if (mo == 'ㅏ') {
        mCandidateClone.add(new Morpheme("았", POSTag.EPT));
        mCandidateClone.addHavingCond(Condition.EUT);
      } else {
        mCandidateClone.add(new Morpheme("었", POSTag.EPT));
        mCandidateClone.addHavingCond(Condition.EUT);
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      mCandidateClone.clearHavingCondition();
      exp = preStem + Hangul.combine(lastHg.cho, mo, ' ');
      if (mo == 'ㅏ') {
        mCandidateClone.add(new Morpheme("아", POSTag.ECS));
        mCandidateClone.addHavingCond(Condition.AH);
      } else {
        mCandidateClone.add(new Morpheme("어", POSTag.ECS));
        mCandidateClone.addHavingCond(Condition.AH);
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ((!lastHg.hasJong()) && (MO_SET2.contains(lastHg.jung)))
    {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, getMoeum(lastHg, preLastHg), 'ㅆ');
      if (lastHg.jung == 'ㅜ')
        mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      else {
        mCandidateClone.add(new Morpheme("았", POSTag.EPT));
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, getMoeum(lastHg, preLastHg), ' ');
      if (lastHg.jung == 'ㅜ')
        mCandidateClone.add(new Morpheme("어", POSTag.ECS));
      else {
        mCandidateClone.add(new Morpheme("아", POSTag.ECS));
      }
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ((!lastHg.hasJong()) && (lastHg.jung == 'ㅚ'))
    {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, 'ㅙ', ' ');
      mCandidateClone.add(new Morpheme("어", POSTag.ECS));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, 'ㅙ', 'ㅆ');
      mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }

    if ("갑겁겹곱굽깁깝껍꼽납눕답덥돕둡땁떱랍럽렵롭립맙맵밉볍섭쉽습엽줍쭙춥탑".indexOf(lastCh) > -1)
    {
      char bChar = Hangul.combine(lastHg.cho, lastHg.jung, ' ');

      if (lastCh == 47101) {
        mCandidateClone = mCandidate.copy();
        exp = preStem + 47088;
        mCandidateClone.add(new Morpheme("ㄴ", POSTag.ETD));
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      }

      mCandidateClone = mCandidate.copy();
      if (lastHg.jung == 'ㅗ') {
        mo = 'ㅘ';
        mCandidateClone.add(new Morpheme("아", POSTag.ECS));
      } else {
        mo = 'ㅝ';
        mCandidateClone.add(new Morpheme("어", POSTag.ECS));
      }
      exp = preStem + bChar + Hangul.combine('ㅇ', mo, ' ');
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.AH);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      mCandidateClone.clearHavingCondition();
      if (lastHg.jung == 'ㅗ') {
        mo = 'ㅘ';
        mCandidateClone.add(new Morpheme("았", POSTag.EPT));
      } else {
        mo = 'ㅝ';
        mCandidateClone.add(new Morpheme("었", POSTag.EPT));
      }
      exp = preStem + bChar + Hangul.combine('ㅇ', mo, 'ㅆ');
      mCandidateClone.setExp(exp);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.EUT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + bChar + 50864;
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.MINUS_BIEUB);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + bChar + 50868;
      mCandidateClone.add(new Morpheme("ㄴ", POSTag.ETD));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      mCandidateClone.add(new Morpheme("ㄹ", POSTag.ETD));
      exp = preStem + bChar + 50872;
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      mCandidateClone.add(new Morpheme("ㅁ", POSTag.ETN));
      exp = preStem + bChar + 50880;
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ("젓짓긋낫붓잇".indexOf(lastCh) > -1)
    {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, ' ');
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.MINUS_SIOT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if (lastHg.jong == 'ㄷ') {
      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㄹ');
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.addHavingCond(Condition.MINUS_SIOT);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }
    else if ((!lastHg.hasJong()) || (lastHg.jong == 'ㄹ') || 
      (lastHg.jong == 'ㅎ'))
    {
      mCandidateClone = mCandidate.copy();
      mCandidateClone.add(new Morpheme("ㄴ", POSTag.ETD));
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㄴ');
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㄹ');
      mCandidateClone.add(new Morpheme("ㄹ", POSTag.ETD));
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setRealDicLen((byte)exp.length());
      ret.add(mCandidateClone);

      if (lastHg.jong == 'ㄹ') {
        mCandidateClone = mCandidate.copy();
        exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㄻ');
        mCandidateClone.add(new Morpheme("ㅁ", POSTag.ETN));
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);

        mCandidateClone = mCandidate.copy();
        exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, ' ');
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.addHavingCond(Condition.MINUS_LIEUL);
        mCandidateClone.setCandDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      }
      else if (lastHg.jong == 'ㅎ') {
        mCandidateClone = mCandidate.copy();
        exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, ' ');
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.addHavingCond(Condition.MINUS_HIEUT);
        mCandidateClone.setCandDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      } else {
        mCandidateClone = mCandidate.copy();
        mCandidateClone.add(new Morpheme("ㅁ", POSTag.ETN));
        exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㅁ');
        mCandidateClone.setExp(exp);
        mCandidateClone.clearHavingCondition();
        mCandidateClone.initHavingCond(exp);
        mCandidateClone.setRealDicLen((byte)exp.length());
        ret.add(mCandidateClone);
      }

      mCandidateClone = mCandidate.copy();
      exp = preStem + Hangul.combine(lastHg.cho, lastHg.jung, 'ㅂ');
      mCandidateClone.setExp(exp);
      mCandidateClone.clearHavingCondition();
      mCandidateClone.addHavingCond(Condition.BIEUB);
      mCandidateClone.initHavingCond(exp);
      mCandidateClone.setCandDicLen((byte)exp.length());
      ret.add(mCandidateClone);
    }

    return ret;
  }

  static char getMoeum(Hangul lastHg, Hangul preLastHg)
  {
    char mo = '\000';
    char mo1 = lastHg.jung;
    if (mo1 == 'ㅗ')
      mo = 'ㅘ';
    else if (mo1 == 'ㅜ') {
      if (lastHg.cho == 'ㅍ')
        mo = 'ㅓ';
      else
        mo = 'ㅝ';
    }
    else if (mo1 == 'ㅡ') {
      if ((preLastHg != null) && (Hangul.MO_POSITIVE_SET.contains(preLastHg.jung)))
        mo = 'ㅏ';
      else {
        mo = 'ㅓ';
      }
    }
    return mo;
  }

  private void add(String exp, MCandidate mc)
    throws Exception
  {
    mc.calcHashCode();

    mc.calcLnprOfTagging();

    MExpression me = get(exp);

    if (me == null) {
      me = new MExpression(exp, mc);
      float lnprOfSpacing = SpacingPDDictionary.getProb(exp);
      mc.setLnprOfSpacing(lnprOfSpacing);
      me.setLnprOfSpacing(lnprOfSpacing);
      this.table.put(exp, me);
      if (this.maxLen < exp.length())
        this.maxLen = exp.length();
    }
    else {
      mc.setLnprOfSpacing(me.getLnprOfSpacing());
      me.add(mc);
    }
  }

  public boolean containVerbStem(String exp)
  {
    return this.verbStemSet.contains(exp);
  }

  public boolean containNoun(String exp)
  {
    MExpression me = (MExpression)this.table.get(exp);
    if (me == null) return false;
    label58: for (Iterator localIterator = me.iterator(); localIterator.hasNext(); 
      return true)
    {
      MCandidate mc = (MCandidate)localIterator.next();
      if ((mc.size() != 1) || (!mc.isFirstTagOf(POSTag.NNA))) break label58;
    }
    return false;
  }

  private synchronized MExpression get(String exp)
  {
    return (MExpression)this.table.get(exp);
  }

  public synchronized MExpression getMExpression(String exp)
  {
    MExpression ret = get(exp);
    return ret == null ? null : ret.copy();
  }

  public synchronized String[] getCompNoun(String noun)
  {
    return (String[])this.compNounTable.get(noun);
  }

  private void load(SimpleDicReader simpleDicReader)
    throws Exception
  {
    String line = null;
    try
    {
      String[] strArrTemp = (String[])null;
      while ((line = simpleDicReader.readLine()) != null)
        if ((Util.valid(line)) && (!line.startsWith("//"))) {
          line = line.trim();

          String exp = null; String mpInfo = null; String condInfo = null;

          if (line.indexOf(';') > 0) {
            strArrTemp = line.split(";");
            mpInfo = strArrTemp[0];
            if (strArrTemp.length > 1)
              condInfo = strArrTemp[1];
          }
          else {
            mpInfo = line;
          }

          exp = mpInfo.split("/")[0];

          String atl = null; String hcl = null; String ccl = null; String ecl = null; String compResult = null;
          if (condInfo != null)
          {
            StringTokenizer st = new StringTokenizer(condInfo, "#&@￢%$", true);
            while (st.hasMoreTokens()) {
              String token = st.nextToken();

              if (token.equals("#")) {
                token = st.nextToken().trim();
                atl = token.substring(1, token.length() - 1);
              }
              else if (token.equals("&")) {
                token = st.nextToken().trim();
                hcl = token.substring(1, token.length() - 1);
              }
              else if (token.equals("@")) {
                token = st.nextToken().trim();
                ccl = token.substring(1, token.length() - 1);
              }
              else if (token.equals("￢")) {
                token = st.nextToken().trim();
                ecl = token.substring(1, token.length() - 1);
              }
              else if (token.equals("$")) {
                token = st.nextToken().trim();
                compResult = token.substring(1, token.length() - 1);
              }
            }
          }

          MCandidate mCandidate = MCandidate.create(exp, mpInfo, atl, hcl, ccl, ecl);

          add(mCandidate.getExp(), mCandidate);

          if (mCandidate.isTagOf(POSTag.V | POSTag.XSV | POSTag.XSA)) {
            this.verbStemSet.add(exp);
            List mcList = getVerbExtendedMC(mCandidate);
            int i = 0; for (int size = mcList.size(); i < size; i++) {
              MCandidate mc = (MCandidate)mcList.get(i);
              add(mc.getExp(), mc);
            }

          }
          else if (Util.valid(compResult)) {
            this.compNounTable.put(exp, compResult.split("[+]"));
          }
        }
    }
    catch (Exception e) {
      System.err.println(line);
      throw e;
    } finally {
      simpleDicReader.cleanup();
    }
  }

  private void load(RawDicReader rawDicReader)
    throws Exception
  {
    String line = null;
    try {
      String[] arr = (String[])null;
      String string = null; String temp = null;
      while ((line = rawDicReader.readLine()) != null)
        if ((Util.valid(line)) && (!line.startsWith("//"))) {
          line = line.trim();
          arr = line.split(":");
          string = arr[0];
          if (arr.length >= 2) {
            arr = arr[1].split(";");
            int i = 0; for (int stop = arr.length; i < stop; i++) {
              temp = arr[i].trim();
              add(string, MCandidate.create(string, temp.substring(1, temp.length() - 1)));
            }
          }
        }
    } catch (Exception e) { System.err.println(line);
      throw e;
    } finally {
      rawDicReader.cleanup();
    }
  }

  private void load(DicReader dicReader)
    throws Exception
  {
    if ((dicReader instanceof SimpleDicReader))
      load((SimpleDicReader)dicReader);
    else if ((dicReader instanceof RawDicReader))
      load((RawDicReader)dicReader);
    else
      throw new Exception("Unknown dictionary reader type.");
  }

  void loadSimple(String fileName)
    throws Exception
  {
    System.out.println("Loading " + fileName);
    Timer timer = new Timer();
    timer.start();
    try {
      load(new SimpleDicFileReader(fileName));
    } finally {
      timer.stop();
      System.out.println("Loaded " + timer.getInterval() + "secs");
    }
  }

  private void loadRaw(String fileName)
    throws Exception
  {
    System.out.println("Loading " + fileName);
    Timer timer = new Timer();
    timer.start();
    try
    {
      load(new RawDicFileReader(fileName));
    } catch (Exception e) {
      throw e;
    } finally {
      timer.stop();
      System.out.println("Loaded " + timer.getInterval() + "secs");
    }
  }

  protected void loadDic()
    throws Exception
  {
    loadSimple("/dic/noun.dic");
    loadSimple("/dic/verb.dic");
    loadSimple("/dic/simple.dic");
    loadSimple("/dic/person.dic");
    loadSimple("/dic/kcc.dic");
    loadRaw("/dic/raw.dic");
  }

  public void printToFile(String fileName)
  {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileOutputStream(new File(fileName)));
      ArrayList list = new ArrayList(this.table.values());
      Collections.sort(list);
      int i = 0; for (int stop = list.size(); i < stop; i++) {
        MExpression me = (MExpression)list.get(i);
        pw.println(me);
        pw.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (pw != null) pw.close();
    }
  }

  public List<MExpression> getAsList()
  {
    if (this.table == null) return null;
    if (this.meList == null) this.meList = new ArrayList(this.table.values());
    return this.meList;
  }

  public List<MCandidate> search(String str)
  {
    Timer timer = new Timer();
    timer.start();
    List ret = new ArrayList();
    getAsList();
    for (int i = 0; i < this.meList.size(); i++) {
      MExpression me = (MExpression)this.meList.get(i);
      if (me.getExp().indexOf(str) > -1) {
        ret.addAll(me);
      }
    }
    timer.printMsg(ret.size() + " candidates found.");
    timer.stop();
    return ret;
  }

  public List<Morpheme> getWordList()
  {
    ArrayList morpList = new ArrayList();
    Iterator localIterator2;
    for (Iterator localIterator1 = this.table.values().iterator(); localIterator1.hasNext(); 
      localIterator2.hasNext())
    {
      MExpression me = (MExpression)localIterator1.next();
      localIterator2 = me.iterator(); continue; MCandidate mc = (MCandidate)localIterator2.next();
      if (mc.size() == 1) {
        morpList.add((Morpheme)mc.get(0));
      }

    }

    return morpList;
  }
}