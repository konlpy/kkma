package org.snu.ids.ha.ma;

import java.util.StringTokenizer;
import org.snu.ids.ha.constants.Condition;
import org.snu.ids.ha.constants.POSTag;

public class MorphemeSpace extends Morpheme
{
  long atlEnc = 0L;
  long hclEnc = 0L;
  long cclEnc = 0L;
  long bclEnc = 0L;
  long eclEnc = 0L;

  protected MorphemeSpace()
  {
    super(" ", 0);
    this.charSet = CharSetType.SPACE;
    this.infoEnc = POSTag.SW;
  }

  MorphemeSpace(String source)
  {
    this();
    String[] arr = source.split("/");
    if (arr.length > 1) {
      StringTokenizer st = new StringTokenizer(arr[1], "*#&@￢%", true);
      String token = null;
      while (st.hasMoreTokens()) {
        token = st.nextToken();

        if (token.equals("#")) {
          token = st.nextToken().trim();
          token = token.substring(1, token.length() - 1);
          this.atlEnc = POSTag.getTagNum(token.split(","));
        }
        else if (token.equals("&")) {
          token = st.nextToken().trim();
          token = token.substring(1, token.length() - 1);
          this.hclEnc = Condition.getCondNum(token.split(","));
        }
        else if (token.equals("@")) {
          token = st.nextToken().trim();
          token = token.substring(1, token.length() - 1);
          this.cclEnc = Condition.getCondNum(token.split(","));
        }
        else if (token.equals("￢")) {
          token = st.nextToken().trim();
          token = token.substring(1, token.length() - 1);
          this.eclEnc = Condition.getCondNum(token.split(","));
        }
      }
    }
  }

  MorphemeSpace(long atlEnc, long hclEnc, long bclEnc, long cclEnc, long eclEnc)
  {
    this();
    this.charSet = CharSetType.SPACE;
    this.infoEnc = POSTag.SW;
    this.atlEnc = atlEnc;
    this.hclEnc = hclEnc;
    this.bclEnc = bclEnc;
    this.cclEnc = cclEnc;
    this.eclEnc = eclEnc;
  }

  public String toString()
  {
    return " ";
  }

  public String getToString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(this.string + "/");

    String temp = POSTag.getTagStr(this.atlEnc);
    if (temp != null) sb.append("#(" + temp + ")");

    temp = Condition.getCondStr(this.hclEnc);
    if (temp != null) sb.append("&(" + temp + ")");

    temp = Condition.getCondStr(this.cclEnc);
    if (temp != null) sb.append("@(" + temp + ")");

    temp = Condition.getCondStr(this.eclEnc);
    if (temp != null) sb.append("￢(" + temp + ")");

    return sb.toString();
  }

  public Morpheme copy()
  {
    return new MorphemeSpace(this.atlEnc, this.hclEnc, this.bclEnc, this.cclEnc, this.eclEnc);
  }
}