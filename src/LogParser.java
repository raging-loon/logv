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
  
  public Apache2Log Apache2LogParser(){
    Apache2Log logResults = new Apache2Log();
    try{
      List<String> allLines = Files.readAllLines(Paths.get(ParserLogFile));
    
      // int passes = 0;        

      for(String line: allLines){
        // passes++;
        // System.out.println(passes);
        String arr[] = line.split(" ");
        String HTTPRequest = new String("");
        String time = new String("");
        String HTTPUserAgent = new String("");
        String HTTPIpAddr = new String("");
        String HTTPstatusCode = new String("");
        String HTTPlen = new String("");
        String Referer = new String("");
        for(int i = 0; i < arr.length; i++){

          // get the actual request
          // vv better looking way of doing this?
          if(arr[i].equals("\"GET")  || arr[i].equals("\"POST")   || arr[i].equals("\"OPTIONS") || arr[i].equals("\"PUT")
          || arr[i].equals("\"HEAD") || arr[i].equals("\"DELETE") || arr[i].equals("\"TRACE")   || arr[i].equals("\"CONNECT")
          || arr[i].equals("27;")){
            // System.out.println("REQ");
            HTTPRequest += arr[i];
            HTTPRequest += " ";
            i++;
            for(;;i++){
              if(arr[i].endsWith("\"")){
                HTTPRequest += arr[i];
                HTTPstatusCode = arr[++i];
                HTTPlen = arr[++i];
                break; 
              } else {
                HTTPRequest += arr[i];
                HTTPRequest += " ";
              }
            }
          }
          // Get user agent
          
          else if(arr[i].matches("(\"Mozilla/\\d.\\d)") || arr[i].matches("(\"Mozilla/\\d.\\d\")")){
            try{
              HTTPUserAgent += arr[i];
              for(int j = 0; j < arr.length; j++){
                HTTPUserAgent += arr[++i] + " ";
              }
            } catch(Exception e){}
          }
          // get the time of the request
          else if(arr[i].matches("(\\[\\d{1,}/\\w\\w\\w/\\d\\d\\d\\d:\\d{1,}:\\d{1,}:\\d{1,})")){
            // System.out.println("TIME");
            time += arr[i] + " ";
            time += arr[++i];
          }

          // get the ip address
          else if(arr[i].matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")){
            HTTPIpAddr = arr[i];
            // System.out.println("IPA");
          }
          // get the referer
          else if(arr[i].matches("(\"http:\\/\\/|https:\\/\\/?(www.)?.*)")){
            Referer = arr[i];
          } 
          else {
            // nothing relavent
          }

        

        }
        logResults.HTTPIpAddresses.add(HTTPIpAddr);
        logResults.HTTPRequestTime.add(time);
        logResults.HTTPRequests.add(HTTPRequest);
        logResults.HTTPResponceLength.add(HTTPlen);
        logResults.HTTPStatusCodes.add(HTTPstatusCode);
        logResults.HTTPUserAgents.add(HTTPUserAgent);
        logResults.HTTPReferer.add(Referer);
      }
      return logResults;
    } //catch(FileNotFoundException e){
    //   System.out.println("File: " + ParserLogFile + ": not found");
    //   System.exit(-1);}
     catch(Exception e){
      System.out.println("Sorry, something went wrong"); // fix
      e.printStackTrace();
      System.exit(-1);
    } 
    return logResults;

  }



  public MySqlErrorLog MySQLErrorLogParser(){
    MySqlErrorLog logResults = new MySqlErrorLog();
    try{
      /*    MySQL error.log format
          time thread [label] [err_code] [subsystem] msg
      */
      List<String> allLines = Files.readAllLines(Paths.get(ParserLogFile));
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
        logResults.ErrorTime.add(logTime);
        logResults.ErrorThread.add(logThreadNo);
        logResults.ErrorLabel.add(logLabel);
        logResults.ErrorCode.add(logErrCode);
        logResults.SubSystem.add(logSubSystem);
        logResults.ErrorMessage.add(logMessage);
      }
      return logResults;

    } catch(FileNotFoundException e){
      System.out.println("Error: log file not found");
      System.exit(-1);
    } catch(Exception e){
      e.printStackTrace();
      System.exit(-1);
    }
    return logResults;
  }



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
