import javax.swing.*;
import java.awt.*;

public class CalculatorFlowLayout extends JFrame{

    public static void main(String args[]){
        CalculatorFlowLayout calculator = new CalculatorFlowLayout();
        calculator.setVisible(true);

    }

    public CalculatorFlowLayout(){
        addWindowListener(new WindowDestroyer());
        JPanel paneltop = new JPanel();
        JPanel content = new JPanel();
        JPanel subcontent1 =new JPanel();
        JPanel subcontent2 = new JPanel();
        JPanel subcontent3 = new JPanel();
        JPanel subcontent4 = new JPanel();

        JLabel output = new JLabel("Ergebnis",JLabel.RIGHT);

        //paneltop.add(input);

        content.setLayout(new GridLayout(4,1));
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

        content.add(subcontent1);
        content.add(subcontent2);
        content.add(subcontent3);
        content.add(subcontent4);
        Container container = getContentPane();
        container.setLayout(new BorderLayout(5,0));

        container.add(output,BorderLayout.NORTH);
        container.add(content, BorderLayout.CENTER);
        pack();

    }
}
