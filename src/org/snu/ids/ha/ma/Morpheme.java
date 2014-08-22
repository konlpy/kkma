package org.snu.ids.ha.ma;

import java.util.ArrayList;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.constants.Symbol;
import org.snu.ids.ha.util.Util;

public class Morpheme extends Token
{
  protected long infoEnc = 0L;

  ArrayList<String> compNounList = null;

  protected Morpheme()
  {
  }

  public Morpheme(String string, int index)
  {
    this.index = index;
    this.string = string;
    this.charSet = CharSetType.HANGUL;
    this.infoEnc = POSTag.UN;
  }

  public Morpheme(String string, long tagNum)
  {
    this.string = string;
    this.charSet = CharSetType.HANGUL;
    this.infoEnc = tagNum;
  }

  public Morpheme(String string, String tag, String compType)
  {
    this.string = string;
    this.charSet = CharSetType.HANGUL;
    this.infoEnc = POSTag.getTagNum(tag);
    setComposed(compType);
  }

  public Morpheme(Token token)
  {
    this.index = token.index;
    this.string = token.string;
    this.charSet = token.charSet;

    if (token.isCharSetOf(CharSetType.HANGUL)) {
      this.infoEnc = POSTag.UN;
    }
    else if (token.isCharSetOf(CharSetType.NUMBER)) {
      this.infoEnc = POSTag.NR;
    }
    else if ((token.isCharSetOf(CharSetType.ENGLISH)) || (token.isCharSetOf(CharSetType.COMBINED))) {
      this.infoEnc = POSTag.OL;
    }
    else if (token.isCharSetOf(CharSetType.HANMUN)) {
      this.infoEnc = POSTag.OH;
    }
    else if (token.isCharSetOf(CharSetType.EMOTICON)) {
      this.infoEnc = POSTag.EMO;
    }
    else
    {
      this.infoEnc = Symbol.getSymbolTag(token.string);
    }
  }

  public Morpheme(Morpheme mp)
  {
    this.index = mp.index;
    this.string = mp.string;
    this.charSet = mp.charSet;
    this.infoEnc = mp.infoEnc;
  }

  public String getTag()
  {
    return POSTag.getTag(getTagNum());
  }

  public void setTag(String tag)
  {
    setTag(POSTag.getTagNum(tag));
  }

  public void setTag(long tagNum)
  {
    this.infoEnc = (this.infoEnc & 0x0 | 0xFFFFFFFF & tagNum);
  }

  public long getTagNum()
  {
    return this.infoEnc & 0xFFFFFFFF;
  }

  public boolean isComposed()
  {
    return this.infoEnc < 0L;
  }

  public String getComposed()
  {
    return isComposed() ? "C" : "S";
  }

  public void setComposed(boolean composed)
  {
    if (composed)
      this.infoEnc |= -9223372036854775808L;
    else
      this.infoEnc &= 9223372036854775807L;
  }

  public void setComposed(String compType)
  {
    setComposed((Util.valid(compType)) && (compType.equals("C")));
  }

  public boolean isTag(long tagNum)
  {
    return getTagNum() == tagNum;
  }

  public boolean isTagOf(long tagNum)
  {
    return (this.infoEnc & 0xFFFFFFFF & tagNum) > 0L;
  }

  public void append(Morpheme mp)
  {
    if (mp.isTag(POSTag.XSM)) {
      setTag(POSTag.MAG);
    }
    else if (!mp.isTag(POSTag.EFR)) {
      setTag(mp.getTagNum());
    }
    else if ((mp.isTag(POSTag.EFR)) && (isTagOf(POSTag.EC)) && (
      (this.string.equals("아")) || (this.string.equals("어")) || (this.string.equals("구")) || (this.string.equals("고"))))
    {
      setTag(POSTag.EFN);
    }
    this.string += mp.string;
    setComposed(false);
  }

  public Morpheme copy()
  {
    Morpheme copy = new Morpheme();
    copy.string = this.string;
    copy.charSet = this.charSet;
    copy.index = this.index;
    copy.infoEnc = this.infoEnc;
    return copy;
  }

  static Morpheme create(String source)
  {
    Morpheme ret = null;
    if (source.startsWith("/")) {
      ret = new Morpheme("/", "SY", null);
    } else {
      String[] arr = source.split("/");
      ret = new Morpheme(arr[0], arr[1], arr.length > 2 ? arr[2] : null);
    }
    return ret;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.index + "/" + this.string + "/" + getTag() + (isComposed() ? "/C" : ""));
    return sb.toString();
  }

  public String getSmplStr()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.string + "/" + getTag());
    return sb.toString();
  }

  public String getSmplStr2()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.string + "/" + getTag() + (isComposed() ? "/C" : ""));
    return sb.toString();
  }

  String getEncStr()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.string + "/" + this.infoEnc);
    return sb.toString();
  }
}