package src;
import java.io.File;

import src.http.*;
import src.sql.*;
import src.misc.*;
public class ParserManager {
  
  public boolean fileExistsChecker(String logFile){
    File file = new File(logFile);
    System.out.println(file.getAbsolutePath());
    File realFile = new File(file.getAbsolutePath());
    if(!realFile.exists()){
      return true;
    }
    return false;
  }
  public LogObject logParser(String logFile,String logFormat){
    if(fileExistsChecker(logFile) == true){
      System.out.println("File doesn't exist");
      return null;
    }
    if(logFormat.equals("apache2") || logFormat.equals("nginx")){
      System.out.println("GOT WEB LOG");
      Apache2Log log = new Apache2Log(logFile);
      log.start();
      return log;
    } 
    if(logFormat.equals("mysql_err")){
      MySqlErrorLog log = new MySqlErrorLog(logFile);
      log.start();
      return log;
    }
    if(logFormat.equals("vsftpd")){
      Vsftpd log = new Vsftpd(logFile);
      log.start();
      return log;
    }
    return null;
  }
}
