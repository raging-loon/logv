import src.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

public class logv implements ActionListener, Runnable{
  private static JFrame mainWindow = new JFrame("LogViewer");
  private static HashMap<JComponent, LogObject> openTabs
                                                = new HashMap<>();

  private static JMenuBar mainMenu = new JMenuBar();
  private static JTabbedPane mainArea = new JTabbedPane();
  private JMenuItem file = new JMenuItem("Open"); 
  private static ParserManager parserManager = new ParserManager();
  private String GlobalObjectStatus = "";
  private List<String> GlobalObjectArguments = new ArrayList<>();
  synchronized private void addNewTab(String logFile, String logFormat){
    System.out.println("adding new tab");
    JComponent panel = new JPanel();
    System.out.println("Made JPANEL");
    LogObject log = parserManager.logParser(logFile, logFormat);
    if(log == null){
      System.out.println("NULL");
      return;
      // don't add a new tab, file doesn't exist.
    }
    JScrollPane sPane = new JScrollPane(((LogFormat)log).getLogTable());
    
    panel.add(sPane,BorderLayout.NORTH);
    panel.setVisible(true);
    panel.setSize(400,300);
    openTabs.put(panel,log);
    mainArea.addTab(logFile,panel);
    System.out.println(logFile);
    System.out.println(log.getLogFile());
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
      JOptionPane logInfo = new JOptionPane("Log Format Chooser");
      JTextField optionField = new JTextField();
      optionField.setText("Enter File Format");
      logInfo.showInputDialog(mainWindow,optionField);
      JFileChooser jfc = new JFileChooser();
      int status = jfc.showSaveDialog(null);
      String fileField;
      if(status == JFileChooser.APPROVE_OPTION)  fileField = jfc.getSelectedFile().getAbsolutePath();
      else  fileField = jfc.getSelectedFile().getAbsolutePath();
      GlobalObjectStatus = "newtab";
      GlobalObjectArguments.clear();
      System.out.println(fileField);
      GlobalObjectArguments.add(fileField);
      GlobalObjectArguments.add(optionField.getText().toString());

      Thread t = new Thread(this);
      t.start();

    }
  }
 
  public void run(){
    System.out.println("New thread");
    if(GlobalObjectStatus.equals("newtab")){
      if(GlobalObjectArguments.isEmpty()){

      }
      this.addNewTab(GlobalObjectArguments.get(0),GlobalObjectArguments.get(1));
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