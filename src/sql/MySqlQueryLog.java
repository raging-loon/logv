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
  public List<String> QId =  new ArrayList<String>();
  public List<String> QCommand = new ArrayList<String>();
  public List<String> QArgs = new ArrayList<String>();

  public MySqlQueryLog(String filename){
    this.logFile = filename;
  }




  public void Parser(){
    try{
      List<String> allLines = Files.readAllLines(Paths.get(logFile));
      for(int i = 0; i < allLines.size();i++){
        if(allLines.get(i).matches(".*(Version\\:).*")){
          i+=2;
          
        }
        int passes = 0;
        int size = allLines.get(i).split(" ").length;
        String time = "";
        String id = "";
        String argument = "";
        String message = "";
        for( String j : allLines.get(i).split(" ")){
        
          // look for time
          // 2021-07-25T15:27:32.081932Z
          if(j.matches("\\d{4}-\\d\\d-.*\\:\\d{0,}..*")){
            time = j;
            passes++;
          } 
          else if((j.strip()).matches("\\d{0,}")){
            id = j;
            passes++;
          }

          else if(j.matches("(Quit|Connect|Query).*")){
            // String command = "";
            if(j.matches("(Quit).*")){
              argument = j.substring(4).strip();
              continue;
            } else if(j.matches("(Connect).*")){
              argument = j.substring(7).strip();

            } else if(j.matches("(Query).*")){
              argument = j.substring(5).strip();
            }
            
            if(!time.equals("")){
              for(int k = passes; k < size; k++){
                message += j;
              }
              for(int k = 0; k < 5; k++){
                if(!allLines.get(i + k).split(" ")[0].matches("\\d{4}-\\d\\d-.*\\:\\d{0,}..*")){
                  message += allLines.get(++i);
                } else {
                  break;
                }
              }
            }
          }

        }
        System.out.println("Time: " + time);
        System.out.println("Argument: " + argument);
        System.out.println("ID: " + id);
        System.out.println("Message: " + message);
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
