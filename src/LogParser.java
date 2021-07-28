package src;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import src.http.*;
import src.sql.MySqlErrorLog;
import src.sql.MySqlQueryLog;

/**
 * <p> The purpose of all of the parsing being done in 
 * file(for the most part) is so that It is all centralized.
 * This will make more sense once its <i>guified</i></p>
 */

public class LogParser {
  private String ParserType;
  private String ParserLogFile;
  public LogParser(String LogFormat, String LogFile){
    switch(LogFormat){
      case "apache2":
      case "nginx":
        ParserType = LogFormat;
        break;
      case "mysql_err":
        ParserType = LogFormat;
        break;
      case "mysql_query":
        ParserType = LogFormat;
        break;
      default:
        System.out.println("Invalid log format: " + LogFormat);
        System.exit(-1);
    }
    ParserLogFile = LogFile;
  }


}
