import src.*;
import src.http.*;
import src.sql.*;
import src.utils.*;
public class Main{
  public static void main(String argv[]){
    /* // uncomment for testing
    if(argv.length == 0){
      OptionPrinter op = new OptionPrinter("1.0.0");
      System.exit(0);
    }
    //*/
    LogParser logParser = new LogParser("apache2", "/var/log/apache2/access.log");
    Apache2Log apache2Log = logParser.Apache2LogParser();
    // apache2Log.PrintIpCounts();
    
    apache2Log.nmapScanSearch(false);
  }

}
