import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import src.*;
import src.http.*;
import src.misc.Vsftpd;
import src.sql.MySqlErrorLog;
import src.utils.*;
public class logv /*implements ActionListener*/{
  

  
  public static void main(String argv[]){
     JFrame mainWindow = new JFrame();
     JMenuBar mainMenu = new JMenuBar();
    String currentOpenFile = "";
    JMenu fileMenu = new JMenu("File");
    JMenu info  = new JMenu("Info");
    // JMenu helpMenu = new JMenu("Help");
    JMenuItem openFileItem = new JMenuItem("Open");
    JMenuItem fileInfo = new JMenuItem("File");
    fileInfo.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        JPopupMenu infoMenu = new JPopupMenu("File Info");
        JTextPane text = new JTextPane();
        infoMenu.add(text);

        mainWindow.add(info);
        infoMenu.show(mainWindow, e.getX(),e.getY());
      }
    });
    fileMenu.add(openFileItem);
    info.add(fileInfo);
    mainMenu.add(fileMenu);
    mainMenu.add(info);
    if(argv.length == 0){
      OptionPrinter op = new OptionPrinter("1.0.0");
      System.exit(0);
    }
    String logFile = argv[0];
    JPanel logPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setSize(400,300);
    StatusObject settings = new StatusObject();
    currentOpenFile = logFile;
    // JPanel searchArea = new JPanel(new BorderLayout());
		// JTextField area = new JTextField();logHeaders
		// searchArea.add(area);
		Vsftpd log = new Vsftpd(logFile);
    log.Parser();
    
		JTable mainTable = new JTable(log.getTableData(),log.getTableHeaders()){
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
        JTable infoTable = new JTable(log.getInfoTable(rowNo,settings),d){
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
