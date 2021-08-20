import src.*;
import src.sql.*;
import src.utils.AttackIdentifier;

public class ex2 {

  public static void main(String[] args) {
    MySqlQueryLog log = new MySqlQueryLog("tests/query.log");
    log.Parser();
    for(int i = 0; i < log.QArgs.size();i++){
      if(AttackIdentifier.BasicSqlLogSQLiDetect(log.QArgs.get(i))){
        System.out.println("-----------------");
        System.out.println("Command: " + log.QArgs.get(i));
      }
    }
  } 
}