package org.snu.ids.ha.index;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class KeywordList extends ArrayList<Keyword>
{
  int docLen = 0;
  private Hashtable<String, Keyword> table = null;

  public KeywordList(List<Keyword> list)
  {
    this.table = new Hashtable();
    addAll(list);
  }

  public void addAll(List<Keyword> list)
  {
    int i = 0; for (int size = list.size(); i < size; i++) {
      Keyword keyword = (Keyword)list.get(i); Keyword org = null;
      org = (Keyword)this.table.get(keyword.getKey());
      this.docLen += keyword.getCnt();
      if (org == null) {
        this.table.put(keyword.getKey(), keyword);
        add(keyword);
      } else {
        org.increaseCnt(keyword.getCnt());
      }
    }

    int i = 0; for (int size = size(); i < size; i++) {
      Keyword keyword = (Keyword)get(i);
      keyword.setFreq(keyword.getCnt() / this.docLen);
    }
  }

  public int getDocLen()
  {
    return this.docLen;
  }
}