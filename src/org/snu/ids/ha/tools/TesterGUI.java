package org.snu.ids.ha.tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import org.snu.ids.ha.dic.Dictionary;
import org.snu.ids.ha.index.Keyword;
import org.snu.ids.ha.index.KeywordExtractor;
import org.snu.ids.ha.index.KeywordList;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.util.Timer;
import org.snu.ids.ha.util.Util;

public class TesterGUI extends JFrame
{
  JTextArea logText = null;
  JPanel statusPanel = null;
  KeywordExtractor ke = null;
  JProgressBar progressBar = null;
  JLabel lineLabel = null;
  JLabel statusLabel = null;

  public static void main(String[] args)
  {
    TesterGUI gui = new TesterGUI();
    gui.setVisible(true);
  }

  public TesterGUI()
  {
    setSize(1024, 800);
    setDefaultCloseOperation(3);
    setTitle("Korean Morpheme Analyzer Tester");

    Container c = getContentPane();
    c.setLayout(new BorderLayout());

    JTabbedPane tabPane = new JTabbedPane();

    tabPane.addTab("색인어 추출기", new KEPanel());
    tabPane.addTab("분석기", new MAPanel());

    JSplitPane sp = new JSplitPane(0, tabPane, getLogPanel());
    sp.setOneTouchExpandable(true);
    sp.setDividerLocation(600);

    c.add(sp, "Center");

    this.statusPanel = new JPanel(new FlowLayout(2));
    this.statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

    this.lineLabel = new JLabel();
    this.statusPanel.add(this.lineLabel);

    this.progressBar = new JProgressBar();
    this.progressBar.setPreferredSize(new Dimension(200, 15));
    this.progressBar.setBorderPainted(false);
    this.statusPanel.add(this.progressBar);

    this.statusLabel = new JLabel();
    this.statusPanel.add(this.statusLabel);

    c.add(this.statusPanel, "South");
  }

  void createKE()
  {
    startJob("사전 읽기");
    Timer timer = new Timer();
    timer.start();
    this.ke = new KeywordExtractor();
    timer.stop();
    endJob(timer.getInterval());
  }

  void startJob(String job)
  {
    this.progressBar.setIndeterminate(true);
    this.progressBar.setBorderPainted(true);
    this.statusLabel.setText(job);
    printlog(job);
  }

  void endJob(double interval)
  {
    this.progressBar.setIndeterminate(false);
    this.progressBar.setBorderPainted(false);
    this.statusLabel.setText(interval + "초");
    printlog("완료: " + interval + "초");
  }

  public JPanel getLogPanel()
  {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Console"));
    this.logText = new JTextArea();
    this.logText.setTabSize(4);
    this.logText.setEditable(false);
    JScrollPane sp = new JScrollPane();
    sp.getViewport().add(this.logText);
    panel.add(sp, "Center");
    return panel;
  }

  void printlog(final String log)
  {
    Thread thread = new Thread()
    {
      public void run()
      {
        TesterGUI.this.logText.append(log + "\n");
      }
    };
    thread.start();
  }

  class KEPanel extends JPanel
    implements ActionListener
  {
    JTextArea srcText = null;
    JCheckBox onlyNounCheck = null;
    JTable table = null;
    KeywordList keywordList = null;
    File recentDir = null;

    public KEPanel()
    {
      super();
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      JSplitPane sp = new JSplitPane(1, getSrcPane(), getResultPane());
      sp.setOneTouchExpandable(true);
      sp.setDividerLocation(500);
      add(sp, "Center");
    }

    public JPanel getSrcPane()
    {
      JPanel panel = new JPanel(new BorderLayout());

      JPanel menuPanel = new JPanel(new FlowLayout(2));

      this.onlyNounCheck = new JCheckBox("명사만 추출");
      menuPanel.add(this.onlyNounCheck);

      JButton button = new JButton("파일 열기");
      button.setActionCommand("OPEN_FILE");
      button.addActionListener(this);
      menuPanel.add(button);

      button = new JButton("파일로 저장");
      button.setActionCommand("SAVE_TO_FILE");
      button.addActionListener(this);
      menuPanel.add(button);

      button = new JButton("분석하기");
      button.setActionCommand("ANALYZE");
      button.addActionListener(this);
      menuPanel.add(button);

      panel.add(menuPanel, "North");

      this.srcText = new JTextArea();
      this.srcText.setTabSize(4);
      JScrollPane sp = new JScrollPane();
      sp.getViewport().add(this.srcText);

      JPanel srcPanel = new JPanel(new BorderLayout());
      srcPanel.setBorder(BorderFactory.createTitledBorder("Contents"));
      srcPanel.add(sp);

      panel.add(srcPanel, "Center");

      return panel;
    }

    public JPanel getResultPane()
    {
      JPanel panel = new JPanel(new BorderLayout());

      this.table = new JTable(new KeywordDataModel());
      this.table.setAutoCreateRowSorter(true);

      JScrollPane sp = new JScrollPane();
      sp.getViewport().add(this.table);

      panel.add(sp, "Center");

      return panel;
    }

    public void actionPerformed(ActionEvent ae)
    {
      String cmd = ae.getActionCommand();

      if (cmd.equals("OPEN_FILE")) {
        JFileChooser jfc = new JFileChooser();
        if (this.recentDir == null) {
          File curDir = new File("");
          this.recentDir = curDir.getAbsoluteFile();
        }
        jfc.setCurrentDirectory(this.recentDir);
        if (jfc.showOpenDialog(this) == 0) {
          File file = jfc.getSelectedFile();
          this.recentDir = file.getParentFile();
          readFile(file);
        }
      } else if (cmd.equals("SAVE_TO_FILE")) {
        JFileChooser jfc = new JFileChooser();
        File curDir = new File("");
        jfc.setCurrentDirectory(curDir.getAbsoluteFile());
        if (jfc.showSaveDialog(this) == 0) {
          File file = jfc.getSelectedFile();
          this.recentDir = file.getParentFile();
          saveToFile(file);
        }
      } else if (cmd.equals("ANALYZE")) {
        analyze();
      }
    }

    void readFile(File file)
    {
      BufferedReader br = null;
      try {
        TesterGUI.this.printlog("READING FILE: " + file.getAbsolutePath());
        br = new BufferedReader(new FileReader(file));

        String line = null;
        cleanSrcText();
        while ((line = br.readLine()) != null) {
          this.srcText.append(line + "\n");
        }
        this.srcText.updateUI();
        br.close();
      } catch (Exception e) {
        TesterGUI.this.printlog("ERROR: " + e.toString());
      }
    }

    void saveToFile(File file)
    {
      PrintWriter pw = null;
      try {
        pw = new PrintWriter(file);

        int i = 0; for (int size = this.keywordList == null ? 0 : this.keywordList.size(); i < size; i++) {
          Keyword keyword = (Keyword)this.keywordList.get(i);
          pw.println(keyword.getIndex() + "\t" + keyword.getString() + "\t" + keyword.getTag() + "\t" + keyword.getCnt());
        }
        pw.flush();
        pw.close();
      } catch (Exception e) {
        TesterGUI.this.printlog("ERROR: " + e.toString());
      }
    }

    void cleanSrcText()
    {
      this.srcText.setText("");
    }

    void analyze()
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          String string = TesterGUI.KEPanel.this.srcText.getText();
          if (!Util.valid(string)) {
            TesterGUI.this.printlog("분석할 문장이 없습니다.");
            return;
          }
          try {
            if (TesterGUI.this.ke == null) TesterGUI.this.createKE();
            TesterGUI.this.startJob("단어 추출");
            Timer timer = new Timer();
            timer.start();
            TesterGUI.KEPanel.this.keywordList = TesterGUI.this.ke.extractKeyword(TesterGUI.this.progressBar, TesterGUI.this.lineLabel, string, TesterGUI.KEPanel.this.onlyNounCheck.isSelected());
            TesterGUI.KEPanel.this.updateTableMode();
            TesterGUI.this.printlog("전체 단어 수: " + TesterGUI.KEPanel.this.keywordList.getDocLen());
            timer.stop();
            TesterGUI.this.endJob(timer.getInterval());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      thread.start();
    }

    void updateTableMode()
    {
      this.table.setModel(new KeywordDataModel());
      TableRowSorter sorter = new TableRowSorter(this.table.getModel());
      Comparator intComparator = new Comparator()
      {
        public int compare(Integer arg0, Integer arg1)
        {
          return arg0.intValue() - arg1.intValue();
        }
      };
      sorter.setComparator(0, intComparator);
      sorter.setComparator(3, intComparator);
      this.table.setRowSorter(sorter);
      this.table.updateUI();
    }

    class KeywordDataModel extends AbstractTableModel
    {
      KeywordDataModel()
      {
      }

      public int getColumnCount()
      {
        return 4;
      }

      public int getRowCount()
      {
        return TesterGUI.KEPanel.this.keywordList == null ? 0 : TesterGUI.KEPanel.this.keywordList.size();
      }

      public String getColumnName(int col)
      {
        switch (col) {
        case 0:
          return "위치";
        case 1:
          return "단어";
        case 2:
          return "품사";
        case 3:
          return "횟수";
        }
        return null;
      }

      public Object getValueAt(int row, int col)
      {
        if ((TesterGUI.KEPanel.this.keywordList == null) || (row >= TesterGUI.KEPanel.this.keywordList.size())) return null;
        Keyword keyword = (Keyword)TesterGUI.KEPanel.this.keywordList.get(row);
        switch (col) {
        case 0:
          return Integer.valueOf(keyword.getIndex());
        case 1:
          return keyword.getString();
        case 2:
          return keyword.getTag();
        case 3:
          return Integer.valueOf(keyword.getCnt());
        }

        return null;
      }
    }
  }

  class MAPanel extends JPanel
    implements ActionListener
  {
    JTextField inputText = null;
    JTextArea resultText = null;

    public MAPanel()
    {
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      JPanel topButtonPanel = new JPanel(new BorderLayout());
      topButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      this.inputText = new JTextField();
      topButtonPanel.add(this.inputText, "Center");

      JButton button = new JButton("분석");
      button.setActionCommand("ANALYZE");
      button.addActionListener(this);
      topButtonPanel.add(button, "East");

      button = new JButton("사전 재로딩");
      button.setActionCommand("RELOAD");
      button.addActionListener(this);

      topButtonPanel.add(button, "West");
      add(topButtonPanel, "North");

      JPanel resultPanel = new JPanel(new BorderLayout());
      resultPanel.setBorder(BorderFactory.createTitledBorder("분석 결과"));

      this.resultText = new JTextArea();
      this.resultText.setTabSize(4);
      this.resultText.setEditable(false);
      JScrollPane sp = new JScrollPane();
      sp.getViewport().add(this.resultText);
      resultPanel.add(sp);

      add(resultPanel, "Center");
    }

    public void actionPerformed(ActionEvent arg0)
    {
      String cmd = arg0.getActionCommand();
      if (cmd.equals("ANALYZE")) {
        analyze();
      } else if (cmd.equals("RELOAD")) {
        Thread thread = new Thread()
        {
          public void run()
          {
            TesterGUI.this.startJob("사전 다시 읽기");
            Timer timer = new Timer();
            timer.start();
            Dictionary.reload();
            timer.stop();
            TesterGUI.this.endJob(timer.getInterval());
          }
        };
        thread.start();
      }
    }

    void analyze()
    {
      Thread thread = new Thread()
      {
        public void run()
        {
          String str = TesterGUI.MAPanel.this.inputText.getText();

          StringBuffer sb = new StringBuffer();
          if (TesterGUI.this.ke == null) TesterGUI.this.createKE(); try
          {
            Timer timer = new Timer();
            timer.start();
            List ret = TesterGUI.this.ke.leaveJustBest(TesterGUI.this.ke.postProcess(TesterGUI.this.ke.analyze(str)));
            timer.stop();
            TesterGUI.this.printlog("총 분석 시간: " + timer.getInterval());

            List stl = TesterGUI.this.ke.divideToSentences(ret);
            for (int i = 0; i < stl.size(); i++) {
              Sentence st = (Sentence)stl.get(i);
              sb.append(st.getSentence() + "\n");
              for (int j = 0; j < st.size(); j++) {
                sb.append("\t" + st.get(j) + "\n");
              }
              sb.append("\n");
            }

            TesterGUI.MAPanel.this.resultText.setText(sb.toString());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      thread.run();
    }
  }
}