package src.sql;
import java.util.List;

import src.*;

import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.ArrayList;
public class MySqlErrorLog extends LogObject implements LogFormat{
  /*    MySQL error.log format
    time thread [label] [err_code] [subsystem] msg
  */
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
}
