package src.utils;

import java.util.HashMap;

import javax.swing.JTable;

import src.LogFormat;
import src.LogObject;

public class CompareTools {

  public static HashMap<String, Integer> compareLogs(LogObject x, LogObject y){
    HashMap<String, Integer> ipAddrFound = new HashMap<>();
    
    HashMap<String,Integer> ipCountsLog1 = getIpCounts((LogFormat)x);
    HashMap<String,Integer> ipCountsLog2 = getIpCounts((LogFormat)y);
    
    if(ipCountsLog1 == null || ipCountsLog2 == null){
      return null;
    }
    for(String addr: ipCountsLog1.keySet()){
      if(ipAddrFound.get(addr) == null && ipCountsLog2.get(addr) != null){
         ipAddrFound.put(addr,ipCountsLog1.get(addr) + ipCountsLog2.get(addr));
      } 
    }

    return ipAddrFound; 
  }


  public static HashMap<String,Integer> getIpCounts(LogFormat x){
    HashMap<String,Integer> ipCounts = new HashMap<>();
    if(x.getIpAddresses() == null){
      return null;
    }
    for(String i : x.getIpAddresses()){
      if(ipCounts.get(i) == null){
        ipCounts.put(i,1);
      } else {
        ipCounts.put(i,ipCounts.get(i)+1);
      }
    }
    return ipCounts;
  }

  public static JTable compareLogsResults(HashMap<String,Integer> x){
    String[] tableHeaders = {"Ip Address","Frequency"};
    String[][] tableContents = new String[x.size()][];
    int loops = 0;
    for(String i : x.keySet()){
      String[] tempTableItems = {
        i, String.valueOf(x.get(i))
      };
      tableContents[loops] = tempTableItems;
      loops++;
    }
    JTable table = new JTable(tableContents,tableHeaders){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };

    return table;
  }
}
