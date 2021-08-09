import src.http.*;
import src.misc.*;
import src.misc.*;
import src.sql.*;
import src.utils.OptionPrinter;
/**
 * This class contains everything needed to run in a headless environment or
 * for people who don't want to use the GUI.
 */
public class logvcli {
  public void argRequiredError(String arg){
    System.out.println("Argument " + arg + ": requires an argument");
    System.exit(-1);
  }
  public void main(String[] args){
    if(args[0].matches("(-h|--help)")){
      OptionPrinter.Printer("1.0.0");
      System.exit(0);
    }
    // vv DEBUG
    // System.out.println("First arg is: " + args[0]);
    String logFormatFlag = "";
    String logFileFlag = "";
    int ipCountsFlag = -1;
    // int XMLOutputFlag = -1
    String spcfcIpCountsFlag = "";
    
    // a nice c++ struct would go nicely here 
    for(int i = 0; i < args.length; i++){
      if(args[i].matches("-(apache2|nginx|mysql_err|vsftpd)")){
        // take the "-" off of it(-nginx -> nginx)
        logFormatFlag = args[i].substring(1);
      }
      else if(args[i].equals("-f")){
          try{
            logFileFlag = args[++i];
          } catch(IndexOutOfBoundsException e){
            argRequiredError("-f");
        }
      }
      
      else if(args[i].equals("-ip-counter")){
        ipCountsFlag = 1;
      }
      else if(args[i].equals("ip-counts")){
        try{
          spcfcIpCountsFlag = args[++i];
        } catch(IndexOutOfBoundsException e){
          argRequiredError("ip-counts");
        }
      }
    }
    
  }
}
