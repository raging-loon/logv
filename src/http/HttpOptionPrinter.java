package src.http;
import src.*;
public class HttpOptionPrinter {
  public static void printApache2XSSHelp(){
    System.out.println("XSS Detection Apache2/Nginx");
    System.out.println("Options");
    System.out.println("\t-h Display this message");
    System.out.println("\t-xml <filename> Save results to XML file");
    System.out.println("\t-html <filename> Save results to an HTML file(looking for help here, contact me at jgnovak1@protonmail.com)");
    System.out.println("\t-p Print results(very ugly)");
  }  
  
  public static void printApache2PathTraversalHelp(){
    System.out.println("Path Traversal Detection, Apache2/Nginx");
    System.out.println("Options");
    System.out.println("\t-h Display this message");
    System.out.println("\t-xml|-html <filename> Save results to an XML or HTML file");
    System.out.println("\t-p Print results(very ugly)");
  }
  
  public static void printUACommandHelp(){
    System.out.println("User Agent Detection, Apache2/Nginx");
    System.out.println("Options");
    System.out.println("\t-h Displays this message");
    System.out.println("\t-xml|-html <filename> Save results to an XML or HTML file");
    
  }
  
  public static void argumentRequired(String commandBase, String arg){
    System.out.println(commandBase + ": " + arg + " requires an argument");
  }
  public static void unknownArg(String commandBase, String arg){
    System.out.println(commandBase + ": unknown argument: " + arg);
  }

  public static void basicApache2CommandHelp(){
    System.out.println("Apache2 Log Object, Logv v" + StatusObject.currentVersion);
    System.out.println("Informatino for the Apache2 log, -apache2, -nginx");
    System.out.println("Options");
    System.out.println("\thelp - display this message");
    System.out.println("\tshow <item> - display information about various things");
    System.out.println("\txss - get information about possible cross site scripting attacks");
    System.out.println("\tpt|pathtrav - get information about possible path traversal attacks");
    System.out.println("\tua|user-agent - look for suspicious user agents, such as \"Mozilla/5.0 (compatible; Nmap Scripting Engine; https://nmap.org/book/nse.html)\"");
    System.out.println("Not yet supported commands");
    System.out.println("\tsqli - look for possible SQL Injection attacks");
    System.out.println("\tscript - look for possible scripted attacks, such as 70 requests in one second from one IP Address");
  }

  public static void printSQLiApache2Help(){
    System.out.println("SQL Injection Detection, Apache2/Nginx");
    System.out.println("Options");
    System.out.println("\t-h Displays this message");
    System.out.println("\t-xml|-html <filename> save results to an XML or HTML file");
    System.out.println("\t-f|--flags Show ip addresses flaged with possible sqli attempts");
  }
}
