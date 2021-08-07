import src.*;
import src.http.*;
import src.misc.*;
import src.sql.*;
import src.utils.*;

import java.util.HashMap;
import javax.swing.*;
public class logv{
  private static JFrame mainWindow = new JFrame("LogViewer");
  private static HashMap<JComponent, String> openTabs
                                                = new HashMap<>();

  private static JMenuBar mainMenu = new JMenuBar();
  private static JTabbedPane mainArea = new JTabbedPane();
  private void addMenuItems(){
    // menu is staring to look like a fake word...
    JMenu menu = new JMenu("File");
    JMenuItem file = new JMenuItem("Open");
    menu.add(file);
    mainMenu.add(menu);
  }
  private void addNewTab(String logFile, String logFormat){
    JComponent panel = new JPanel();
    

  }
  private void prepareWindows(){

  }
  public static void main(String[] args){

  }
}