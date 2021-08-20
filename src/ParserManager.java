package src;
import java.io.File;

import src.http.*;
import src.sql.*;
import src.misc.*;
public class ParserManager{
  String logFile;
  String logFormat;
  // setters, getters
  public void setLogFile(String file){ this.logFile = file; }
  public void setLogFormat(String format){this.logFormat = format;}
  public String getLogFormat(){return this.logFormat;}
  public String getLogFile(){return this.logFile; }
  public void clearLogInfo(){
    this.logFile = "";
    this.logFormat = "";
  }
  
  public boolean fileExistsChecker(String logFile){
    File file = new File(logFile);
    // System.out.println(file.getAbsolutePath());
    File realFile = new File(file.getAbsolutePath());
    if(!realFile.exists()){
      return true;
    }
    return false;
  }

  public LogObject logParser(File logFile,String logFormat){
    if(fileExistsChecker(logFile.getAbsolutePath()) == true){
      System.out.println("File doesn't exist");
      return null;
    }


    if(logFormat.equals("apache2") || logFormat.equals("nginx")){
      // System.out.println("GOT WEB LOG");
      Apache2Log log = new Apache2Log(logFile.getAbsolutePath());
      log.Parser();
      // notifyAll();
      return log;
    } 
    if(logFormat.equals("mysql_err")){
      MySqlErrorLog log = new MySqlErrorLog(logFile.getAbsolutePath());
      log.Parser();
      return log;
    }
    if(logFormat.equals("vsftpd")){
      Vsftpd log = new Vsftpd(logFile.getAbsolutePath());
      log.Parser();
      return log;
    }
    if(logFormat.equals("mysql_qry")){
      MySqlQueryLog log = new MySqlQueryLog(logFile.getAbsolutePath());
      log.Parser();
      return log;
    }
    
    return null;
  }


}
