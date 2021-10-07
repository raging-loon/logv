import java.util.Scanner;

import src.*;
public class logvcli{
  public void StartShell() {
    System.out.println("Logv Shell v" + StatusObject.currentVersion);
    ShellManager shellManager = new ShellManager();
    
    
    String command = "";
    Scanner sc = new Scanner(System.in);
    int passes = 0;
    while(!command.equals("exit") ){
      System.out.printf(getPrompt(shellManager));
      command = sc.nextLine();
      if(shellManager.currentLog == null){
        shellManager.ParseCommand(command);
      } else {
        ((LogFormat)shellManager.currentLog).commandParser(shellManager, command);
      }

      passes++;
    }
    sc.close();
  }

  private String getPrompt(ShellManager x){
    String prompt = x.getCurrentLogFile().equals("") ? "logv >" : "logv(" + x.getCurrentLogFile() + ") >";
    return prompt;
  }
}