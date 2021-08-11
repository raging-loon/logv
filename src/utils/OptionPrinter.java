package src.utils;

public class OptionPrinter {
  public static void Printer(String version){
    System.out.println(
      "LogViewer " + version + "\n" + 
      "usage: <log format> <filename> <commands>\n" +
      "LOG FORMATS\n" + 
      "\tWhat type of logs\n" +
      "\t--list : list all log formats currently available\n" +
      "\t--info : show info about the log\n" +
      "\t\t--nonsilent : show additional information"+
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
  public static void SupportedLogFormatPrinter(String version){
    System.out.printf(
      "Logs currently supported by LogViewer v" + version + ":" +
      "\n\tApache2/Nginx access.log, -apache2 or -nginx" +
      "\n\tMySQl Error logs, error.log, -mysql_err" +
      "\n\tVSFTPD log, vsftpd.log, -vsftpd\n" +
      "\n" + 
      "\nIf you wish to contribute to this project, contact me at jgnovak1@protonmail.com" 
    );
  }  
}
