import java.util.Scanner;
import src.*;
public class logvcli{
  public void StartShell() {
    ShellManager shellManager = new ShellManager();
    
    
    String command = "";
    Scanner sc = new Scanner(System.in);
    int passes = 0;
    while(!command.equals("exit") ){
      System.out.printf("logv >");
      command = sc.nextLine();
      shellManager.ParseCommand(command);
      passes++;
    }
  }
}