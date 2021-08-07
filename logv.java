import src.*;
import src.http.*;
import src.misc.*;
import src.sql.*;
import src.utils.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;


public class logv implements ActionListener{
  private static JFrame mainWindow = new JFrame("LogViewer");
  private static HashMap<JComponent, LogObject> openTabs
                                                = new HashMap<>();

  private static JMenuBar mainMenu = new JMenuBar();
  private static JTabbedPane mainArea = new JTabbedPane();
  private JMenuItem file = new JMenuItem("Open"); 

  synchronized private void addNewTab(String logFile, String logFormat){
    JComponent panel = new JPanel();
    ParserManager pm = new ParserManager(logFile,logFormat);
    LogObject log = pm.logParser();
    JScrollPane sPane = new JScrollPane(((LogFormat)log).getLogTable());
    
    panel.add(sPane,BorderLayout.NORTH);
    panel.setVisible(true);
    panel.setSize(400,300);
    openTabs.put(panel,log);
    mainArea.addTab(logFile,panel);
  }
 
  private  void prepareWindows(){

    mainArea.setBounds(0,20,400,300);
    mainArea.setSize(400,300);
    mainWindow.add(mainArea,BorderLayout.CENTER);
    mainWindow.add(mainMenu,BorderLayout.NORTH);
    mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setVisible(true);
  }
  public void actionPerformed(ActionEvent e){
    // kind of a lame workaround, should probably fix this.
    if(e.getSource() == file){
      // open a 
    }
  }
  public void addComponents(){
    JMenu menu = new JMenu("File");
    file.addActionListener(this);
    
    menu.add(file);
    mainMenu.add(menu);
  }
  public logv(){
    addComponents();
    prepareWindows();
  }
  public static void main(String[] args){
    logv main = new logv();
    
  }
}