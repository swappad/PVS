package aufgabe1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowDestroyer extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        System.out.println("gestoppt");
        System.exit(0);
    }
    public void windowOpened(WindowEvent e){
        System.out.println("gestartet");
    }

}
