package src;
public class LogParser {
  private String ParserType;
  private String ParserLogFile;
  public LogParser(String LogFormat, String LogFile){
    switch(LogFormat){
      case "apache2":
      case "nginx":
        ParserType = LogFormat;
        break;
      case "mysql_err":
        ParserType = LogFormat;
        break;
      case "mysql_query":
        ParserType = LogFormat;
        break;
      default:
        System.out.println("Invalid log format: " + LogFormat);
        System.exit(-1);
    }
    ParserLogFile = LogFile;
  }


}
