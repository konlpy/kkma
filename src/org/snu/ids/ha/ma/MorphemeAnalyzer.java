package org.snu.ids.ha.ma;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.snu.ids.ha.constants.Condition;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.dic.Dictionary;
import org.snu.ids.ha.util.Util;

public class MorphemeAnalyzer
{
  protected Dictionary dic = null;

  PrintWriter logger = null;
  boolean doLogging = false;

  public static final boolean DEBUG = "DO_DEBUG".equals(System.getProperty("DO_DEBUG"));

  public MorphemeAnalyzer()
  {
    this.dic = Dictionary.getInstance();
  }

  public List<MExpression> analyze(String string)
    throws Exception
  {
    if (!Util.valid(string)) return null;
    string = string.trim();

    ArrayList ret = new ArrayList();

    List tokenList = Tokenizer.tokenize(string);

    MExpression mePrev = null; MExpression meCur = null;
    int i = 0; for (int stop = tokenList.size(); i < stop; i++) {
      Token token = (Token)tokenList.get(i);
      if (!token.isCharSetOf(CharSetType.SPACE)) {
        List meList = analyze(mePrev, token);

        int j = 0; for (int jStop = meList.size(); j < jStop; j++) {
          meCur = (MExpression)meList.get(j);
          if (mePrev != null) mePrev.pruneWithNext(meCur);
          ret.add(meCur);
          mePrev = meCur;
        }
      }
    }
    return ret;
  }

  private List<MExpression> analyze(MExpression mePrev, Token token)
    throws Exception
  {
    List ret = new ArrayList();

    if (!token.isCharSetOf(CharSetType.HANGUL)) {
      ret.add(new MExpression(token.string, new MCandidate(token)));
      return ret;
    }

    String string = token.string;
    int strlen = string.length();
    MExpression meHeadTemp = null; MExpression meTailTemp = null; MExpression meNew = null;

    MExpression[] meArr = new MExpression[strlen];
    String substr = null; String tail = null;
    int tailCutPos = 1;

    int firstOffset = token.index;
    for (; tailCutPos <= strlen; tailCutPos++)
    {
      substr = string.substring(0, tailCutPos);

      MExpression meCur = meArr[(tailCutPos - 1)] =  = getMExpression(substr, firstOffset);

      meCur.pruneWithPrev(mePrev);

      for (int headCutPos = 1; headCutPos < tailCutPos; headCutPos++) {
        writeLog("==========================================[" + substr + "]");
        tail = substr.substring(headCutPos, tailCutPos);
        meTailTemp = getMExpression(tail, firstOffset + headCutPos);
        meHeadTemp = meArr[(headCutPos - 1)];
        meNew = meHeadTemp.derive(meTailTemp);
        meNew.pruneWithPrev(mePrev);
        writeLog("[     HEAD ] " + meHeadTemp);
        writeLog("[     TAIL ] " + meTailTemp);
        writeLog("[GENERATED ] " + meNew);
        writeLog("[   STORED ] " + meCur);
        meCur.merge(meNew);
        writeLog("[   MERGED ] " + meCur);
        writeLog("==================================================");
      }

      if (tailCutPos != strlen)
      {
        if ((meCur.isComplete()) || (tailCutPos >= 11))
        {
          if ((strlen > 5) && (meCur.size() > 3) && (tailCutPos > 4))
          {
            String strHead = meCur.getCommonHead();

            if (strHead != null)
            {
              writeLog("[COMMON HEAD]==============" + strHead);
              int headLen = strHead.length();
              String tailStr = meCur.getExp().substring(headLen);
              MExpression[] meHeadTail = meCur.divideHeadTailAt(strHead, firstOffset, tailStr, firstOffset + headLen);
              MExpression headME = meHeadTail[0];
              ret.add(headME);
              mePrev = headME;
              writeLog(ret);

              MExpression[] newExps = new MExpression[tailCutPos - headLen];
              int k = headLen; for (int l = 0; k < tailCutPos; l++) {
                meHeadTail = meArr[k].divideHeadTailAt(strHead, token.index, tailStr.substring(0, l + 1), token.index + k);
                newExps[l] = meHeadTail[1];

                k++;
              }

              string = string.substring(strHead.length());
              strlen = string.length();
              meArr = new MExpression[strlen];
              tailCutPos = 0;

              int j = 0; for (int stop = newExps.length; j < stop; j++) {
                meArr[j] = newExps[j];
              }
              tailCutPos = tailStr.length();

              firstOffset += strHead.length();
            }
          }
        }
      }
    }
    if (tailCutPos > 1) ret.add(meArr[(meArr.length - 1)]);

    int i = 0; for (int stop = ret.size(); i < stop; i++) {
      MExpression me = (MExpression)ret.get(i);
      if ((me.size() == 0) || (((MCandidate)me.get(0)).getDicLenOnlyReal() == 0)) {
        me.add(new MCandidate(me.exp, token.index));
      }
    }

    return ret;
  }

  private MExpression getMExpression(String string, int index)
    throws Exception
  {
    MExpression ret = this.dic.getMExpression(string);
    if (ret == null) {
      MCandidate mc = new MCandidate(string, index);
      ret = new MExpression(string, mc);
      ret.setLnprOfSpacing(mc.lnprOfSpacing);
    } else {
      ret.setIndex(index);
    }
    return ret;
  }

  public List<MExpression> postProcess(List<MExpression> melAnalResult)
    throws Exception
  {
    MExpression me1 = null; MExpression me2 = null; MExpression me3 = null;
    for (int i = 1; i < melAnalResult.size(); i++) {
      me1 = (MExpression)melAnalResult.get(i - 1);
      me2 = (MExpression)melAnalResult.get(i);
      if ((!me2.isComplete()) || (me1.isOneEojeolCheckable()) || (me2.isOneEojeolCheckable()))
      {
        if ((me1.isNotHangul()) && (me2.isNotHangul())) {
          MCandidate mc1 = (MCandidate)me1.get(0); MCandidate mc2 = (MCandidate)me2.get(0);

          if (mc1.firstMorp.index + mc1.getExp().length() == mc2.firstMorp.index) {
            me1.exp += me2.exp;
            mc1.addAll(mc2);
            mc1.setExp(me1.exp);
            melAnalResult.remove(i);
            i--;
          }

        }
        else if (me1.isNotHangul()) {
          MCandidate mc1 = (MCandidate)me1.get(0); MCandidate mc2 = (MCandidate)me2.get(0);

          if (mc1.firstMorp.index + mc1.getExp().length() == mc2.firstMorp.index) {
            me3 = me1.derive(me2);
            melAnalResult.remove(i - 1);
            melAnalResult.remove(i - 1);
            melAnalResult.add(i - 1, me3);
            i--;
          }
        }
        else
        {
          me3 = me1.derive(me2);
          if (me3.isOneEojeol()) {
            melAnalResult.remove(i - 1);
            melAnalResult.remove(i - 1);
            melAnalResult.add(i - 1, me3);
            i--;
          }
        }

      }

    }

    me1 = (MExpression)melAnalResult.get(0);
    int i = 0; for (int size = 0; (i < (size = me1.size())) && (size > 1); i++) {
      MCandidate mc = (MCandidate)me1.get(i);
      if (((mc.cclEnc != Condition.ENG) && (mc.cclEnc != 0L)) || 
        (mc.firstMorp.isTagOf(POSTag.J | POSTag.E | POSTag.XS)))
      {
        me1.remove(i);
        i--;
      }
    }

    setBestPrevMC(melAnalResult);

    return melAnalResult;
  }

  private void setBestPrevMC(List<MExpression> meList)
  {
    MExpression mePrev = null; MExpression meCurr = null;
    int i = 0; for (int size = meList.size(); i < size; i++) {
      meCurr = (MExpression)meList.get(i);
      meCurr.setBestPrevMC(mePrev);
      mePrev = meCurr;
    }

    int idx = meList.size() - 1;
    MExpression me = (MExpression)meList.get(idx);
    me.sortByBestLnpr();
    MCandidate mc = (MCandidate)me.get(0);
    for (idx--; (mc != null) && (idx >= 0); idx--) {
      mc = mc.prevBestMC;
      me = (MExpression)meList.get(idx);
      me.remove(mc);
      me.add(0, mc);
    }
  }

  public List<MExpression> leaveJustBest(List<MExpression> meList)
    throws Exception
  {
    MCandidate mc;
    for (int i = 0; i < meList.size(); i++) {
      MExpression me = (MExpression)meList.get(i);
      mc = (MCandidate)me.get(0);
      me.clear();
      me.add(mc);
    }

    List tempMEList = new ArrayList();
    for (MExpression me : meList) {
      tempMEList.addAll(me.split());
    }

    meList.clear();
    meList.addAll(tempMEList);

    return meList;
  }

  public List<Sentence> divideToSentences(List<MExpression> melAnalResult)
    throws Exception
  {
    List ret = new ArrayList();

    MExpression me1 = null;

    Eojeol eojeol = null; Eojeol prevEojeol = null;
    Sentence sentence = null;
    for (int i = 0; i < melAnalResult.size(); i++) {
      if (sentence == null) {
        sentence = new Sentence();
      }
      me1 = (MExpression)melAnalResult.get(i);

      if ((prevEojeol != null) && (((MCandidate)me1.get(0)).isTagOf(POSTag.S)) && 
        (prevEojeol.getStartIndex() + prevEojeol.exp.length() == ((MCandidate)me1.get(0)).firstMorp.index))
      {
        eojeol.addAll((Collection)me1.get(0));
        eojeol.exp += me1.exp;
      } else {
        eojeol = new Eojeol(me1);
        sentence.add(eojeol);
        prevEojeol = eojeol;
      }

      if (eojeol.isEnding()) {
        if (i < melAnalResult.size() - 1) {
          while (i < melAnalResult.size() - 1) {
            me1 = (MExpression)melAnalResult.get(i + 1);
            if ((!me1.getExp().startsWith(".")) && 
              (!me1.getExp().startsWith(",")) && 
              (!me1.getExp().startsWith("!")) && 
              (!me1.getExp().startsWith("?")) && 
              (!me1.getExp().startsWith(";")) && 
              (!me1.getExp().startsWith("~")) && 
              (!me1.getExp().startsWith(")")) && 
              (!me1.getExp().startsWith("]")) && 
              (!me1.getExp().startsWith("}"))) {
              break;
            }
            if (eojeol.firstMorp.index + eojeol.exp.length() == ((MCandidate)me1.get(0)).firstMorp.index)
            {
              eojeol.addAll((Collection)me1.get(0));
              eojeol.exp += me1.exp;
            }
            else
            {
              sentence.add(new Eojeol(me1));
            }
            i++;
          }

        }

        ret.add(sentence);
        sentence = null;
        prevEojeol = null;
      }

    }

    if ((sentence != null) && (sentence.size() > 0)) {
      ret.add(sentence);
    }

    return ret;
  }

  public void createLogger(String fileName)
  {
    try
    {
      System.out.println("DO LOGGING!!");
      if (fileName == null) this.logger = new PrintWriter(System.out, true); else
        this.logger = new PrintWriter(new FileWriter(fileName), true);
      this.doLogging = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeLogger()
  {
    if ((this.doLogging) && (this.logger != null)) this.logger.close();
    this.doLogging = false;
  }

  private void writeLog(Object obj)
  {
    if (DEBUG)
      if (this.logger != null)
        this.logger.println(obj);
      else
        System.out.println(obj);
  }
}