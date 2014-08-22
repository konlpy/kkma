package org.snu.ids.ha.ma;

import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.util.Util;

public class Eojeol extends MorphemeList
{
  String exp = null;

  private Eojeol()
  {
  }

  public Eojeol(MCandidate mc)
  {
    this.exp = mc.getExp();
    addAll(mc);
    this.firstMorp = mc.firstMorp;
    this.lastMorp = mc.lastMorp;
  }

  public Eojeol(MExpression me)
  {
    this((MCandidate)me.get(0));
  }

  public String getExp()
  {
    return this.exp;
  }

  public boolean isLastTagOf(long tag)
  {
    if (this.lastMorp.isTag(POSTag.JX))
      for (int i = size() - 1; i > 0; i--) {
        Morpheme mp = (Morpheme)get(i);
        if (!mp.isTag(POSTag.JX)) {
          if (mp.isTagOf(POSTag.J | POSTag.EM)) return mp.isTagOf(tag);
          return this.lastMorp.isTagOf(tag);
        }
      }
    else if (this.lastMorp.isTagOf(POSTag.S)) {
      for (int i = size() - 1; i > 0; i--) {
        Morpheme mp = (Morpheme)get(i);
        if (!mp.isTag(POSTag.S))
          return mp.isTagOf(tag);
      }
    }
    return this.lastMorp.isTagOf(tag);
  }

  public boolean containsTagOf(long tag)
  {
    int i = 0; for (int size = size(); i < size; i++) {
      Morpheme mp = (Morpheme)get(i);
      if (mp.isTagOf(tag)) return true;
    }
    return false;
  }

  public boolean isEnding()
  {
    return this.lastMorp.isTagOf(POSTag.EF);
  }

  Eojeol removeIncorrectlyCombinedEojeol()
  {
    if (size() < 2) return null;
    Morpheme mp1 = (Morpheme)get(0);
    Morpheme mp2 = (Morpheme)get(1);

    if (((mp1.isTagOf(POSTag.MA)) && (mp2.isTagOf(POSTag.VP))) || ((mp1.isTagOf(POSTag.MD)) && (mp2.isTagOf(POSTag.N)))) {
      Eojeol ej = new Eojeol();
      ej.exp = mp1.string;
      ej.add(mp1);
      this.exp = this.exp.substring(ej.exp.length());
      remove(mp1);
      return ej;
    }

    for (int i = 1; i < size(); i++) {
      mp1 = (Morpheme)get(i - 1);
      mp2 = (Morpheme)get(i);

      if ((mp1.isTag(POSTag.ECS)) && (mp2.isTagOf(POSTag.VP))) {
        Eojeol ej = new Eojeol();

        int idx = 0;
        for (int j = 0; j < i; j++) {
          if (j == 0) idx = ((Morpheme)get(0)).index;
          ej.add((Morpheme)get(0));
          remove(0);
        }
        ej.exp = this.exp.substring(0, mp2.index - idx);

        this.exp = this.exp.substring(mp2.index - idx);
        return ej;
      }
    }
    return null;
  }

  public String toString()
  {
    return Util.getTabbedString(getExp(), 4, 16) + "=> [" + super.toString() + "]";
  }
}