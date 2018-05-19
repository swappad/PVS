import javax.swing.*;
import java.awt.*;

public class CalculatorBorderLayout extends JFrame {

    public static void main(String args[]){
        CalculatorBorderLayout calculator = new CalculatorBorderLayout();
        calculator.setVisible(true);

    }

    public CalculatorBorderLayout(){
        addWindowListener(new WindowDestroyer());



        JLabel output = new JLabel("Ergebnis",JLabel.RIGHT);
        JPanel subcontentSouth = new JPanel();
        JPanel contentWest = new JPanel();
        contentWest.setLayout(new GridLayout(4,1));
        JPanel contentEast = new JPanel();
        contentEast.setLayout(new GridLayout(4,1));
        JPanel contentCenter = new JPanel();
        contentCenter.setLayout(new GridLayout(4,2));


        contentWest.add(new JButton("+"));
        contentCenter.add(new JButton("1"));
        contentCenter.add(new JButton("2"));
        contentEast.add(new JButton("3"));

        contentWest.add(new JButton("-"));
        contentCenter.add(new JButton("4"));
        contentCenter.add(new JButton("5"));
        contentEast.add(new JButton("6"));

        contentWest.add(new JButton("X"));
        contentCenter.add(new JButton("7"));
        contentCenter.add(new JButton("8"));
        contentEast.add(new JButton("9"));

        contentWest.add(new JButton(":"));
        contentCenter.add(new JButton("0"));
        contentCenter.add(new JButton("="));
        contentEast.add(new JButton("C"));


        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(output,BorderLayout.NORTH);
        content.add(contentCenter,BorderLayout.CENTER);
        content.add(contentEast,BorderLayout.EAST);
        content.add(contentWest,BorderLayout.WEST);


        pack();

    }
}
