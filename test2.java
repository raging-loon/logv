// package tests;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.*;
import src.http.*;
import src.misc.Vsftpd;

public class test2{
  public static void main(String argv[]){
    // List<JTabbedPane> allTabs = new ArrayList<>();
    JFrame mainWindow = new JFrame("Logviewer");
    // HashMap<JTabbedPane,String> tabs = new HashMap<>();
    String file1 = "tests/weblog1.log";
    String file2 = "tests/vsftpd.log";
    Apache2Log log1 = new Apache2Log(file1);
    log1.Parser();
    Vsftpd log2 = new Vsftpd(file2);
    log2.Parser();

    JTabbedPane tab1 = new JTabbedPane();
    tab1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    JComponent p1 = new JPanel(new BorderLayout()), p2 = new JPanel(new BorderLayout());

    p1.add(new JScrollPane(log1.getLogTable()),BorderLayout.NORTH);
    p2.add(new JScrollPane(log2.getLogTable()),BorderLayout.NORTH);
    p1.setSize(400,300);
    p2.setSize(400,300);
    p1.setVisible(true);
    p2.setVisible(true);
    tab1.addTab(log1.getLogFile(), p1);
    tab1.addTab(log2.getLogFile(),p2);
    // tab1.setMnemonicAt(0, KeyEvent.VK_0);
    // tab1.setMnemonicAt(1,KeyEvent.VK_1);
    tab1.setBounds(0,20,400,300);
    tab1.setSize(400,300);
    mainWindow.add(tab1);

    mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setVisible(true);
  }
}