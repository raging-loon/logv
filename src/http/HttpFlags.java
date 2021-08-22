package src.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    for(int i = 0; i <= 0; i++){
      data[i] = new String[]{
        log.HTTPIpAddresses.get(flagLoc.get(i)),
        log.HTTPRequestTime.get(flagLoc.get(i)),
        log.HTTPRequests.get(flagLoc.get(i)),
        log.HTTPUserAgents.get(flagLoc.get(i))
      };
    }
    JTable table = new JTable(data,headers);
    return table;
  }


}
