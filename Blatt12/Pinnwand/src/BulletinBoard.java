import java.util.List;

public class BulletinBoard {
    public synchronized void publishToBoard(List<String> messages){
        for (String message:messages) {
            System.out.println(message);
        }
    }

}
