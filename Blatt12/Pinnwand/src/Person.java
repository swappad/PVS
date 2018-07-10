import java.util.ArrayList;
import java.util.List;

public class Person implements  Runnable {
BulletinBoard bulletinBoard = null;
String Name = null;
List<String> messages = new ArrayList<>();

    public Person(BulletinBoard bulletinBoard, String name) {
        this.bulletinBoard = bulletinBoard;
        Name = name;
    }

    public synchronized void run(){
        messages.add("Testnachricht von "+Name) ;
        messages.add(Name +" hat seinen Senf dazu gegeben");
        bulletinBoard.publishToBoard(messages);
    }
}
