import javax.swing.*;
import java.awt.*;

public class CalculatorGridLayout extends JFrame{



    public static void main(String args[]){
        CalculatorGridLayout calculator = new CalculatorGridLayout();
        calculator.setVisible(true);

    }

    public CalculatorGridLayout(){

        addWindowListener(new WindowDestroyer());
        Container content = getContentPane();
        JPanel subcontent1 =new JPanel();
        JPanel subcontent2 = new JPanel();
        JPanel subcontent3 = new JPanel();
        JPanel subcontent4 = new JPanel();
        JLabel output = new JLabel("Ergebnis",JLabel.RIGHT);



        content.setLayout(new GridLayout(5,1));
        subcontent1.add(new JButton("+"));
        subcontent1.add(new JButton("1"));
        subcontent1.add(new JButton("2"));
        subcontent1.add(new JButton("3"));

        subcontent2.add(new JButton("-"));
        subcontent2.add(new JButton("4"));
        subcontent2.add(new JButton("5"));
        subcontent2.add(new JButton("6"));

        subcontent3.add(new JButton("X"));
        subcontent3.add(new JButton("7"));
        subcontent3.add(new JButton("8"));
        subcontent3.add(new JButton("9"));

        subcontent4.add(new JButton(":"));
        subcontent4.add(new JButton("0"));
        subcontent4.add(new JButton("="));
        subcontent4.add(new JButton("C"));

        content.add(output);
        content.add(subcontent1);
        content.add(subcontent2);
        content.add(subcontent3);
        content.add(subcontent4);


        pack();






    }

}
