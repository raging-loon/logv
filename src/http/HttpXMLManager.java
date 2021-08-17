package src.http;
import java.io.*;
import javax.swing.JOptionPane;
import src.*;
import src.http.Apache2Log;
public class HttpXMLManager {
  public static final int HTMLFORMAT = 12;
  public static final int XMLFORMAT = 13;
  private Apache2Log log;
  private String logFile = "";
  public HttpXMLManager(Apache2Log log, String logFile){
    this.log = log;
    this.logFile = logFile;
  }
  public void saveXSSReport(String filename,int format){
    // System.out.println(StatusObject.headless);

    try{
      File file = new File(filename);
      if(file.createNewFile()){
        // if we're in the shell
        if(StatusObject.headless){
          System.out.println("File " + filename + ": successfully created");
        } 
        else {
          JOptionPane jo = new JOptionPane();
          jo.showMessageDialog(StatusObject.mWindow, 
                               "File:" + filename + ": successfully created", 
                               "Info", JOptionPane.INFORMATION_MESSAGE);
        }
      } else {
        System.out.println("File " + filename + " already exists.");
        return;
      }
      if(format == XMLFORMAT){
        FileWriter fw = new FileWriter(filename);
        fw.write("<xml>\n\t<xss-results>\n\t\t<log-file>" + log.getLogFile() + 
                 "</log-file>\n\t\t<logv-version>" + StatusObject.currentVersion +
                 "</logv-version>\n");
        for(int i = 0; i < log.HTTPIpAddresses.size();i++){
          if(log.XssDetector(i)){
            fw.write("\t\t\t<ip-addr>" + log.HTTPIpAddresses.get(i) + "</ip-addr>\n");
            fw.write("\t\t\t<time>" + log.HTTPRequestTime.get(i) + "</time>\n");
            fw.write("\t\t\t<request>" + log.HTTPRequests.get(i) + "</request>\n");            
          }
        }
        fw.write("\t</xss-results>\n</xml>");
        fw.close(); 
      } else if(format == HTMLFORMAT){
        FileWriter fw = new FileWriter(filename);
        fw.write(getBasicHTMLLayout("XSS Detection"));
        fw.write("\n<hr>\n<table border=\"1\" style=\"width: 100%; white-space: nowrap; table-layout: fixed;\" >" +
                  "\n\t<tr>"+
                  "\n\t\t<th class=\"trheadermain\">Ip Address</th>"+
                  "\n\t\t<th class=\"trheadermain\">Time</th>"+
                  "\n\t\t<th class=\"trheadermain\">Request</th>"+
                  "\n\t</tr>");
        for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
          if(log.XssDetector(i)){
            fw.write("\n\t<tr>\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPIpAddresses.get(i) + "</td>" +
                    "\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPRequestTime.get(i) + "</td>" +
                    "\n\t\t<td style=\"white-space: nowrap; text-overflow:ellipsis; overflow: hidden; max-width:1px;\">" + log.HTTPRequests.get(i) + "</td>\n\t</tr>");
          }
        }
        fw.write("</table></body></html>");
        fw.close();
      }

    } catch(IOException e){
      if(StatusObject.headless == true){
        System.out.println("File: " + filename + " already exists");
      } else {
        JOptionPane jo = new JOptionPane();
        jo.showMessageDialog(
          StatusObject.mWindow, 
          "File:" + filename + " already exists",
           "Error: HttpXMLMangager", 
           JOptionPane.ERROR_MESSAGE
        );
      } 
    } catch(Exception e){
      if(StatusObject.headless){
        System.out.println("Failure creating file: " + filename);
      } else {
        JOptionPane jo = new JOptionPane();
        jo.showMessageDialog(
          StatusObject.mWindow, 
          "Failure creating file: " + filename,
           "Error: HttpXMLMangager", 
           JOptionPane.ERROR_MESSAGE
        );
      }
    }

  }

  public void savePathTraversalReport(String filename, int format){
    try{
      File file = new File(filename);
      if(file.createNewFile()){
        if(StatusObject.headless){
          System.out.println("File " + filename + " successfully created");
        } else {
          JOptionPane jo = new JOptionPane();
          jo.showMessageDialog(
            StatusObject.mWindow, 
            "File " + filename + " successfully created", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        }
      } else {
        if(StatusObject.headless){
          System.out.println("File " + filename + " already exists");
          return;
        } else {
          JOptionPane jo = new JOptionPane();
          jo.showMessageDialog(
            StatusObject.mWindow, 
            "File " + filename + " already exists", 
            "Failure: HttpXMLManager", 
            JOptionPane.ERROR_MESSAGE);
            return;
        }
      }

      if(format == XMLFORMAT){
        FileWriter fw = new FileWriter(file);
        fw.write("<xml>\n\t<path-traversal-results>\n\t\t<log-file>" + log.getLogFile() +
                 "</log-file>\n\t\t<logv-version>" + StatusObject.currentVersion +
                 "</logv-version>\n");
        for(int i = 0 ; i < log.HTTPIpAddresses.size(); i++){
          if(log.PathTraversalDetector(i)){
            fw.write("\t\t\t<ip-addr>" + log.HTTPIpAddresses.get(i) + "</ip-addr>\n");
            fw.write("\t\t\t<time>" + log.HTTPRequestTime.get(i) + "</time>\n");
            fw.write("\t\t\t<request>" + log.HTTPRequests.get(i) + "</request>\n");
          }
        }
        fw.write("\t</xss-results>\n</xml>");
        fw.close();
      }
      else if(format == HTMLFORMAT){
        FileWriter fw = new FileWriter(file);
        fw.write(getBasicHTMLLayout("Path Traversal Detection"));
        fw.write("\n<hr>\n<table border=\"1\" style=\"width: 100%; white-space: nowrap; table-layout: fixed;\" >" +
        "\n\t<tr>"+
        "\n\t\t<th class=\"trheadermain\">Ip Address</th>"+
        "\n\t\t<th class=\"trheadermain\">Time</th>"+
        "\n\t\t<th class=\"trheadermain\">Request</th>"+
        "\n\t</tr>");
        for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
          if(log.PathTraversalDetector(i)){
            fw.write("\n\t<tr>\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPIpAddresses.get(i) + "</td>" +
                    "\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPRequestTime.get(i) + "</td>" +
                    "\n\t\t<td style=\"white-space: nowrap; text-overflow:ellipsis; overflow: hidden; max-width:1px;\">" + log.HTTPRequests.get(i) + "</td>\n\t</tr>");
          }
        }

        fw.write("</table></body></html>");
        fw.close();
      }
    } catch(Exception e){

    }
  }

  public void saveUAReport(String filename, int format){
    try{
      File file = new File(filename);
      if(file.createNewFile()){
        System.out.println("File " + filename + " was successfully created");
      } else {
        return;
      }
      if(format == XMLFORMAT){
        FileWriter fw = new FileWriter(file);
        fw.write("<xml>\n\t<user-agent-results>\n\t\t<log-file>" + log.getLogFile() +
                 "</log-file>\n\t\t<logv-version>" + StatusObject.currentVersion +
                 "</logv-version>\n");
        for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
          if(log.nmapScanSearch(i)){
            fw.write("\t\t\t<ip-addr>" + log.HTTPIpAddresses.get(i) + "</ip-addr>\n");
            fw.write("\t\t\t<user-agent>" + log.HTTPUserAgents.get(i) + "</user-agent>\n");
            fw.write("\t\t\t<time>" + log.HTTPRequestTime.get(i) + "</time>\n");
          }
        }
        fw.write("\t</user-agent-results>\n</xml>");
        fw.close();
      } 
      else if(format == HTMLFORMAT){
        FileWriter fw = new FileWriter(file);
        fw.write(getBasicHTMLLayout("User Agent Detection"));
        
        fw.write("\n<hr>\n<table border=\"1\" style=\"width: 100%; white-space: nowrap; table-layout: fixed;\" >" +
        "\n\t<tr>"+
        "\n\t\t<th class=\"trheadermain\">Ip Address</th>"+
        "\n\t\t<th class=\"trheadermain\">Time</th>"+
        "\n\t\t<th class=\"trheadermain\">User Agent</th>"+
        "\n\t</tr>");
        for(int i = 0; i < log.HTTPIpAddresses.size(); i++){
          if(log.nmapScanSearch(i)){
            fw.write("\n\t<tr>\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPIpAddresses.get(i) + "</td>" +
            "\n\t\t<td style=\"white-space: nowrap;\">" + log.HTTPRequestTime.get(i) + "</td>" +
            "\n\t\t<td style=\"white-space: nowrap; text-overflow:ellipsis; overflow: hidden; max-width:1px;\">" + log.HTTPUserAgents.get(i) + "</td>\n\t</tr>");
          }
        }

        fw.write("</table></body></html>");
        fw.close();
      }

    } catch(IOException e){

    }
  }

  public String getBasicHTMLLayout(String title){
    String string = "<!DOCTYPE html>\n\t" +
    "<head>\n\t\t<title>"+ title + "</title>\n" +
    "\t<style>\n\ttable,th,td{" +
    "\n\tborder: 1px, solid black;\n}"+
    "\n</style>\n</head>" +
    "\t<body>" + 
    "\t\t\n<center><h1>Logv "+ title + "</h1></center>\n<hr>" +
    "\n<h4>File: " + log.getLogFile() + "<br>" + 
    "\n    Format: Apache2<br>" +
    "\n    Total Entries: " + log.HTTPIpAddresses.size() + "<br>" + 
    "\n    Logv Version: " + StatusObject.currentVersion + "<br></h4>";
    return string;
  }
}
