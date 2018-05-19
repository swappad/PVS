package aufgabe2;

import aufgabe1.WindowDestroyer;

import javax.swing.*;
import javax.imageio.ImageIO.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.imageio.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImageViewer extends JFrame{




    JPanel preView = new JPanel(new BorderLayout());
    JPanel fullView = new JPanel(new BorderLayout());


    public ImageViewer(){
        super();

        setTitle("Image Viewer");
        setPreferredSize(new Dimension(600, 400));
        Container contentPane = getContentPane();

        addWindowListener(new aufgabe2.WindowDestroyer());


        contentPane.setLayout(new BorderLayout());


        //Buttons
        JButton loadfromFile = new JButton("Aus Datei laden");
        JButton loadfromURL = new JButton("Aus URL laden");
        JPanel south = new JPanel(new GridLayout(1,2));
        south.add(loadfromFile);
        south.add(loadfromURL);
        contentPane.add(south,BorderLayout.SOUTH);




        //Preview
        JLabel preLabel = new JLabel();
        preLabel.setPreferredSize(new Dimension(150,150));
        preView.add(preLabel);
        TitledBorder preBorder = BorderFactory.createTitledBorder("Vorschau");
        preView.setBorder(preBorder);
        contentPane.add(preView,BorderLayout.WEST);
        //FullView
        JLabel fullLabel = new JLabel();
        TitledBorder fullBorder = BorderFactory.createTitledBorder("Volles Bild");
        fullView.setBorder(fullBorder);

        JScrollPane fullImageScrollPane = new JScrollPane(fullLabel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        fullView.add(fullImageScrollPane);
        contentPane.add(fullView, BorderLayout.CENTER);

        loadfromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage fullImage = null;
                JFileChooser chooser = new JFileChooser();

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal==chooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();
                    try{
                        fullImage = ImageIO.read(file);

                        Icon previewImageIcon = new ImageIcon(resize(fullImage,150));

                        fullLabel.setIcon(new ImageIcon(fullImage));
                        preLabel.setIcon(previewImageIcon);


                    }catch(IOException eIO) {
                        JOptionPane.showMessageDialog(new JFrame(), "Keine Bilddatei gefunden. Bitte die Adresse überprüfen!");
                        return;
                    }catch(NullPointerException nullpointer){
                        JOptionPane.showMessageDialog(new JFrame(), "Dieser Datentyp kann nicht eingelesen werden!", "Fehler beim Einlesen",JOptionPane.ERROR_MESSAGE);
                    }



                }

            }


        });//end Action Listener
        loadfromURL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage fullImage = null;
                try{
                    URL imageURL = new URL(JOptionPane.showInputDialog("Addresse zum Bild eingeben:"));
                    fullImage = ImageIO.read(imageURL);

                    Icon previewImageIcon = new ImageIcon(resize(fullImage,150));

                    fullLabel.setIcon(new ImageIcon(fullImage));
                    preLabel.setIcon(previewImageIcon);

                }catch(IOException eIO){
                    JOptionPane.showMessageDialog(new JFrame(),"Keine Bilddatei gefunden. Bitte die Adresse überprüfen!","Fehler beim Einlesen",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        pack();

    }

    public Image resize(BufferedImage input, int max_side){
        float max_sidef = (float)max_side;
        float newHeight;
        float newWidth;
        if(input.getHeight()<input.getWidth()){
            newWidth = max_side;
            newHeight = ((float)max_side/(float)input.getWidth())*(float)input.getHeight();
        }else{
            newHeight = max_side;
            newWidth = ((float)max_side/(float)input.getHeight())*(float)input.getWidth();
        }

        Image tmp =input.getScaledInstance((int)newWidth,(int)newHeight,Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage((int)newWidth,(int)newHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
