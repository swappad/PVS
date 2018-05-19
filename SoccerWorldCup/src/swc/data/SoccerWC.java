package swc.data;

import java.util.Vector;

public class SoccerWC {
    private Vector<Group> groups = new Vector<>();
    private Final finals;
    private String name;
    private String filename;

    public SoccerWC(String filename, String wcName) {
        this.name = wcName;
        this.filename = filename;

    }

    public SoccerWC(String wcName) {
        this.name = wcName;
    }

    public SoccerWC() {
    }

    public void addGroup(Group toAdd){
        groups.add(toAdd);
    }

    public Vector<Group> getGroups() {
        return groups;
    }

    public Final getFinals() {
        return finals;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public void setGroups(Vector<Group> groups) {
        this.groups = groups;
    }

    public void setFinals(Final finals) {
        this.finals = finals;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
