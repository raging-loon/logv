import src.CLIManager;
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
  public void argNotSupportedError(String arg){
    System.out.println("LogViewer: unrecognized argument: " + arg);
    System.out.println("See the output of logv -h for a summary of options");
    System.exit(-1);

  }
  public void main(String[] args){
    if(args[0].matches("(-h|--help)")){
      OptionPrinter.Printer("1.0.0");
      System.exit(0);
    }
    // vv DEBUG
    // System.out.println("First arg is: " + args[0]);
    
    CLIManager cliManager = new CLIManager();

    // a nice c++ struct would go nicely here 

    String logFile = "", logFormat = "";
    for(int i = 0; i < args.length; i++){
      if(args[i].matches(".*(apache2|nginx|mysql_err|vsftpd)")){
        // take the "-" off of it(-nginx -> nginx)
        logFormat = args[i].substring(1);
      }
      else if(args[i].equals("-f")){
          try{
            logFile = args[++i];
          } catch(IndexOutOfBoundsException e){
            argRequiredError("-f");
        }
      }
      
      else if(args[i].equals("-ip-counter")){
        cliManager.setIpCountsFlag();
      }
      else if(args[i].equals("ip-counts")){
        try{
          cliManager.setSpecIpCountsFlag(args[++i]);
        } catch(IndexOutOfBoundsException e){
          argRequiredError("ip-counts");
        }
      }
      else if(args[i].equals("--list")){
        cliManager.setDoList();
      }
      else if(args[i].equals("--info")){
        cliManager.infoFlag = 0;
      }
      else if(args[i].equals("--nonsilent")){
        cliManager.infoFlagNonSilent = 1;
      } 
      else if(args[i].equals("--log-print")){
        cliManager.logPrintFlag = 1;
      }
      else {
        argNotSupportedError(args[i]);
      }
      if(!(logFile.equals("") || logFormat.equals(""))){
      
        cliManager.logInfo.put(logFile,logFormat);
        logFile = "";
        logFormat = "";
      }
    }
    cliManager.Start();

  }
}
