package swc.data;

public class Team implements java.lang.Comparable<Team> {
    private String strName;
    private int points;
    private int gf;
    private int ga;
    private int played;
    private int won;
    private int loss;
    private int draw;

    public Team(String name, int points, int gf, int ga, int played, int won, int loss, int draw){


    }

    @Override
    public int compareTo(Team o) {
       if(o.getPoints()!=points){
           return (int)Math.signum(o.getPoints()-points);
       }
       if((o.getGa()-o.getGf()!=(ga-gf)))
           return (int)Math.signum((o.getGa()-o.getGf())-(ga-gf));
       return (int) Math.signum(o.getGa()-ga);
    }

    public void clearTeam(){
        points=0;
        gf=0;
        ga=0;
        played=0;
        won=0;
        loss=0;
        draw=0;
    }


    public String getStrName() {
        return strName;
    }

    public int getPoints() {
        return points;
    }

    public int getGf() {
        return gf;
    }

    public int getGa() {
        return ga;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getLoss() {
        return loss;
    }

    public int getDraw() {
        return draw;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setGf(int gf) {
        this.gf = gf;
    }

    public void setGa(int ga) {
        this.ga = ga;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}

