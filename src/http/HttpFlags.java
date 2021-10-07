package src.http;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;

import src.utils.AttackIdentifier;

/**
 * Class HttpFlags
 * 
 * Produces JTables and string tables about flagged
 * ip addresses
 * 
 *
 * */
public class HttpFlags {
  
  private static HashMap<String,Integer> sqliFlags(Apache2Log log){
    HashMap<String,Integer> ipAddrSqliFlags = new HashMap<>();
    // List<Integer> flaggedEntries = new ArrayList<>();
    for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
      if(AttackIdentifier.SqliMatchFound(log.HTTPRequests.get(i))){
        if(ipAddrSqliFlags.get(log.HTTPIpAddresses.get(i)) == null){
          ipAddrSqliFlags.put(log.HTTPIpAddresses.get(i),1);
        } else {
          ipAddrSqliFlags.put(
                        log.HTTPIpAddresses.get(i),
                        ipAddrSqliFlags.get(log.HTTPIpAddresses.get(i)) + 1
                        );
        }
      }
    }
    return ipAddrSqliFlags;
  }
  // ############################### PRINTING ###############################
  public static void printSqliFlags(Apache2Log log){
    HashMap<String,Integer> ipMap = sqliFlags(log);
    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    for(String addr: ipMap.keySet()){
      System.out.println("= " + addr + "  =  "  + ipMap.get(addr) + "   =");
    }
    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
  }
  public static JTable sqliFlagTable(Apache2Log log){
    String[] headers = {"Ip Address","Time","Request","User Agent"};
    List<Integer> flagLoc = new ArrayList<>();
    for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
      if(AttackIdentifier.SqliMatchFound(log.HTTPRequests.get(i))){
        flagLoc.add(i);
      }
    }
    String[][] data = new String[flagLoc.size()][];
    int passes = 0;
    for(int i : flagLoc){
      data[passes] = new String[]{
        log.HTTPIpAddresses.get(flagLoc.get(passes)),
        log.HTTPRequestTime.get(flagLoc.get(passes)),
        log.HTTPRequests.get(flagLoc.get(passes)),
        log.HTTPUserAgents.get(flagLoc.get(passes))
      };
      passes++;
    }
    JTable table = new JTable(data,headers);
    return table;
  }



  public static JTable fullFlagReport(Apache2Log log){
    List<String> UserAgentFlag = new ArrayList<>();
    List<String> XSSFlag = new ArrayList<>();
    List<String> SQLiFlag = new ArrayList<>();
    List<String> PTFlag = new ArrayList<>();
    List<String> AllLocations = new ArrayList<>();
    System.out.println();
    for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
      if(AttackIdentifier.PathTraversalMatchFound(log.HTTPRequests.get(i))){
        PTFlag.add(log.HTTPIpAddresses.get(i));
        continue;
      } else if(AttackIdentifier.XssMatchFound(log.HTTPRequests.get(i))){
        XSSFlag.add(log.HTTPIpAddresses.get(i));
        continue;
      } else if(AttackIdentifier.SqliMatchFound(log.HTTPRequests.get(i))){
        SQLiFlag.add(log.HTTPIpAddresses.get(i));
        continue;
      } else if(log.nmapScanSearch(i)){
        UserAgentFlag.add(log.HTTPIpAddresses.get(i));
        continue;
      } else {
        
      }
    }
    
    AllLocations.addAll(UserAgentFlag);
    AllLocations.addAll(XSSFlag);
    AllLocations.addAll(SQLiFlag);
    AllLocations.addAll(PTFlag);
  
    List<String> newList = AllLocations.stream().distinct().collect(Collectors.toList());
    
    System.out.println(AllLocations.size() == newList.size());
    String[] headers = {"Ip Address","XSS Flag","SQLi Flag","Path Traversal Flag","UA Flag"};
    Object[][] data = new Object[newList.size()][];
    List<String> usedIp = new ArrayList<>();
    for(int i = 0; i <  newList.size(); i++){
      Object[] tempData = new Object[headers.length];
      for(String j : newList){
        if(usedIp.contains(j)){
          continue;
        }
        if(XSSFlag.contains(j)){
          tempData[1] = Boolean.TRUE;
        } 
        if(SQLiFlag.contains(j)){
          tempData[2] = Boolean.TRUE;
        }
        if(PTFlag.contains(j)){
          tempData[3] = Boolean.TRUE;
        }
        if(UserAgentFlag.contains(j)){
          tempData[4] = Boolean.TRUE;
        }
        usedIp.add(j);
        
        tempData[i] = log.HTTPIpAddresses.get(i);        
      }
      data[i] = tempData;
    }
    JTable table = new JTable(data,headers);
    return table;
  }
}
