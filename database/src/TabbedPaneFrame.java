import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

public class TabbedPaneFrame extends JFrame{

 
 public TabbedPaneFrame(){
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  createTabbedPane();
  setTitle("scheduler");
  setSize(800, 450);
    
  setVisible(true);
 }
 
 
 void createTabbedPane(){
  JTabbedPane tPane = new JTabbedPane();
  add(tPane);
  
  JLabel mainLabel = new JLabel("첫번째", SwingConstants.CENTER);
  JPanel mainPanel = new JPanel();
  mainPanel.add(mainLabel);
  tPane.addTab("1", mainPanel);
  
  
  
  JLabel schedulerLabel = new JLabel("두번째", SwingConstants.CENTER);
  JPanel schedulerPanel = new JPanel();
  schedulerPanel.add(schedulerLabel);
  tPane.addTab("2", schedulerPanel);
  
  
  JLabel reportLabel = new JLabel("세번째", SwingConstants.CENTER);
  JPanel reportPanel = new JPanel();
  reportPanel.add(reportLabel);
  tPane.addTab("3", reportPanel);
  
  
  JLabel diaryLabel = new JLabel("네번째", SwingConstants.CENTER);
  JPanel diaryPanel = new JPanel();
  diaryLabel.add(diaryPanel);
  tPane.addTab("4", diaryPanel);
 }

 public static void main(String[] ar){
  new TabbedPaneFrame();
 }
}
