package org.snu.ids.ha.constants;

import java.util.Hashtable;

public class Symbol
{
  private static final Hashtable<String, Long> SYMBOL_TYPE_HASH = new Hashtable();

  static
  {
    SYMBOL_TYPE_HASH.put("...", Long.valueOf(POSTag.SE));
    SYMBOL_TYPE_HASH.put("‥", Long.valueOf(POSTag.SE));
    SYMBOL_TYPE_HASH.put("…", Long.valueOf(POSTag.SE));

    SYMBOL_TYPE_HASH.put("!", Long.valueOf(POSTag.SF));
    SYMBOL_TYPE_HASH.put(".", Long.valueOf(POSTag.SF));
    SYMBOL_TYPE_HASH.put("?", Long.valueOf(POSTag.SF));
    SYMBOL_TYPE_HASH.put("？", Long.valueOf(POSTag.SF));

    SYMBOL_TYPE_HASH.put("­", Long.valueOf(POSTag.SO));
    SYMBOL_TYPE_HASH.put("~", Long.valueOf(POSTag.SO));
    SYMBOL_TYPE_HASH.put("～", Long.valueOf(POSTag.SO));
    SYMBOL_TYPE_HASH.put("∼", Long.valueOf(POSTag.SO));

    SYMBOL_TYPE_HASH.put(",", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("，", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("/", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("／", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put(":", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("：", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put(";", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("；", Long.valueOf(POSTag.SP));
    SYMBOL_TYPE_HASH.put("·", Long.valueOf(POSTag.SP));

    SYMBOL_TYPE_HASH.put("'", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("\"", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("(", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put(")", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("）", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("[", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("]", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("`", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("｀", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("{", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("}", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("˝", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("‘", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("’", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("“", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("”", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("〈", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("〉", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("《", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("》", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("「", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("」", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("『", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("』", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("【", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("】", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("〔", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("〕", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("〃", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("<", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("＜", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put(">", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("＞", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("≪", Long.valueOf(POSTag.SS));
    SYMBOL_TYPE_HASH.put("≫", Long.valueOf(POSTag.SS));

    SYMBOL_TYPE_HASH.put("㎀", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎁", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎂", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎃", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎄", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎈", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎉", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎊", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎋", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎌", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎍", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎎", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎏", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎐", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎑", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎒", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎓", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎔", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎕", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎖", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎗", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎘", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎙", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎚", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎛", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎜", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎝", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎞", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎟", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎠", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎡", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎢", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎣", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎤", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎥", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎦", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎧", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎨", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎩", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎪", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎫", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎬", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎭", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎮", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎯", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎰", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎱", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎲", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎳", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎴", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎵", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎶", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎷", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎸", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎹", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎺", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎻", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎼", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎽", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎾", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㎿", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏀", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏁", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏂", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏃", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏄", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏅", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏆", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏇", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏈", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏉", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏊", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏏", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏐", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏓", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏖", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏘", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏛", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏜", Long.valueOf(POSTag.NNM));
    SYMBOL_TYPE_HASH.put("㏝", Long.valueOf(POSTag.NNM));
  }

  public static final long getSymbolTag(String symbol)
  {
    Long lv = (Long)SYMBOL_TYPE_HASH.get(symbol);
    if (lv == null) return POSTag.SW;
    return lv.longValue();
  }
}