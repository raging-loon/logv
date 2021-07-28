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
    if(line.matches(".*%3Cscript%3E.*")||
       line.matches(".*%253Cscripr%253E.*")){
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
    if(line.matches(".*(select+.*+from).*") ||
       line.matches("\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b")){
      return true;
    }
    return false;
  }
}
