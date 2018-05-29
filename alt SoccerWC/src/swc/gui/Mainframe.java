package swc.gui;

import swc.data.Group;
import swc.data.SoccerWC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Mainframe extends JFrame{
    public SoccerWC soccerWC;
    Container container = getContentPane();
    JFrame owner;
    JPanel frame = new JPanel(new BorderLayout());
    JTabbedPane tabbedPane = new JTabbedPane();
    public Mainframe(SoccerWC toOpen){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        soccerWC = toOpen;
        container.add(frame);
        setSize(new Dimension(500,500));

        JMenuBar menuBar = new JMenuBar();
        frame.add(menuBar,BorderLayout.NORTH);

        frame.add(tabbedPane,BorderLayout.CENTER);

        JMenu file = new JMenu("File");
        menuBar.add(file);
            JMenuItem loadWC = new JMenuItem("Load World Cup");
            file.add(loadWC);
            JMenuItem newWC = new JMenuItem("New World Cup");
            file.add(newWC);
            JMenuItem save = new JMenuItem("Save");
            file.add(save);
            JMenuItem saveAs = new JMenuItem("Save As");
            file.add(saveAs);

        JMenu extra = new JMenu("Extra");
        menuBar.add(extra);
            JMenuItem bettingWC = new JMenuItem("World Cup Betting");
            extra.add(bettingWC);
            JMenuItem loadFromServer = new JMenuItem("Load From Server");
            extra.add(loadFromServer);

        JMenu help = new JMenu("Help");
        menuBar.add(help);
            JMenuItem about = new JMenuItem("About");
            help.add(about);

        newWC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateDialog createDialog = new CreateDialog(owner,soccerWC);
                createDialog.setVisible(true);
                initializeGroupTabs(); //GroupTabs nach neuem WC initialisieren
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog(new JFrame(), "Luca Krueger");
            }
        });


    }

    //Grouptabs initialisieren
    public void initializeGroupTabs(){
        if(soccerWC!= null) {
            tabbedPane.removeAll();

            frame.add(tabbedPane, BorderLayout.CENTER);

            ArrayList<GroupPanel> groupPanelList = new ArrayList<>();
            for (Group group : soccerWC.getGroups()) {
                groupPanelList.add(new GroupPanel());
                tabbedPane.addTab(group.getStrGroupName(), groupPanelList.get(groupPanelList.size() - 1));
                System.out.println(group.getStrGroupName());
            }
        }

    }

}
