package org.snu.ids.ha.sp;

public class ParseTreeEdge
{
  private String relation = null;
  private ParseTreeNode childNode = null;
  private int distance = 0;
  private int priority = 0;

  public ParseTreeEdge(String relation, ParseTreeNode childNode, ParseTreeNode parentNode, int distance, int priority)
  {
    this.relation = relation;
    this.childNode = childNode;
    this.childNode.setParentNode(parentNode);
    this.distance = distance;
    this.priority = priority;
  }

  public String getRelation()
  {
    return this.relation;
  }

  public void setRelation(String relation)
  {
    this.relation = relation;
  }

  public ParseTreeNode getChildNode()
  {
    return this.childNode;
  }

  public void setChildNode(ParseTreeNode childNode)
  {
    this.childNode = childNode;
  }

  public int getFromId()
  {
    return this.childNode.getParentNode().getId();
  }

  public int getToId()
  {
    return this.childNode.getId();
  }

  public int getDistance()
  {
    return this.distance;
  }

  public void setDistance(int distance)
  {
    this.distance = distance;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public void setPriority(int priority)
  {
    this.priority = priority;
  }
}