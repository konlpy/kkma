package org.snu.ids.ha.sp;

import java.util.ArrayList;
import java.util.List;

public class ParseTree
{
  String sentenec = null;
  ParseTreeNode root = new ParseTreeNode(null);
  List<ParseTreeNode> nodeList = null;
  List<ParseTreeEdge> edgeList = null;

  public void setRoot(ParseTreeNode ptn)
  {
    this.root.addChildEdge(new ParseTreeEdge("연결", ptn, this.root, 1, 1));
  }

  public void traverse(StringBuffer sb)
  {
    this.root.traverse(0, null, sb);
  }

  public void setId()
  {
    this.root.traverse(0);
  }

  public void setAllList()
  {
    this.nodeList = new ArrayList();
    this.edgeList = new ArrayList();
    this.root.traverse(this.nodeList, this.edgeList);
  }

  public List<ParseTreeNode> getNodeList()
  {
    return this.nodeList;
  }

  public List<ParseTreeEdge> getEdgeList()
  {
    return this.edgeList;
  }

  public String getSentenec()
  {
    return this.sentenec;
  }

  public void setSentenec(String sentenec)
  {
    this.sentenec = sentenec;
  }
}