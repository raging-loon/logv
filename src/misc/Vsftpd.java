package src.misc;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import src.*;

public class Vsftpd extends LogObject implements Runnable, LogFormat{
  public Vsftpd(String logFile){this.logFile = logFile; this.logFormat = "vsftpd";}
  public List<String> LogDate     = new ArrayList<>();
  public List<String> PID         = new ArrayList<>();
  public List<String> Command     = new ArrayList<>();
  public List<String> User        = new ArrayList<>();
  public List<String> Client      = new ArrayList<>();
  public List<String> Message     = new ArrayList<>();
  public void Parser(){
    try{
      List<String> allLines = Files.readAllLines(Paths.get(this.logFile));
      for(String line: allLines){
        String arr[] = line.split(" ");
        String date = "",pid = "", command = "", user ="", client = "",msg="";
        for(int i = 0; i < arr.length; i++){
          // get the data
          if(arr[i].matches("(Mon|Tue|Wed|Thu|Fri|Sat|Sun)")){
            date = arr[i];
            for(int j = i; j < 10; j++){
              if(arr[i + 1].equals("[pid")){
                date += " " + arr[++i];
                break;
              }
              date += arr[++i] + " ";

            }
          }
          // get the process id
          else if(arr[i].equals("[pid")){
            pid = arr[i] + " " + arr[++i];
          }
          else if(arr[i].equals("CONNECT")){
            command = arr[i];
          }
          else if(arr[i].matches("(\\[.*\\])")){
            user = arr[i];
          }
          else if(arr[i].matches("(\\[.*)") && pid != null){
            user = arr[i] + " " + arr[++i];
          }
          
          else if((command == null && user != null) && arr[i].matches("(OK|CONNECT|FAIL|LOGIN)")){
            command = arr[i];
            if(arr[i+1].matches("(OK|CONNECT|FAIL|LOGIN|DOWNLOAD)")) 
              command += " " + arr[++i];
          }
          else if(arr[i].equals("Client")){
            String[] temp = arr[++i].split(":");
            for(String x: temp){
              if(x.endsWith("\"") || x.endsWith("\",")){
                client = x.substring(0,x.indexOf("\""));
              }
            }
          }
          else if(client != null && command != null && date != null){
            for(int j = i; j < arr.length; j++,i++){
              msg += arr[i];
            }
          }
          else {
          }

        }
        this.Client.add(client);
        this.Command.add(command);
        this.LogDate.add(date);
        this.Message.add(msg);
        this.PID.add(pid);
        this.User.add(user);
      }

    } catch(FileNotFoundException e){
      System.out.println("File: " + this.logFile + ": doesn't exist");
      System.exit(-1);
    } catch(Exception e){
      e.printStackTrace();
      System.exit(-1);
    }
  }






  public JTable getLogTable(){return null;}
  public String[][] getInfoTable(int row, StatusObject s){return null;}
  public String[][] getTableData(){return null;}
  public void logPrint(){
    for(int i = 0; i < this.Client.size(); i++){
      System.out.println("-------------");
      System.out.println("TIME: " + LogDate.get(i));
      System.out.println("PID: " + PID.get(i));
      System.out.println("COMMAND: " + Command.get(i));
      System.out.println("User: " + User.get(i));
      System.out.println("CLIENT: " + Client.get(i));
      System.out.println("Message: " + Message.get(i));
    }
  }

  public void run(){
    this.Parser();
  }
  public void start(){
    Thread t = new Thread(this);
    t.start();
  }


}