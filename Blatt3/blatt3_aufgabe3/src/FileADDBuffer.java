import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

public class FileADDBuffer {

    /*Zu Aufgabe c)
    * Der Buffered Reader speichert zunächst einen ganzen Abschnitt(Zeile) der Textdatei im Buffer(Ram) und
    * kümmert sich erst dann um die weitere Verarbeitung der Daten im Buffer.
    * Das ist schneller, als der "einfache" FileReader, bei dem Daten direkt während dem Auslesen verarbeitet werden.
    * Dabei verlängert sich zum Beispiel die Zugriffszeit auf die Festplatte, was mehr Resourcen und Zeit in Anspruch nimmt.
    * Daten aus dem Buffer können hingegen schnell verarbeitet, wie hier zum Beispiel addiert, werden.
    * Laufzeit FileReader: 8759ms
    * Laufzeit Buffer: 1258ms
    * */
    public static void main(String arg[]) {

        try {
            long startTime = System.currentTimeMillis();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("bigInput.txt"));
            String str;
            long result = 0;
            while (((str=bufferedReader.readLine())!=null)) {
                result += Integer.parseInt(str);
            }
            bufferedReader.close();
            System.out.println(result);
            System.out.println("Laufzeit: " + (System.currentTimeMillis()-startTime) +"ms");
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
