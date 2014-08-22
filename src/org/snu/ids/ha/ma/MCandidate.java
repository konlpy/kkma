package org.snu.ids.ha.ma;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.snu.ids.ha.constants.Condition;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.dic.Dictionary;
import org.snu.ids.ha.dic.PDDictionary;
import org.snu.ids.ha.dic.SpacingPDDictionary;
import org.snu.ids.ha.dic.UNPDDictionary;
import org.snu.ids.ha.util.Hangul;
import org.snu.ids.ha.util.StringSet;
import org.snu.ids.ha.util.Util;

public class MCandidate extends MorphemeList
  implements Comparable<MCandidate>
{
  long atlEnc = 0L;
  long hclEnc = 0L;
  long cclEnc = 0L;
  long eclEnc = 0L;
  long bclEnc = 0L;

  byte realDicLen = 0;
  byte candDicLen = 0;
  byte numOfApndblMC = 0;

  MCandidate prevBestMC = null;
  int diclenOfBestMC = 0;
  float spacingLnprOfBestMC = 0.0F;
  float taggingLnprOfBestMC = 0.0F;

  private ArrayList<String> expList = null;

  int hashCode = 0;

  float lnprOfSpacing = 0.0F;

  float lnprOfTagging = 0.0F;
  public static final String DLMT_ATL = "#";
  public static final String DLMT_HCL = "&";
  public static final String DLMT_BCL = "~";
  public static final String DLMT_CCL = "@";
  public static final String DLMT_ECL = "￢";
  public static final String DLMT_PCL = "%";
  public static final String DLMT_CNL = "$";

  private MCandidate()
  {
    this.expList = new ArrayList();
  }

  MCandidate(String string, int index)
    throws Exception
  {
    this();
    add(new Morpheme(string, index));
    initConds(string);
    setExp(string);
    calcHashCode();

    this.lnprOfTagging = UNPDDictionary.getProb(string);

    calcLnprOfSpacing();
  }

  MCandidate(String string, String tag, String compType)
    throws Exception
  {
    this();
    add(new Morpheme(string, tag, compType));
    initConds(string);
    setExp(string);
    this.realDicLen = ((byte)string.length());
  }

  public MCandidate(String string, String tag)
    throws Exception
  {
    this();
    add(new Morpheme(string, tag, "S"));
    initConds(string);
    setExp(string);
  }

  MCandidate(Token token)
    throws Exception
  {
    this();
    if (token.isCharSetOf(CharSetType.HANGUL)) {
      throw new Exception("Token이 한글입니다.");
    }

    add(new Morpheme(token));
    this.realDicLen = ((byte)token.string.length());
    setExp(token.string);
    initConds();
  }

  public String getTag()
  {
    if (size() == 1) {
      return ((Morpheme)get(0)).getTag();
    }
    return null;
  }

  public boolean isTagOf(long tags)
  {
    if (size() == 1) {
      return ((Morpheme)get(0)).isTagOf(tags);
    }
    return false;
  }

  public String getATL()
  {
    return POSTag.getTagStr(this.atlEnc);
  }

  public long getATLEnc()
  {
    return this.atlEnc;
  }

  public String getHCL()
  {
    return Condition.getCondStr(this.hclEnc);
  }

  public long getHCLEnc()
  {
    return this.hclEnc;
  }

  public String getCCL()
  {
    return Condition.getCondStr(this.cclEnc);
  }

  public long getCCLEnc()
  {
    return this.cclEnc;
  }

  public String getECL()
  {
    return Condition.getCondStr(this.eclEnc);
  }

  public long getECLEnc()
  {
    return this.eclEnc;
  }

  public void initConds(String string)
  {
    addApndblTag(getBasicApndblTags());

    initHavingCond(string);
  }

  public void initHavingCond(String string)
  {
    addHavingCond(getBasicPhonemeConds(string));

    addHavingCond(getBasicHavingConds());
  }

  private void initConds()
  {
    if (!this.lastMorp.isCharSetOf(CharSetType.HANGUL)) {
      if (this.lastMorp.isCharSetOf(CharSetType.ENGLISH)) {
        addHavingCond(Condition.ENG);
      }
      addHavingCond(Condition.SET_FOR_UN);
      addHavingCond(Condition.N);
    }
  }

  public long getBasicApndblTags()
  {
    long tags = 0L;

    if (this.firstMorp.isTagOf(POSTag.NNA | POSTag.XR)) {
      tags |= POSTag.XPN;
    }
    else if (this.firstMorp.isTagOf(POSTag.VV | POSTag.VA | POSTag.XR)) {
      tags |= POSTag.XPV;
    }
    else if (this.firstMorp.isTagOf(POSTag.XSN)) {
      tags |= POSTag.NNA;
    }
    else if (this.firstMorp.isTagOf(POSTag.XSA | POSTag.XSV)) {
      tags |= POSTag.NN | POSTag.XR;
    }
    else if (this.firstMorp.isTagOf(POSTag.NNM | POSTag.NR)) {
      tags |= POSTag.NR;
    }
    else if (this.firstMorp.isTagOf(POSTag.J)) {
      tags |= POSTag.O | POSTag.NR;
    }

    return tags;
  }

  public long getBasicPhonemeConds(String string)
  {
    long cond = 0L;
    char lastCh = string.charAt(string.length() - 1);
    Hangul lastHg = Hangul.split(lastCh);

    if (lastHg.hasJong())
      cond |= Condition.JAEUM;
    else {
      cond |= Condition.MOEUM;
    }

    if (Hangul.MO_POSITIVE_SET.contains(lastHg.jung))
      cond |= Condition.YANGSEONG;
    else {
      cond |= Condition.EUMSEONG;
    }

    if (this.lastMorp.isTagOf(POSTag.VP))
    {
      if (Hangul.MO_DOUBLE_SET.contains(lastHg.jung)) {
        cond |= Condition.JAEUM;
      }

      if (lastCh == 54616) {
        cond |= Condition.HA;
      }
      else if (lastCh == 44032) {
        cond |= Condition.GADA;
      }
      else if (lastCh == 50724) {
        cond |= Condition.ODA;
      }
      else if (lastHg.jong == 'ㄹ') {
        cond |= Condition.LIEUL;
      }

    }
    else if (this.lastMorp.isTagOf(POSTag.N))
    {
      if (lastHg.jong == 'ㄹ') {
        cond |= Condition.LIEUL;
      }

    }
    else if (this.lastMorp.isTagOf(POSTag.ET)) {
      if (this.lastMorp.string.equals("ㄴ"))
        cond |= Condition.NIEUN;
      else if (this.lastMorp.string.equals("ㄹ"))
        cond |= Condition.LIEUL;
      else if (this.lastMorp.string.equals("ㅁ")) {
        cond |= Condition.MIEUM;
      }
    }

    return cond;
  }

  public long getBasicHavingConds()
  {
    long cond = 0L;

    if (this.lastMorp.isTagOf(POSTag.N | POSTag.ETN))
    {
      cond |= Condition.N;
    }
    else if (this.lastMorp.isTagOf(POSTag.MD | POSTag.ETD)) {
      cond |= Condition.D;
    }
    else if (this.lastMorp.isTagOf(POSTag.MA | POSTag.JKM)) {
      cond |= Condition.A;
    }
    else if (this.lastMorp.isTagOf(POSTag.ECS | POSTag.ECD)) {
      cond |= Condition.EC;
    }

    return cond;
  }

  public MCandidate copy()
  {
    MCandidate clone = new MCandidate();
    clone.addAll(this);
    clone.expList.addAll(this.expList);
    clone.atlEnc = this.atlEnc;
    clone.hclEnc = this.hclEnc;
    clone.cclEnc = this.cclEnc;
    clone.bclEnc = this.bclEnc;
    clone.eclEnc = this.eclEnc;
    clone.candDicLen = this.candDicLen;
    clone.realDicLen = this.realDicLen;
    clone.numOfApndblMC = this.numOfApndblMC;
    clone.spacingLnprOfBestMC = this.spacingLnprOfBestMC;
    clone.prevBestMC = this.prevBestMC;
    clone.lnprOfSpacing = this.lnprOfSpacing;
    clone.lnprOfTagging = this.lnprOfTagging;
    clone.hashCode = this.hashCode;
    return clone;
  }

  public void setIndex(int index)
  {
    Morpheme mp = null;
    int offset = 0;
    int i = 0; for (int size = size(); i < size; i++) {
      mp = (Morpheme)get(i);
      mp.setIndex(index + offset);
      offset += mp.string.length();
    }
  }

  public void addAll(MorphemeList mpList)
  {
    int i = 0; for (int stop = mpList.size(); i < stop; i++)
      add(((Morpheme)mpList.get(i)).copy());
  }

  public void addApndblTag(String tag)
  {
    addApndblTag(POSTag.getTagNum(tag));
  }

  public void addApndblTag(long tagNum)
  {
    this.atlEnc |= tagNum;
  }

  public void addApndblTags(String[] tags)
  {
    int i = 0; for (int stop = tags.length; i < stop; i++)
      addApndblTag(tags[i]);
  }

  public void addHavingCond(String cond)
  {
    addHavingCond(Condition.getCondNum(cond));
  }

  public void addHavingConds(String[] conds)
  {
    int i = 0; for (int stop = conds.length; i < stop; i++)
      addHavingCond(conds[i]);
  }

  public void addHavingCond(long condNum)
  {
    this.hclEnc |= condNum;
    if ((this.lastMorp.isTag(POSTag.ETD)) && (Condition.checkAnd(this.hclEnc, Condition.NIEUN))) {
      this.bclEnc |= Condition.NIEUN;
    } else if ((this.lastMorp.isTag(POSTag.ETD)) && (Condition.checkAnd(this.hclEnc, Condition.LIEUL))) {
      this.bclEnc |= Condition.LIEUL;
    } else if ((this.lastMorp.isTagOf(POSTag.ETN)) && (Condition.checkAnd(this.hclEnc, Condition.MIEUM))) {
      this.bclEnc |= Condition.MIEUM;
    } else if (this.lastMorp.isTagOf(POSTag.V))
    {
      this.bclEnc |= this.hclEnc & Condition.MINUS_JA_SET;

      if ((Condition.checkAnd(this.hclEnc, Condition.BIEUB)) && (!Hangul.endsWith(this.lastMorp.string, "ㅂ"))) this.bclEnc |= Condition.BIEUB;
    }
  }

  public boolean isHavingCond(long condNum)
  {
    return Condition.checkAnd(this.hclEnc, condNum);
  }

  public void clearHavingCondition()
  {
    this.hclEnc = 0L;
    this.bclEnc = 0L;
  }

  public void addChkCond(String cond)
  {
    this.cclEnc |= Condition.getCondNum(cond);
  }

  void addChkConds(String[] conds)
  {
    int i = 0; for (int stop = conds.length; i < stop; i++)
      addChkCond(conds[i]);
  }

  void addExclusionCond(String cond)
  {
    this.eclEnc |= Condition.getCondNum(cond);
  }

  void addExclusionConds(String[] conds)
  {
    int i = 0; for (int stop = conds.length; i < stop; i++)
      addExclusionCond(conds[i]);
  }

  private boolean isCondExclusive(long exlCondEnc)
  {
    if (exlCondEnc == 0L) return false;
    return Condition.checkOr(this.hclEnc, exlCondEnc);
  }

  public void setExp(String exp)
  {
    this.expList.clear();
    this.expList.add(exp);
  }

  public String getExp()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = this.expList.size(); i < stop; i++) {
      if (i > 0) sb.append(" ");
      sb.append((String)this.expList.get(i));
    }
    return sb.toString();
  }

  public int getSpaceCnt()
  {
    return this.expList.size() - 1;
  }

  public char getFirstSyllable()
  {
    String str = (String)this.expList.get(0);
    return str.charAt(0);
  }

  public char getLastSyllable()
  {
    String str = (String)this.expList.get(this.expList.size() - 1);
    return str.charAt(str.length() - 1);
  }

  String getExp(int toIdx)
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = Math.min(this.expList.size(), toIdx + 1); i < stop; i++) {
      sb.append((String)this.expList.get(i));
    }
    return sb.toString();
  }

  String getHead(String head)
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = this.expList.size(); i < stop; i++) {
      sb.append((String)this.expList.get(i));
      if (sb.toString().equals(head)) return head;
    }
    return null;
  }

  String geExpStrWithSpace()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = this.expList.size(); i < stop; i++) {
      if (i > 0) sb.append(" ");
      sb.append((String)this.expList.get(i));
    }
    return sb.toString();
  }

  public boolean isApndbl(MCandidate mcToAppend)
  {
    boolean ret = !isHavingCond(Condition.F);
    if (ret) ret = this.lastMorp.isTagOf(mcToAppend.atlEnc);
    if (ret) ret = Condition.checkAnd(this.hclEnc, mcToAppend.cclEnc);
    if (ret) ret = !isCondExclusive(mcToAppend.eclEnc);

    if ((ret) && (mcToAppend.firstMorp.isTagOf(POSTag.E))) {
      ret = Condition.checkAnd(mcToAppend.cclEnc, this.bclEnc);
    }
    return ret;
  }

  boolean isApndblWithSpace(MCandidate mcToAppend)
  {
    if ((this.lastMorp.isTagOf(POSTag.V | POSTag.EP | POSTag.XP)) || 
      (mcToAppend.firstMorp.isTagOf(POSTag.E | POSTag.XS | POSTag.VCP | POSTag.J)) || 
      (mcToAppend.isHavingCond(Condition.SHORTEN)))
    {
      return false;
    }

    return true;
  }

  public MCandidate derive(MCandidate mcToAppend)
  {
    boolean isApndbl = isApndbl(mcToAppend);

    if ((isApndbl) && (this.lastMorp.isCharSetOf(CharSetType.ENGLISH)) && 
      (this.lastMorp.index + this.lastMorp.string.length() != mcToAppend.firstMorp.index)) return null;

    boolean isApndblWithSpace = isApndblWithSpace(mcToAppend);

    if ((!isApndbl) && (!isApndblWithSpace)) return null;

    if (isApndbl)
    {
      if (((this.lastMorp.isTag(POSTag.XPN)) || (mcToAppend.firstMorp.isTag(POSTag.XSN))) && 
        (Dictionary.getInstance().containNoun(this.lastMorp.string + mcToAppend.firstMorp.string))) return null;

    }

    MCandidate mcNew = new MCandidate();

    mcNew = new MCandidate();
    mcNew.addAll(this);
    mcNew.addAll(mcToAppend);
    mcNew.expList.addAll(this.expList);
    mcNew.atlEnc = this.atlEnc;
    mcNew.hclEnc = mcToAppend.hclEnc;
    mcNew.bclEnc = mcToAppend.bclEnc;
    mcNew.cclEnc = this.cclEnc;
    mcNew.eclEnc = this.eclEnc;

    if (!isApndbl)
    {
      mcNew.add(size(), new MorphemeSpace(mcToAppend.atlEnc, this.hclEnc, this.bclEnc, mcToAppend.cclEnc, mcToAppend.eclEnc));
      mcNew.expList.add("");
    }

    mcNew.expList.add(
      (String)mcNew.expList.remove(mcNew.expList.size() - 1) + 
      (String)mcToAppend.expList.get(0));

    mcNew.expList.addAll(mcToAppend.expList.subList(1, mcToAppend.expList.size()));

    if (isApndbl)
    {
      ((Morpheme)mcNew.get(size() - 1)).infoEnc &= (0x0 | mcToAppend.atlEnc);

      if ((this.lastMorp.isTagOf(POSTag.EM)) && (mcToAppend.firstMorp.isTagOf(POSTag.EM))) {
        mcNew.mergeAt(size() - 1);
      }
      else if (mcToAppend.firstMorp.isTagOf(POSTag.XSM)) {
        mcNew.mergeAt(size() - 1);
      }

    }

    float lnpr = SpacingPDDictionary.getProb(getLastSyllable(), mcToAppend.getFirstSyllable(), !isApndbl);

    mcNew.setLnprOfSpacing(this.lnprOfSpacing + mcToAppend.lnprOfSpacing + lnpr);
    mcNew.calcLnprOfTagging();

    mcNew.calcLnprOfTagging();

    mcNew.calcDicLen();

    return mcNew;
  }

  List<MCandidate> split()
  {
    if ((get(0) instanceof MorphemeSpace)) {
      this.expList.remove(0);
      remove(0);
    }

    ArrayList ret = new ArrayList();
    MCandidate mc = new MCandidate();
    mc.atlEnc = this.atlEnc;
    mc.cclEnc = this.cclEnc;
    mc.eclEnc = this.eclEnc;
    Morpheme mp = null;
    int expIdx = 0;
    int i = 0; for (int stop = size(); i < stop; i++) {
      mp = (Morpheme)get(i);
      if ((mp instanceof MorphemeSpace)) {
        if (i != 0) {
          mc.setExp((String)this.expList.get(expIdx));
          MorphemeSpace mps = (MorphemeSpace)mp;
          mc.hclEnc = mps.hclEnc;
          mc.bclEnc = mps.bclEnc;
          mc.calcDicLen();
          mc.calcLnprOfSpacing();
          mc.calcLnprOfTagging();
          expIdx++;
          ret.add(mc);

          mc = new MCandidate();
          mc.atlEnc = mps.atlEnc;
          mc.cclEnc = mps.cclEnc;
          mc.eclEnc = mps.eclEnc;
        }
      } else mc.add(mp);
    }

    mc.setExp((String)this.expList.get(expIdx));
    mc.hclEnc = this.hclEnc;
    mc.bclEnc = this.bclEnc;
    mc.calcDicLen();
    mc.calcLnprOfSpacing();
    mc.calcLnprOfTagging();
    ret.add(mc);
    return ret;
  }

  MCandidate[] divideHeadTailAt(String headStr, int headIdx, String tailStr, int tailIdx)
    throws Exception
  {
    int divideIdx = 0;
    boolean dividable = false;
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = this.expList.size(); i < stop; i++) {
      sb.append((String)this.expList.get(i));
      if (sb.toString().equals(headStr)) {
        dividable = true;
        break;
      }
      divideIdx++;
    }
    if (!dividable) {
      return new MCandidate[] { new MCandidate(headStr, headIdx), new MCandidate(tailStr, tailIdx) };
    }

    MCandidate[] ret = new MCandidate[2];

    MCandidate headMC = ret[0] =  = new MCandidate();
    MCandidate tailMC = ret[1] =  = new MCandidate();

    headMC.atlEnc = this.atlEnc;
    headMC.cclEnc = this.cclEnc;
    headMC.eclEnc = this.eclEnc;

    int spaceIdx = 0;
    int idx = 0; int stop = size(); int accIdx = 0;
    for (; idx < stop; idx++) {
      Morpheme mp = (Morpheme)get(idx);
      if ((mp instanceof MorphemeSpace)) {
        if (spaceIdx < divideIdx) {
          headMC.add(mp);
          spaceIdx++;
        }
        else {
          int j = 0; for (int jStop = divideIdx + 1; j < jStop; j++) {
            headMC.expList.add((String)this.expList.get(j));
          }

          MorphemeSpace mps = (MorphemeSpace)mp;
          headMC.hclEnc = mps.hclEnc;
          headMC.bclEnc = mps.bclEnc;

          tailMC.atlEnc = mps.atlEnc;
          tailMC.hclEnc = this.hclEnc;
          tailMC.bclEnc = this.bclEnc;
          tailMC.cclEnc = mps.bclEnc;
          tailMC.eclEnc = mps.eclEnc;
          idx++;
          break;
        }
      } else { mp.setIndex(headIdx + accIdx);
        accIdx += mp.getString().length();
        headMC.add(mp);
      }
    }

    if (idx < stop) {
      for (; idx < stop; idx++) {
        tailMC.add((Morpheme)get(idx));
      }

      int i = divideIdx + 1; for (int iStop = this.expList.size(); i < iStop; i++) {
        tailMC.expList.add((String)this.expList.get(i));
      }
    }
    headMC.calcDicLen();
    headMC.calcLnprOfSpacing();
    headMC.calcLnprOfTagging();
    tailMC.calcDicLen();
    tailMC.calcLnprOfSpacing();
    tailMC.calcLnprOfTagging();

    return ret;
  }

  boolean isUNBfrOrAftrIthSpace(int idx)
  {
    int spaceIdx = 0;
    int i = 0; for (int stop = size() - 1; i < stop; i++) {
      Morpheme mp = (Morpheme)get(i);
      if ((mp instanceof MorphemeSpace))
        if (spaceIdx != idx) {
          spaceIdx++;
        }
        else {
          mp = (Morpheme)get(i + 1);

          return (((Morpheme)get(i + 1)).isTag(POSTag.UN)) || 
            (((Morpheme)get(i - 1)).isTag(POSTag.UN));
        }
    }
    return false;
  }

  public int hashCode()
  {
    return this.hashCode;
  }

  public void calcHashCode()
  {
    this.hashCode = getEncStr().hashCode();
  }

  public boolean equals(Object obj)
  {
    return hashCode() == obj.hashCode();
  }

  public int compareTo(MCandidate comp)
  {
    if (getDicLenWithCand() != comp.getDicLenWithCand()) {
      return comp.getDicLenWithCand() - getDicLenWithCand();
    }

    if (getLnpr() > comp.getLnpr())
      return -1;
    if (getLnpr() < comp.getLnpr()) {
      return 1;
    }
    return 0;
  }

  public float getLnpr()
  {
    return this.lnprOfSpacing + this.lnprOfTagging;
  }

  int getDicLenOnlyReal()
  {
    return this.realDicLen;
  }

  int getDicLenWithCand()
  {
    return this.candDicLen + this.realDicLen;
  }

  int getDicLenOnlyCand()
  {
    return this.candDicLen;
  }

  boolean isComplete()
    throws Exception
  {
    return this.candDicLen == 0;
  }

  private void calcDicLen()
  {
    byte size = (byte)size();

    this.realDicLen = 0;
    this.candDicLen = 0;

    int expIdx = 0;
    int nrDicLen = 0;
    boolean hasPreWord = false; boolean hasJo = false;
    boolean hasStem = false; boolean hasEP = false; boolean hasEM = false;

    Morpheme mp = null;
    int i = 0; for (int stop = size + 1; i < stop; i++) {
      if (i < size)
        mp = (Morpheme)get(i);
      else {
        mp = null;
      }
      if ((mp == null) || ((mp instanceof MorphemeSpace)))
      {
        boolean complete = (!hasEP) || (!(hasStem ^ hasEM));

        complete = (complete) && ((!hasJo) || (hasPreWord));

        if (complete)
          this.realDicLen = ((byte)(this.realDicLen + (((String)this.expList.get(expIdx)).length() - nrDicLen)));
        else {
          this.candDicLen = ((byte)(this.candDicLen + (((String)this.expList.get(expIdx)).length() - nrDicLen)));
        }

        if (mp == null)
        {
          if ((size == 2) && (this.lastMorp.isTagOf(POSTag.J)) && (this.firstMorp.isTag(POSTag.UN))) {
            this.candDicLen = ((byte)(this.candDicLen + (nrDicLen - 1)));
          }

        }

        hasPreWord = false;
        hasJo = false;
        hasStem = false;
        hasEP = false;
        hasEM = false;
        nrDicLen = 0;

        expIdx++;
      }
      else if (mp.isTagOf(POSTag.V)) {
        hasStem = true;
        hasPreWord = true;

        if (mp.isTag(POSTag.VCP)) hasJo = true;

      }
      else if (mp.isTagOf(POSTag.EP)) {
        hasEP = true;
        hasPreWord = true;
      }
      else if (mp.isTagOf(POSTag.EM)) {
        hasEM = true;
        hasPreWord = true;

        if (mp.isTag(POSTag.ETN)) hasPreWord = true;

      }
      else if (mp.isTagOf(POSTag.J)) {
        hasJo = true;
      }
      else if (mp.isTag(POSTag.UN)) {
        hasPreWord = true;
        nrDicLen += mp.string.length();
      }
      else
      {
        hasPreWord = true;
      }

    }

    calcHashCode();
  }

  public static MCandidate create(String exp, String source)
  {
    MCandidate mCandidate = new MCandidate();
    mCandidate.setExp(exp);
    StringTokenizer st = new StringTokenizer(source, "[]", false);

    String token = null; String infos = "";
    String[] arr = (String[])null;
    for (int i = 0; st.hasMoreTokens(); i++) {
      token = st.nextToken();
      if (i == 0) {
        arr = token.split("\\+");
        for (int j = 0; j < arr.length; j++)
        {
          if (arr[j].startsWith(" ")) {
            mCandidate.add(new MorphemeSpace(arr[j]));
            mCandidate.expList.add(0, "");
          }
          else
          {
            mCandidate.add(Morpheme.create(arr[j]));
          }
        }
      } else {
        infos = token;
      }

    }

    st = new StringTokenizer(infos, "*#~&@￢%", true);
    while (st.hasMoreTokens()) {
      token = st.nextToken();

      if (token.equals("#")) {
        token = st.nextToken().trim();
        token = token.substring(1, token.length() - 1);
        mCandidate.addApndblTags(token.split(","));
      }
      else if (token.equals("&")) {
        token = st.nextToken().trim();
        token = token.substring(1, token.length() - 1);
        mCandidate.addHavingConds(token.split(","));
      }
      else if (token.equals("@")) {
        token = st.nextToken().trim();
        token = token.substring(1, token.length() - 1);
        mCandidate.addChkConds(token.split(","));
      }
      else if (token.equals("￢")) {
        token = st.nextToken().trim();
        token = token.substring(1, token.length() - 1);
        mCandidate.addExclusionConds(token.split(","));
      }
    }
    mCandidate.initConds(exp);
    mCandidate.calcDicLen();
    return mCandidate;
  }

  public static MCandidate create(String exp, String analResult, String atl, String hcl, String ccl, String ecl)
  {
    MCandidate mCandidate = new MCandidate();
    mCandidate.setExp(exp);

    String[] arr = analResult.split("\\+");
    for (int j = 0; j < arr.length; j++)
    {
      if (arr[j].startsWith(" ")) {
        mCandidate.add(new MorphemeSpace(arr[j]));
        mCandidate.expList.add(0, "");
      }
      else
      {
        mCandidate.add(Morpheme.create(arr[j]));
      }
    }

    mCandidate.initConds(exp);
    mCandidate.calcDicLen();

    if (Util.valid(atl)) mCandidate.addApndblTags(atl.split(","));

    if (Util.valid(hcl)) mCandidate.addHavingConds(hcl.split(","));

    if (Util.valid(ccl)) mCandidate.addChkConds(ccl.split(","));

    if (Util.valid(ecl)) mCandidate.addExclusionConds(ecl.split(","));

    return mCandidate;
  }

  public String toString()
  {
    return getString();
  }

  public String getString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append(String.format("%4d", new Object[] { Byte.valueOf(this.realDicLen) }));
    sb.append(String.format("%4d", new Object[] { Byte.valueOf(this.candDicLen) }));
    sb.append(String.format("%4d", new Object[] { Integer.valueOf(size()) }));
    sb.append(String.format("%4d", new Object[] { Integer.valueOf(this.diclenOfBestMC) }));
    sb.append(String.format("%10.3f", new Object[] { Float.valueOf(this.spacingLnprOfBestMC) }));
    sb.append(String.format("%10.3f", new Object[] { Float.valueOf(this.taggingLnprOfBestMC) }));
    sb.append(String.format("%10.3f", new Object[] { Float.valueOf(this.lnprOfSpacing) }));
    sb.append(String.format("%10.3f  ", new Object[] { Float.valueOf(this.lnprOfTagging) }));

    sb.append("[" + super.toString() + "]");

    String temp = POSTag.getZipTagStr(this.atlEnc);
    if (temp != null) sb.append("#(" + temp + ")");

    temp = Condition.getCondStr(this.hclEnc);
    if (temp != null) sb.append("&(" + temp + ")");

    temp = Condition.getCondStr(this.bclEnc);
    if (temp != null) sb.append("~(" + temp + ")");

    temp = Condition.getCondStr(this.cclEnc);
    if (temp != null) sb.append("@(" + temp + ")");

    temp = Condition.getCondStr(this.eclEnc);
    if (temp != null) sb.append("￢(" + temp + ")");

    sb.append("\t" + this.hashCode);

    sb.append("\t" + this.expList);

    if (this.prevBestMC != null) {
      sb.append("\t" + this.prevBestMC.lastMorp.getTag());
    }

    return sb.toString();
  }

  public String getSmplDicStr(String compResult)
  {
    StringBuffer sb = new StringBuffer();

    long mask = -1L;
    long basicATL = getBasicApndblTags();
    long basicHCL = getBasicHavingConds() | getBasicPhonemeConds(getExp());

    sb.append(super.getSmplStr2());

    StringBuffer sb2 = new StringBuffer();

    String temp = POSTag.getZipTagStr(this.atlEnc & (0xFFFFFFFF ^ basicATL));
    if (temp != null) sb2.append("#(" + temp + ")");

    temp = Condition.getCondStr(this.hclEnc & (0xFFFFFFFF ^ basicHCL));
    if (temp != null) sb2.append("&(" + temp + ")");

    temp = Condition.getCondStr(this.cclEnc);
    if (temp != null) sb2.append("@(" + temp + ")");

    temp = Condition.getCondStr(this.eclEnc);
    if (temp != null) sb2.append("￢(" + temp + ")");

    if (Util.valid(compResult)) sb2.append("$(" + compResult + ")");

    if (sb2.length() > 0) {
      sb.append(";");
      sb.append(sb2);
    }

    return sb.toString();
  }

  public String getRawDicStr()
  {
    StringBuffer sb = new StringBuffer();

    long mask = -1L;
    long basicATL = getBasicApndblTags();
    long basicHCL = getBasicHavingConds() | getBasicPhonemeConds(getExp());

    String temp = null;

    sb.append(getExp() + ":{[" + super.getSmplStr2() + "]");

    if ((temp = POSTag.getZipTagStr(this.atlEnc & (0xFFFFFFFF ^ basicATL))) != null) sb.append("#(" + temp + ")");

    if ((temp = Condition.getCondStr(this.hclEnc & (0xFFFFFFFF ^ basicHCL))) != null) sb.append("&(" + temp + ")");

    if ((temp = Condition.getCondStr(this.cclEnc)) != null) sb.append("@(" + temp + ")");

    temp = Condition.getCondStr(this.eclEnc);
    if ((temp = Condition.getCondStr(this.eclEnc)) != null) sb.append("￢(" + temp + ")");
    sb.append("}");

    return sb.toString();
  }

  public String toSimpleStr()
  {
    return super.toString();
  }

  String getEncStr()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(super.getEncStr());
    sb.append("!" + this.atlEnc);
    sb.append("!" + this.hclEnc);
    sb.append("!" + this.cclEnc);
    sb.append("!" + this.eclEnc);
    return sb.toString();
  }

  boolean merge(MCandidate mc)
  {
    int size = size();
    if (size != mc.size()) return false;
    if (this.atlEnc != mc.atlEnc) return false;
    if (this.hclEnc != mc.hclEnc) return false;
    if (this.cclEnc != mc.cclEnc) return false;
    if (this.eclEnc != mc.eclEnc) return false;

    Morpheme mp1 = null; Morpheme mp2 = null; Morpheme catchedMp1 = null; Morpheme catchedMp2 = null;

    for (int i = 0; i < size; i++) {
      mp1 = (Morpheme)get(i);
      mp2 = (Morpheme)mc.get(i);

      if (!mp1.string.equals(mp2.string)) return false;
      if (mp1.infoEnc != mp2.infoEnc) {
        if (catchedMp1 != null) return false;
        catchedMp1 = mp1;
        catchedMp2 = mp2;
      }
    }

    if (catchedMp1 == null) return true;

    catchedMp1.infoEnc |= catchedMp2.infoEnc;

    return true;
  }

  public void setBestPrevMC(MCandidate mcPrev)
  {
    if (mcPrev == null) {
      this.prevBestMC = null;
      this.diclenOfBestMC = getDicLenWithCand();
      this.spacingLnprOfBestMC = this.lnprOfSpacing;
      this.taggingLnprOfBestMC = (PDDictionary.getLnprPosGMorpInter(POSTag.BOS, this.firstMorp.string, this.firstMorp.getTagNum()) + this.lnprOfTagging);
      return;
    }

    boolean apndbl = false;

    if ((mcPrev.lastMorp.isTagOf(POSTag.EF)) && (this.firstMorp.isTagOf(POSTag.SF))) apndbl = true;

    int newBestDicLen = mcPrev.diclenOfBestMC + mcPrev.getDicLenWithCand();

    float newBestSpacingLnpr = mcPrev.spacingLnprOfBestMC + this.lnprOfSpacing;
    newBestSpacingLnpr += SpacingPDDictionary.getProb(mcPrev.getLastSyllable(), getFirstSyllable(), !apndbl);

    float newBestTaggingLnpr = mcPrev.taggingLnprOfBestMC + this.lnprOfTagging;
    if (apndbl)
    {
      newBestTaggingLnpr += PDDictionary.getLnprPosGMorpIntra(mcPrev.lastMorp.getTagNum(), this.firstMorp.string, this.firstMorp.getTagNum());
    }
    else newBestTaggingLnpr += PDDictionary.getLnprPosGMorpInter(mcPrev.lastMorp.getTagNum(), this.firstMorp.string, this.firstMorp.getTagNum());

    if (this.prevBestMC == null) {
      this.prevBestMC = mcPrev;
      this.diclenOfBestMC = newBestDicLen;
      this.spacingLnprOfBestMC = newBestSpacingLnpr;
      this.taggingLnprOfBestMC = newBestTaggingLnpr;
    }
    else if (newBestDicLen > this.diclenOfBestMC) {
      this.prevBestMC = mcPrev;
      this.diclenOfBestMC = newBestDicLen;
      this.spacingLnprOfBestMC = newBestSpacingLnpr;
      this.taggingLnprOfBestMC = newBestTaggingLnpr;
    }
    else if ((newBestDicLen == this.diclenOfBestMC) && 
      (newBestSpacingLnpr + newBestTaggingLnpr > this.spacingLnprOfBestMC + this.taggingLnprOfBestMC))
    {
      this.prevBestMC = mcPrev;
      this.diclenOfBestMC = newBestDicLen;
      this.spacingLnprOfBestMC = newBestSpacingLnpr;
      this.taggingLnprOfBestMC = newBestTaggingLnpr;
    }
  }

  public void calcLnprOfSpacing()
  {
    this.lnprOfSpacing = SpacingPDDictionary.getProb(getExp());
  }

  public void calcLnprOfTagging()
  {
    this.lnprOfTagging = 0.0F;

    boolean isApndbl = true;
    Morpheme prevMp = null;
    Morpheme mp = null;
    int i = 0; for (int size = size(); i < size; i++) {
      mp = (Morpheme)get(i);

      if ((mp instanceof MorphemeSpace)) {
        isApndbl = false;
      }
      else
      {
        this.lnprOfTagging += PDDictionary.getLnprPosGExp(mp.string, mp.getTagNum());

        if (prevMp != null)
        {
          if (isApndbl)
          {
            if ((!prevMp.isTagOf(POSTag.XS)) || (!(mp.getString().equals("ㄴ") | mp.getString().equals("ㅁ") | mp.getString().equals("ㄹ"))))
            {
              this.lnprOfTagging += PDDictionary.getLnprPosGMorpIntra(prevMp.getTagNum(), mp.string, mp.getTagNum());
            }
          }
          else
          {
            this.lnprOfTagging += PDDictionary.getLnprPosGMorpInter(prevMp.getTagNum(), mp.string, mp.getTagNum());
            isApndbl = true;
          }

        }

        prevMp = mp;
      }
    }
  }

  public boolean isFirstTagOf(long tagEnc)
  {
    return this.firstMorp.isTagOf(tagEnc);
  }

  public boolean isNotHangul()
  {
    return !this.lastMorp.isCharSetOf(CharSetType.HANGUL);
  }

  public byte getRealDicLen()
  {
    return this.realDicLen;
  }

  public void setRealDicLen(byte realDicLen)
  {
    this.realDicLen = realDicLen;
  }

  public byte getCandDicLen()
  {
    return this.candDicLen;
  }

  public void setCandDicLen(byte candDicLen)
  {
    this.candDicLen = candDicLen;
  }

  public void setLnprOfSpacing(float lnprOfSpacing)
  {
    this.lnprOfSpacing = lnprOfSpacing;
  }

  public float getBestLnpr()
  {
    return this.spacingLnprOfBestMC + this.taggingLnprOfBestMC;
  }
}