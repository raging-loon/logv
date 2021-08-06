package src.http;

import java.util.*;

import javax.swing.JTable;

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
public class Apache2Log extends LogObject implements LogFormat,Runnable {
  public final String   TableHeaders[] = {"Ip Address", "Time","Request","Referer","Status Code","Length","User Agent"};
  public List<String>   HTTPUserAgents = new ArrayList<String>();
  public List<String>   HTTPRequests = new ArrayList<String>();
  public List<String>   HTTPIpAddresses = new ArrayList<String>();
  public List<String>   HTTPResponceLength = new ArrayList<String>();
  public List<String>   HTTPStatusCodes = new ArrayList<String>();
  public List<String>   HTTPRequestTime = new ArrayList<String>();
  public List<String>   HTTPReferer = new ArrayList<String>();
  
  public Apache2Log(String logFile){ this.logFile = logFile; this.logFormat="apache2";}



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

  public int getIpCounts(String ipAddr){
    int counter = 0;
    for(String addr: HTTPIpAddresses){
      if(addr.equals(ipAddr)){
        counter++;
      }
    }
    return counter;
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
    if(HTTPUserAgents.get(index).matches(".*Nmap Scripting Engine; https://nmap.org/book/nse.html.*"))
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

  public JTable getLogTable(){
    if(this.HTTPIpAddresses.size() == 0){
      this.start();
    }
    JTable table = new JTable(this.getTableData(),this.TableHeaders){
      @Override 
      public boolean isCellEditable(int row, int column){ return false; }
    };
    return table;
  }


  public String[] getTableHeaders(){
    return this.TableHeaders;
  }

  public List<String> getIpAddresses(){
    return this.HTTPIpAddresses;
  }


  public int getLogSize(){
    return this.HTTPIpAddresses.size();
  }
}
 