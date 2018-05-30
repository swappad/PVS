package swc.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import swc.ctrl.CtrlGroup;
import swc.data.Group;
import swc.data.SoccerWC;


public class Mainframe extends javax.swing.JFrame {
	private static final long serialVersionUID = 632345753774989L;
	private SoccerWC worldCup;
	
	public Mainframe(SoccerWC toOpen) {

		try {
			//javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		worldCup = toOpen;
	    initComponents();
	    displayDialog();
	}

	/**
	 * Initalizes components, listener, etc.
	 */

	//Grouptabs initialisieren

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
	
		//prepare frame
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		//======== menuFile ========
			menuFile.setText("File");

			//---- menuItemLoadWC ----
			menuItemLoadWC.setText("Load World Cup");
			menuFile.add(menuItemLoadWC);
			
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
			
			//---- menuItemSaveAs ----
			menuItemSaveAs.setText("Save As...");
			menuFile.add(menuItemSaveAs);
			
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
		
		
	
		//======== adding components ========
		
		setJMenuBar(menuBar);
		contentPane.add(tabContainer);
	}

	public void initializeGroupTabs(){
		if(worldCup!= null) {
			tabContainer.removeAll();

			ArrayList<GroupPanel> groupPanelList = new ArrayList<>();
			for (Group group : worldCup.getGroups()) {
				groupPanelList.add(new GroupPanel(group,this));
				tabContainer.addTab(group.getStrGroupName(), groupPanelList.get(groupPanelList.size() - 1));

			}
			tabContainer.addTab("Final",new FinalPanel(worldCup.getFinals(),this));
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
				if(cd.wasSuccessful()){
					
					
					
					// entry point for data visualisation
					
					
					
				}	
			}	
		}else{
			CreateDialog cd = new CreateDialog(this, worldCup);
			cd.setVisible(true);
			if(cd.wasSuccessful()){
				
				
				
				// entry point for data visualisation
				
				
				
			}
			initializeGroupTabs();
		}
	}

	private void menuItemAboutActionPerformed(ActionEvent e) {
		String message = "This Soccer World Cup 2018 managing program was created for " +
				"the practices along\n with the lecture 'Programmierung von Systemen' at Ulm University, summer 2018.\n\n" +
				"Created by Martin Liebrecht and Florian Rapp, administered by Kevin Andrews.\n" +
				"To get information regarding this tool contact kevin.andrews@uni-ulm.de.";
		JOptionPane.showMessageDialog(this, message, "Soccer World Cup 2018", JOptionPane.INFORMATION_MESSAGE);
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
}
