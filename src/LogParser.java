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


  // #   # #   #  ####  ####    #     ####    ####  #   #
  // ## ##  # #  ####  #    #   #    #    #   #   #  # #
  // # # #   #       # #    #   #    #    #   ####    #
  // #   #   #   ####   #### #  ####  #### #  #   #   #
  public MySqlQueryLog mySqlQueryLogParser(){
    MySqlQueryLog logResults = new MySqlQueryLog(this.ParserLogFile);
    try{
      List<String> allLines = Files.readAllLines(Paths.get(logResults.filename));
      // skip to three because the first three lines don't contain any relavent ifo
      for(int i = 0; i < allLines.size(); i++){
        String arr[] = allLines.get(i).split(" ");
        if(i == 0){
          // skip to three because the first three lines 
          // don't contain any relavent info other than the version
          logResults.sqlVersion = arr[2];
          i+=2;
        }
        if(arr == null){
          continue;
        }
        String QryTime;
        int QryID;
        String QryCommand;
        String QryMessage;
        for(int j = 0; j < arr.length; i++){
          
        }

      }
      return logResults;


    } catch(FileNotFoundException e){
      System.out.println("File: " + this.ParserLogFile + ": file not found");
      System.exit(-1);
    } catch(IOException e){
      System.out.println("File: " + this.ParserLogFile + ": unrecognized encoding");
    } catch(Exception e){
      System.out.println("Something went wrong...");
      e.printStackTrace();
      System.exit(-1);
    }
    return logResults;
  }


}
