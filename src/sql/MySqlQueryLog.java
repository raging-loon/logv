package src.sql;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

import src.LogFormat;
import src.LogObject;

public class MySqlQueryLog extends LogObject implements LogFormat{
  public String sqlVersion;
  public List<String> QTime = new ArrayList<String>();
  public List<Integer> QId =  new ArrayList<Integer>();
  public List<String> QCommand = new ArrayList<String>();
  public List<String> QArgs = new ArrayList<String>();

  public MySqlQueryLog(String filename){
    this.logFile = filename;
  }




  public void Parser(){
    try{
      List<String> allLines = Files.readAllLines(Paths.get(logFile));
      // skip to three because the first three lines don't contain any relavent ifo
      for(int i = 0; i < allLines.size(); i++){
        String arr[] = allLines.get(i).split(" ");
        if(i == 0){
          // skip to three because the first three lines 
          // don't contain any relavent info other than the version
          this.sqlVersion = arr[2];
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


    } catch(java.io.FileNotFoundException e){
      System.out.println("File: " + this.logFile + ": file not found");
      System.exit(-1);
    } catch(java.io.IOException e){
      System.out.println("File: " + this.logFile + ": unrecognized encoding");
    } catch(Exception e){
      System.out.println("Something went wrong...");
      e.printStackTrace();
      System.exit(-1);
    }

  }
}
