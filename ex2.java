import src.*;
import src.sql.*;

public class ex2 {

  public static void main(String[] args) {
    MySqlQueryLog log = new MySqlQueryLog("tests/query.log");
    log.Parser();
    log.logPrint();
  } 
}