package src;
import java.util.*;
import java.io.File;
import src.*;

public class ShellManager {
  ParserManager pm = new ParserManager();
  List<String> logFormats = new ArrayList<>();
  List<LogObject> openLogs = new ArrayList<>();
  List<String> logNames = new ArrayList<>();
  public String currentLogFile = "";
  public String currentLogFormat = "";
  public LogObject currentLog;
  public void ParseCommand(String command){
    if(command.equals("")){
      return;
    }
    if(command.matches("(newlog).*")){
      String[] cmd_arr = command.split(" ");
      if(cmd_arr.length != 3){
        System.out.println("\033[31mError in syntax: " + command + "\033[0m");
        System.out.println("\033[31mCorrect syntax: newlog <logformat> <file>\033[0m");
        return;
      }
      else {
        String logFormat = cmd_arr[1];
        String logFile = cmd_arr[2];
        LogObject log = pm.logParser(new File(logFile), logFormat);
        if(log == null){
          return;
        }
        logFormats.add(logFormat);
        openLogs.add(log);
        logNames.add("log" + openLogs.size());
      }
    } 
    else if(command.equals("show logs")){
      String banner = "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+";
      System.out.println(banner);
      for(int i = 0; i < logFormats.size(); i++){
        String entry = "+  " + logNames.get(i) + "  |  " + logFormats.get(i) + " +";
        System.out.println(entry);
        
      }

      System.out.println(banner);
    
    } else if(command.matches("exit|e|q|quit")){
      System.exit(0);
      return;
    } else if(command.equals("clear")){
      System.out.println("\033[H\033[2J");
    } else if(command.matches(".*use.*")){
      String[] cmd = command.split(" ");
      // use xyzLogFile
      if(cmd.length != 2){
        System.out.println("\033[31mInvalid syntax: \033[0m" + command);
        System.out.println("\033[31mUsage: use <log>\033[0m");
  
      } else {
        if(logNames.contains(cmd[1])){
          currentLogFile = cmd[1];
          currentLogFormat = logFormats.get(logNames.indexOf(cmd[1]));
          currentLog = openLogs.get(logNames.indexOf(cmd[1]));
        } else {
          System.out.println("\033[31mLog doesn't exist\033[0m");
        }
      }
    } 

    else if(command.equals("back")){
      currentLog = null;
      currentLogFile = "";
      currentLogFormat = "";
    }
    else {
      System.out.println("\033[31mUnkown command: \033[0m " + command);
    }

  }


  public String getCurrentLogFile(){ return this.currentLogFile; }
  public String getLogFormat(){ return this.currentLogFormat; }

}
