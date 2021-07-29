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
    Apache2Log log = new Apache2Log("tests/logs/test2.log");
    log.Parser();
    log.PathTraversalDetector(false);
  }

}
