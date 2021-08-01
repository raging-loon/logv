import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import src.*;
import src.http.*;
import src.sql.*;
import src.utils.*;
public class Main /*implements ActionListener*/{
  private static JFrame mainWindow = new JFrame();
  private static JMenuBar mainMenu = new JMenuBar();
  private static void addMenuItems(){
    JMenu fileMenu = new JMenu("File");
    // JMenu helpMenu = new JMenu("Help");
    JMenuItem openFileItem = new JMenuItem("Open");
    fileMenu.add(openFileItem);
    mainMenu.add(fileMenu);
  }

  
  public static void main(String argv[]){
    if(argv.length == 0){
      OptionPrinter op = new OptionPrinter("1.0.0");
      System.exit(0);
    }
    String logFile = argv[0];
    addMenuItems();
    JPanel logPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setSize(400,300);

    // JPanel searchArea = new JPanel(new BorderLayout());
		// JTextField area = new JTextField();
		// searchArea.add(area);
		Apache2Log log = new Apache2Log(logFile);
    log.Parser();
    
		JTable mainTable = new JTable(log.getTableData(),log.TableHeaders){
      @Override
      public boolean isCellEditable(int row, int column){
        return false;
      }
    };
  
		JScrollPane scrollBar = new JScrollPane(mainTable);		
		mainTable.setBounds(0,20,400,300);
		mainTable.setSize(400,300);
    logPanel.setSize(400,300);
    logPanel.add(scrollBar,BorderLayout.NORTH);
    String[] d = {"",""};

		mainTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
        JTable clickedOn = (JTable)e.getSource();
        int rowNo = clickedOn.getSelectedRow(); 
        JTable infoTable = new JTable(log.getInfoTable(rowNo),d){
          @Override
          public boolean isCellEditable(int row, int column) { return false; }
        };

        infoPanel.add(infoTable);
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
			}
		});

		mainWindow.setSize(1000,600);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setTitle("logv - LogViewser");
    // mainWindow.add(scrollBar);
    // logPaneimplementsl.add(scrollBar,BorderLayout.NORTH);
    mainWindow.add(logPanel,BorderLayout.CENTER);
    mainWindow.add(mainMenu,BorderLayout.NORTH);
		mainWindow.add(infoPanel,BorderLayout.SOUTH);
		
    mainWindow.setVisible(true);
		
  }

}
