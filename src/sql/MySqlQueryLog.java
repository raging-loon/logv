package src.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.nio.file.*;

import src.LogFormat;
import src.LogObject;

public class MySqlQueryLog extends LogObject //implements LogFormat{
  {public String sqlVersion;
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

      ArrayList<String> whitespace = new ArrayList<>();whitespace.add(" ");
        
      for(int i = 3; i < allLines.size();i++){
        List<String> lines = Arrays.stream(allLines.get(i).split(" ")).map(String::trim).collect(Collectors.toList());

        String time = "";
        int id = 0;
        String command = "";
        String message = "";
        for(int j = 0; j < lines.size(); j++){
          time = lines.get(0);
          id = Integer.valueOf(lines.get(1));
          command = lines.get(2);
          message = lines.get(3);
        }

        System.out.println("----------------------");
        System.out.println("TIME: " + time);
        System.out.println("ID: " + id);
        System.out.println("COMMAND: " + command);
        System.out.println("MESSAGE: " + message);
      }



    } catch(java.io.FileNotFoundException e){
      System.out.println("File: " + this.logFile + ": file not found");
      System.exit(-1);
    } catch(java.io.IOException e){
      System.out.println("File: " + this.logFile + ": unrecognized encoding");
      e.printStackTrace();
    } catch(Exception e){
      System.out.println("Something went wrong...");
      e.printStackTrace();
      System.exit(-1);
    }

  }
  public void logPrint(){}
}
