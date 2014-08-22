package org.snu.ids.ha.index;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.dic.Dictionary;
import org.snu.ids.ha.ma.CharSetType;
import org.snu.ids.ha.ma.MCandidate;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Token;
import org.snu.ids.ha.ma.Tokenizer;
import org.snu.ids.ha.util.StringSet;
import org.snu.ids.ha.util.Util;
import org.tartarus.snowball.EnglishStemmer;

public class KeywordExtractor extends MorphemeAnalyzer
{
  static WordDic UOMDic = new WordDic("/dic/ecat/UOM.dic");
  static WordDic ChemFormulaDic = new WordDic("/dic/ecat/ChemFormula.dic");
  static WordDic CompNounDic = new WordDic("/dic/ecat/CompNoun.dic");
  static WordDic VerbNounDic = new WordDic("/dic/ecat/VerbNoun.dic");
  static WordDic JunkWordDic = new WordDic("/dic/ecat/JunkWord.dic");
  static WordDic VerbJunkWordDic = new WordDic("/dic/ecat/VerbJunkWord.dic");
  static final int MAX_UOM_SIZE = 7;
  public static final StringSet MULTIPLYERS = new StringSet(new String[] { "*", "x", "X", "×", "Ⅹ" });
  public static final StringSet RANGE_INDICATOR = new StringSet(new String[] { "-", "±", "~", "+" });
  public static final String STD_UOM_CONNECTOR = "*";

  public KeywordList extractKeyword(JProgressBar progressBar, JLabel label, String string, boolean onlyNoun)
  {
    KeywordList ret = null;

    String line = null;
    int offset = 0;

    String[] strArr = string.split("\n");

    if (progressBar != null) {
      progressBar.setIndeterminate(false);
      progressBar.setMaximum(strArr.length);
      progressBar.setStringPainted(true);
      label.setText("0");
    }

    int lineNo = 0; for (int len = strArr.length; lineNo < len; lineNo++) {
      line = strArr[lineNo];
      if (Util.valid(line)) {
        KeywordList keywordList = extractKeyword(line, onlyNoun);

        if (offset > 0) {
          int i = 0; for (int size = keywordList.size(); i < size; i++) {
            Keyword keyword = (Keyword)keywordList.get(i);
            keyword.setIndex(offset + keyword.getIndex());
          }

        }

        if ((keywordList != null) && (keywordList.size() > 0)) {
          if (ret == null)
            ret = new KeywordList(keywordList);
          else {
            ret.addAll(keywordList);
          }
        }
      }
      if (progressBar != null) {
        progressBar.setValue(lineNo + 1);
        label.setText(lineNo + 1);
      }
      offset += line.length() + 1;
    }
    if (progressBar != null) {
      progressBar.setStringPainted(false);
    }

    return ret;
  }

  public KeywordList extractKeyword(String string, boolean onlyNoun)
  {
    List ret = new ArrayList();
    EnglishStemmer engStemmer = new EnglishStemmer();
    try
    {
      List meList = leaveJustBest(postProcess(analyze(string)));

      Morpheme mp = null;
      MCandidate mc = null;
      MExpression me = null;
      Keyword keyword = null;
      List mpList = new ArrayList();
      int i = 0; for (int size = meList == null ? 0 : meList.size(); i < size; i++) {
        me = (MExpression)meList.get(i);
        mc = (MCandidate)me.get(0);

        int jSize = mc.size();
        if (jSize == 1) {
          mp = (Morpheme)mc.get(0);
          mp.setString(me.getExp());
          mpList.add(mp);
        }
        else {
          for (int j = 0; j < jSize; j++) {
            mpList.add((Morpheme)mc.get(j));
          }
        }

      }

      for (int endIdx = mpList.size() - 1; endIdx > 0; endIdx--) {
        for (int startIdx = Math.max(endIdx - 7, 0); startIdx < endIdx; startIdx++) {
          String tempName = "";
          for (int i = startIdx; i <= endIdx; i++) {
            tempName = tempName + ((Morpheme)mpList.get(i)).getString();
          }

          if (UOMDic.contains(tempName)) {
            for (; startIdx < endIdx; endIdx--) {
              mpList.remove(startIdx + 1);
            }
            mp = (Morpheme)mpList.get(startIdx);
            mp.setString(tempName);
            mp.setCharSet(CharSetType.COMBINED);
            mp.setTag(POSTag.NNM);
          }
          else if (ChemFormulaDic.contains(tempName)) {
            for (; startIdx < endIdx; endIdx--) {
              mpList.remove(startIdx + 1);
            }
            mp = (Morpheme)mpList.get(startIdx);
            mp.setString(tempName);
            mp.setCharSet(CharSetType.COMBINED);
            mp.setTag(POSTag.UN);
          }
          else if (CompNounDic.contains(tempName)) {
            for (; startIdx < endIdx; endIdx--) {
              mpList.remove(startIdx + 1);
            }
            if (!JunkWordDic.contains(tempName)) {
              mp = (Morpheme)mpList.get(startIdx);
              mp.setString(tempName);
              mp.setCharSet(CharSetType.COMBINED);
              mp.setTag(POSTag.NNG);
              mp.setComposed(true);
            }
          }
        }

      }

      int i = 0; for (int size = mpList.size(); i < size; i++) {
        mp = (Morpheme)mpList.get(i);
        mp.setString(mp.getString().toLowerCase());

        if (((!onlyNoun) || (mp.isTagOf(POSTag.N))) && 
          (!JunkWordDic.contains(mp.getString())))
        {
          if ((mp.isTagOf(POSTag.UN)) && 
            (mp.getCharSet() == CharSetType.ENGLISH))
          {
            keyword = new Keyword(mp);
            engStemmer.setCurrent(keyword.getString().toLowerCase());
            engStemmer.stem();
            keyword.setString(engStemmer.getCurrent());
            ret.add(keyword);
          }
          else if (mp.isTagOf(POSTag.V)) {
            String temp = mp.getString();
            int tempLen = temp.length();
            char ch = temp.charAt(tempLen - 1);
            if ((tempLen > 2) && ((ch == 54616) || (ch == 46104)) && 
              (VerbNounDic.contains(temp = temp.substring(0, tempLen - 1))))
            {
              keyword = new Keyword(mp);
              keyword.setString(temp);
              keyword.setTag(POSTag.NNG);
              ret.add(keyword);
            }
            else
            {
              keyword = new Keyword(mp);
              ret.add(keyword);
            }
          }
          else {
            mp.isTagOf(POSTag.NP);
            keyword = new Keyword(mp);
            ret.add(keyword);
          }
        }
      }

      Morpheme mp0 = null; Morpheme mp1 = null; Morpheme mp2 = null; Morpheme mp3 = null;
      int i = 0; int size = mpList.size(); for (int step = 0; i < size; i++) {
        mp0 = (Morpheme)mpList.get(i);
        step = 0;

        if ((i + 1 < size) && 
          (mp0.isTagOf(POSTag.NN)) && 
          ((mp1 = (Morpheme)mpList.get(i + 1)).isTagOf(POSTag.NN)) && 
          (mp0.getIndex() + mp0.getString().length() == mp1.getIndex()))
        {
          if ((i + 2 < size) && 
            ((mp2 = (Morpheme)mpList.get(i + 2)).isTagOf(POSTag.NN)) && 
            (mp1.getIndex() + mp1.getString().length() == mp2.getIndex()))
          {
            if ((i + 3 < size) && 
              ((mp3 = (Morpheme)mpList.get(i + 3)).isTagOf(POSTag.NN)) && 
              (mp2.getIndex() + mp2.getString().length() == mp3.getIndex()))
            {
              keyword = new Keyword(mp0);
              keyword.setComposed(true);
              keyword.setString(mp0.getString() + mp1.getString() + mp2.getString() + mp3.getString());
              ret.add(keyword);
              step++;
            } else {
              keyword = new Keyword(mp0);
              keyword.setComposed(true);
              keyword.setString(mp0.getString() + mp1.getString() + mp2.getString());
              ret.add(keyword);
            }
            step++;
          } else {
            keyword = new Keyword(mp0);
            keyword.setComposed(true);
            keyword.setString(mp0.getString() + mp1.getString());
            ret.add(keyword);
          }
          step++;
        }
        i += step;
      }

      for (int i = 0; i < ret.size(); i++) {
        keyword = (Keyword)ret.get(i);

        if ((keyword.isTagOf(POSTag.XP | POSTag.XS | POSTag.VX)) || (JunkWordDic.contains(mp.getString()))) {
          ret.remove(i);
          i--;
        }

      }

      List cnKeywordList = new ArrayList();
      String[] cnKeywords = (String[])null;
      int i = 0; for (int size = ret.size(); i < size; i++) {
        Keyword k = (Keyword)ret.get(i);
        if ((k.isComposed()) && ((cnKeywords = this.dic.getCompNoun(k.getString())) != null)) {
          int addIdx = 0;
          int j = 0; for (int len = cnKeywords.length; j < len; j++)
            if (!JunkWordDic.contains(cnKeywords[j])) {
              Keyword newKeyword = new Keyword(k);
              newKeyword.setVocTag("E");
              newKeyword.setString(cnKeywords[j]);
              newKeyword.setComposed(false);
              newKeyword.setIndex(k.getIndex() + addIdx);
              addIdx += newKeyword.getString().length();
              cnKeywordList.add(newKeyword);
            }
        }
      }
      ret.addAll(cnKeywordList);

      Collections.sort(ret, new Comparator()
      {
        public int compare(Keyword o1, Keyword o2)
        {
          if (o1.getIndex() == o2.getIndex()) {
            return o1.getString().length() - o2.getString().length();
          }
          return o1.getIndex() - o2.getIndex();
        }
      });
    }
    catch (Exception e) {
      System.err.println(string);
      e.printStackTrace();
    }

    return new KeywordList(ret);
  }

  public KeywordList removeJunkWord(KeywordList keywordList)
  {
    int i = 0; for (int size = keywordList == null ? 0 : keywordList.size(); i < size; i++);
    return keywordList;
  }

  public Keyword getCompositeNoun(MCandidate mc)
  {
    Keyword ret = null;
    if ((mc == null) || (mc.size() < 2)) return null;

    int nnCnt = 0;
    for (int i = 0; i < mc.size(); i++) {
      Morpheme mp = (Morpheme)mc.get(i);
      if (mp.isTagOf(POSTag.NN)) {
        if (ret == null) {
          ret = new Keyword(mp);
          ret.setComposed(true);
          nnCnt++; } else {
          if (nnCnt == 0) {
            return null;
          }
          ret.setString(ret.getString() + mp.getString());
          nnCnt++;
        }
      } else { if ((ret != null) && (nnCnt > 1)) {
          return ret;
        }
        nnCnt = 0;
      }
    }
    if (nnCnt == 0) return null;
    return ret;
  }

  public static String getFormatedUOMValues(String inputString)
  {
    String resultString = "";
    List list = Tokenizer.tokenize(inputString);
    Token token = null;

    for (int i = 0; i < list.size(); i++) {
      token = (Token)list.get(i);
      if (token.isCharSetOf(CharSetType.NUMBER))
        resultString = resultString + token.getString();
      else if (isUOMConnector(token.getString()))
        resultString = resultString + "*";
      else if ((!token.getString().equals(" ")) && (!token.getString().equals("\t"))) {
        resultString = resultString + token.getString();
      }
    }

    return resultString;
  }

  private static boolean isUOMConnector(String uomCon)
  {
    return MULTIPLYERS.contains(uomCon);
  }

  private static boolean isUOMConnector2(String uomCon)
  {
    return (MULTIPLYERS.contains(uomCon)) || (RANGE_INDICATOR.contains(uomCon));
  }

  public static void main(String[] args)
  {
    String string = "문서 엔터티의 개념이 명확하지 못하다. 즉, 문서 엔터티에 저장되는 단위개체인 문서가 다른 부서로 발신을 하면 다른 문서가 되는 것인지 수정을 할 때는 문서가 새로 생성되지 않는 것인지, 혹은 결재선으로 발신하면 문서가 그대로 있는 것인지 등에 대한 명확한 정의가 없다. 개발 담당자 마저도 이러한 개념을 명확히 설명하지 못하고 있다.";
    string = string + "\n사용노즐 : Variojet 045\n작동압력 : 10∼135 bar\n최대압력 : 150 bar\n물토출량 : 1400 rpm 11 L/min\n물흡입허용최고온도 : 70 ℃\n최대물흡입높이 : 2.5 m\n소비전력(시작) : 3.1 kW\n소비전력(정상작동) : 2.3 kW\n크기 : 350×330×900 mm\n무게 : 32 kg\n세제흡입가능 HClO4 ClO4 KClO4 CH3OC6H4OH H2(SO4)2";

    KeywordExtractor ke = new KeywordExtractor();

    Keyword keyword = null;
    List ret = ke.extractKeyword(string, false);
    int size = ret == null ? 0 : ret.size();
    for (int i = 0; i < size; i++) {
      keyword = (Keyword)ret.get(i);
      System.out.println(i + "\t" + keyword);
    }
  }
}