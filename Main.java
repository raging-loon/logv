import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import src.*;
import src.http.*;
import src.sql.*;
import src.utils.*;
public class Main /*implements ActionListener*/{
  private static JFrame mainWindow = new JFrame();
  private static JMenuBar mainMenu = new JMenuBar();
  private static void addMenuItems(){
    JMenu fileMenu = new JMenu("File");
    // JMenu helpMenu = new JMenu("Help");
    JMenuItem openFileItem = new JMenuItem("Open");
    fileMenu.add(openFileItem);
    mainMenu.add(fileMenu);
  }

  
  public static void main(String argv[]){
    addMenuItems();
    JPanel logPanel = new JPanel(new BorderLayout());
    Apache2Log log = new Apache2Log("tests/logs/apachetest2.log");
    log.Parser();
    JTable mainTable = new JTable(log.getTableData(),log.TableHeaders){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };
    JScrollPane scrollBar = new JScrollPane(mainTable);
    
    mainTable.setBounds(30,40,950,550);
    logPanel.setBounds(30,40,200,300);
    logPanel.setSize(950,550);
    mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setTitle("logv - LogViewser");
    // mainWindow.add(scrollBar);
    logPanel.add(scrollBar,BorderLayout.PAGE_START);
    mainWindow.add(logPanel,BorderLayout.NORTH);
    mainWindow.add(mainMenu,BorderLayout.NORTH);



    mainWindow.setVisible(true);

  }

}
