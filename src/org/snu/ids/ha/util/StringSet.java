package org.snu.ids.ha.util;

import java.util.HashSet;

public class StringSet extends HashSet<String>
{
  int maxLen = 0;

  public StringSet(String[] words)
  {
    addAll(words);
  }

  public boolean contains(char ch)
  {
    return super.contains(ch);
  }

  public void addAll(String[] words)
  {
    if (words == null) return;
    int len = -1;
    String temp = null;
    int i = 0; for (int stop = words.length; i < stop; i++) {
      temp = words[i];
      len = temp.length();
      add(temp);
      if (len > this.maxLen) this.maxLen = len;
    }
  }
}