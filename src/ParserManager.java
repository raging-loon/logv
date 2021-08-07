package src;
import java.io.File;

import src.http.*;
import src.sql.*;
import src.misc.*;
public class ParserManager {
  String logFile;
  String logFormat;
  public ParserManager(String logFile,String logFormat){
    this.logFile = logFile;
    this.logFormat = logFormat;
    
  }
  public boolean fileExistsChecker(){
    File file = new File(logFile);
    if(file.exists() && !(file.isDirectory())){
      return true;
    }
    return false;
  }
  public LogObject logParser(){
    if(!(fileExistsChecker())){
      // throw a popup maybe?
      return null;
    }
    switch(logFormat){
      case "apache2":
      case "nginx":
        Apache2Log log = new Apache2Log(logFile);
        log.start();
        return log;
      case "mysql_err":
        MySqlErrorLog log2 = new MySqlErrorLog(logFile);
        log2.start();
        return log2;
      case "vsftpd":
        Vsftpd log3 = new Vsftpd(logFile);
        log3.start();
        return log3;
      default:
        // possibly a raw log parser for unknown log types
        return null;
    }
  }
}
