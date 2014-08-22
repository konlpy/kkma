package org.snu.ids.ha.ma;

import java.util.ArrayList;

public class Sentence extends ArrayList<Eojeol>
{
  public boolean add(Eojeol e)
  {
    Eojeol ej = e.removeIncorrectlyCombinedEojeol();
    if (ej != null) {
      super.add(ej);
    }
    return super.add(e);
  }

  public String getSentence()
  {
    StringBuffer sb = new StringBuffer();
    Eojeol eojeol = null;
    String temp = null;
    int i = 0; for (int stop = size(); i < stop; i++) {
      eojeol = (Eojeol)get(i);
      temp = eojeol.exp;
      if (i > 0) sb.append(" ");
      sb.append(temp);
    }
    return sb.toString();
  }
}