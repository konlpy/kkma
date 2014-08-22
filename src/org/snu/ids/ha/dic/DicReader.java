package org.snu.ids.ha.dic;

import java.io.IOException;

public abstract interface DicReader
{
  public abstract String readLine()
    throws IOException;

  public abstract void cleanup()
    throws IOException;
}