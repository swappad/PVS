package swc.gui;

import jdk.jfr.Enabled;
import swc.data.Final;
import swc.data.Game;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class FinalPanel extends JPanel {
    private Final afinal;
    private Mainframe mainframe;
    private JPanel panel;

    public FinalPanel(Final afinal, Mainframe mainframe) {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.afinal = afinal;
        this.mainframe = mainframe;
        init();
    }

    private void init(){
        add(new JLabel("Round of 16:"));

        JTable roundOf16Table;
        roundOf16Table = new JTable(GroupPanel.getMatchTableModel(afinal.getRoundOf16()));
        roundOf16Table.setEnabled(false);
        JScrollPane roundOf16TablePane = new JScrollPane(roundOf16Table);
        add(roundOf16TablePane);
        roundOf16Table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GroupPanel.matchTableDoubleClicked(mainframe,e,roundOf16Table,afinal.getRoundOf16());
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });

        add(new JLabel("Quarterfinals"));
        JTable quarterfinalsTable;
        quarterfinalsTable= new JTable(GroupPanel.getMatchTableModel(afinal.getQuarterFinals()));
        quarterfinalsTable.setEnabled(false);
        JScrollPane quarterfinalsTablePane = new JScrollPane(quarterfinalsTable);
        add(quarterfinalsTablePane);
        quarterfinalsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GroupPanel.matchTableDoubleClicked(mainframe,e,quarterfinalsTable,afinal.getQuarterFinals());
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }

        });

        add(new JLabel("Semifinals"));
        JTable semifinalsTable;
        semifinalsTable = new JTable(GroupPanel.getMatchTableModel(afinal.getSemiFinals()));
        semifinalsTable.setEnabled(false);
        JScrollPane semifinalsTablePane = new JScrollPane(semifinalsTable);
        add(semifinalsTablePane);
        semifinalsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GroupPanel.matchTableDoubleClicked(mainframe,e,semifinalsTable,afinal.getSemiFinals());
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }

        });

        add(new JLabel("Match for third Place"));
        JTable thirdPlaceTable;
        Vector<Game> thirdPlaceGame = new Vector<>();
        thirdPlaceGame.add(afinal.getThirdGame());
        thirdPlaceTable = new JTable(GroupPanel.getMatchTableModel(thirdPlaceGame));
        thirdPlaceTable.setEnabled(false);
        JScrollPane thirdPlaceTablePane = new JScrollPane(thirdPlaceTable);
        add(thirdPlaceTablePane);
        thirdPlaceTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GroupPanel.matchTableDoubleClicked(mainframe,e,thirdPlaceTable,thirdPlaceGame);
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }

        });

        add(new JLabel("Final"));
        JTable finalTable;
        Vector<Game> finalGame = new Vector<>();
        finalGame.add(afinal.getFinalGame());
        finalTable = new JTable(GroupPanel.getMatchTableModel(finalGame));
        finalTable.setEnabled(false);
        JScrollPane finalTablePane = new JScrollPane(finalTable);
        add(finalTablePane);
        finalTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GroupPanel.matchTableDoubleClicked(mainframe,e,finalTable,finalGame);
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }

        });

        add(new JLabel("World Cup Winner", JLabel.CENTER));
    }
}
