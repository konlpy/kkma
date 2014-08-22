package org.snu.ids.ha.sp;

import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.Morpheme;

public class ParseGrammar
{
  String relation = null;
  String dependantMorp = null;
  long dependantTag = 0L;
  String dominantMorp = null;
  long dominantTag = 0L;
  int distance = 1;
  int priority = 10;

  public ParseGrammar(String relation, long dependantTag, long dominantTag, int distance, int priority)
  {
    this.relation = relation;
    this.dependantTag = dependantTag;
    this.dominantTag = dominantTag;
    this.distance = distance;
    this.priority = priority;
  }

  public ParseGrammar(String relation, String dependantMorp, long dependantTag, String dominantMorp, long dominantTag, int distance, int priority)
  {
    this.relation = relation;
    this.dependantMorp = dependantMorp;
    this.dependantTag = dependantTag;
    this.dominantMorp = dominantMorp;
    this.dominantTag = dominantTag;
    this.distance = distance;
    this.priority = priority;
  }

  public ParseTreeEdge dominate(ParseTreeNode prevNode, ParseTreeNode nextNode, int distance)
  {
    if (this.dominantTag == POSTag.VCP) {
      if ((prevNode.getEojeol().isLastTagOf(this.dependantTag)) && 
        (nextNode.getEojeol().containsTagOf(this.dominantTag)) && 
        (distance <= this.distance))
      {
        return new ParseTreeEdge(this.relation, prevNode, nextNode, distance, this.priority);
      }
    }
    else if ((prevNode.getEojeol().isLastTagOf(this.dependantTag)) && 
      (nextNode.getEojeol().getFirstMorp().isTagOf(this.dominantTag)) && 
      (distance <= this.distance))
    {
      return new ParseTreeEdge(this.relation, prevNode, nextNode, distance, this.priority);
    }

    return null;
  }
}