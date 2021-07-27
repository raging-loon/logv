package src.http;
import java.util.List;


import java.util.ArrayList;
import java.util.HashMap;
public class Apache2Log {
  public List<String>   HTTPUserAgents = new ArrayList<String>();
  public List<String>   HTTPRequests = new ArrayList<String>();
  public List<String>   HTTPIpAddresses = new ArrayList<String>();
  public List<String>   HTTPResponceLength = new ArrayList<String>();
  public List<String>   HTTPStatusCodes = new ArrayList<String>();
  public List<String>   HTTPRequestTime = new ArrayList<String>();
  public List<String>   HTTPReferer = new ArrayList<String>();
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
