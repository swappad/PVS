package aufgabe1.aufgabe1_b;

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

        //Schaltflächen
        String[] content = {"red","blue","green"};
        JComboBox colorBox = new JComboBox(content);

        //ActionListener
        colorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String input = (String)cb.getSelectedItem();
                if(input.equals("red")){
                    panel.setBackground(Color.RED);
                    colorBox.setBackground(Color.RED);
                }else if(input.equals("green")){
                    panel.setBackground(Color.GREEN);
                    colorBox.setBackground(Color.GREEN);
                }else if(input.equals("blue")){
                    panel.setBackground(Color.BLUE);
                    colorBox.setBackground(Color.BLUE);
                }
            }
        });

        panel.add(colorBox);
        pack();



    }
}
