package src.misc;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

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
        String date = "";
        String pid = "";
        String command = "";
        String user ="";
        String client = "";
        String msg="";
        for(int i = 0; i < arr.length; i++){
          
          // get the date
          if(arr[i].matches("(Mon|Tue|Wed|Thu|Fri|Sat|Sun)")){
            date = arr[i] + " ";
            for(int j = i;  j < 10; j++){
              if(arr[i + 1].equals("[pid")){
                date += " " + arr[i];
                break;
              }
              date += arr[++i] + " ";

            }
          }
          
          // get the process id and strip it so it looks lke 4001 and not [pid 4001]
          else if(arr[i].equals("[pid")){
            pid = arr[++i].substring(0, arr[i].indexOf("]"));
          }
          
          // get CONNECT if CONNECT is by itself
          else if(arr[i].equals("CONNECT:")){
            command = arr[i].substring(0, arr[i].indexOf(":"));
          }
          
          // get the user if the user is all in one
          else if(arr[i].matches("(\\[.*\\])") && !(pid.equals(""))){
            user = arr[i];
          }

          // if the user is more than one line(e.g [user www]) and pid is 
          // not "" than this index and the next must be the user name
          else if(arr[i].matches("(\\[.*)") && !(pid.equals(""))){
            user = arr[i] + " " + arr[++i];
          }

          // double word command such as FAIL LOGIN
          else if((command.equals("") && !(user.equals(""))) && arr[i].matches("(OK|CONNECT|FAIL|LOGIN)")){
            command = arr[i] + " " +  arr[++i];
            if(command.endsWith(":"))
              command = command.substring(0, command.indexOf(":"));
          //  if(arr[i+1].matches("(OK|CONNECT|FAIL|LOGIN|DOWNLOAD)")) 
            //  command += " " + arr[++i];
          }
          
          
           // get the IP address
          else if(arr[i].equals("Client")){
            if(arr[i+1].equals("\"::1\"") || arr[i+1].equals("\"::1\",")){
              client = arr[++i].substring(arr[i].indexOf("\"") + 1,arr[i].lastIndexOf("\""));
              continue;
            }
            String[] temp = arr[++i].split(":");
            for(String x: temp){
              if(x.endsWith("\"") || x.endsWith("\",")){
                client = x.substring(0,x.indexOf("\""));
              }
            }
          }
          // everything else is the message
          else if(!(client.equals("") && date.equals("") && command.equals(""))){
            for(int j = i; j < arr.length; j++,i++){
              msg += " " + arr[i];
            }
          }
          else {
            // what could go here?
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
      JOptionPane fail = new JOptionPane();
      String message = "File " + logFile + " doesn't exist";
      fail.showMessageDialog(StatusObject.mWindow, message, "Failure", JOptionPane.ERROR_MESSAGE);
    } catch(java.nio.file.AccessDeniedException e){
      JOptionPane fail = new JOptionPane();
      String message = "Access to file: " + logFile + " was denied";
      fail.showMessageDialog(StatusObject.mWindow, message,"Failure", JOptionPane.ERROR_MESSAGE);
    }catch(Exception e){
      JOptionPane fail = new JOptionPane();
      String message = e.getMessage();
      fail.showMessageDialog(StatusObject.mWindow, message, "Parsing Failure", JOptionPane.ERROR_MESSAGE);
    }
  }


  public List<String> getIpAddresses(){
    return this.Client;
  }
  public int getLogSize(){
    return this.Client.size();
  }


  public JTable getLogTable(){
    if(this.Client.size() == 0){
      this.Parser();
    }
    JTable table = new JTable(this.getTableData(),this.getTableHeaders()){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };
    table.setBounds(0,20,1000,600);
		table.setSize(1000,600);
    return table;
  }
  public String[][] getInfoTable(int row, StatusObject s){
    return null;
  }
 
  public String[][] getTableData(){
    int max = this.Client.size();
    String[][] table = new String[max][];
    for(int i = 0; i < max; i++){
      // {"Time","Ip Address","User","Command","PID","Message"};
      String[] tableItem = {
        LogDate.get(i),
        Client.get(i),
        User.get(i),
        Command.get(i),
        PID.get(i),
        Message.get(i)
      };
      table[i] = tableItem;
    }
    
    return table;
  }
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

  public String[] getTableHeaders(){
    return new String[]{"Time","Ip Address","User","Command","PID","Message"};
  }

  public JPanel getLogPanel(JFrame frame){
    return null;
  }
}
