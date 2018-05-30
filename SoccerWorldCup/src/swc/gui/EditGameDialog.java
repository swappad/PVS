package swc.gui;

import swc.data.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditGameDialog extends JDialog {
    private int inputTeamG=-1;
    private int inputTeamH=-1;
    private Team teamG,teamH;
    private boolean successfull=false;

    private EditGameDialog(Frame owner,Team teamG, Team teamH){
        super(owner);
        this.teamG = teamG;
        this.teamH = teamH;
        setModal(true);
        Container contentpane = getContentPane();
        contentpane.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(3,3));
        contentpane.add(panel);
        setLocationRelativeTo(getOwner());
        JLabel teamGLabel = new JLabel(teamG.getName(),JLabel.LEFT);
        panel.add(teamGLabel);
        JTextField inputGField = new JTextField(JTextField.CENTER);
        panel.add(inputGField);
        JLabel t1 = new JLabel("Goals",JLabel.LEFT);
        panel.add(t1);
        JLabel teamHLabel = new JLabel(teamH.getName(),JLabel.LEFT);
        panel.add(teamHLabel);
        JTextField inputHField = new JTextField(JTextField.CENTER);
        panel.add(inputHField);
        JLabel t2 = new JLabel("Goals",JLabel.LEFT);
        panel.add(t2);

        JButton apply = new JButton("Apply changes");
        panel.add(apply);

        JButton cancel = new JButton("cancel");
        panel.add(cancel);

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                successfull = ((
                        inputTeamG = Integer.valueOf(inputGField.getText())) >= 0)
                        && ((inputTeamH = Integer.valueOf(inputHField.getText())) >= 0
                );
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                successfull=false;
                dispose();
            }
        });

        pack();

    }

    public static int[] getGameResult(Frame owner,Team teamG, Team teamH) throws Exception{
        System.out.println("test");
        int[] result = new int[2];
        EditGameDialog editGameDialog = new EditGameDialog(owner,teamG,teamH);
        editGameDialog.setVisible(true);
        if(editGameDialog.successfull) {
            result[0] = editGameDialog.inputTeamG;
            result[1] = editGameDialog.inputTeamH;
            return result;
        }else throw new Exception("NoValidInput");

    }

}
