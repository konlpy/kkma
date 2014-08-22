package org.tartarus.snowball;

public class EnglishStemmer extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final EnglishStemmer methodObject = new EnglishStemmer();

  private static final Among[] A_0 = { 
    new Among("arsen", -1, -1, "", methodObject), 
    new Among("commun", -1, -1, "", methodObject), 
    new Among("gener", -1, -1, "", methodObject) };

  private static final Among[] A_1 = { 
    new Among("'", -1, 1, "", methodObject), 
    new Among("'s'", 0, 1, "", methodObject), 
    new Among("'s", -1, 1, "", methodObject) };

  private static final Among[] A_2 = { 
    new Among("ied", -1, 2, "", methodObject), 
    new Among("s", -1, 3, "", methodObject), 
    new Among("ies", 1, 2, "", methodObject), 
    new Among("sses", 1, 1, "", methodObject), 
    new Among("ss", 1, -1, "", methodObject), 
    new Among("us", 1, -1, "", methodObject) };

  private static final Among[] A_3 = { 
    new Among("", -1, 3, "", methodObject), 
    new Among("bb", 0, 2, "", methodObject), 
    new Among("dd", 0, 2, "", methodObject), 
    new Among("ff", 0, 2, "", methodObject), 
    new Among("gg", 0, 2, "", methodObject), 
    new Among("bl", 0, 1, "", methodObject), 
    new Among("mm", 0, 2, "", methodObject), 
    new Among("nn", 0, 2, "", methodObject), 
    new Among("pp", 0, 2, "", methodObject), 
    new Among("rr", 0, 2, "", methodObject), 
    new Among("at", 0, 1, "", methodObject), 
    new Among("tt", 0, 2, "", methodObject), 
    new Among("iz", 0, 1, "", methodObject) };

  private static final Among[] A_4 = { 
    new Among("ed", -1, 2, "", methodObject), 
    new Among("eed", 0, 1, "", methodObject), 
    new Among("ing", -1, 2, "", methodObject), 
    new Among("edly", -1, 2, "", methodObject), 
    new Among("eedly", 3, 1, "", methodObject), 
    new Among("ingly", -1, 2, "", methodObject) };

  private static final Among[] A_5 = { 
    new Among("anci", -1, 3, "", methodObject), 
    new Among("enci", -1, 2, "", methodObject), 
    new Among("ogi", -1, 13, "", methodObject), 
    new Among("li", -1, 16, "", methodObject), 
    new Among("bli", 3, 12, "", methodObject), 
    new Among("abli", 4, 4, "", methodObject), 
    new Among("alli", 3, 8, "", methodObject), 
    new Among("fulli", 3, 14, "", methodObject), 
    new Among("lessli", 3, 15, "", methodObject), 
    new Among("ousli", 3, 10, "", methodObject), 
    new Among("entli", 3, 5, "", methodObject), 
    new Among("aliti", -1, 8, "", methodObject), 
    new Among("biliti", -1, 12, "", methodObject), 
    new Among("iviti", -1, 11, "", methodObject), 
    new Among("tional", -1, 1, "", methodObject), 
    new Among("ational", 14, 7, "", methodObject), 
    new Among("alism", -1, 8, "", methodObject), 
    new Among("ation", -1, 7, "", methodObject), 
    new Among("ization", 17, 6, "", methodObject), 
    new Among("izer", -1, 6, "", methodObject), 
    new Among("ator", -1, 7, "", methodObject), 
    new Among("iveness", -1, 11, "", methodObject), 
    new Among("fulness", -1, 9, "", methodObject), 
    new Among("ousness", -1, 10, "", methodObject) };

  private static final Among[] A_6 = { 
    new Among("icate", -1, 4, "", methodObject), 
    new Among("ative", -1, 6, "", methodObject), 
    new Among("alize", -1, 3, "", methodObject), 
    new Among("iciti", -1, 4, "", methodObject), 
    new Among("ical", -1, 4, "", methodObject), 
    new Among("tional", -1, 1, "", methodObject), 
    new Among("ational", 5, 2, "", methodObject), 
    new Among("ful", -1, 5, "", methodObject), 
    new Among("ness", -1, 5, "", methodObject) };

  private static final Among[] A_7 = { 
    new Among("ic", -1, 1, "", methodObject), 
    new Among("ance", -1, 1, "", methodObject), 
    new Among("ence", -1, 1, "", methodObject), 
    new Among("able", -1, 1, "", methodObject), 
    new Among("ible", -1, 1, "", methodObject), 
    new Among("ate", -1, 1, "", methodObject), 
    new Among("ive", -1, 1, "", methodObject), 
    new Among("ize", -1, 1, "", methodObject), 
    new Among("iti", -1, 1, "", methodObject), 
    new Among("al", -1, 1, "", methodObject), 
    new Among("ism", -1, 1, "", methodObject), 
    new Among("ion", -1, 2, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("ous", -1, 1, "", methodObject), 
    new Among("ant", -1, 1, "", methodObject), 
    new Among("ent", -1, 1, "", methodObject), 
    new Among("ment", 15, 1, "", methodObject), 
    new Among("ement", 16, 1, "", methodObject) };

  private static final Among[] a_8 = { 
    new Among("e", -1, 1, "", methodObject), 
    new Among("l", -1, 2, "", methodObject) };

  private static final Among[] a_9 = { 
    new Among("succeed", -1, -1, "", methodObject), 
    new Among("proceed", -1, -1, "", methodObject), 
    new Among("exceed", -1, -1, "", methodObject), 
    new Among("canning", -1, -1, "", methodObject), 
    new Among("inning", -1, -1, "", methodObject), 
    new Among("earring", -1, -1, "", methodObject), 
    new Among("herring", -1, -1, "", methodObject), 
    new Among("outing", -1, -1, "", methodObject) };

  private static final Among[] a_10 = { 
    new Among("andes", -1, -1, "", methodObject), 
    new Among("atlas", -1, -1, "", methodObject), 
    new Among("bias", -1, -1, "", methodObject), 
    new Among("cosmos", -1, -1, "", methodObject), 
    new Among("dying", -1, 3, "", methodObject), 
    new Among("early", -1, 9, "", methodObject), 
    new Among("gently", -1, 7, "", methodObject), 
    new Among("howe", -1, -1, "", methodObject), 
    new Among("idly", -1, 6, "", methodObject), 
    new Among("lying", -1, 4, "", methodObject), 
    new Among("news", -1, -1, "", methodObject), 
    new Among("only", -1, 10, "", methodObject), 
    new Among("singly", -1, 11, "", methodObject), 
    new Among("skies", -1, 2, "", methodObject), 
    new Among("skis", -1, 1, "", methodObject), 
    new Among("sky", -1, -1, "", methodObject), 
    new Among("tying", -1, 5, "", methodObject), 
    new Among("ugly", -1, 8, "", methodObject) };

  private static final char[] g_v = { '\021', 'A', '\020', '\001' };

  private static final char[] g_v_WXY = { '\001', '\021', 'A', 'Ð', '\001' };

  private static final char[] g_valid_LI = { '7', '', '\002' };
  private boolean B_Y_found;
  private int I_p2;
  private int I_p1;

  private void copy_from(EnglishStemmer other)
  {
    this.B_Y_found = other.B_Y_found;
    this.I_p2 = other.I_p2;
    this.I_p1 = other.I_p1;
    super.copy_from(other);
  }

  private boolean r_prelude()
  {
    this.B_Y_found = false;

    int v_1 = this.cursor;

    this.bra = this.cursor;

    if (eq_s(1, "'"))
    {
      this.ket = this.cursor;

      slice_del();
    }
    this.cursor = v_1;

    int v_2 = this.cursor;

    this.bra = this.cursor;

    if (eq_s(1, "y"))
    {
      this.ket = this.cursor;

      slice_from("Y");

      this.B_Y_found = true;
    }this.cursor = v_2;

    int v_3 = this.cursor;
    int v_4;
    while (true) {
      v_4 = this.cursor;
      while (true)
      {
        int v_5 = this.cursor;

        if (in_grouping(g_v, 97, 121))
        {
          this.bra = this.cursor;

          if (eq_s(1, "y"))
          {
            this.ket = this.cursor;
            this.cursor = v_5;
            break;
          }
        }
        this.cursor = v_5;
        if (this.cursor >= this.limit) {
          break label221;
        }
        this.cursor += 1;
      }
      int v_5;
      slice_from("Y");

      this.B_Y_found = true;
    }

    label221: this.cursor = v_4;

    this.cursor = v_3;
    return true;
  }

  private boolean r_mark_regions()
  {
    this.I_p1 = this.limit;
    this.I_p2 = this.limit;

    int v_1 = this.cursor;

    int v_2 = this.cursor;

    if (find_among(A_0, 3) == 0)
    {
      this.cursor = v_2;

      while (!in_grouping(g_v, 97, 121))
      {
        if (this.cursor >= this.limit) {
          break label222;
        }
        this.cursor += 1;
      }

      while (!out_grouping(g_v, 97, 121))
      {
        if (this.cursor >= this.limit) {
          break label222;
        }
        this.cursor += 1;
      }
    }

    this.I_p1 = this.cursor;

    while (!in_grouping(g_v, 97, 121))
    {
      if (this.cursor >= this.limit) {
        break label222;
      }
      this.cursor += 1;
    }

    while (!out_grouping(g_v, 97, 121))
    {
      if (this.cursor >= this.limit) {
        break label222;
      }
      this.cursor += 1;
    }

    this.I_p2 = this.cursor;

    label222: this.cursor = v_1;
    return true;
  }

  private boolean r_shortv()
  {
    int v_1 = this.limit - this.cursor;

    if ((!out_grouping_b(g_v_WXY, 89, 121)) || 
      (!in_grouping_b(g_v, 97, 121)) || 
      (!out_grouping_b(g_v, 97, 121)))
    {
      this.cursor = (this.limit - v_1);

      if (!out_grouping_b(g_v, 97, 121)) {
        return false;
      }
      if (!in_grouping_b(g_v, 97, 121)) {
        return false;
      }

      if (this.cursor > this.limit_backward) {
        return false;
      }
    }
    return true;
  }

  private boolean r_R1()
  {
    if (this.I_p1 > this.cursor) {
      return false;
    }
    return true;
  }

  private boolean r_R2()
  {
    if (this.I_p2 > this.cursor) {
      return false;
    }
    return true;
  }

  private boolean r_Step_1a()
  {
    int v_1 = this.limit - this.cursor;

    this.ket = this.cursor;

    int among_var = find_among_b(A_1, 3);
    if (among_var == 0) {
      this.cursor = (this.limit - v_1);
    }
    else
    {
      this.bra = this.cursor;
      switch (among_var) {
      case 0:
        this.cursor = (this.limit - v_1);
        break;
      case 1:
        slice_del();
      }

    }

    this.ket = this.cursor;

    among_var = find_among_b(A_2, 6);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;
    switch (among_var) {
    case 0:
      return false;
    case 1:
      slice_from("ss");
      break;
    case 2:
      int v_2 = this.limit - this.cursor;

      int c = this.cursor - 2;
      if ((this.limit_backward <= c) && (c <= this.limit))
      {
        this.cursor = c;

        slice_from("i");
      }
      else {
        this.cursor = (this.limit - v_2);

        slice_from("ie");
      }
      break;
    case 3:
      if (this.cursor <= this.limit_backward) {
        return false;
      }
      this.cursor -= 1;

      while (!in_grouping_b(g_v, 97, 121))
      {
        if (this.cursor <= this.limit_backward) {
          return false;
        }
        this.cursor -= 1;
      }

      slice_del();
    }

    return true;
  }

  private boolean r_Step_1b()
  {
    this.ket = this.cursor;

    int among_var = find_among_b(A_4, 6);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;
    switch (among_var) {
    case 0:
      return false;
    case 1:
      if (!r_R1()) {
        return false;
      }

      slice_from("ee");
      break;
    case 2:
      int v_1 = this.limit - this.cursor;

      while (!in_grouping_b(g_v, 97, 121))
      {
        if (this.cursor <= this.limit_backward) {
          return false;
        }
        this.cursor -= 1;
      }
      this.cursor = (this.limit - v_1);

      slice_del();

      int v_3 = this.limit - this.cursor;

      among_var = find_among_b(A_3, 13);
      if (among_var == 0) {
        return false;
      }
      this.cursor = (this.limit - v_3);
      switch (among_var) {
      case 0:
        return false;
      case 1:
        int c = this.cursor;
        insert(this.cursor, this.cursor, "e");
        this.cursor = c;

        break;
      case 2:
        this.ket = this.cursor;

        if (this.cursor <= this.limit_backward) {
          return false;
        }
        this.cursor -= 1;

        this.bra = this.cursor;

        slice_del();
        break;
      case 3:
        if (this.cursor != this.I_p1) {
          return false;
        }

        int v_4 = this.limit - this.cursor;

        if (!r_shortv()) {
          return false;
        }
        this.cursor = (this.limit - v_4);

        int c = this.cursor;
        insert(this.cursor, this.cursor, "e");
        this.cursor = c;
      }

      break;
    }

    return true;
  }

  private boolean r_Step_1c()
  {
    this.ket = this.cursor;

    int v_1 = this.limit - this.cursor;

    if (!eq_s_b(1, "y"))
    {
      this.cursor = (this.limit - v_1);

      if (!eq_s_b(1, "Y")) {
        return false;
      }
    }

    this.bra = this.cursor;
    if (!out_grouping_b(g_v, 97, 121)) {
      return false;
    }

    int v_2 = this.limit - this.cursor;

    if (this.cursor <= this.limit_backward)
    {
      return false;
    }
    this.cursor = (this.limit - v_2);

    slice_from("i");
    return true;
  }

  private boolean r_Step_2()
  {
    this.ket = this.cursor;

    int among_var = find_among_b(A_5, 24);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;

    if (!r_R1()) {
      return false;
    }
    switch (among_var) {
    case 0:
      return false;
    case 1:
      slice_from("tion");
      break;
    case 2:
      slice_from("ence");
      break;
    case 3:
      slice_from("ance");
      break;
    case 4:
      slice_from("able");
      break;
    case 5:
      slice_from("ent");
      break;
    case 6:
      slice_from("ize");
      break;
    case 7:
      slice_from("ate");
      break;
    case 8:
      slice_from("al");
      break;
    case 9:
      slice_from("ful");
      break;
    case 10:
      slice_from("ous");
      break;
    case 11:
      slice_from("ive");
      break;
    case 12:
      slice_from("ble");
      break;
    case 13:
      if (!eq_s_b(1, "l")) {
        return false;
      }

      slice_from("og");
      break;
    case 14:
      slice_from("ful");
      break;
    case 15:
      slice_from("less");
      break;
    case 16:
      if (!in_grouping_b(g_valid_LI, 99, 116)) {
        return false;
      }

      slice_del();
    }

    return true;
  }

  private boolean r_Step_3()
  {
    this.ket = this.cursor;

    int among_var = find_among_b(A_6, 9);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;

    if (!r_R1()) {
      return false;
    }
    switch (among_var) {
    case 0:
      return false;
    case 1:
      slice_from("tion");
      break;
    case 2:
      slice_from("ate");
      break;
    case 3:
      slice_from("al");
      break;
    case 4:
      slice_from("ic");
      break;
    case 5:
      slice_del();
      break;
    case 6:
      if (!r_R2()) {
        return false;
      }

      slice_del();
    }

    return true;
  }

  private boolean r_Step_4()
  {
    this.ket = this.cursor;

    int among_var = find_among_b(A_7, 18);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;

    if (!r_R2()) {
      return false;
    }
    switch (among_var) {
    case 0:
      return false;
    case 1:
      slice_del();
      break;
    case 2:
      int v_1 = this.limit - this.cursor;

      if (!eq_s_b(1, "s"))
      {
        this.cursor = (this.limit - v_1);

        if (!eq_s_b(1, "t")) {
          return false;
        }
      }

      slice_del();
    }

    return true;
  }

  private boolean r_Step_5()
  {
    this.ket = this.cursor;

    int among_var = find_among_b(a_8, 2);
    if (among_var == 0) {
      return false;
    }

    this.bra = this.cursor;
    switch (among_var) {
    case 0:
      return false;
    case 1:
      int v_1 = this.limit - this.cursor;

      if (!r_R2())
      {
        this.cursor = (this.limit - v_1);

        if (!r_R1()) {
          return false;
        }

        int v_2 = this.limit - this.cursor;

        if (r_shortv())
        {
          return false;
        }
        this.cursor = (this.limit - v_2);
      }

      slice_del();
      break;
    case 2:
      if (!r_R2()) {
        return false;
      }

      if (!eq_s_b(1, "l")) {
        return false;
      }

      slice_del();
    }

    return true;
  }

  private boolean r_exception2()
  {
    this.ket = this.cursor;

    if (find_among_b(a_9, 8) == 0) {
      return false;
    }

    this.bra = this.cursor;

    if (this.cursor > this.limit_backward) {
      return false;
    }
    return true;
  }

  private boolean r_exception1()
  {
    this.bra = this.cursor;

    int among_var = find_among(a_10, 18);
    if (among_var == 0) {
      return false;
    }

    this.ket = this.cursor;

    if (this.cursor < this.limit) {
      return false;
    }
    switch (among_var) {
    case 0:
      return false;
    case 1:
      slice_from("ski");
      break;
    case 2:
      slice_from("sky");
      break;
    case 3:
      slice_from("die");
      break;
    case 4:
      slice_from("lie");
      break;
    case 5:
      slice_from("tie");
      break;
    case 6:
      slice_from("idl");
      break;
    case 7:
      slice_from("gentl");
      break;
    case 8:
      slice_from("ugli");
      break;
    case 9:
      slice_from("earli");
      break;
    case 10:
      slice_from("onli");
      break;
    case 11:
      slice_from("singl");
    }

    return true;
  }

  private boolean r_postlude()
  {
    if (!this.B_Y_found)
      return false;
    int v_1;
    while (true)
    {
      v_1 = this.cursor;
      while (true)
      {
        int v_2 = this.cursor;

        this.bra = this.cursor;

        if (eq_s(1, "Y"))
        {
          this.ket = this.cursor;
          this.cursor = v_2;
          break;
        }
        this.cursor = v_2;
        if (this.cursor >= this.limit) {
          break label99;
        }
        this.cursor += 1;
      }
      int v_2;
      slice_from("y");
    }

    label99: this.cursor = v_1;

    return true;
  }

  public boolean stem()
  {
    int v_1 = this.cursor;

    if (!r_exception1())
    {
      this.cursor = v_1;

      int v_2 = this.cursor;

      int c = this.cursor + 3;
      if ((c >= 0) && (c <= this.limit))
      {
        this.cursor = c;
      }
      else
      {
        this.cursor = v_2;

        break label380;
      }
      this.cursor = v_1;

      int v_3 = this.cursor;

      if (!r_prelude());
      this.cursor = v_3;

      int v_4 = this.cursor;

      if (!r_mark_regions());
      this.cursor = v_4;

      this.limit_backward = this.cursor;
      this.cursor = this.limit;

      int v_5 = this.limit - this.cursor;

      if (!r_Step_1a());
      this.cursor = (this.limit - v_5);

      int v_6 = this.limit - this.cursor;

      if (!r_exception2())
      {
        this.cursor = (this.limit - v_6);

        int v_7 = this.limit - this.cursor;

        if (!r_Step_1b());
        this.cursor = (this.limit - v_7);

        int v_8 = this.limit - this.cursor;

        if (!r_Step_1c());
        this.cursor = (this.limit - v_8);

        int v_9 = this.limit - this.cursor;

        if (!r_Step_2());
        this.cursor = (this.limit - v_9);

        int v_10 = this.limit - this.cursor;

        if (!r_Step_3());
        this.cursor = (this.limit - v_10);

        int v_11 = this.limit - this.cursor;

        if (!r_Step_4());
        this.cursor = (this.limit - v_11);

        int v_12 = this.limit - this.cursor;

        if (!r_Step_5());
        this.cursor = (this.limit - v_12);
      }
      this.cursor = this.limit_backward;
      int v_13 = this.cursor;

      if (!r_postlude());
      this.cursor = v_13;
    }
    label380: return true;
  }

  public boolean equals(Object o)
  {
    return o instanceof EnglishStemmer;
  }

  public int hashCode()
  {
    return EnglishStemmer.class.getName().hashCode();
  }
}