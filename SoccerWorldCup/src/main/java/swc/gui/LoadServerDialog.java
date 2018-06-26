package swc.gui;


import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadServerDialog extends JDialog{
    Frame owner;
    String url;
    boolean successfull=false;

    public LoadServerDialog(Frame owner) {
        this.owner = owner;
        init();
    }

    public void init(){
        String urltmp = (String) JOptionPane.showInputDialog(owner,"Url to file","Load file from Server",JOptionPane.YES_NO_CANCEL_OPTION);
        successfull= (urltmp==null);
    }
}
