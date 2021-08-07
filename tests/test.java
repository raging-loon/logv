package tests;
import java.awt.event.KeyEvent;

import javax.swing.*;
import src.http.*;

public class test{
  public static void main(String argv[]){
    // List<JTabbedPane> allTabs = new ArrayList<>();
    JFrame mainWindow = new JFrame("Logviewer");
    // HashMap<JTabbedPane,String> tabs = new HashMap<>();
    String file1 = "tests/logs/access.log";
    String file2 = "tests/logs/apachetest1.log";
    Apache2Log log1 = new Apache2Log(file1);
    log1.start();
    Apache2Log log2 = new Apache2Log(file2);
    log2.start();

    JTabbedPane tab1 = new JTabbedPane();
    tab1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    JComponent p1 = new JPanel(), p2 = new JPanel();
    p1.add(log1.getLogTable());
    p2.add(log2.getLogTable());
    
    p1.setVisible(true);
    p2.setVisible(true);
    tab1.addTab(log1.getLogFile(), p1);
    tab1.addTab(log2.getLogFile(),p2);
    tab1.setMnemonicAt(0, KeyEvent.VK_0);
    tab1.setMnemonicAt(1,KeyEvent.VK_1);
    tab1.setBounds(50,50,200,200);
    mainWindow.add(tab1);

    mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setVisible(true);
  }
}