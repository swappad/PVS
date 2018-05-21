package aufgabe2;

import java.util.Observable;
import java.util.Observer;

public class RunGui implements Observer{
    public static void main(String[] args){
        RunGui blackboardGui = new RunGui();
    }

    private BlackBoard blackBoard = new BlackBoard();

    public RunGui() {

        //Beliebige Gui Implementierung
        System.out.println("Start Test");
        blackBoard.addObserver(this);

        //Hier nur eine Kommandozeilenausgabe mit den geforderten Testschritten
        User testUser = new User("Testuser1", blackBoard);
        blackBoard.addUser(testUser);
        testUser.newMessage("Hello World");
        testUser.newMessage("Still alive");

        System.out.println("Ende Test");
    }

    @Override
    public void update(Observable o, Object arg) {
        //Nur zum Testen. Sonst Ausgabe bspw. in Listbox.
        String out ="";
        try {
            out = blackBoard.toString();
        }catch(NullPointerException e){
            out = "Keine Meldungen vorhanden";
        }
        System.out.println("aktuelles Brett:");
        System.out.println(out);
    }
}
