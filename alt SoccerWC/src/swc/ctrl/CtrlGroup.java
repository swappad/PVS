package swc.ctrl;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import swc.data.Game;
import swc.data.Group;
import swc.data.SoccerWC;
import swc.data.Team;

public class CtrlGroup {
	
	public static ImageIcon getFlagIcon(String name){	
		if(name.equals("default"))
			return new ImageIcon("default");
		URL imgUrl = null;
		if(name.equals("cup")){
			imgUrl = CtrlGroup.class.getResource("/data/icon/cup.png");
			return new ImageIcon(imgUrl);
		}
		imgUrl = CtrlGroup.class.getResource("/data/icon/" + name + ".gif");
		if(imgUrl == null)
			return new ImageIcon("default");
		return new ImageIcon(imgUrl);
	}

	public static Image getMainWindowIcon(Toolkit tk) {
		URL imgUrl = CtrlGroup.class.getResource("/data/icon/icon1.png");
	    Image image = tk.getImage(imgUrl);
	    return image;
	}

	public static Vector<String> getDefaultTeams() throws IOException{
		Vector<String> teams = new Vector<String>();
		BufferedReader br = null;
		try {
			URL confUrl = CtrlGroup.class.getResource("/data/config/teams.cfg");
			InputStreamReader isR = new InputStreamReader(confUrl.openStream());
			br = new BufferedReader(isR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String value;
		while((value = br.readLine())!= null){
			for (int i = 0; i < 4; i++) {
				value = br.readLine();
				teams.add(value);
			}
		}
		return teams;
	}

	public static void setNewWorldCup(SoccerWC worldCup,
			Vector<DefaultListModel> models, String name) throws NumberFormatException, IOException {
		// World Cup configuration
		worldCup.setName(name);	
		worldCup.setFilename("");
		// Groups configuration
		Vector<Group> groups = worldCup.getGroups();
		groups.clear();
		groups.add(new Group("Group A"));
		groups.add(new Group("Group B"));
		groups.add(new Group("Group C"));
		groups.add(new Group("Group D"));
		groups.add(new Group("Group E"));
		groups.add(new Group("Group F"));
		groups.add(new Group("Group G"));
		groups.add(new Group("Group H"));
		// Getting games.cfg ready
			
		BufferedReader br = null;
		try {
			URL confUrl = CtrlGroup.class.getResource("/data/config/games.cfg");
			InputStreamReader isR = new InputStreamReader(confUrl.openStream());
			br = new BufferedReader(isR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int i = 0;
		DefaultListModel dlm;
		for (Group group : groups) {
			// New Teams
			Vector<Team> teams = group.getTeams();
			teams.clear();
			dlm = models.get(i);
			for (int j = 0; j < 4; j++) {
				teams.add(new Team(dlm.get(j).toString(), 0, 0, 0, 0, 0, 0, 0));
			}
			Vector<Game> games = group.getGames();
			games.clear();
			for (int j = 0; j < 6; j++) {
				games.add(new Game(
						Integer.valueOf(br.readLine()).intValue(),
						br.readLine(),
						br.readLine(),
						br.readLine(),
						teams.get(Integer.valueOf(br.readLine()).intValue() -1),
						teams.get(Integer.valueOf(br.readLine()).intValue() -1),
						0,
						0,
						false
						));
			}
			i++;
		}
		//CtrlFinals.createDefaultFinals(worldCup);
	}	
}
