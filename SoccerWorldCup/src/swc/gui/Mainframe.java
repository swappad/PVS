package swc.gui;
import org.apache.commons.csv.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.swing.*;

import swc.ctrl.CtrlFinals;
import swc.ctrl.CtrlGroup;
import swc.ctrl.Print;
import swc.data.Game;
import swc.data.Group;
import swc.data.SoccerWC;
import swc.data.Team;


public class Mainframe extends javax.swing.JFrame {
	private static final long serialVersionUID = 632345753774989L;
	private ImageIcon [] icons;
	private SoccerWC worldCup;
	private FinalsPanel finals;
	
	public Mainframe(SoccerWC toOpen) {

		try {
			java.util.Locale.setDefault(Locale.ENGLISH);
			UIManager.put("OptionPane.yesButtonText","Yes");
			UIManager.put("OptionPane.noButtonText","No");
			UIManager.put("OptionPane.cancelButtonText", "Cancel");
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		worldCup = toOpen;
	    initComponents();
	    displayDialog();
	}

	/**
	 * Initializes components, listener, etc.
	 */
	private void initComponents() {	
		
		//initialize Components
		menuBar = new JMenuBar();
		menuFile = new JMenu();
		menuExtra = new JMenu();
		menuHelp = new JMenu();
		menuItemLoadWCfromServer = new JMenuItem();
		menuItemAbout = new JMenuItem();
		menuItemWCBetting = new JMenuItem();
		menuItemLoadWC = new JMenuItem();
		menuItemNewWC = new JMenuItem();
		menuItemSave = new JMenuItem();
		menuItemSaveAs = new JMenuItem();
		menuItemExit = new JMenuItem();
		tabContainer = new JTabbedPane();
		toolBar = new JToolBar();
	
		//prepare frame
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		//======== menuFile ========
			menuFile.setText("File");

			//---- menuItemLoadWC ----
			menuItemLoadWC.setText("Load World Cup");
			menuFile.add(menuItemLoadWC);
			menuItemLoadWC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					menuItemLoadWCActionPerformed(e);
				}
			});
			
			//---- menuItemNewWC ----
			menuItemNewWC.setText("New World Cup");
			menuItemNewWC.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuItemNewWCActionPerformed(e);
				}
			});
			menuFile.add(menuItemNewWC);
			
			menuFile.addSeparator();
			
			//---- menuItemSave ----
			menuItemSave.setText("Save");
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuItemSaveAsActionPerformed(e);
			}
		});
			
			//---- menuItemSaveAs ----
			menuItemSaveAs.setText("Save As...");
			menuFile.add(menuItemSaveAs);
			menuItemSaveAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					menuItemSaveAsActionPerformed(e);
				}
			});
			menuFile.addSeparator();
			
			//---- menuItemExit ----
			menuItemExit.setText("Exit");
			menuItemExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuItemExitActionPerformed(e);
				}
			});
			menuFile.add(menuItemExit);
			
		menuBar.add(menuFile);
		
		
		//======== menuExtra ========
			menuExtra.setText("Extra");
	
			//---- menuItemWCBetting ----
			menuItemWCBetting.setText("World Cup betting");
			menuExtra.add(menuItemWCBetting);
			
			//---- menuItemLoadWCfromServer ----
			menuItemLoadWCfromServer.setText("Load from sever...");
			menuExtra.add(menuItemLoadWCfromServer);
		
		menuBar.add(menuExtra);	
		
		
		//======== menuHelp ========
			menuHelp.setText("Help");
	
			//---- menuItemAbout ----
			menuItemAbout.setText("About");
			menuItemAbout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuItemAboutActionPerformed(e);
				}
			});
			menuHelp.add(menuItemAbout);
			
		menuBar.add(menuHelp);	
		
		//======== Toolbar ========
		
		toolBar.setLayout(new BoxLayout(toolBar,BoxLayout.LINE_AXIS));

		toolBar.add(new JLabel(" World Cup:   "));
		wcName = new JLabel();
		wcName.setFont(new Font("Arial", Font.BOLD, 13));
		toolBar.add(wcName);
		toolBar.add(new JLabel("    Status:   "));
		wcStatus = new JLabel();
		wcStatus.setFont(new Font("Arial", Font.BOLD, 11));
		toolBar.add(wcStatus);
				
		toolBar.add(Box.createHorizontalGlue());
		
		icons = CtrlGroup.getDefaultIcons();

		buttonOpen = new JButton(icons[0]);
		buttonOpen.setToolTipText("Open World Cup");
	
		buttonNew = new JButton(icons[1]);
		buttonNew.setToolTipText("Create New World Cup");
		buttonNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				menuItemNewWCActionPerformed(e);
			}
		});
		//buttonEdit = new JButton(icons[2]);
		//buttonEdit.setToolTipText("Edit current Group");
		buttonSave = new JButton(icons[3]);
		buttonSave.setToolTipText("Save World Cup");
	
		buttonPrint = new JButton(icons[4]);
		buttonPrint.setToolTipText("Print current Group");
		buttonPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				buttonPrintActionPerformed(e);
			}
		});
		toolBar.add(buttonOpen);
		toolBar.add(buttonNew);
		//toolBar.add(buttonEdit);
		toolBar.add(buttonSave);
		toolBar.add(buttonPrint);
		toolBar.setVisible(false);
		toolBar.setFloatable(false);
	
		//======== adding components ========
		
		setJMenuBar(menuBar);
		contentPane.add(toolBar, BorderLayout.PAGE_START);
		contentPane.add(tabContainer);
	}

	protected void buttonPrintActionPerformed(ActionEvent e) {
		try {
	    	PrinterJob objPrinterJob = PrinterJob.getPrinterJob();
	    	if (objPrinterJob.printDialog()) {
	            PageFormat objPageFormat = objPrinterJob.pageDialog(objPrinterJob.defaultPage());

	            Component toPrint = tabContainer.getSelectedComponent();
	            if(toPrint instanceof JScrollPane)
	            	toPrint = ((JScrollPane) toPrint).getViewport().getComponent(0);
	            objPrinterJob.setPrintable(new Print(toPrint), objPageFormat);
	            objPrinterJob.print();        
	        }
	    } catch (PrinterException objException) {
	    	objException.printStackTrace();
	    }
	}

	protected void menuItemExitActionPerformed(ActionEvent e) {
		this.dispose();
		System.exit(0);
	}

	protected void menuItemNewWCActionPerformed(ActionEvent e) {
		if(!(worldCup.getName() == null)){
			int ok = JOptionPane.showConfirmDialog(null,"Open new World Cup? Unsaved changes will be lost!", 
                    "Confirmation required", 
                    JOptionPane.YES_NO_OPTION);
			if (ok == JOptionPane.YES_OPTION){
				CreateDialog cd = new CreateDialog(this, worldCup);
				cd.setVisible(true);
				if(cd.wasSuccessful())
					initializeTabContainer();
			}	
		}else{
			CreateDialog cd = new CreateDialog(this, worldCup);
			cd.setVisible(true);
			if(cd.wasSuccessful())
				initializeTabContainer();
		}
	}

	private void menuItemAboutActionPerformed(ActionEvent e) {
		String message = "This Soccer World Cup 2018 managing program was created for " +
				"the practices along\n with the lecture 'Programmierung von Systemen' at Ulm University, summer 2018.\n\n" +
				"Created by Martin Liebrecht and Florian Rapp, administered by Kevin Andrews.\n" +
				"To get information regarding this tool contact kevin.andrews@uni-ulm.de.";
		JOptionPane.showMessageDialog(this, message, "Soccer World Cup 2018", JOptionPane.INFORMATION_MESSAGE);
	}

	private void menuItemSaveAsActionPerformed(ActionEvent e){
		/* An dieser Stelle muss org.apache.commons.csv.* korrekt eingebunden sein!!!
		Ich habe alles in IntelliJ eingebunden und hoffe, dass das beim Eclipse Export vermerkt wird.
		Ansonsten liegt die Library auch im Projektordner.
		 */



		if(worldCup==null){
			JOptionPane.showMessageDialog(this, "No WC loaded!", "Alert", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		File file;
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();

			try {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
				CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT);

				for (Group group : worldCup.getGroups()) {
					//Gruppenname
					csvPrinter.printRecord(worldCup.getName().toString());
					csvPrinter.printRecord(group.getStrGroupName());

					//Scoreboard
					//Teams:
					csvPrinter.printRecord("Teams:");
					csvPrinter.printRecord("Position", "Team", "Played", "Won", "Draw", "Loss", "GF", "GA", "Points");
					int count = 0;
					for (Team team : group.getTeams()) {
						csvPrinter.printRecord(count, team.getName(), team.getPlayed(), team.getWon(), team.getDraw(), team.getLoss(), team.getGf(), team.getGa(), team.getPoints());
						count++;
					}
					//Games:
					csvPrinter.printRecord("Games:");
					csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
					for (Game game : group.getGames()) {
						csvPrinter.printRecord(game.getIntId(), game.getDate(), game.getTime(), game.getLocation(), game.getTeamH().getName(), game.getTeamG().getName(),
								game.getGoalsH(), game.getGoalsG(), Boolean.toString(game.isPlayed()));
					}


				}
				//Finals:
				csvPrinter.print("Finals:");
				//Round of 16:
				csvPrinter.print("Round of 16:");
				csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
				for (Game game : worldCup.getFinals().getRoundOf16()) {
					csvPrinter.printRecord(game.getIntId(), game.getDate(), game.getTime(), game.getLocation(), game.getTeamH().getName(), game.getTeamG().getName(),
							game.getGoalsH(), game.getGoalsG(), Boolean.toString(game.isPlayed()));
				}

				//Quarterfinals
				csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
				for (Game game : worldCup.getFinals().getQuarterFinals()) {
					csvPrinter.printRecord(game.getIntId(), game.getDate(), game.getTime(), game.getLocation(), game.getTeamH().getName(), game.getTeamG().getName(),
							game.getGoalsH(), game.getGoalsG(), Boolean.toString(game.isPlayed()));
				}

				//Semifinals:
				csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
				for (Game game : worldCup.getFinals().getSemiFinals()) {
					csvPrinter.printRecord(game.getIntId(), game.getDate(), game.getTime(), game.getLocation(), game.getTeamH().getName(), game.getTeamG().getName(),
							game.getGoalsH(), game.getGoalsG(), Boolean.toString(game.isPlayed()));
				}

				//Match for 3rd Place:
				csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
				Game gamethird = worldCup.getFinals().getThirdGame();
				csvPrinter.printRecord(gamethird.getIntId(), gamethird.getDate(), gamethird.getTime(), gamethird.getLocation(), gamethird.getTeamH().getName(), gamethird.getTeamG().getName(),
						gamethird.getGoalsH(), gamethird.getGoalsG(), Boolean.toString(gamethird.isPlayed()));

				//Final:
				csvPrinter.printRecord("Gameid", "Date", "Time", "Venue", "Home Team", "Guest Team", "GH", "GG", "IsPlayed");
				Game gamefinal = worldCup.getFinals().getFinalGame();
				csvPrinter.printRecord(gamefinal.getIntId(), gamefinal.getDate(), gamefinal.getTime(), gamefinal.getLocation(), gamefinal.getTeamH().getName(), gamefinal.getTeamG().getName(),
						gamefinal.getGoalsH(), gamefinal.getGoalsG(), Boolean.toString(gamefinal.isPlayed()));

				csvPrinter.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void menuItemLoadWCActionPerformed(ActionEvent e){

	}

	private void initializeTabContainer() {
		createHeadLine();	
		toolBar.setVisible(true);
		tabContainer.removeAll();
		for (int i = 0; i < 8; i++) {
			tabContainer.addTab(worldCup.getGroups().get(i).getStrGroupName(), new GroupPanel(this, worldCup.getGroups().get(i)));
		}
		JScrollPane fsp = new JScrollPane();
		JScrollBar jsb = new JScrollBar();
		jsb.setAutoscrolls(false);
		jsb.setUnitIncrement(15);
		jsb.setBlockIncrement(15);
		fsp.setVerticalScrollBar(jsb);
		finals = new FinalsPanel(this, worldCup.getFinals(), fsp);
		finals.setPreferredSize(new Dimension(600, 760));
		fsp.setViewportView(finals);
		tabContainer.addTab("Finals", fsp);
	}

	public void createHeadLine() {
		wcName.setText(worldCup.getName());
		wcStatus.setText(CtrlFinals.getStatus(worldCup));
	}

	/**
	 * Configures the frame setup an displays.
	 */
	private void displayDialog() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(690, 600);
		setResizable(false);
		Dimension objScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int intTop              = (objScreenSize.height - this.getHeight()) / 2;
	    int intLeft             = (objScreenSize.width - this.getWidth()) / 2;    
	         
	    setLocation(intLeft, intTop);
	    setTitle("Soccer World Cup 2018");
	    Toolkit tk = getToolkit();
	    setIconImage(CtrlGroup.getMainWindowIcon(tk));
	}

	public void callFinalCalucalion() {
		CtrlFinals.calculateFinals(worldCup);
		finals.drawMatches();
	}

	/**
	 * GUI Elements.
	 */
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuExtra;
	private JMenu menuHelp;
	private JMenuItem menuItemWCBetting;
	private JMenuItem menuItemLoadWCfromServer;
	private JMenuItem menuItemAbout;
	private JMenuItem menuItemLoadWC;
	private JMenuItem menuItemNewWC;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemSaveAs;
	private JMenuItem menuItemExit;
	private JTabbedPane tabContainer;
	private JToolBar toolBar;
	private JLabel wcName;
	private JLabel wcStatus;
	private JButton buttonOpen;
	private JButton buttonNew;
	private JButton buttonSave;
	private JButton buttonPrint;
}
