
import java.util.HashMap;

import src.http.Apache2Log;
import src.utils.CompareTools;
// import src.*;
// import src.http.*;
// import src.sql.*;
public class test2 {
  // VSFTPD TEST
  public static void main(String[] args) {
    Apache2Log log = new Apache2Log("tests/weblog1.log");
    log.Parser();
    Apache2Log log1 = new Apache2Log("tests/access.log");
    log1.Parser();
    HashMap<String,Integer> x =  CompareTools.compareLogs(log,log1);
    if(x == null){
      System.exit(-1);
    }
    for(String i: x.keySet()){
      System.out.println(i + ": " + x.get(i));
    }
  }  
}
