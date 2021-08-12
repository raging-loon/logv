package src;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.util.List;
public interface LogFormat{
  // Parser() actually parses the file
  // can be called directly or through run via start by Thread
  public void Parser();
  // print the logs in the terminal/cli 
  public void logPrint();
  // get the main table to display in a JTabbedPane
  public JTable getLogTable();
  // get the information table, for the Apache2Log, this contains
  // things like Status Code translation for each cell 
  public String[][] getInfoTable(int row, StatusObject s);
  // Get the actual table, only used by getLogTable
  public String[][] getTableData();
  // get the headers
  public String[] getTableHeaders();
  // get ALL ip addresses from a log, some logs like the Mysql logs will return null
  public List<String> getIpAddresses();
  // get the log size for for loops
  public int getLogSize();
  // public boolean parserSuccess();
  public JPanel getLogPanel(JFrame frame);
}
