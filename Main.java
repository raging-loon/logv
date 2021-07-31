import javax.swing.*;

import src.*;
import src.http.*;
import src.sql.*;
import src.utils.*;
public class Main{
  public static void main(String argv[]){
    
    JFrame mainWindow = new JFrame();
    
    Apache2Log log = new Apache2Log("tests/logs/apachetest2.log");
    log.Parser();
    // String[][][] table = log.getTableData();
    // for(int i = 0;i < table.length; i++){
    //   for(int j = 0; j<7;j++){
    //     table[0][i][j] = table[0][i][j].toString();
    //   }

    // }
    JTable mainTable = new JTable(log.getTableData(),log.TableHeaders);
    JScrollPane scrollBar = new JScrollPane(mainTable);

    mainTable.setBounds(30,40,200,300);

    mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setTitle("logv - LogViewser");
    mainWindow.add(scrollBar);



    mainWindow.setVisible(true);

  }

}
