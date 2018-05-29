package swc.data;

import java.util.Vector;

public class Group {
    private String strGroupName;
    private Vector<Team> teams;
    private Vector<Game> games;
    private boolean isGroupcompleted;

    public Group(String strGroupName){
        teams=new Vector<>();
        games=new Vector<>();
    }

    public void addTeam(Team teamName){
        teams.add(teamName);
    }

    public void addGame(Game newGame){
        games.add(newGame);

    }

    public Vector<Team> getTeams() {
        return teams;
    }

    public Vector<Game> getGames() {
        return games;
    }

    public void setStrGroupName(String strGroupName) {
        this.strGroupName = strGroupName;
    }

    public void setTeams(Vector<Team> teams) {
        this.teams = teams;
    }

    public void setGames(Vector<Game> games) {
        this.games = games;
    }

    public void setGroupcompleted(boolean groupcompleted) {
        isGroupcompleted = groupcompleted;
    }

    public String getStrGroupName() {
        return strGroupName;
    }

    public boolean isGroupcompleted() {
        return isGroupcompleted;
    }


}
