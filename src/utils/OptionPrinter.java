package src.utils;

public class OptionPrinter {
  public OptionPrinter(String version){
    System.out.println(
      "LogViewer " + version + "\n" + 
      "usage: <log format> <filename> <commands>\n" +
      "LOG FORMATS\n" + 
      "\tWhat type of logs\n" +
      "\t-apache : Apache2 logs(access.log)\n" +
      "\t-nginx : Nginx Logs(access.log, error.log)\n" +
      "\t-mysql : MySQL logs(query.log)\n" +
      "\t-mysql-err : MySql error.log\n" +
      "\t-f : required, specify log file\n" +
      "COMMANDS\n" +
      "\tIf no argument is given, the log will simply be printed\n" +
      "\tIf there are no arguments at all whatsoever, the GUI will start\n" +
      "\t-ip-counter : print all ip addresses found file as well as the number of times they show up\n" + 
      "\t-ip-count <ip address> : print the number of times the given IP address shows up\n" +
      "\t\t--time : print the times at which the ip address showed up\n" +
      "EXAMPLES\n" + 
      "\tlogv -apache -f /var/log/apache2/access.log\n" +
      "\tlogv -nginx  -f /var/log/nginx/access.log -ip-counter\n" +
      "\tlogv -apache -f /var/log/apache2/access.log -ip-count 127.0.0.1"
    );
  }  
}
