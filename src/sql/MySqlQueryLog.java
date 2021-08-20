package src.sql;
import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
// import java.util.regex.*;
import java.util.stream.Collectors;

import java.nio.file.*;

import src.LogFormat;
import src.LogObject;
import src.StatusObject;
import src.utils.AttackIdentifier;

public class MySqlQueryLog extends LogObject implements LogFormat,ActionListener{
  // public String sqlVersion;
  public List<String> QTime = new ArrayList<String>();
  public List<String> QId =  new ArrayList<String>();
  public List<String> QCommand = new ArrayList<String>();
  public List<String> QArgs = new ArrayList<String>();
  
  private JPanel mainFrame = new JPanel(new BorderLayout());
  private JMenuBar mainMenu = new JMenuBar();
  private JMenu LogInfo = new JMenu("Log Info");
  private JMenuItem getMysqlInfo = new JMenuItem("Get MySQL Info");
  private JMenuItem sqliDetect = new JMenuItem("SQLi Detector");
  private JFrame mainWindowRef = StatusObject.mWindow;
  public MySqlQueryLog(String filename){
    this.logFile = filename;
  }

  public JPanel getLogPanel(JFrame frame){
    LogInfo.add(getMysqlInfo);
    LogInfo.add(sqliDetect);
    mainMenu.add(LogInfo);
    getMysqlInfo.addActionListener(this);
    sqliDetect.addActionListener(this);
    mainFrame.add(mainMenu, BorderLayout.NORTH);
    mainFrame.add(new JScrollPane(this.getLogTable()));
    return this.mainFrame;
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == sqliDetect){
      JDialog jd = new JDialog(StatusObject.mWindow,"SQLi Results",true);
      jd.setSize(300,300);
      jd.add(new JScrollPane(this.sqliDetector()));
      jd.setVisible(true);
    } else if(e.getSource() == getMysqlInfo){
      JDialog jd = new JDialog(StatusObject.mWindow,"MySQL Info",true);
      jd.setSize(300,300);
      jd.add(this.getLogInfo());
      jd.setVisible(true);
    }


  }

  public JTable getLogInfo(){
    String[] headers = {"Version","Port","File","Socket Location"};
    String[] baseInfo = new String[3];
    String version = "";
    String port = "";
    String file = "";
    String sockLoc = "";
    try{
      List<String> tempLineCount = Files.readAllLines(Paths.get(this.logFile));
      baseInfo[0] = tempLineCount.get(0);
      baseInfo[1] = tempLineCount.get(1);
      baseInfo[2] = tempLineCount.get(2);
    }catch(Exception e){

    }
    String[][] data = new String[1][];
    for(String i : baseInfo){
      if(i.matches(".*(Version).*")){
        version = i.substring(i.indexOf("Version:") + 8, i.indexOf("started")-1);
      } else if(i.matches(".*(/.*).*")){
        file = i.substring(0,i.indexOf(","));
      } else if(i.matches(".*(Tcp Port:).*")){
        port = i.substring(i.indexOf("Tcp Port:") + 9, i.indexOf("Unix"));
      } else if(i.matches(".*(Unix Socket).*")){
        sockLoc = i.substring(i.indexOf("Unix Socket:") + 12);
      }
    }
    String[]legitData = {version,port,file,sockLoc};
    data[0] = legitData;
    JTable table = new JTable(data,headers);

    return table;
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
              // that it will throw an error because of var k
            }
          }
         
        }
        QId.add(id);
        QArgs.add(argument);
        QCommand.add(message); 
        QTime.add(time);
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

  

  public void logPrint(){
    for(int i = 0 ; i < QTime.size(); i++){
      System.out.println("Time: " + QTime.get(i));
      System.out.println("ID: " + QId.get(i));
      System.out.println("Command: " + QCommand.get(i));
      System.out.println("Message: " + QArgs.get(i));
    }
  }


  public JTable getLogTable(){
  
    int max = this.QArgs.size();
    String[] headers = {"Time","ID","Command","Message"};
    String[][] data = new String[max][];
    for(int i = 0 ; i < max; i++){
      data[i] = new String[]{
        QTime.get(i),
        QId.get(i),
        QCommand.get(i),
        QArgs.get(i)
      };  
    }
    

    JTable table = new JTable(data,headers){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };

    table.setBounds(20,40,400,300);
		table.setSize(400,300);
    return table;
  }

  public JTable sqliDetector(){
    String[] headers = {"Time","Command"};
    List<Integer> loc = new ArrayList<>();
    for(int i  = 0; i < QArgs.size(); i++){
      if(AttackIdentifier.BasicSqlLogSQLiDetect(QCommand.get(i))){
        loc.add(i);
      }
    }
    String[][] data = new String[loc.size()][];
    for(int i = 0 ; i < loc.size(); i++){
      data[i] = new String[]{
        QTime.get(i),
        QCommand.get(i)
      };
    }
    
    JTable table = new JTable(data,headers);
    return table;
  }
}
