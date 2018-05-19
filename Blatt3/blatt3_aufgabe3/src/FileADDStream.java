import java.io.FileInputStream;
import java.util.Scanner;

public class FileADDStream {

    public static void main(String arg[]) {

        try {
            long startTime = System.currentTimeMillis();
            Scanner inputStream = new Scanner(new FileInputStream("bigInput.txt"));

            long result = 0;
            while (inputStream.hasNextLine())  {
                result += Integer.parseInt(inputStream.nextLine());
            }
            inputStream.close();
            System.out.println(result);
            System.out.println("Laufzeit: " + (System.currentTimeMillis()-startTime)+"ms");
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}