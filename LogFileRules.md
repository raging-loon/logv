# New Log File Rules
This is a guide(mostly for me) on creating new log files.
## Inheritance and implementations
A log object must:
  - inherit LogObject
  - implement LogFormat
```java
import src.LogObject;
import src.LogFormat;
public class SomeLogFormat extends LogObject implements LogFormat{

}
```
It must have the following functions:
  ```java 
  // actual parsing
  public void Parser();
  // print the logs(raw) in the terminal
  public void logPrint();
  // get the log table
  public JTable getLogTable();
  // get info table, this may be deleted
  public String[][] getInfoTable(int row, StatusObject s);
  public String[][] getTableData();
  public String[] getTableHeaders();
  // make a list of all IP addresses, some log object such
  // as a mysql query logs will return null
  public List<String> getIpAddresses();
  public int getLogSize();
  // IMPORTANT
  // THE JFRAME MUST BE StatusObject.mWindow!!!!!!!
  public JPanel getLogPanel(JFrame frame);
   ```

## The Parser
The parser must add items(ip address, times, etc) to various lists.
The parser <i>must</i> have adequate error handling.
Most logs will throw one of a few errors:
```java
FileNotFoundException
java.nio.AccessDeniedException
IOException
```
On headless systems, this should print a message, on the gui, do the following:
```java
JOptionPane someNameHere = new JOptionPane();
String message = "something seomthing something, congratulations, you failed...";
// message can also be the message from an exception(e.getMessage())
fail.showMessageDialog(StatusObject.mWindow, message,"Nelson Muntz: Ha Ha!",JOptionPane.ERROR_MESSAGE);
```

## The Functionality
Each log must have some feature to get IP address frequencies and produce a table based on it. Obviously, some logs(mysql)
do not log IP addresses.



