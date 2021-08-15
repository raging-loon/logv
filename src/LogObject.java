package src;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.util.List;
public class LogObject{
  protected String logFile;
  public String getLogFile() { return this.logFile; }
  protected String logFormat;
  public String getLogFormat() { return this.logFormat;}
  public void Parser(){ };
  public void logPrint(){ }
  public JTable getLogTable(){  return null;  }
  public String[][] getInfoTable(int row, StatusObject s){  return null;  }
  public String[][] getTableData(){  return null; }
  public String[] getTableHeaders(){  return null;  }
  public List<String> getIpAddresses(){ return null;  }
  public int getLogSize(){ return 0; }
  public JPanel getLogPanel(JFrame frame){ return null; }
  public void commandParser(ShellManager shm, String command){ }
}
