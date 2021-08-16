package src.http;

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
}
