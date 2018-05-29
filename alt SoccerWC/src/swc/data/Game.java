package swc.data;

public class Game {
    private int intId;
    private String time;
    private String date;
    private String location;
    private int goalsH;
    private int goalsG;
    private boolean isplayed;
    private Team teamH;
    private Team teamG;

    public Game(){

    }
    public Game(int intId, String date, String time, String location, Team home, Team guest, int goalsH, int goalsG, boolean isplayed) {
        this.intId = intId;
        this.time = time;
        this.date = date;
        this.location = location;
        this.goalsH = goalsH;
        this.goalsG = goalsG;
        this.isplayed = isplayed;
        this.teamH = home;
        this.teamG = guest;
    }

    public int getIntId() {
        return intId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getGoalsH() {
        return goalsH;
    }

    public int getGoalsG() {
        return goalsG;
    }

    public boolean isIsplayed() {
        return isplayed;
    }

    public Team getTeamH() {
        return teamH;
    }

    public Team getTeamG() {
        return teamG;
    }

    public void setIntId(int intId) {
        this.intId = intId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGoalsH(int goalsH) {
        this.goalsH = goalsH;
    }

    public void setGoalsG(int goalsG) {
        this.goalsG = goalsG;
    }

    public void setPlayed(boolean isplayed) {
        this.isplayed = isplayed;
    }

    public void setTeamH(Team teamH) {
        this.teamH = teamH;
    }

    public void setTeamG(Team teamG) {
        this.teamG = teamG;
    }
}
