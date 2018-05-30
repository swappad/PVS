package swc.gui;

import swc.data.Game;
import swc.data.Group;
import swc.data.SoccerWC;
import swc.data.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

public class GroupPanel extends JPanel {
    private Group group;
    private Mainframe mainframe;
    private JPanel panel;

    public GroupPanel(Group group, Mainframe mainframe) {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.group = group;
        this.mainframe = mainframe;
        init();
    }

    public void init() {
        add(new JLabel("Table for " + group.getStrGroupName() + " - Top two teams will advance"));


        JTable teamTable = new JTable(getTeamTableModel());
        JScrollPane teamTablePane = new JScrollPane(teamTable);
        add(teamTablePane);

        add(new JLabel("Matches for " + group.getStrGroupName() ));
        JTable matchTable = new JTable(getMatchTableModel(group.getGames()));
        JScrollPane matchTablePane = new JScrollPane(matchTable);
        add(matchTablePane);
        matchTable.setEnabled(false);
        matchTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e5) {
                matchTableDoubleClicked(mainframe,e5,matchTable,group.getGames());
                teamTable.setModel(getTeamTableModel());
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
    }

    public static void matchTableDoubleClicked(Frame mainframe,MouseEvent mouseEvent, JTable matchTable,Vector<Game> games){
        if(mouseEvent.getClickCount() == 2){
            int row = matchTable.rowAtPoint(mouseEvent.getPoint());
            for (Game game:games) {
                System.out.println(String.valueOf(game.getIntId()));
                if((Integer.parseInt((String)matchTable.getValueAt(row,0))==(game.getIntId()))){

                    try {
                        int[] changes = EditGameDialog.getGameResult(mainframe,game.getTeamG(), game.getTeamH());
                        game.setGoalsG(changes[0]);
                        game.setGoalsH(changes[1]);
                        matchTable.setModel(getMatchTableModel(games));
                        break;
                    }catch(Exception e1){ }
                }
            }

        }

    }
    public DefaultTableModel getTeamTableModel(){

        Vector<Team > sortedTeams =group.getTeams();
        Collections.sort(sortedTeams);
        Vector<String> titles = new Vector<>();
        String[] titlesStr ={"#","Team","Played","Won","Draw","Loss","GF","GA","Difference","Points"};
        titles.addAll(Arrays.asList(titlesStr));
        DefaultTableModel tableModel = new DefaultTableModel(titles,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        for (Team team: sortedTeams) {
            int cnt=1;
            Vector<String> row = new Vector<>();
            row.add(Integer.toString(cnt));
            row.add(team.getName());
            row.add(Integer.toString(team.getPlayed()));
            row.add(Integer.toString(team.getWon()));
            row.add(Integer.toString(team.getDraw()));
            row.add(Integer.toString(team.getLoss()));
            row.add(Integer.toString(team.getGf()));
            row.add(Integer.toString(team.getGa()));
            row.add(Integer.toString(team.getGf()-team.getGa()));
            row.add(Integer.toString(team.getPoints()));

            tableModel.addRow(row);

            cnt++;

        }
        return tableModel;

    }

    public static DefaultTableModel getMatchTableModel(Vector<Game> games){

        Vector<String> titles = new Vector<>();
        String[] titlesStr ={"Match","Date","Time","Venue","","Result",""};
        titles.addAll(Arrays.asList(titlesStr));
        DefaultTableModel tableModel = new DefaultTableModel(titles,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Game game:games) {
            Vector<String> row = new Vector<>();
            row.add(Integer.toString(game.getIntId()));
            row.add(game.getDate());
            row.add(game.getTime());
            row.add(game.getLocation());
            row.add(game.getTeamG().getName());
            row.add(Integer.toString(game.getGoalsG())+"-"+Integer.toString(game.getGoalsH()));
            row.add(game.getTeamH().getName());
            tableModel.addRow(row);
        }
        return tableModel;

    }
    public Group getGroup() {
        return group;
    }

    public Mainframe getMainframe() {
        return mainframe;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setMainframe(Mainframe mainframe) {
        this.mainframe = mainframe;
    }
}
