package src;

import javax.swing.JTable;
import java.util.List;
public interface LogFormat {
  public void Parser();
  public void logPrint();
  public JTable getLogTable();
  public String[][] getInfoTable(int row, StatusObject s);
  public String[][] getTableData();
  public String[] getTableHeaders();
  public List<String> getIpAddresses();
  public int getLogSize();
  // public boolean parserSuccess();
}
