package src.utils;

public class MiscUtils {
  public static String getStatusCodeMessage(int x){
    switch(x){
      case 200:
        return "OK";
      case 206:
        return "Partial Content";
      case 301:
        return "Moved Permanently";
      case 302:
        return "Found";
      case 303:
        return "See Other";  
      case 304:
        return "Not Modified";
      case 404:
        return "Not Found";
      case 500:
        return "Internal Server Error";
      default:
        return "Unkown";  
    }
  }

}
