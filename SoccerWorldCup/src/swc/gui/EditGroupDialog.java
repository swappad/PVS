package swc.gui;


import swc.ctrl.CtrlGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
;
import java.util.HashSet;
import java.util.Vector;

public class EditGroupDialog extends JDialog {


    private JPanel panel = new JPanel(new BorderLayout());
    private Object input;


    public EditGroupDialog(String message, String title){
        super();
        setModal(true);
        setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        add(panel);

        JLabel label = new JLabel(message);
        panel.add(label, BorderLayout.NORTH);

        JTextField inputField = new JTextField("Name");
        panel.add(inputField,BorderLayout.CENTER);

        JPanel confirmPanel = new JPanel(new GridLayout(1,2));
        panel.add(confirmPanel,BorderLayout.SOUTH);
            JButton apply = new JButton("Apply changes");
            confirmPanel.add(apply);

            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String strIn =  inputField.getText();
                    try{
                        Vector<String> teams = CtrlGroup.getDefaultTeams();
                        for (String team: teams) {
                            if(team.equals(strIn)) {
                                input = strIn;
                                dispose();
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(null,"Diese Eingabe ist ungültig!");
                        input=null;

                    }catch(IOException e1){
                        e1.printStackTrace();
                    }

                }
            });
            JButton cancel = new JButton("Cancel");
            confirmPanel.add(cancel);
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

        pack();


    }

    public Object getInput() {
        return input;
    }

    public static Object showEditGroupDialog(String message, String title){

        EditGroupDialog editGroupDialog = new EditGroupDialog(message, title);
        editGroupDialog.setVisible(true);
        editGroupDialog.toFront();

        if(editGroupDialog.getInput()!=null)
            return editGroupDialog.getInput();
        else return null;

    }

}
