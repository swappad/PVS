package aufgabe1;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;


public class Program {

    public static void main(String[] args) {
        Mercedes car1 = new Mercedes("UL-M_12", "2017", 6, 4, 5, "FakeMercedes", 500);
        Porsche car2 = new Porsche("HA-HA-7", "1990", 2, 4, 3, "Oldtimer", 50);

        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Cars.xml")));
            e.writeObject(car1);
            e.writeObject(car2);
            e.close();
        }catch(IOException ioException1) {
            ioException1.printStackTrace();
        }

        try{
            XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("Cars.xml")));
            Object object;
            while((object=xmlDecoder.readObject())!=null){
                System.out.println(object.toString());
            }
            xmlDecoder.close();

        }catch( IndexOutOfBoundsException e2){
            //e2.printStackTrace();

        }
        catch(IOException e4){
            //e4.printStackTrace();
        }
}
}
