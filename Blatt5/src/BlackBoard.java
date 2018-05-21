import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BlackBoard extends Observable {

    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public BlackBoard(){

    }

    public void addUser(User user){
        users.add(user);
    }

    public void changeMessage(Message message){
        messages.add(message);
        setChanged();
        notifyObservers(message);
    }

    public List<Message> getMessages() throws NullPointerException {
        if(messages!=null)
        return messages;
        else throw new NullPointerException("no Messages");
    }

    @Override
    public String toString() throws NullPointerException{
        if(messages!=null){
            String out="";
            for (Message message : messages)
                out += message + "\n";
            return out;
        }else throw new NullPointerException("no Messages");

    }
}
