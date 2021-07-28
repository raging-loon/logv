package src.utils;

public class AttackIdentifier {
  public static boolean XssMatchFound(String line){
    if(line.matches(".*%3Cscript%3E.*")||
       line.matches(".*%253Cscripr%253E.*")){
      return true;
    } else {
      return false;
    }
  }  


  public static boolean SqliMatchFound(String line){
    // line = line.toLowerCase();
    if(line.matches(".*(select+.*+from).*") ||
       line.matches("\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b")){
      return true;
    }
    return false;
  }
}
