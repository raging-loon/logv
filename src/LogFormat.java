package src;

import javax.swing.JTable;

public interface LogFormat {
  public void Parser();
  public void logPrint();
  public JTable getLogTable();
  public String[][] getInfoTable(int row, StatusObject s);
  public String[][] getTableData();

}
