package org.snu.ids.ha.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SimpleDicFileReader
  implements SimpleDicReader
{
  BufferedReader br = null;

  public SimpleDicFileReader(String fileName)
    throws UnsupportedEncodingException
  {
    this.br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName), "UTF-8"));
  }

  public String readLine()
    throws IOException
  {
    return this.br.readLine();
  }

  public void cleanup()
    throws IOException
  {
    if (this.br != null) this.br.close();
  }
}