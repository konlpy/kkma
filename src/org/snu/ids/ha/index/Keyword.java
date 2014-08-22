package org.snu.ids.ha.index;

import org.snu.ids.ha.ma.Morpheme;

public class Keyword extends Morpheme
{
  int id = 0;
  String vocTag = "S";
  int cnt = 1;
  double freq = 1.0D;

  public Keyword()
  {
  }

  public Keyword(Morpheme mp)
  {
    super(mp);
  }

  public int getId()
  {
    return this.id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setId(String id)
  {
    this.id = Integer.parseInt(id);
  }

  public double getFreq()
  {
    return this.freq;
  }

  public void setFreq(double freq)
  {
    this.freq = freq;
  }

  public String getVocTag()
  {
    return this.vocTag;
  }

  public void setVocTag(String vocTag)
  {
    this.vocTag = vocTag;
  }

  public int getCnt()
  {
    return this.cnt;
  }

  public void setCnt(int cnt)
  {
    this.cnt = cnt;
  }

  public void increaseCnt()
  {
    this.cnt += 1;
  }

  public void increaseCnt(int cntToAdd)
  {
    this.cnt += cntToAdd;
  }

  public String getKey()
  {
    return super.getString() + ":" + super.getTag();
  }

  public String toString()
  {
    return super.toString() + "\t" + this.id + "\t" + this.vocTag + "\t" + this.cnt + "\t" + this.freq;
  }
}