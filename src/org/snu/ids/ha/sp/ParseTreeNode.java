package org.snu.ids.ha.sp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.util.Util;

public class ParseTreeNode
{
  int id = 0;
  private Eojeol eojeol;
  private ParseTreeNode parentNode;
  private List<ParseTreeEdge> childEdges;

  protected ParseTreeNode(Eojeol eojeol)
  {
    this.eojeol = eojeol;
  }

  public List<ParseTreeEdge> getChildEdges()
  {
    return this.childEdges;
  }

  public void addChildEdge(ParseTreeEdge arc)
  {
    if (this.childEdges == null) this.childEdges = new ArrayList();
    this.childEdges.add(arc);
  }

  public int getId()
  {
    return this.id;
  }

  public Eojeol getEojeol()
  {
    return this.eojeol;
  }

  public ParseTreeNode getParentNode()
  {
    return this.parentNode;
  }

  public void setParentNode(ParseTreeNode parentNode)
  {
    this.parentNode = parentNode;
  }

  public boolean contains(ParseTreeNode node)
  {
    if (this.eojeol == node.eojeol) return true;
    for (Iterator iterChildEdge = this.childEdges.iterator(); iterChildEdge.hasNext(); ) {
      ParseTreeEdge edge = (ParseTreeEdge)iterChildEdge.next();
      ParseTreeNode child = edge.getChildNode();

      if (child.contains(node)) {
        return true;
      }
    }
    return false;
  }

  public void traverse(int depth, String relation, StringBuffer sb)
  {
    for (int i = 0; i < depth; i++)
      sb.append("\t");
    if (relation != null) sb.append("<=[" + relation + "]=| ");
    sb.append(this.id + "\t" + this.eojeol + "\n");
    int i = 0; for (int size = this.childEdges == null ? 0 : this.childEdges.size(); i < size; i++) {
      ParseTreeEdge edge = (ParseTreeEdge)this.childEdges.get(i);
      edge.getChildNode().traverse(depth + 1, edge.getRelation(), sb);
    }
  }

  public int traverse(int id)
  {
    this.id = id;
    int ret = id;
    int i = 0; for (int size = this.childEdges == null ? 0 : this.childEdges.size(); i < size; i++) {
      ParseTreeEdge edge = (ParseTreeEdge)this.childEdges.get(i);
      ret = edge.getChildNode().traverse(ret + 1);
    }
    return ret;
  }

  public void traverse(List<ParseTreeNode> nodeList, List<ParseTreeEdge> edgeList)
  {
    nodeList.add(this);
    int i = 0; for (int size = this.childEdges == null ? 0 : this.childEdges.size(); i < size; i++) {
      ParseTreeEdge edge = (ParseTreeEdge)this.childEdges.get(i);
      edgeList.add(edge);
      edge.getChildNode().traverse(nodeList, edgeList);
    }
  }

  public String getExp()
  {
    return this.eojeol == null ? "ROOT" : this.eojeol.getExp();
  }

  public String getMorpXmlStr()
  {
    StringBuffer sb = new StringBuffer();
    int i = 0; for (int size = this.eojeol == null ? 0 : this.eojeol.size(); i < size; i++) {
      Morpheme morp = (Morpheme)this.eojeol.get(i);
      if (i > 0) sb.append("+");
      sb.append(Util.rplcXMLSpclChar(morp.getString()) + "/" + morp.getTag());
    }
    return sb.toString();
  }
}