import src.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.*;


public class logv implements ActionListener, Runnable{
  private static JFrame mainWindow = new JFrame("LogViewer");
  private static HashMap<JComponent, LogObject> openTabs
                                                = new HashMap<>();

  private static JMenuBar mainMenu = new JMenuBar();
  private static JTabbedPane mainArea = new JTabbedPane();
  private JMenuItem newApache2 = new JMenuItem("New Apache2 Log");
  private JMenuItem newMysqlErr = new JMenuItem("New MySql Error ");
  private JMenuItem newVsftpd = new JMenuItem("New VSFTPD Log"); 
  private static ParserManager parserManager = new ParserManager();
  private String GlobalObjectStatus = "";
  private List<Object> GlobalObjectArguments = new ArrayList<>();
  synchronized private void addNewTab(File logFile, String logFormat){
    JComponent panel = new JPanel(new BorderLayout());

    LogObject log = parserManager.logParser(logFile, new String(logFormat));
    // try{
    //    wait();
    // } catch(Exception e){

    // } 
    if(log.equals(null)){
      System.out.println("NULL");
      return;
      // don't add a new tab, file doesn't exist.
    }
    JTable table = ((LogFormat)log).getLogTable();
    table.setSize(1000,300);
    JScrollPane sPane = new JScrollPane(table);
    
    panel.add(sPane,BorderLayout.NORTH);
    panel.setSize(1000,300);
    panel.setVisible(true);
    openTabs.put(panel,log);
    mainArea.addTab(logFile.getName(),panel);
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
    if(e.getSource() instanceof JMenuItem){
      String logFormat = "";
      File logFile;
      if(e.getSource() == newApache2){
        logFormat = "apache2";

      } else if( e.getSource() == newMysqlErr){
        logFormat = "mysql_err";
      } else if(e.getSource() == newVsftpd){
        logFormat = "vsftpd";
      } else {
        // not one of the accepted JMenuItems, break out of this
        // should fix this later.
        return;
      }
      JFileChooser jfc = new JFileChooser();
      jfc.showOpenDialog(null);
      logFile = jfc.getSelectedFile();
      GlobalObjectStatus = "newtab";
      GlobalObjectArguments.clear();
      GlobalObjectArguments.add(logFile);
      GlobalObjectArguments.add(logFormat);
      Thread t  = new Thread(this);
      t.start();
    }
  }
 
  public void run(){
    System.out.println("New thread");
    if(GlobalObjectStatus.equals("newtab")){
      if(GlobalObjectArguments.isEmpty()){
      }

      this.addNewTab((File)GlobalObjectArguments.get(0),GlobalObjectArguments.get(1).toString());
      GlobalObjectArguments.clear();
      GlobalObjectStatus = "";
    }
  }
  public void addComponents(){
    JMenu menu = new JMenu("File");
    newApache2.addActionListener(this);
    menu.add(newApache2);
    newMysqlErr.addActionListener(this);
    menu.add(newMysqlErr);
    newVsftpd.addActionListener(this);
    menu.add(newVsftpd);
    mainMenu.add(menu);
  }
  public logv(){
    addComponents();
    prepareWindows();
  }
  public static void main(String[] args){
    if(args.length != 0){
      logvcli l = new logvcli();
      l.main(args);
      System.exit(0);
    }
    logv main = new logv();
     
  }
}