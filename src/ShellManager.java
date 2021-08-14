package src;
import java.util.*;
import java.io.File;
import src.*;

public class ShellManager {
  ParserManager pm = new ParserManager();
  List<String> logFormats = new ArrayList<>();
  List<LogObject> openLogs = new ArrayList<>();
  List<String> logNames = new ArrayList<>();
  public void ParseCommand(String command){
    if(command.equals("")){
      return;
    }
    if(command.matches("(newlog).*")){
      String[] cmd_arr = command.split(" ");
      if(cmd_arr.length != 3){
        System.out.println("\033[31mError in syntax: " + command + "\033[0m");
        System.out.println("\033[31mCorrect syntax: newlog -<logformat> <file>\033[0m");
        return;
      }
      else {
        String logFormat = cmd_arr[1];
        String logFile = cmd_arr[2];
        LogObject log = pm.logParser(new File(logFile), logFormat);
        logFormats.add(logFormat.substring(logFormat.indexOf("-")));
        openLogs.add(log);
        logNames.add("log" + openLogs.size() + logFormat);
      }
    } 
    else if(command.equals("show logs")){
      String banner = "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+";
      System.out.println(banner);
      for(int i = 0; i < logFormats.size(); i++){
        String entry = "+  " + logNames.get(i) + "| " + logFormats.get(i);
        System.out.println(entry);
        for(int j = 0; j <= banner.length() - entry.length(); j++){
          System.out.printf(" ");
        }
        System.out.println("+");
      }
      System.out.println(banner);
    
    } else if(command.equals("exit")){
      return;
    }
    else {
      System.out.println("\033[31mUnkown command: \033[0m: " + command);
    }

  }

}
