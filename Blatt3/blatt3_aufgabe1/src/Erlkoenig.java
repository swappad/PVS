import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class Erlkoenig {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    public static void main(String args[]){
        JFrame erlWindow = new JFrame();
        erlWindow.setSize(WIDTH,HEIGHT);
        String s = new String();

        JLabel erlLabel = new JLabel("<html>Wer reitet so spät durch Nacht und Wind?<br>Es ist der Vater mit seinem Kind;<br>Er hat den Knaben wohl in dem Arm,<br>Er fasst ihn sicher, er hält ihn warm.");
        erlWindow.getContentPane().add(erlLabel);
        WindowDestroyer destroyListener = new WindowDestroyer();
        erlWindow.addWindowListener(destroyListener);
        erlWindow.setVisible(true);

    }

}
