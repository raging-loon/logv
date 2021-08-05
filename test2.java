
import src.misc.Vsftpd;

public class test2 {
  // VSFTPD TEST
  public static void main(String[] args) {
    Vsftpd log = new Vsftpd("tests/vsftpd.log");
    log.Parser();
    log.logPrint();
    
  }  
}
