package src.sql;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import src.*;

import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.ArrayList;
public class MySqlErrorLog extends LogObject implements LogFormat,Runnable{
  /*    MySQL error.log format
    time thread [label] [err_code] [subsystem] msg
  */
  public final String[] TableHeaders = {"Time","Thread","Label","Code","Subsystem","Message"};
  public List<String> ErrorTime = new ArrayList<String>();
  public List<String> ErrorThread = new ArrayList<String>();
  public List<String> ErrorLabel = new ArrayList<String>();
  public List<String> ErrorCode = new ArrayList<String>();
  public List<String> SubSystem = new ArrayList<String>();
  public List<String> ErrorMessage = new ArrayList<String>();
  
  public MySqlErrorLog(String logFile){ this.logFile = logFile; }
 
  public void Parser(){
    try{
      /*    MySQL error.log format
          time thread [label] [err_code] [subsystem] msg
      */
      List<String> allLines = Files.readAllLines(Paths.get(this.logFile));
      for(String line: allLines){
        // might have to make this a little more complicated in the future
        String arr[] = line.split(" ");
        String logTime = arr[0];
        String logThreadNo = arr[1];
        String logLabel = arr[2];
        String logErrCode = arr[3];
        String logSubSystem = arr[4];
        String logMessage = new String("");
        for(int i = 5; i <  arr.length; i++){
          logMessage += arr[i] + " ";
        }
        this.ErrorTime.add(logTime);
        this.ErrorThread.add(logThreadNo);
        this.ErrorLabel.add(logLabel);
        this.ErrorCode.add(logErrCode);
        this.SubSystem.add(logSubSystem);
        this.ErrorMessage.add(logMessage);
      }

    } catch(FileNotFoundException e){
      System.out.println("Error: log file not found");
      System.exit(-1);
    } catch(Exception e){
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public void logPrint(){
    for(int i = 0; i < ErrorCode.size(); i++){
      System.out.println("----------------------");
      System.out.println("Time: " + ErrorTime.get(i));
      System.out.println("Thread: " + ErrorThread.get(i));
      System.out.println("Label: " + ErrorLabel.get(i));
      System.out.println("Code: " + ErrorCode.get(i));
      System.out.println("Subsystem: " + SubSystem.get(i));
      System.out.println("Message: " + ErrorMessage.get(i));

    }
  }
  public String[][] getTableData(){
    String[][] tempTable = new String[ErrorCode.size()][];
    for(int i = 0; i < ErrorCode.size(); i++){
      //  public final String[] TableHeaders = {"Time","Thread","Label","Code","Subsystem","Message"};
      String[] temp = { ErrorTime.get(i),
                        ErrorThread.get(i),
                        ErrorLabel.get(i),
                        ErrorCode.get(i),
                        SubSystem.get(i),
                        ErrorMessage.get(i)
                      };                    
      tempTable[i] = temp;
    }                
    return tempTable;
  }
  public String[] getTableHeaders(){
    return this.TableHeaders;
  }
  public String[][] getInfoTable(int row, StatusObject s){
    return null;
  }

  public JTable getLogTable(){
    if(ErrorTime.size() == 0){
      this.Parser(); // thread later
    }
    JTable table = new JTable(this.getTableData(),this.TableHeaders){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };
    return table;

  }
  public List<String> getIpAddresses(){return null;}
  public int getLogSize(){
    return this.ErrorCode.size();
  }
  public void run(){
    this.Parser();
  }
  public void start(){
    Thread t = new Thread(this);
    t.start();
  }

  public JPanel getLogPanel(JFrame frame){
    return null;
  }
  public void commandParser(ShellManager shm,String command){
    
  }
}
