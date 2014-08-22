package org.snu.ids.ha.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import org.snu.ids.ha.util.Timer;

public class WordDic extends HashSet<String>
{
  int maxLen = -2147483648;
  int minLen = 2147483647;

  public WordDic(String fileName)
  {
    load(fileName);
  }

  public void load(String fileName)
  {
    System.out.println("Loading " + fileName);
    Timer timer = new Timer();
    timer.start();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName), "UTF-8"));
      String line = null;
      while ((line = br.readLine()) != null) {
        int len = line.length();
        if (len > this.maxLen) this.maxLen = len;
        if (len < this.minLen) this.minLen = len;
        super.add(line);
      }
    } catch (IOException e) {
      System.err.println("Loading Error!");
    } finally {
      timer.stop();
      System.out.println("Loaded " + timer.getInterval() + "secs");
    }
  }
}