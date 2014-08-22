package org.snu.ids.ha.ma;

import java.io.Serializable;

public class Token
  implements Serializable, Comparable<Token>
{
  protected String string = null;
  protected CharSetType charSet = CharSetType.ETC;
  protected int index = 0;

  protected Token()
  {
  }

  protected Token(String string, CharSetType tokenType)
  {
    this(string, tokenType, 0);
  }

  public Token(String string, CharSetType charSet, int index)
  {
    setString(string);
    setCharSet(charSet);
    setIndex(index);
  }

  public Object clone()
  {
    return new Token(this);
  }

  public boolean equals(String string)
  {
    return (this.string != null) && (string != null) && (this.string.equals(string));
  }

  public Token(Token token)
  {
    this(token.string, token.charSet, token.index);
  }

  public CharSetType getCharSet()
  {
    return this.charSet;
  }

  public String getCharSetName()
  {
    return getCharSet(this.charSet);
  }

  public int getIndex()
  {
    return this.index;
  }

  public String getString()
  {
    return this.string;
  }

  public void setCharSet(CharSetType charSet)
  {
    this.charSet = charSet;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  public void setString(String string)
  {
    this.string = string;
  }

  public boolean isCharSetOf(CharSetType charSet)
  {
    return this.charSet == charSet;
  }

  public String toString()
  {
    return "(" + this.index + "," + this.string + "," + getCharSet(this.charSet) + ")";
  }

  public static String getCharSet(CharSetType tokenType)
  {
    if (CharSetType.SPACE == tokenType)
      return "Space";
    if (CharSetType.HANGUL == tokenType)
      return "Hangul";
    if (CharSetType.ENGLISH == tokenType)
      return "English";
    if (CharSetType.ETC == tokenType)
      return "Etc";
    if (CharSetType.NUMBER == tokenType)
      return "Number";
    if (CharSetType.HANMUN == tokenType)
      return "Hanmun";
    if (CharSetType.SYMBOL == tokenType)
      return "Symbol";
    if (CharSetType.EMOTICON == tokenType)
      return "Emoticon";
    if (CharSetType.COMBINED == tokenType) {
      return "Combined";
    }
    return "Undefined";
  }

  public Token copy()
  {
    Token copy = new Token();
    copy.string = this.string;
    copy.charSet = this.charSet;
    copy.index = this.index;
    return copy;
  }

  public int compareTo(Token tk)
  {
    return this.index - tk.index;
  }
}