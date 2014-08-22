package org.snu.ids.ha.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.snu.ids.ha.util.Util;

class ProbDicReader
{
  private BufferedReader br = null;
  String line = null;

  ProbDicReader(String fileName)
    throws UnsupportedEncodingException
  {
    this.br = new BufferedReader(new InputStreamReader(PDDictionary.class.getResourceAsStream(fileName), "UTF-8"));
  }

  public String[] read()
    throws IOException
  {
    while ((this.line = this.br.readLine()) != null) {
      this.line = this.line.trim();
      if ((Util.valid(this.line)) && (!this.line.startsWith("//"))) {
        return this.line.split("\t");
      }
    }
    return null;
  }

  public void close()
    throws IOException
  {
    if (this.br != null) this.br.close();
  }
}