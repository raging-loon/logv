package src.http;
import java.util.*;
import java.nio.file.*;
import src.LogFormat;
import src.LogObject;

import java.util.ArrayList;
import java.util.HashMap;
public class Apache2Log extends LogObject implements LogFormat {
  public List<String>   HTTPUserAgents = new ArrayList<String>();
  public List<String>   HTTPRequests = new ArrayList<String>();
  public List<String>   HTTPIpAddresses = new ArrayList<String>();
  public List<String>   HTTPResponceLength = new ArrayList<String>();
  public List<String>   HTTPStatusCodes = new ArrayList<String>();
  public List<String>   HTTPRequestTime = new ArrayList<String>();
  public List<String>   HTTPReferer = new ArrayList<String>();
  
  public Apache2Log(String logFile){ this.logFile = logFile; }



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
        IpCount.put(addr, 0);
      } else {
        IpCount.put(addr,IpCount.get(addr)+1);
      }
    }
    // fix an off by one error
    for(String i: IpCount.keySet()){
      IpCount.put(i, IpCount.get(i) + 1);
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

}
