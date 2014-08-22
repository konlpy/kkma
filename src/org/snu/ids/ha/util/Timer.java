package org.snu.ids.ha.util;

import java.io.PrintStream;

public class Timer
{
  long startTime = 0L;
  long endTime = 0L;

  public void start()
  {
    this.startTime = System.currentTimeMillis();
  }

  public void stop()
  {
    this.endTime = System.currentTimeMillis();
  }

  public long getStartTime()
  {
    return this.startTime;
  }

  public long getEndTime()
  {
    return this.endTime;
  }

  public double getInterval()
  {
    return getIntervalL() / 1000.0D;
  }

  public long getIntervalL()
  {
    if (this.startTime < this.endTime) return this.endTime - this.startTime;
    return 0L;
  }

  public void printMsg(String msg)
  {
    try
    {
      System.out.println(msg + "::" + getInterval() + " seconds");
    } catch (Exception e) {
      System.err.println("print error [" + msg + "]");
    }
  }
}