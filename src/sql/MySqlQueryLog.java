package src.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.nio.file.*;

import src.LogFormat;
import src.LogObject;

public class MySqlQueryLog extends LogObject {
  // public String sqlVersion;
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

        String lineArr[] = allLines.get(i).split(" ");
        for(int j = 0 ; j < lineArr.length; j++){
        
          // look for time
          // 2021-07-25T15:27:32.081932Z
          if(lineArr[j].matches("\\d{4}-\\d\\d-.*\\:\\d{0,}..*")){
            time = lineArr[j];
            passes++;
          } 
          else if((lineArr[j].strip()).matches("\\d{0,}")){
            id = lineArr[j];
            passes++;
          }

          else if(lineArr[j].matches("(Quit|Connect|Query).*")){
            // String command = "";
              if(lineArr[j].matches("(Quit).*")){
                argument = lineArr[j].substring(0,4);
                // message = lineArr[j].substring(4);
                continue;
              } else if(lineArr[j].matches("(Connect).*")){
                argument = lineArr[j].substring(0,7); 
                if(j != lineArr.length) message = lineArr[j].substring(7);

              } else if(lineArr[j].matches("(Query).*")){
                argument = lineArr[j].substring(0,5);
                message = lineArr[j].substring(5);
              }
          
        } 
          else if(!argument.equals("") && !time.equals("") && !id.equals("")){
            message += " " + lineArr[j];
            try{
              for(int k = 1; k <= 4; k++){
                if(!allLines.get(i+k).split(" ")[0].matches("\\d{4}-\\d\\d-.*\\:\\d{0,}..*")){
                  message += " " + allLines.get(i+k).stripLeading().stripTrailing();

                } else {
                  break;
                }
              }
            } catch(Exception e){
              continue;
              // This just means that we are so close to the end of the file
              // that it will throw an error because of k
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
