import java.util.Observable;
import java.util.Observer;

public class User implements Observer {
    private String name;
    private BlackBoard blackBoard;

    public User(String name, BlackBoard blackBoard) {
        this.name = name;
        this.blackBoard = blackBoard;
        this.blackBoard.addObserver(this);
    }

    public void newMessage(String message){
        blackBoard.changeMessage(new Message(message));
    }

    @Override
    public void update(Observable o, Object arg) {
        //Alert in der GUI als Übergabe an die GUI
        //hier nur zu Testzwecken als Kommandozeilenausgabe
        System.out.println("Neue Benachrichtigung: "+ o.toString());


    }
}
