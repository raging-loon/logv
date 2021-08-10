package src;

import src.http.*;
import src.misc.*;
import src.misc.*;
import src.sql.*;
import src.utils.*;
import java.io.File;
import java.util.*;
public class CLIManager {
  public String logFormatFlag = "";
  public String logFileFlag = "";
  public int ipCountsFlag = -1;
  public String specIpCountsFlag = "";
  public int doList = -1;
  public int infoFlag = -1;
  // setters 
  public HashMap<String,String> logInfo = new HashMap<>();
  public List<LogObject> logs = new ArrayList<>();
  public void setLogFormatFlag(String str){ this.logFormatFlag = str;}
  public void setLogFileFlag(String str){ this.logFileFlag = str;}
  public void setIpCountsFlag(){this.ipCountsFlag = 1;}
  public void setSpecIpCountsFlag(String str){this.specIpCountsFlag  = str;}
  public void setDoList(){ this.doList = 1; }
  public void Start(){
    if(logInfo.size() == 0){
      if(doList > -1){
        OptionPrinter.SupportedLogFormatPrinter(StatusObject.currentVersion);
        System.exit(0);
      } else {
        System.out.println("Logs and Formats are required.");
        System.exit(-1);
      }
    } 
    else{
      ParserManager pm = new ParserManager();
      for( String logFormat : logInfo.keySet() ){
        logs.add(pm.logParser(new File(logInfo.get(logFormat)), logFormat));
      }
      for(int i = 0; i < logs.size(); i++){
        if(logs.get(i) == null){
          System.out.println("Invalid log file");
          System.exit(-1);
        }
      }
    }
    if(infoFlag > -1){
      for(int i = 0; i < logs.size(); i++){
        System.out.println("------------------------");
        System.out.println("File Name: " + logs.get(i).getLogFile());
        System.out.println("Format: " + logs.get(i).getLogFormat());
        System.out.println("Entries: " + ((LogFormat)logs.get(i)).getLogSize());
        if(logs.get(i).getLogFormat().matches("apache2|nginx")){
          Apache2Log log = (Apache2Log)logs.get(i);
          System.out.println("Nmap Scan Detected? " + log.nmapScanSearch(true));
          System.out.println("Path Traversal Detected? " + log.PathTraversalDetector(true));
          System.out.println("XSS Detected? " + log.XssDetector(true));
        } 
      }
      System.exit(0);
    }
    else if(ipCountsFlag > -1){
      for(int i = 0; i < logs.size(); i++){
        if(logs.get(i).getLogFormat().matches("apache2|nginx")){
          Apache2Log log = (Apache2Log)logs.get(i);
          MiscUtils.printIpCountResults(log.getIpCounts());
        } else {
          System.out.println("Log Format" + logs.get(i).getLogFormat() + ": doesn't support IP indexing");
        }
      }
    }

  }
}
