
import java.util.HashMap;

import src.http.Apache2Log;
import src.misc.Vsftpd;
import src.utils.CompareTools;
// import src.*;
// import src.http.*;
// import src.sql.*;
public class test2 {
  // VSFTPD TEST
  public static void main(String[] args) {
    Apache2Log log = new Apache2Log("/var/log/access.log");
    log.Parser();

    HashMap<String,Integer> x =  CompareTools.getIpCounts(log);
    for(String i: x.keySet()){
      System.out.println(i + ": " + x.get(i));
    }
  }  
}
