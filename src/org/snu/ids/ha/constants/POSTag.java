package org.snu.ids.ha.constants;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.snu.ids.ha.util.Util;

public class POSTag
{
  private static final String[] TAG_ARR = { 
    "NNG", 
    "NNP", 
    "NNB", 
    "NNM", 
    "NR", 
    "NP", 
    "VV", 
    "VA", 
    "VXV", 
    "VXA", 
    "VCP", 
    "VCN", 
    "MDN", 
    "MDT", 
    "MAG", 
    "MAC", 
    "IC", 
    "JKS", 
    "JKC", 
    "JKG", 
    "JKO", 
    "JKM", 
    "JKI", 
    "JKQ", 
    "JX", 
    "JC", 
    "EPH", 
    "EPT", 
    "EPP", 
    "EFN", 
    "EFQ", 
    "EFO", 
    "EFA", 
    "EFI", 
    "EFR", 
    "ECE", 
    "ECD", 
    "ECS", 
    "ETN", 
    "ETD", 
    "XPN", 
    "XPV", 
    "XSN", 
    "XSV", 
    "XSA", 
    "XSM", 
    "XSO", 
    "XR", 
    "SY", 
    "SF", 
    "SP", 
    "SS", 
    "SE", 
    "SO", 
    "SW", 
    "UN", 
    "UV", 
    "UE", 
    "OL", 
    "OH", 
    "ON", 
    "BOS", 
    "EMO" };

  private static final Hashtable<String, Long> TAG_HASH = new Hashtable();
  private static final Hashtable<Long, String> TAG_NUM_HASH = new Hashtable();
  public static final long NNG;
  public static final long NNP;
  public static final long NNB;
  public static final long NNM;
  public static final long NR;
  public static final long NP;
  public static final long VV;
  public static final long VA;
  public static final long VXV;
  public static final long VXA;
  public static final long VCP;
  public static final long VCN;
  public static final long MDT;
  public static final long MDN;
  public static final long MAG;
  public static final long MAC;
  public static final long IC;
  public static final long JKS;
  public static final long JKC;
  public static final long JKG;
  public static final long JKO;
  public static final long JKM;
  public static final long JKI;
  public static final long JKQ;
  public static final long JX;
  public static final long JC;
  public static final long EPH;
  public static final long EPT;
  public static final long EPP;
  public static final long EFN;
  public static final long EFQ;
  public static final long EFO;
  public static final long EFA;
  public static final long EFI;
  public static final long EFR;
  public static final long ECE;
  public static final long ECD;
  public static final long ECS;
  public static final long ETN;
  public static final long ETD;
  public static final long XPN;
  public static final long XPV;
  public static final long XSN;
  public static final long XSV;
  public static final long XSA;
  public static final long XSM;
  public static final long XSO;
  public static final long XR;
  public static final long SF;
  public static final long SP;
  public static final long SS;
  public static final long SE;
  public static final long SO;
  public static final long SW;
  public static final long UN;
  public static final long UV;
  public static final long UE;
  public static final long OL;
  public static final long OH;
  public static final long ON;
  public static final long BOS;
  public static final long EMO;
  public static final long NNA;
  public static final long NN;
  public static final long N;
  public static final long VX;
  public static final long VP;
  public static final long VC;
  public static final long V;
  public static final long MD;
  public static final long MA;
  public static final long M;
  public static final long JK;
  public static final long J;
  public static final long EP;
  public static final long EF;
  public static final long EC;
  public static final long ET;
  public static final long EM;
  public static final long E;
  public static final long XP;
  public static final long XS;
  public static final long O;
  public static final long S;
  static final long MASK_ALL = -1L;
  static final String[] ZIP_TAG_ARR = { 
    "N", 
    "NN", 
    "NNA", 
    "V", 
    "VP", 
    "VC", 
    "VX", 
    "M", 
    "MD", 
    "MA", 
    "J", 
    "JK", 
    "E", 
    "EM", 
    "EP", 
    "EF", 
    "EC", 
    "ET", 
    "XS", 
    "S" };

  static final int ZIP_TAG_ARR_LEN = ZIP_TAG_ARR.length;
  public static final long COMPOSED = -9223372036854775808L;
  public static final long MASK_TAG = 9223372036854775807L;

  static
  {
    long hgFuncNum = 0L;
    int i = 0; for (int stop = TAG_ARR.length; i < stop; i++) {
      hgFuncNum = 1L << i;
      TAG_HASH.put(TAG_ARR[i], new Long(hgFuncNum));
      TAG_NUM_HASH.put(new Long(hgFuncNum), TAG_ARR[i]);
    }

    NNG = getTagNum("NNG");
    NNP = getTagNum("NNP");
    NNB = getTagNum("NNB");
    NNM = getTagNum("NNM");
    NR = getTagNum("NR");
    NP = getTagNum("NP");
    VV = getTagNum("VV");
    VA = getTagNum("VA");
    VXV = getTagNum("VXV");
    VXA = getTagNum("VXA");
    VCP = getTagNum("VCP");
    VCN = getTagNum("VCN");
    MDT = getTagNum("MDT");
    MDN = getTagNum("MDN");
    MAG = getTagNum("MAG");
    MAC = getTagNum("MAC");
    IC = getTagNum("IC");
    JKS = getTagNum("JKS");
    JKC = getTagNum("JKC");
    JKG = getTagNum("JKG");
    JKO = getTagNum("JKO");
    JKM = getTagNum("JKM");
    JKI = getTagNum("JKI");
    JKQ = getTagNum("JKQ");
    JX = getTagNum("JX");
    JC = getTagNum("JC");
    EPH = getTagNum("EPH");
    EPT = getTagNum("EPT");
    EPP = getTagNum("EPP");
    EFN = getTagNum("EFN");
    EFQ = getTagNum("EFQ");
    EFO = getTagNum("EFO");
    EFA = getTagNum("EFA");
    EFI = getTagNum("EFI");
    EFR = getTagNum("EFR");
    ECE = getTagNum("ECE");
    ECD = getTagNum("ECD");
    ECS = getTagNum("ECS");
    ETN = getTagNum("ETN");
    ETD = getTagNum("ETD");
    XPN = getTagNum("XPN");
    XPV = getTagNum("XPV");
    XSN = getTagNum("XSN");
    XSV = getTagNum("XSV");
    XSA = getTagNum("XSA");
    XSM = getTagNum("XSM");
    XSO = getTagNum("XSO");
    XR = getTagNum("XR");
    SF = getTagNum("SF");
    SP = getTagNum("SP");
    SS = getTagNum("SS");
    SE = getTagNum("SE");
    SO = getTagNum("SO");
    SW = getTagNum("SW");
    UN = getTagNum("UN");
    UV = getTagNum("UV");
    UE = getTagNum("UE");
    OL = getTagNum("OL");
    OH = getTagNum("OH");
    ON = getTagNum("ON");
    BOS = getTagNum("BOS");
    EMO = getTagNum("EMO");

    NNA = NNG | NNP;

    NN = NNA | NNB | NNM | NR | UN | ON;

    N = NP | NN;

    VX = VXV | VXA;

    VP = VV | VA | VX | VCN | XSV | XSA;

    VC = VCN | VCP;

    V = VP | VCP;

    MD = MDN | MDT;

    MA = MAG | MAC;

    M = MD | MA;

    JK = JKS | JKC | JKG | JKO | JKM | JKI | JKQ;

    J = JK | JX | JC;

    EP = EPH | EPT | EPP;

    EF = EFN | EFQ | EFO | EFA | EFI | EFR;

    EC = ECE | ECD | ECS;

    ET = ETN | ETD;

    EM = EF | EC | ET;

    E = EP | EM;

    XP = XPN | XPV;

    XS = XSN | XSV | XSA | XSM | XSO;

    O = OL | OH;

    S = SF | SP | SS | SE | SO | SW;

    TAG_HASH.put("E", Long.valueOf(E));
    TAG_HASH.put("EC", Long.valueOf(EC));
    TAG_HASH.put("EF", Long.valueOf(EF));
    TAG_HASH.put("EM", Long.valueOf(EM));
    TAG_HASH.put("EP", Long.valueOf(EP));
    TAG_HASH.put("ET", Long.valueOf(ET));
    TAG_HASH.put("J", Long.valueOf(J));
    TAG_HASH.put("JK", Long.valueOf(JK));
    TAG_HASH.put("M", Long.valueOf(M));
    TAG_HASH.put("MA", Long.valueOf(MA));
    TAG_HASH.put("MD", Long.valueOf(MD));
    TAG_HASH.put("N", Long.valueOf(N));
    TAG_HASH.put("NN", Long.valueOf(NN));
    TAG_HASH.put("NNA", Long.valueOf(NNA));
    TAG_HASH.put("S", Long.valueOf(S));
    TAG_HASH.put("V", Long.valueOf(V));
    TAG_HASH.put("VC", Long.valueOf(VC));
    TAG_HASH.put("VP", Long.valueOf(VP));
    TAG_HASH.put("VX", Long.valueOf(VX));
    TAG_HASH.put("XP", Long.valueOf(XP));
    TAG_HASH.put("XS", Long.valueOf(XS));
    TAG_HASH.put("O", Long.valueOf(O));
    TAG_NUM_HASH.put(Long.valueOf(E), "E");
    TAG_NUM_HASH.put(Long.valueOf(EC), "EC");
    TAG_NUM_HASH.put(Long.valueOf(EF), "EF");
    TAG_NUM_HASH.put(Long.valueOf(EM), "EM");
    TAG_NUM_HASH.put(Long.valueOf(EP), "EP");
    TAG_NUM_HASH.put(Long.valueOf(ET), "ET");
    TAG_NUM_HASH.put(Long.valueOf(J), "J");
    TAG_NUM_HASH.put(Long.valueOf(JK), "JK");
    TAG_NUM_HASH.put(Long.valueOf(M), "M");
    TAG_NUM_HASH.put(Long.valueOf(MA), "MA");
    TAG_NUM_HASH.put(Long.valueOf(MD), "MD");
    TAG_NUM_HASH.put(Long.valueOf(N), "N");
    TAG_NUM_HASH.put(Long.valueOf(NN), "NN");
    TAG_NUM_HASH.put(Long.valueOf(NNA), "NNA");
    TAG_NUM_HASH.put(Long.valueOf(S), "S");
    TAG_NUM_HASH.put(Long.valueOf(V), "V");
    TAG_NUM_HASH.put(Long.valueOf(VX), "VX");
    TAG_NUM_HASH.put(Long.valueOf(VP), "VP");
    TAG_NUM_HASH.put(Long.valueOf(XP), "XP");
    TAG_NUM_HASH.put(Long.valueOf(XS), "XS");
    TAG_NUM_HASH.put(Long.valueOf(O), "O");
  }

  private static final long getTagNum(int i)
  {
    return 1L << i;
  }

  public static long getTagNum(String tag)
  {
    if (tag == null) return 0L;
    if (tag.indexOf(',') > -1) return getTagNum(tag.split(","));
    long l = 0L;
    try {
      l = ((Long)TAG_HASH.get(tag)).longValue();
    } catch (Exception e) {
      System.err.println("[" + tag + "] 정의되지 않은 태그입니다.");
    }
    return l;
  }

  public static long getTagNum(String[] tagArr)
  {
    long l = 0L;
    int i = 0; for (int stop = tagArr == null ? 0 : tagArr.length; i < stop; i++) {
      l |= getTagNum(tagArr[i]);
    }
    return l;
  }

  public static String getTag(long tagNum)
  {
    if (tagNum == 0L) return null;
    String tag = (String)TAG_NUM_HASH.get(Long.valueOf(tagNum));
    if (tag == null) tag = getTagStr(tagNum);
    return tag;
  }

  public static List<String> getTagList(long encTagNum)
  {
    List ret = new ArrayList();
    int i = 0; for (int stop = TAG_ARR.length; i < stop; i++) {
      if ((encTagNum & getTagNum(i)) > 0L)
        ret.add(TAG_ARR[i]);
    }
    return ret;
  }

  public static String getTagStr(long encTagNum)
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int stop = TAG_ARR.length; i < stop; i++) {
      if ((encTagNum & getTagNum(i)) > 0L) {
        if (sb.length() > 0) sb.append(",");
        sb.append(TAG_ARR[i]);
      }
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  public static String getZipTagStr(long encTagNum)
  {
    StringBuffer sb = new StringBuffer();

    int zipTagCnt = 0;
    long zipTagEnc = 0L;
    for (int i = 0; i < ZIP_TAG_ARR_LEN; i++) {
      zipTagEnc = getTagNum(ZIP_TAG_ARR[i]);
      if ((encTagNum & zipTagEnc) == zipTagEnc) {
        if (zipTagCnt > 0) sb.append(",");
        sb.append(ZIP_TAG_ARR[i]);
        zipTagCnt++;
        encTagNum &= (0xFFFFFFFF ^ zipTagEnc);
      }
    }

    String temp = getTagStr(encTagNum);

    if (Util.valid(temp)) {
      if (zipTagCnt > 0) sb.append(",");
      sb.append(temp);
    }
    temp = sb.toString();
    return Util.valid(temp) ? temp : null;
  }

  public static final long encode(String hgTag, String compType)
  {
    long enc = getTagNum(hgTag);
    if ((Util.valid(compType)) && (compType.equals("C"))) enc |= -9223372036854775808L;
    return enc;
  }

  public static final String[] decode(long hgEnc)
  {
    String[] ret = new String[2];
    ret[0] = getTag(hgEnc & 0xFFFFFFFF);
    ret[1] = ((hgEnc & 0x0) == -9223372036854775808L ? "C" : "S");
    return ret;
  }

  public static boolean isTagOf(String tag, long tagsEnc)
  {
    return isTagOf(getTagNum(tag), tagsEnc);
  }

  public static boolean isTagOf(long tagNum, long tagsEnc)
  {
    return Long.bitCount(tagNum & tagsEnc) > 0;
  }
}