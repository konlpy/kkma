package org.snu.ids.ha.ma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.snu.ids.ha.util.Util;

public class MExpression extends ArrayList<MCandidate>
  implements Comparable<MExpression>
{
  String exp = null;
  float lnprOfSpacing = 0.0F;
  private static final int PRUNE_SIZE = 12;

  MExpression(String exp)
  {
    this.exp = exp;
  }

  public MExpression(String exp, MCandidate mc)
    throws Exception
  {
    this(exp);
    add(mc);
  }

  MExpression(MCandidate mc)
    throws Exception
  {
    this(mc.getExp());
    add(mc);
  }

  public boolean add(MCandidate mc)
  {
    if ((mc != null) && (!contains(mc)))
    {
      return super.add(mc);
    }
    return false;
  }

  public boolean add2(MCandidate mc)
  {
    if ((mc != null) && (!contains(mc))) return super.add(mc);
    return false;
  }

  public String getExp()
  {
    return this.exp;
  }

  void setIndex(int index)
  {
    int i = 0; for (int size = size(); i < size; i++)
      ((MCandidate)get(i)).setIndex(index);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer(this.exp + Util.LINE_SEPARATOR);
    sb.append(String.format("\t %4s%4s%4s%4s%10s%10s%10s%10s", new Object[] { "rdl", "cdl", "siz", "dic", "spacing", "tagging", "spacing!", "tagging!" }));
    sb.append(Util.LINE_SEPARATOR);

    int i = 0; for (int stop = size(); i < stop; i++) {
      sb.append("\t{" + get(i) + "};" + Util.LINE_SEPARATOR);
    }
    return sb.toString();
  }

  public String toSmplStr()
  {
    StringBuffer sb = new StringBuffer(this.exp + Util.LINE_SEPARATOR);
    int i = 0; for (int stop = size(); i < stop; i++) {
      sb.append("\t{" + ((MCandidate)get(i)).toSimpleStr() + "};" + Util.LINE_SEPARATOR);
    }
    return sb.toString();
  }

  String getEncStr()
  {
    StringBuffer sb = new StringBuffer(this.exp + ":");
    int i = 0; for (int stop = size(); i < stop; i++) {
      if (i > 0) sb.append(";");
      sb.append(((MCandidate)get(i)).getEncStr());
    }
    return sb.toString();
  }

  MExpression derive(MExpression meToAppend)
  {
    MExpression ret = new MExpression(this.exp + meToAppend.exp);
    MCandidate mcThis = null; MCandidate mcToAppend = null;
    int jStop = meToAppend.size();
    int i = 0; for (int iStop = size(); i < iStop; i++) {
      mcThis = (MCandidate)get(i);
      for (int j = 0; j < jStop; j++) {
        mcToAppend = (MCandidate)meToAppend.get(j);
        ret.add(mcThis.derive(mcToAppend));
      }
    }
    ret.prune();
    return ret;
  }

  void prune()
  {
    int size = size();
    if (size < 2) return;

    int maxDicLen = -1;
    int expLen = this.exp.length(); int tempDicLen = 0;

    sort();
    MCandidate mcBest = (MCandidate)get(0);

    float bestLnpr = mcBest.getLnpr();

    boolean uncomplete = (mcBest.candDicLen > 0) || (expLen > mcBest.getDicLenWithCand());

    if ((!uncomplete) && (size < 12)) {
      return;
    }

    int pruneIdx = 1;
    for (int stop = size(); pruneIdx < stop; ) {
      MCandidate mcToPrune = (MCandidate)get(pruneIdx);

      tempDicLen = mcToPrune.getDicLenWithCand();

      if ((uncomplete) && (mcToPrune.getDicLenOnlyCand() == 0) && (pruneIdx < 12)) {
        pruneIdx++;
      }
      else
      {
        if (mcToPrune.getLnpr() < bestLnpr - 5.0F) {
          break;
        }
        if (tempDicLen < maxDicLen)
        {
          break;
        }
        pruneIdx++;
      }
    }

    int i = pruneIdx; for (int stop = size(); i < stop; i++)
    {
      if ((uncomplete) && (i == stop - 1) && (((MCandidate)get(pruneIdx)).realDicLen == 0) && (((MCandidate)get(pruneIdx)).getExp().length() < 10)) break;
      remove(pruneIdx);
    }
  }

  void pruneWithPrev(MExpression mePrev)
    throws Exception
  {
    if (mePrev == null) return;
    int thisMESize = size(); int preMESize = mePrev.size();
    if (preMESize == 0) return;
    for (int i = 0; i < thisMESize; i++) {
      MCandidate mcThis = (MCandidate)get(i);
      mcThis.numOfApndblMC = 0;
      for (int j = 0; j < preMESize; j++) {
        MCandidate preMC = (MCandidate)mePrev.get(j);

        if ((preMC.isApndblWithSpace(mcThis)) || (preMC.isApndbl(mcThis)))
        {
          MCandidate tmp82_80 = mcThis; tmp82_80.numOfApndblMC = ((byte)(tmp82_80.numOfApndblMC + 1));
          break;
        }
      }
      if (mcThis.numOfApndblMC == 0) {
        remove(i);
        i--;
        thisMESize--;
      }
    }
  }

  void pruneWithNext(MExpression nextME)
    throws Exception
  {
    int thisMESize = size(); int nextMESize = nextME.size();
    if (nextMESize == 0) return;
    for (int i = 0; i < thisMESize; i++) {
      MCandidate thisMC = (MCandidate)get(i);
      thisMC.numOfApndblMC = 0;
      for (int j = 0; j < nextMESize; j++) {
        MCandidate nextMC = (MCandidate)nextME.get(j);

        if (thisMC.isApndblWithSpace(nextMC))
        {
          MCandidate tmp67_65 = thisMC; tmp67_65.numOfApndblMC = ((byte)(tmp67_65.numOfApndblMC + 1));
          break;
        }
      }
      if ((thisMC.numOfApndblMC == 0) && (size() > 1)) {
        remove(i);
        i--;
        thisMESize--;
      }
    }
  }

  MExpression[] divideHeadTailAt(String headStr, int headIndex, String tailStr, int tailIndex)
    throws Exception
  {
    MExpression[] ret = new MExpression[2];
    MExpression headME = ret[0] =  = new MExpression(headStr);
    MExpression tailME = ret[1] =  = new MExpression(tailStr);

    int j = 0; for (int stop = size(); j < stop; j++) {
      MCandidate[] mcHeadTail = ((MCandidate)get(j)).divideHeadTailAt(headStr, headIndex, tailStr, tailIndex);
      if (mcHeadTail != null) {
        headME.add(mcHeadTail[0]);
        tailME.add(mcHeadTail[1]);
      }
    }
    return ret;
  }

  void merge(MExpression mExp)
  {
    int i = 0; for (int stop = mExp.size(); i < stop; i++) {
      add((MCandidate)mExp.get(i));
    }
    prune();
  }

  List<MExpression> split()
    throws Exception
  {
    if (size() == 0) return null;
    ArrayList ret = new ArrayList();

    MCandidate mc = (MCandidate)get(0);
    List splitedMCList = mc.split();
    int splitedMCSize = splitedMCList.size();
    for (int i = 0; i < splitedMCSize; i++) {
      ret.add(new MExpression((MCandidate)splitedMCList.get(i)));
    }

    int size = size();

    if (size > 1) {
      String preExpWithSpace = mc.geExpStrWithSpace();
      for (int i = 1; i < size; i++) {
        mc = (MCandidate)get(i);

        String curExpWithSpace = mc.geExpStrWithSpace();

        if (preExpWithSpace.equals(curExpWithSpace))
        {
          splitedMCList = mc.split();
          for (int j = 0; j < splitedMCSize; j++) {
            ((MExpression)ret.get(j)).add((MCandidate)splitedMCList.get(j));
          }
        }
      }
    }
    return ret;
  }

  boolean isOneEojeol()
  {
    return (size() > 0) && (((MCandidate)get(0)).getSpaceCnt() == 0);
  }

  void sort()
  {
    Collections.sort(this);
  }

  void sortByLnpr()
  {
    Collections.sort(this, new Comparator()
    {
      public int compare(MCandidate mc1, MCandidate mc2)
      {
        if (mc1.getLnpr() > mc2.getLnpr())
          return -1;
        if (mc1.getLnpr() < mc2.getLnpr()) {
          return 1;
        }
        return 0;
      }
    });
  }

  void sortByBestLnpr()
  {
    Collections.sort(this, new Comparator()
    {
      public int compare(MCandidate mc1, MCandidate mc2)
      {
        if (mc1.getBestLnpr() > mc1.getBestLnpr())
          return -1;
        if (mc1.getBestLnpr() > mc1.getBestLnpr()) {
          return 1;
        }
        return 0;
      }
    });
  }

  public int compareTo(MExpression comp)
  {
    return this.exp.compareTo(comp.exp);
  }

  String getCommonHead()
  {
    MCandidate mc = (MCandidate)get(0);
    int spaceCnt = mc.getSpaceCnt();
    if (spaceCnt < 1) return null;

    int size = size();
    for (int i = spaceCnt - 1; i >= 0; i--)
    {
      String maxCommonHead = mc.getExp(i);

      if (getExp().length() - maxCommonHead.length() >= 2)
      {
        for (int j = 1; j < size; j++) {
          if (((MCandidate)get(j)).getHead(maxCommonHead) == null) {
            maxCommonHead = null;
            break;
          }

        }

        if ((maxCommonHead != null) && (maxCommonHead.length() > 1))
        {
          if (mc.isUNBfrOrAftrIthSpace(i)) {
            maxCommonHead = null;
          }
          else
          {
            return maxCommonHead;
          }
        }
      }
    }
    return null;
  }

  boolean isComplete()
    throws Exception
  {
    return (size() > 0) && (((MCandidate)get(0)).isComplete());
  }

  boolean isOneEojeolCheckable()
  {
    if (size() == 1) {
      MCandidate mc = (MCandidate)get(0);
      Morpheme mp = mc.firstMorp;
      if ((mc.size() == 1) && (
        (mp.isCharSetOf(CharSetType.NUMBER)) || 
        (mp.isCharSetOf(CharSetType.ENGLISH)) || 
        (mp.isCharSetOf(CharSetType.COMBINED))))
      {
        return true;
      }
    }
    return false;
  }

  public MExpression copy()
  {
    MExpression copy = new MExpression(this.exp);
    int i = 0; for (int stop = size(); i < stop; i++) {
      copy.add(((MCandidate)get(i)).copy());
    }
    return copy;
  }

  public void leaveJustBest()
  {
    for (int i = size() - 1; i > 0; i--)
      remove(i);
  }

  public void setBestPrevMC(MExpression mePrev)
  {
    int i = 0; for (int size = size(); i < size; i++) {
      MCandidate mcCur = (MCandidate)get(i);

      if (mePrev == null) {
        mcCur.setBestPrevMC(null);
      } else {
        int j = 0; for (int jSize = mePrev.size(); j < jSize; j++) {
          MCandidate mcPrev = (MCandidate)mePrev.get(j);
          mcCur.setBestPrevMC(mcPrev);
        }
      }
    }
  }

  public boolean isNotHangul()
  {
    return (size() == 1) && (((MCandidate)get(0)).isNotHangul());
  }

  public MCandidate getBest()
  {
    return (MCandidate)get(0);
  }

  public float getLnprOfSpacing()
  {
    return this.lnprOfSpacing;
  }

  public void setLnprOfSpacing(float lnprOfSpacing)
  {
    this.lnprOfSpacing = lnprOfSpacing;
  }
}