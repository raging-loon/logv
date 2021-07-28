package src.sql;

import java.util.ArrayList;
import java.util.List;

public class MySqlQueryLog {
  public String filename;
  public String sqlVersion;
  public List<String> QTime = new ArrayList<String>();
  public List<Integer> QId =  new ArrayList<Integer>();
  public List<String> QCommand = new ArrayList<String>();
  public List<String> QArgs = new ArrayList<String>();
  public MySqlQueryLog(String filename){
    this.filename = filename;
  }

}
