package swc.gui;

import swc.data.SoccerWC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Mainframe extends JFrame{
    private SoccerWC soccerWC;
    Container container = getContentPane();
    JFrame owner;
    JPanel frame = new JPanel(new BorderLayout());

    public Mainframe(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        container.add(frame);
        setSize(new Dimension(500,500));

        JMenuBar menuBar = new JMenuBar();
        frame.add(menuBar,BorderLayout.NORTH);

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
                CreateDialog createDialog = new CreateDialog(null,soccerWC);
                createDialog.setVisible(true);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog(new JFrame(), "Luca Krueger");
            }
        });


    }

}
