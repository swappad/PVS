package aufgabe1.aufgabe1_a;

import aufgabe1.WindowDestroyer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Farbenspiel extends JDialog{
    Container container = getContentPane();

    JPanel panel;
    JRadioButton red;
    JRadioButton green;
    JRadioButton blue;

    public Farbenspiel(){
        
        addWindowListener(new WindowDestroyer());

        //GUI-Layout
        panel=new JPanel();

        panel.setLayout(new GridLayout(3,1));
        container.add(panel);
        setSize(new Dimension(100,100));

        //Schaltflächen
        red = new JRadioButton("red");
        green = new JRadioButton("green");
        blue = new JRadioButton("blue");


        //ActionListener
        red.addActionListener(e -> {
            panel.setBackground(Color.GREEN);
            red.setBackground(Color.GREEN);
            blue.setBackground(Color.GREEN);
            green.setBackground(Color.GREEN);
        });
        green.addActionListener(e -> {
            red.setBackground(Color.RED);
            blue.setBackground(Color.RED);
            green.setBackground(Color.RED);
            panel.setBackground(Color.RED);
        });
        blue.addActionListener(e -> {
            panel.setBackground(Color.BLUE);
            red.setBackground(Color.BLUE);
            blue.setBackground(Color.BLUE);
            green.setBackground(Color.BLUE);
        });

        //Gruppierung
        ButtonGroup radiogroup = new ButtonGroup();
        radiogroup.add(red);
        radiogroup.add(blue);
        radiogroup.add(green);

        panel.add(red);
        panel.add(green);
        panel.add(blue);






    }
}
