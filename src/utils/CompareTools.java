package src.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import src.http.*;
import src.misc.*;
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
}
