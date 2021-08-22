package src.http;

import java.util.*;

import javax.swing.*;


// import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.nio.file.*;
import src.*;
import src.utils.AttackIdentifier;
import src.utils.MiscUtils;

/**
 * Apache2 Log Class
 * 
 * Log parser for the standard Apache2 and Nginx log format 
 * specified in /etc/apache2 or /etc/nginx
 * 
 * Functions:
 *  Parser()        -> Self explanatory, parses the log
 * 
 *  getIpCounts     -> @return a Hashmap that contains all Ip addresses
 *                     and the number of times they show up
 * 
 *  getIpCounts     -> overrides the above, takes @param ipAddr
 *                     and @return the number of times it showed up
 * 
 *  printIpCounts   -> calls getIpCounts(the one that returns a HashMap)
 *                     and prints the results
 * 
 *  nmapScanSearch  -> uses a regular expression to parse through HTTPUserAgents
 *                     @return true if one or more is found. 
 *                     @param silent is used for printing output
 * 
 *  XssDetector     -> uses {@link src.utils.AttackIdentifier#XssMatchFound(String)}
 *                     to parse through all of the HTTPRequests and look for the 
 *                     signiture XSS patterns
 * 
 *  SqliDetector    -> uses {@link src.utils.AttackIdentifier#SqliMatchFound(String)}
 *                     to parse though all of the HTTPRequests and look for the 
 *                     signiture SQLi patterns
 *  getTableData    -> get the proper table data for the JTable in 
 *                     {@link Main.java#main(String argv[])}
 * 
 *  logPrint        -> Print the logs in a better looking format(console only)
 * 
 *  getTableData    -> Get all of the table information, used only by {@link #getLogTable()}
 * 
 *  getInfoTable    -> Get the table data for a specific row in the mainWindow's JTable.
 *                     Has information like NMap detection, XSS detection, etc. 
 *                     WHOIS can be turned on or off.
 * 
 *  getLogTable     -> @return a JTable with all of the information provided by the 
 *                     {@link #getTableData()} and {@link #TableHeaders}
 * @version 1.0
 *
 */
public class Apache2Log extends LogObject implements LogFormat,Runnable, ActionListener {
  public final String   TableHeaders[] = {"Ip Address", "Time","Request","Referer","Status Code","Length","User Agent"};
  // NON GUI COMPONENTS
  public List<String>   HTTPUserAgents = new ArrayList<String>();
  public List<String>   HTTPRequests = new ArrayList<String>();
  public List<String>   HTTPIpAddresses = new ArrayList<String>();
  public List<String>   HTTPResponceLength = new ArrayList<String>();
  public List<String>   HTTPStatusCodes = new ArrayList<String>();
  public List<String>   HTTPRequestTime = new ArrayList<String>();
  public List<String>   HTTPReferer = new ArrayList<String>();
  // GUI COMPONENTS
  private JMenuBar webLogMenu = new JMenuBar();
  private JPanel panel = new JPanel(new BorderLayout());
  
  private JMenu DetectionTools = new JMenu("Detection Tools");
  private JMenu FrequencyTools = new JMenu("Frequency Tools");
  private JMenu FlagMenus = new JMenu("Flags");
  // ********************DETECTION STUFF***********************
  private JMenuItem nmapScanD = new JMenuItem("User Agent Scanner");
  private JMenuItem xxsScanD = new JMenuItem("XSS Detector");
  private JMenuItem sqliDetect = new JMenuItem("SQLi Detector");
  private JMenuItem pathTravD = new JMenuItem("Path Traversal Detector");
  //***********************FREQUENCY STUFF***********************
  private JMenuItem ipCountOpt = new JMenuItem("Show IP Frequencies");
  //***********************FLAGS***********************
  private JMenuItem ipFlagsTotal = new JMenuItem("IP Address Flags");
  // vv a reference to the mainWindow JFrame in logv.java
  private JFrame logvMainWindow = StatusObject.mWindow;
  // private boolean logPanelRetrieved = false;
  
  public JPanel getLogPanel(JFrame frame){
    logvMainWindow = frame;
    // logPanelRetrieved = true;
    nmapScanD.addActionListener(this);
    xxsScanD.addActionListener(this);
    pathTravD.addActionListener(this);
    ipCountOpt.addActionListener(this);
    sqliDetect.addActionListener(this);
    ipFlagsTotal.addActionListener(this);
    // set sizes and visibility
    // webLogMenu.setSize(1000,20);
    webLogMenu.setBounds(0,10,100,100);
    panel.setSize(1000,300);
    panel.setVisible(true);

    // add stuff
    DetectionTools.add(nmapScanD);
    DetectionTools.add(xxsScanD);
    DetectionTools.add(pathTravD);
    DetectionTools.add(sqliDetect);
    FrequencyTools.add(ipCountOpt);
    FlagMenus.add(ipFlagsTotal);
    webLogMenu.add(DetectionTools);
    webLogMenu.add(FrequencyTools);
    webLogMenu.add(FlagMenus);
    panel.add(webLogMenu,BorderLayout.NORTH);
    JTable logTable = this.getLogTable();
    logTable.setBounds(0,20,400,300);
    // logTable.setSize(1000,300);
    panel.add(new JScrollPane(logTable));
    return this.panel;
  }






  public void actionPerformed(ActionEvent s){
    if(s.getSource() == nmapScanD){
      String[] headers = {"Ip Address","Request","Time"};
    
      int size = 0;
      // nsdLoc = Nmap Scan Detected Location
      List<Integer> nsdLoc = new ArrayList<>();
      for(int j = 0; j < HTTPUserAgents.size();j++){
        if(nmapScanSearch(j)) {
          size++;
          nsdLoc.add(j);
        }
      }
      String[][] results = new String[size][];
      for(int i = 0; i < nsdLoc.size() ;i++){
          String[] temp = {
            HTTPIpAddresses.get(nsdLoc.get(i)),
            HTTPRequests.get(nsdLoc.get(i)),
            HTTPRequestTime.get(nsdLoc.get(i))
          };
          results[i] = temp;
        // }
      }
      JTable table = new JTable(results,headers);
      // table.setBounds(0,0,300,300);
      // table.setSize(300,300);
      
      JDialog jd = new JDialog(logvMainWindow,"Nmap Scan Results");
      jd.add(new JScrollPane(table));
      jd.setSize(300,300);
      // jd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jd.setVisible(true);
    }



    else if(s.getSource() == xxsScanD){
      String[] headers = {"IP Address","Time","Request","Status Code"};
      int size = 0;
      List<Integer> xssDLoc = new ArrayList<>();
      for(int i = 0; i < HTTPRequests.size();i++){
        if(XssDetector(i)){
          size++;
          xssDLoc.add(i);
        } 
      }
      String[][] data = new String[size + 1][];
      for(int i = 0; i < xssDLoc.size(); i++){
        
          String[] tempData = {
            HTTPIpAddresses.get(xssDLoc.get(i)),
            HTTPRequestTime.get(xssDLoc.get(i)),
            HTTPRequests.get(xssDLoc.get(i)),
            HTTPStatusCodes.get(xssDLoc.get(i))
          };
          data[i] = tempData;
        
      }
      JTable XSSTable = new JTable(data, headers);
      // XSSTable.setBounds(0,0,300,300);
      JDialog jd = new JDialog(logvMainWindow, "XSS Detection Results");
      // jd.setLayout(new BorderLayout());
      jd.add(new JScrollPane(XSSTable));
      jd.setSize(300,300);
      jd.setVisible(true);
      
    }

    else if(s.getSource() == ipCountOpt){
      HashMap<String,Integer> ipCounts = this.getIpCounts();
      String[] headers = {"Ip Address","Frequency"};
      String[][] data = new String[ipCounts.size()][];
      int passes = 0;
      for(String addr: ipCounts.keySet()){
        String[] tempData = {
          addr, String.valueOf(ipCounts.get(addr))
        };
        data[passes] = tempData;
        passes++;
      }
      JTable table = new JTable(data,headers);
      JDialog jd = new JDialog(logvMainWindow,"IP Frequency Table");
      jd.add(new JScrollPane(table));
      jd.setSize(300,300);
      jd.setVisible(true);
    }

    else if(s.getSource() == pathTravD){
      String[] headers = {"Ip Address","Time","Request","User Agent"};
      List<Integer> ptLoc = new ArrayList<>();
      for(int i = 0; i < HTTPRequests.size();i++){
        if(PathTraversalDetector(i)){
          ptLoc.add(i);
        }
      }
      String[][] data = new String[ptLoc.size()][];
      for(int i = 0; i < ptLoc.size(); i++){
        String[] tempData = {
          HTTPIpAddresses.get(ptLoc.get(i)),
          HTTPRequestTime.get(ptLoc.get(i)),
          HTTPRequests.get(ptLoc.get(i)),
          HTTPUserAgents.get(ptLoc.get(i))
        };
        data[i] = tempData;
      }
      JTable table = new JTable(data,headers);
      JDialog jd = new JDialog(logvMainWindow,"Path Traversal Results");
      jd.setSize(300,300);
      jd.add(new JScrollPane(table));
      jd.setVisible(true);
    }

    else if(s.getSource() == sqliDetect){
      JDialog jd = new JDialog(StatusObject.mWindow, "SQLi Detection Results",true);
      JTable table = HttpFlags.sqliFlagTable(this);
      jd.add(new JScrollPane(table));
      jd.setSize(300,300);
      jd.setVisible(true);
    }
  
    else if(s.getSource() == ipFlagsTotal){

    }
  }


  public Apache2Log(String logFile){ this.logFile = logFile; this.logFormat="apache2";}
  
  public String[] getTableHeaders(){
    return this.TableHeaders;
  }
  
  public List<String> getIpAddresses(){
    return this.HTTPIpAddresses;
  }
  
  public int getIpCounts(String ipAddr){
    int counter = 0;
    for(String addr: HTTPIpAddresses){
      if(addr.equals(ipAddr)){
        counter++;
      }
    }
    return counter;
  }
  
  public int getLogSize(){
    return this.HTTPIpAddresses.size();
  }
  
  public JTable getLogTable(){
    if(this.HTTPIpAddresses.size() == 0){
      this.start();
    }
    JTable table = new JTable(this.getTableData(),this.TableHeaders){
       
      public boolean isCellEditable(int row, int column){ return false; }
    };
    table.setBounds(20,40,400,300);
		table.setSize(400,300);
    return table;
  }
  
  public void Parser(){
    try{
      List<String> allLines = Files.readAllLines(Paths.get(this.logFile));
    
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
          
          else if((arr[i].matches("(\"Mozilla/\\d.\\d.*)")|| arr[i].matches("(\"sqlmap/.*pip)")) && !HTTPRequest.equals("")){
            try{
              HTTPUserAgent = arr[i];
              // int max = arr.length;
              // // some logs have a "-" right after the user agent
              // if(arr[arr.length] == "\"-\""){
              //   max--;
              // }
              for(int j = 0; j < arr.length; j++){
                HTTPUserAgent += " " + arr[++i];
              }
            } catch(Exception e){
            }
          }
          // get the time of the request
          else if(arr[i].matches("(\\[\\d{1,}/\\w\\w\\w/\\d\\d\\d\\d:\\d{1,}:\\d{1,}:\\d{1,})")){
            // System.out.println("TIME");
            time += arr[i] + " ";
            time += arr[++i];
          }

          // get the ip address
          else if(arr[i].matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")
          || arr[i].matches("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))")){
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
        this.HTTPIpAddresses.add(HTTPIpAddr);
        this.HTTPRequestTime.add(time);
        this.HTTPRequests.add(HTTPRequest);
        this.HTTPResponceLength.add(HTTPlen);
        this.HTTPStatusCodes.add(HTTPstatusCode);
        this.HTTPUserAgents.add(HTTPUserAgent);
        this.HTTPReferer.add(Referer);

      }
      
    } //catch(FileNotFoundException e){
    //   System.out.println("File: " + ParserLogFile + ": not found");
    //   System.exit(-1);}
      catch(java.io.FileNotFoundException e){
        System.out.println("File: " + this.logFile + ": not found");
        return;
      } catch(java.nio.file.AccessDeniedException e){
        System.out.println("Access to " + logFile +" was denied");
        return;
      } catch(java.io.IOException e){
        System.out.println("Error: could not read from " + logFile + ": may be a directory");
        return;
      }
      catch(Exception e){
        System.out.println("Sorry, something went wrong"); // fix
        e.printStackTrace();
        System.exit(-1);
    }
  }
  
  public HashMap<String,Integer> getIpCounts(){
    HashMap<String,Integer> IpCount = new HashMap<>();
    for(String addr: HTTPIpAddresses){
      if(IpCount.get(addr)==null){
        IpCount.put(addr, 1);
      } else {
        IpCount.put(addr,IpCount.get(addr)+1);
      }
    }
    
    return IpCount;
  }

  public void PrintIpCounts(){
    HashMap<String,Integer> ipCount = getIpCounts();
    for(String i: ipCount.keySet()){
      System.out.println(i + ": " + ipCount.get(i));
    }
  }

  public boolean nmapScanSearch(boolean silent){
    /**
     * Looks for the user agent used with nmap scans on HTTP[S]
     * "\"Mozilla/5.0 (compatible; Nmap Scripting Engine; https://nmap.org/book/nse.html)\""
     */
    int occurences = 0;
    boolean found = false;
    for(int i = 0; i < HTTPUserAgents.size(); i++){
      if(HTTPUserAgents.get(i).matches(".*Nmap Scripting Engine; https://nmap.org/book/nse.html.*")){
        found = true; occurences++;
        if(!(silent)){
          System.out.println("Nmap Scan detected| ip address " + HTTPIpAddresses.get(i) + "| request " + HTTPRequests.get(i) + "| time " + HTTPRequestTime.get(i));
        }
      }
    }
    if(!(silent)) System.out.println("Total Nmap user agents found: " + occurences);
    return found;
  }

  public boolean nmapScanSearch(int index){
    if(HTTPUserAgents.get(index).matches(".*Nmap Scripting Engine; https://nmap.org/book/nse.html.*") 
    || HTTPUserAgents.get(index).matches(".*sqlmap.*"))
      return true;
    else return false;
  }
 
  public boolean XssDetector(boolean silent){
    int occurences = 0;
    boolean found = false;
    for(int i = 0; i < HTTPRequests.size(); i++){
      if(AttackIdentifier.XssMatchFound(HTTPRequests.get(i))){
        found = true;occurences++;
        if(!silent){
          System.out.println("--------------------------");
          System.out.println("| Ip Address: " + HTTPIpAddresses.get(i));
          System.out.println("| Verb: " + HTTPRequests.get(i).split(" ")[0].substring(1));
          System.out.println("| Request: " + HTTPRequests.get(i).split(" ")[1]);
          System.out.println("| Time: " + HTTPRequestTime.get(i));
        }
      }
    }
    if(!silent){
      System.out.println("Total possible xss attacks identified: " + occurences);
    }
    return found;
  }

  public boolean XssDetector(int index){
    if(AttackIdentifier.XssMatchFound(HTTPRequests.get(index))) return true;
    else return false;
  }

  public boolean SqliDetector(boolean silent){
    int occurences = 0;
    boolean found = false;
    for(int i = 0; i < HTTPRequests.size(); i++){
      if(AttackIdentifier.SqliMatchFound(HTTPRequests.get(i))){
        found = true;occurences++;
        if(!silent){
          System.out.println("--------------------------");
          System.out.println("| Ip Address: " + HTTPIpAddresses.get(i));
          System.out.println("| Request: " + HTTPRequests.get(i).split(" ")[1]);
          System.out.println("| Time: " + HTTPRequestTime.get(i));
        }
      }
    }
    if(!silent) System.out.println("Total possible sqli attacks: " + occurences);
    return found;
  }

  public boolean PathTraversalDetector(boolean silent){
    int occurences = 0;
    boolean found = false;
    for(int i = 0; i < HTTPRequests.size(); i++){
      if(AttackIdentifier.PathTraversalMatchFound(HTTPRequests.get(i))){
        found = true; occurences++;
        if(!silent){
          System.out.println("--------------------------");
          System.out.println("| Ip Address: " + HTTPIpAddresses.get(i));
          System.out.println("| Request: " + HTTPRequests.get(i));
          System.out.println("| Time: " + HTTPRequestTime.get(i));
        }
      }
    }

    if(!silent) System.out.println("Total possible path traversal attacks found: " + occurences);
    return found;
  }

  public boolean PathTraversalDetector(int index){
    if(AttackIdentifier.PathTraversalMatchFound(HTTPRequests.get(index))) return true;
    else return false;
  }
  
  public String[][] getTableData(){
    int max = HTTPIpAddresses.size();
    String[][] table = new String[max][];// = {{},{},{}};
    //{"Ip Address", "Time","Request","Referer","Status Code","Length","User Agent"};
    for(int i = 0; i < max;i++){

      String[] tempTable = {HTTPIpAddresses.get(i),
                            HTTPRequestTime.get(i),
                            HTTPRequests.get(i),
                            HTTPReferer.get(i),
                            String.valueOf(HTTPStatusCodes.get(i)),
                            String.valueOf(HTTPResponceLength.get(i)),
                            HTTPUserAgents.get(i)};
  
      table[i] = tempTable;
      // for(int j = 0; j < tempTable.length;j++) 
        // table[0][i][j] = table[0][i][j].toString();
    }
    return table;
  }
  
  public void logPrint(){
    for(int i = 0; i < this.HTTPIpAddresses.size();i++){
      System.out.println("-------------------------");
      System.out.println("Ip Address: " + HTTPIpAddresses.get(i));
      System.out.println("Time: " + HTTPRequestTime.get(i));
      System.out.println("Request: " + HTTPRequests.get(i));
      System.out.println("Status Code: " + HTTPStatusCodes.get(i));
      System.out.println("Length: " + HTTPResponceLength.get(i));
      System.out.println("Referer: " + HTTPReferer.get(i));
      System.out.println("User Agent: " + HTTPUserAgents.get(i));
    }
  }
  
  public String[][] getInfoTable(int rowNo,StatusObject s){
    // WhoisLookup needs to be put on another thread.
    if(s.doWhoisLookup == true){
      String[][]table = new String[7][];
      String[] whoisRes = MiscUtils.WhoisApiDisector(HTTPIpAddresses.get(rowNo));
    
      table[0] =  new String[]{"Full Request", HTTPIpAddresses.get(rowNo) + " - - " +
      HTTPRequestTime.get(rowNo) + " " + 
      HTTPRequests.get(rowNo) + " " + 
      HTTPStatusCodes.get(rowNo) + " " +
      HTTPResponceLength.get(rowNo) + " " +  
      HTTPReferer.get(rowNo) + " " + 
      HTTPUserAgents.get(rowNo)};

      table[1] = new String[]{"Status Code", MiscUtils.getStatusCodeMessage(Integer.valueOf(HTTPStatusCodes.get(rowNo)))};

      table[2] = new String[]{"Ip Address Name",whoisRes == null ? "Unknown" :whoisRes[0]};
      table[3] = new String[]{"Ip Address Organization",whoisRes == null ? "Unknown" :whoisRes[1]};
      table[4] = new String[]{"Nmap Scan Detected", nmapScanSearch(rowNo) ? "true" : "false"};
      table[5] = new String[]{"Cross Site Scripting Detected", XssDetector(rowNo) ? "true" : "false"};
      table[6] = new String[]{"Path Traversal Detected", PathTraversalDetector(rowNo) ? "true" : "false"};
      return table;
    } else {
      String[][]table = new String[5][];
    
      table[0] =  new String[]{"Full Request", HTTPIpAddresses.get(rowNo) + " - - " +
                  HTTPRequestTime.get(rowNo) + " " + 
                  HTTPRequests.get(rowNo) + " " + 
                  HTTPStatusCodes.get(rowNo) + " " +
                  HTTPResponceLength.get(rowNo) + " " +  
                  HTTPReferer.get(rowNo) + " " + 
                  HTTPUserAgents.get(rowNo)};

      table[1] = new String[]{"Status Code", MiscUtils.getStatusCodeMessage(Integer.valueOf(HTTPStatusCodes.get(rowNo)))};
      table[2] = new String[]{"Nmap Scan Detected", nmapScanSearch(rowNo) ? "true" : "false"};
      table[3] = new String[]{"Cross Site Scripting Detected", XssDetector(rowNo) ? "true" : "false"};
      table[4] = new String[]{"Path Traversal Detected", PathTraversalDetector(rowNo) ? "true" : "false"};
      return table;
    }
    /**
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * | Full Request     | <full request>   |
     * | Status Code:     | <error code>     |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     **/
  
  }

  public void run(){
    this.Parser();
  }
  
  public void start(){
    Thread t = new Thread(this);
    t.start();
  }
  
   public void commandParser(ShellManager shm,String command){


    HttpXMLManager httpXMLManager = new HttpXMLManager(this, this.logFile);
   
   
    if(command.split(" ")[0].equals("show")){

      String[] cmdArr = command.split(" ");
      if(cmdArr.length == 1){
        // System.out.println("");
        System.out.println("usage: show <item>");
        System.out.println("item can be one of the following: ");
        System.out.println("\tip-addr");
        System.out.println();
        System.out.println("Use -xml|-html at the end to save to an xml/html file[not currently supported]");
        return;
      } else {

        if(cmdArr[1].equals("ip-addr")){
          HashMap<String,Integer> ipCounts = this.getIpCounts();
          for(String addr : ipCounts.keySet()){
            
            System.out.printf("%s : %d\n",addr,ipCounts.get(addr));
            
            // System.out.println(addr + " : " + ipCounts.get(addr) + " : %" + pc);
          }
        }
      }

      // ################### XSS COMMAND ##################
    } else if(command.matches(".*xss.*")){
      String[] cmdArr = command.split(" ");
      if(cmdArr.length == 1){
        System.out.println("usage: xss [-h] < -xml|-html <file> >");
      }else{  
        if(cmdArr[1].equals("-h")){
          HttpOptionPrinter.printApache2XSSHelp();
        }
        else if(cmdArr[1].equals("-html")){
          if(cmdArr.length == 2){
            HttpOptionPrinter.argumentRequired("xss","-html");
          } else {
            String filename = cmdArr[2];
            httpXMLManager.saveXSSReport(filename, HttpXMLManager.HTMLFORMAT);
          }
        } 
        else if(cmdArr[1].equals("-xml")){
          if(cmdArr.length == 2){
            HttpOptionPrinter.argumentRequired("xss", "-xml");
          } else {
            String filename = cmdArr[2];
            httpXMLManager.saveXSSReport(filename, HttpXMLManager.XMLFORMAT);
          }
        } 
        else if(cmdArr[1].matches("-p|--print")){
          this.XssDetector(false);
          return;
        } else {
          HttpOptionPrinter.printApache2XSSHelp();
        }
      } 
    } 
    // ################### PATH TRAVERSAL #####################
    else if(command.matches("(pt|pathtrav|path-traversal).*")){
      String[] cmdArr = command.split(" ");
      if(cmdArr.length == 1){
        HttpOptionPrinter.printApache2PathTraversalHelp();
      } else {
        if(cmdArr[1].equals("-xml")){
          if(cmdArr.length == 2) { HttpOptionPrinter.argumentRequired("pt","-xml"); }
          else {
            String filename = cmdArr[2];
            httpXMLManager.savePathTraversalReport(filename, HttpXMLManager.XMLFORMAT);
          }
        } else if(cmdArr[1].equals("-html")){
          if(cmdArr.length == 2){ HttpOptionPrinter.argumentRequired("pt","-html"); }
          else {
            String filename = cmdArr[2];
            httpXMLManager.savePathTraversalReport(filename, HttpXMLManager.HTMLFORMAT);
          }
        } else {
          HttpOptionPrinter.printApache2PathTraversalHelp();
        }
      }
    }
    // ################### USER AGENT ####################
    else if(command.matches("(ua|user-agent).*")){
      String[] cmdArr = command.split(" ");
      if(cmdArr.length == 1){
        HttpOptionPrinter.printUACommandHelp();
      } else {
        if(cmdArr[1] == "-html"){
          if(cmdArr.length == 2){
            HttpOptionPrinter.argumentRequired("ua", "-html");
            return;
          } 
          httpXMLManager.saveUAReport(cmdArr[2], HttpXMLManager.HTMLFORMAT);
        } else if(cmdArr[1].equals("-xml")){
          if(cmdArr.length == 2){ HttpOptionPrinter.argumentRequired("ua", "-xml"); return;}
          
          httpXMLManager.saveUAReport(cmdArr[2],HttpXMLManager.XMLFORMAT);
        }
      }
    }
    else if(command.equals("help")){
      HttpOptionPrinter.basicApache2CommandHelp();
    }
    // ################### SQL INJECTION ##################
    else if(command.matches("sqli.*")){
      String cmdArr[] = command.split(" ");
      if(cmdArr.length == 1){
        HttpOptionPrinter.printSQLiApache2Help();
        return;
      }
      if(cmdArr[1].matches("-f|--flags")){
        if(cmdArr.length == 2){
          HttpOptionPrinter.argumentRequired("sqli","-f|--flags");
        
        } else{
          if(cmdArr[2].equals("show")){
            HttpFlags.printSqliFlags(this);
          } else if(cmdArr[2].equals("-xml")){
            if(cmdArr.length == 3){
              HttpOptionPrinter.argumentRequired("sqli","-xml");
            } else {
              httpXMLManager.saveSQLiReport(cmdArr[3],HttpXMLManager.XMLFORMAT);
            }
          } else if(cmdArr[2].equals("-html")){
            if(cmdArr.length == 3){ HttpOptionPrinter.argumentRequired("sqli","-html");}
            else {
              httpXMLManager.saveSQLiReport(cmdArr[3],HttpXMLManager.HTMLFORMAT);
            }
          }
        }
        
      } else if(cmdArr[1].matches("-h|--help|help")){
        HttpOptionPrinter.printSQLiApache2Help();
      } else{
        HttpOptionPrinter.unknownArg("sqli", cmdArr[1]);
      }
    }
    else {

      // if all else fails, send the command to the shell manager
      shm.ParseCommand(command);
    }
  }
}