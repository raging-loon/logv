package src.utils;

  /**
   *    Attack Identifier   
   * @version 1.0
   * @summary Identify common attacks based on regex
   */ 
public class AttackIdentifier {
  /**
   * Look at line and use regex to determine if it is
   * a possible XSS attack
   * @param line
   * @return true||false
   */
  public static boolean XssMatchFound(String line){
    if(line.matches(".*%(25)+3Cscript.*")||
       line.matches(".*%253Cscript%253E.*") ||
       line.matches(".*%3Cscript%3E.*")){ 
      return true;
    } else {
      return false;
    }
  }  
  /**
   * Look at the line and use a regexp to determine if it is
   * a possible SQL Injection attack
   * @implNote Doesn't work right now
   * @param line
   * @return true||false
   */

  public static boolean SqliMatchFound(String line){
    // line = line.toLowerCase();
    // select user, password, etc from sometable where something = 1
    // .*(union)(\+|%20)select(\+|%20).*from.*(where).*
    if(line.matches(".*select.*;--.*") ){
      return true;
    } else {
    return false;
    }
  }
  /**
   * Look for path/directory traversal attacks based on a regex
   * Perhaps soemtime in the future there should be an option
   * for a user supplied file list and the function
   * will look for those files either by themselves or with 
   * path traversal characters
   * @param line
   * @return boolean
   */
  public static boolean PathTraversalMatchFound(String line){
    // still doesn't match ..\/ for some reason...
    if(line.matches(".*(\\.\\./|\\.\\.\\|\\.\\.\\.\\./\\|\\.\\.\\.\\./\\//)+.*"))  return true;
    else return false;
  }
}
