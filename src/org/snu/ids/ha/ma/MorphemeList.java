package org.snu.ids.ha.ma;

import java.util.ArrayList;

public class MorphemeList extends ArrayList<Morpheme>
{
  Morpheme firstMorp = null;
  Morpheme lastMorp = null;

  public boolean add(Morpheme mp)
  {
    if (this.firstMorp == null) this.firstMorp = mp;
    this.lastMorp = mp;
    return super.add(mp);
  }

  public void mergeAt(int idx)
  {
    Morpheme mp1 = (Morpheme)get(idx);
    Morpheme mp2 = (Morpheme)remove(idx + 1);
    mp1.append(mp2);
    if (mp2 == this.lastMorp) this.lastMorp = mp1;
  }

  public boolean equals(MorphemeList ml)
  {
    return getEncStr().equals(ml.getEncStr());
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = size(); i < stop; i++) {
      if (i > 0) sb.append("+");
      sb.append(get(i));
    }
    return sb.toString();
  }

  public String getSmplStr2()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = size(); i < stop; i++) {
      if (i > 0) sb.append("+");
      sb.append(((Morpheme)get(i)).getSmplStr2());
    }
    return sb.toString();
  }

  String getEncStr()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = size(); i < stop; i++) {
      if (i > 0) sb.append("+");
      sb.append(((Morpheme)get(i)).getEncStr());
    }
    return sb.toString();
  }

  public Morpheme getFirstMorp()
  {
    return this.firstMorp;
  }

  public Morpheme getLastMorp()
  {
    return this.lastMorp;
  }

  public int getStartIndex()
  {
    return this.firstMorp.index;
  }
}