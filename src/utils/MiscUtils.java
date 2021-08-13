package src.utils;
import java.net.URL;
import java.util.HashMap;
import java.io.*;
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
        return "Unknown";  
    }
  }
  public static String[] WhoisApiDisector(String ipAddr){
    try{
      URL url = new URL("http://whois.arin.net/rest/ip/" + ipAddr);
      String name = "", Org = "";
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
        for (String line; (line = reader.readLine()) != null;) {
          if(line.matches(".*(<td>Name</td>).*")){
            name = line.substring(line.lastIndexOf("<td>")+4,line.lastIndexOf("</td>"));
          } 
          if(line.matches(".*(<td>Organization</td>).*")){
            Org = line.substring(line.lastIndexOf("<td>")+4);
          }
        }
      } 
      if(!(name.equals("") && Org.equals(""))){
        return new String[]{name,Org};
      }
    } catch(Exception e){
      return null;
    }
    return null;
  }

  public static void printIpCountResults(HashMap<String,Integer> x){
    for(String addr: x.keySet()){
      System.out.println(addr + ": " + x.get(addr));
    }
  }
}
