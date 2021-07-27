package src.sql;
import java.util.List;
import java.util.ArrayList;
public class MySqlErrorLog {
  /*    MySQL error.log format
    time thread [label] [err_code] [subsystem] msg
  */
  public List<String> ErrorTime = new ArrayList<String>();
  public List<String> ErrorThread = new ArrayList<String>();
  public List<String> ErrorLabel = new ArrayList<String>();
  public List<String> ErrorCode = new ArrayList<String>();
  public List<String> SubSystem = new ArrayList<String>();
  public List<String> ErrorMessage = new ArrayList<String>();
  
}
