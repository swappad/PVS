package aufgabe2;

public class Message {
    private String messageText;
    //MessageID vielleicht noch sinnvoll aber fürs einfache Testen nicht notwendig

    public Message(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return messageText;
    }


}
