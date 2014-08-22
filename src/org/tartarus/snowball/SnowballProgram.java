package org.tartarus.snowball;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SnowballProgram
{
  protected StringBuffer current;
  protected int cursor;
  protected int limit;
  protected int limit_backward;
  protected int bra;
  protected int ket;

  protected SnowballProgram()
  {
    this.current = new StringBuffer();
    setCurrent("");
  }

  public void setCurrent(String value)
  {
    this.current.replace(0, this.current.length(), value);
    this.cursor = 0;
    this.limit = this.current.length();
    this.limit_backward = 0;
    this.bra = this.cursor;
    this.ket = this.limit;
  }

  public String getCurrent()
  {
    String result = this.current.toString();

    this.current = new StringBuffer();
    return result;
  }

  protected void copy_from(SnowballProgram other)
  {
    this.current = other.current;
    this.cursor = other.cursor;
    this.limit = other.limit;
    this.limit_backward = other.limit_backward;
    this.bra = other.bra;
    this.ket = other.ket;
  }

  protected boolean in_grouping(char[] s, int min, int max)
  {
    if (this.cursor >= this.limit) return false;
    char ch = this.current.charAt(this.cursor);
    if ((ch > max) || (ch < min)) return false;
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) return false;
    this.cursor += 1;
    return true;
  }

  protected boolean in_grouping_b(char[] s, int min, int max)
  {
    if (this.cursor <= this.limit_backward) return false;
    char ch = this.current.charAt(this.cursor - 1);
    if ((ch > max) || (ch < min)) return false;
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) return false;
    this.cursor -= 1;
    return true;
  }

  protected boolean out_grouping(char[] s, int min, int max)
  {
    if (this.cursor >= this.limit) return false;
    char ch = this.current.charAt(this.cursor);
    if ((ch > max) || (ch < min)) {
      this.cursor += 1;
      return true;
    }
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) {
      this.cursor += 1;
      return true;
    }
    return false;
  }

  protected boolean out_grouping_b(char[] s, int min, int max)
  {
    if (this.cursor <= this.limit_backward) return false;
    char ch = this.current.charAt(this.cursor - 1);
    if ((ch > max) || (ch < min)) {
      this.cursor -= 1;
      return true;
    }
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) {
      this.cursor -= 1;
      return true;
    }
    return false;
  }

  protected boolean in_range(int min, int max)
  {
    if (this.cursor >= this.limit) return false;
    char ch = this.current.charAt(this.cursor);
    if ((ch > max) || (ch < min)) return false;
    this.cursor += 1;
    return true;
  }

  protected boolean in_range_b(int min, int max)
  {
    if (this.cursor <= this.limit_backward) return false;
    char ch = this.current.charAt(this.cursor - 1);
    if ((ch > max) || (ch < min)) return false;
    this.cursor -= 1;
    return true;
  }

  protected boolean out_range(int min, int max)
  {
    if (this.cursor >= this.limit) return false;
    char ch = this.current.charAt(this.cursor);
    if ((ch <= max) && (ch >= min)) return false;
    this.cursor += 1;
    return true;
  }

  protected boolean out_range_b(int min, int max)
  {
    if (this.cursor <= this.limit_backward) return false;
    char ch = this.current.charAt(this.cursor - 1);
    if ((ch <= max) && (ch >= min)) return false;
    this.cursor -= 1;
    return true;
  }

  protected boolean eq_s(int s_size, String s)
  {
    if (this.limit - this.cursor < s_size) return false;

    for (int i = 0; i != s_size; i++) {
      if (this.current.charAt(this.cursor + i) != s.charAt(i)) return false;
    }
    this.cursor += s_size;
    return true;
  }

  protected boolean eq_s_b(int s_size, String s)
  {
    if (this.cursor - this.limit_backward < s_size) return false;

    for (int i = 0; i != s_size; i++) {
      if (this.current.charAt(this.cursor - s_size + i) != s.charAt(i)) return false;
    }
    this.cursor -= s_size;
    return true;
  }

  protected boolean eq_v(CharSequence s)
  {
    return eq_s(s.length(), s.toString());
  }

  protected boolean eq_v_b(CharSequence s)
  {
    return eq_s_b(s.length(), s.toString());
  }

  protected int find_among(Among[] v, int v_size)
  {
    int i = 0;
    int j = v_size;

    int c = this.cursor;
    int l = this.limit;

    int common_i = 0;
    int common_j = 0;

    boolean first_key_inspected = false;
    while (true)
    {
      int k = i + (j - i >> 1);
      int diff = 0;
      int common = common_i < common_j ? common_i : common_j;
      Among w = v[k];

      for (int i2 = common; i2 < w.s_size; i2++) {
        if (c + common == l) {
          diff = -1;
          break;
        }
        diff = this.current.charAt(c + common) - w.s[i2];
        if (diff != 0) break;
        common++;
      }
      if (diff < 0) {
        j = k;
        common_j = common;
      } else {
        i = k;
        common_i = common;
      }
      if (j - i <= 1) {
        if ((i > 0) || 
          (j == i))
        {
          break;
        }

        if (first_key_inspected) break;
        first_key_inspected = true;
      }
    }
    do {
      Among w = v[i];
      if (common_i >= w.s_size) {
        this.cursor = (c + w.s_size);
        if (w.method == null) return w.result; boolean res;
        try
        {
          Object resobj = w.method.invoke(w.methodobject, new Object[0]);
          res = resobj.toString().equals("true");
        }
        catch (InvocationTargetException e)
        {
          boolean res;
          res = false;
        }
        catch (IllegalAccessException e)
        {
          boolean res;
          res = false;
        }

        this.cursor = (c + w.s_size);
        if (res) return w.result;
      }
      i = w.substring_i;
    }while (i >= 0); return 0;
  }

  protected int find_among_b(Among[] v, int v_size)
  {
    int i = 0;
    int j = v_size;

    int c = this.cursor;
    int lb = this.limit_backward;

    int common_i = 0;
    int common_j = 0;

    boolean first_key_inspected = false;
    while (true)
    {
      int k = i + (j - i >> 1);
      int diff = 0;
      int common = common_i < common_j ? common_i : common_j;
      Among w = v[k];

      for (int i2 = w.s_size - 1 - common; i2 >= 0; i2--) {
        if (c - common == lb) {
          diff = -1;
          break;
        }
        diff = this.current.charAt(c - 1 - common) - w.s[i2];
        if (diff != 0) break;
        common++;
      }
      if (diff < 0) {
        j = k;
        common_j = common;
      } else {
        i = k;
        common_i = common;
      }
      if (j - i <= 1) {
        if ((i > 0) || 
          (j == i) || 
          (first_key_inspected)) break;
        first_key_inspected = true;
      }
    }
    do {
      Among w = v[i];
      if (common_i >= w.s_size) {
        this.cursor = (c - w.s_size);
        if (w.method == null) return w.result;
        boolean res;
        try
        {
          Object resobj = w.method.invoke(w.methodobject, new Object[0]);
          res = resobj.toString().equals("true");
        }
        catch (InvocationTargetException e)
        {
          boolean res;
          res = false;
        }
        catch (IllegalAccessException e)
        {
          boolean res;
          res = false;
        }

        this.cursor = (c - w.s_size);
        if (res) return w.result;
      }
      i = w.substring_i;
    }while (i >= 0); return 0;
  }

  protected int replace_s(int c_bra, int c_ket, String s)
  {
    int adjustment = s.length() - (c_ket - c_bra);
    this.current.replace(c_bra, c_ket, s);
    this.limit += adjustment;
    if (this.cursor >= c_ket)
      this.cursor += adjustment;
    else if (this.cursor > c_bra) this.cursor = c_bra;
    return adjustment;
  }

  protected void slice_check()
  {
    if ((this.bra < 0) || (this.bra > this.ket) || (this.ket > this.limit) || (this.limit > this.current.length()))
    {
      System.err.println("faulty slice operation");
    }
  }

  protected void slice_from(String s)
  {
    slice_check();
    replace_s(this.bra, this.ket, s);
  }

  protected void slice_from(CharSequence s)
  {
    slice_from(s.toString());
  }

  protected void slice_del()
  {
    slice_from("");
  }

  protected void insert(int c_bra, int c_ket, String s)
  {
    int adjustment = replace_s(c_bra, c_ket, s);
    if (c_bra <= this.bra) this.bra += adjustment;
    if (c_bra <= this.ket) this.ket += adjustment;
  }

  protected void insert(int c_bra, int c_ket, CharSequence s)
  {
    insert(c_bra, c_ket, s.toString());
  }

  protected StringBuffer slice_to(StringBuffer s)
  {
    slice_check();
    s.replace(0, s.length(), this.current.substring(this.bra, this.ket));
    return s;
  }

  protected StringBuilder slice_to(StringBuilder s)
  {
    slice_check();
    s.replace(0, s.length(), this.current.substring(this.bra, this.ket));
    return s;
  }

  protected StringBuffer assign_to(StringBuffer s)
  {
    s.replace(0, s.length(), this.current.substring(0, this.limit));
    return s;
  }

  protected StringBuilder assign_to(StringBuilder s)
  {
    s.replace(0, s.length(), this.current.substring(0, this.limit));
    return s;
  }
}