package aufgabe2;

import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;

public class ArraytoFile {



    public static void main(String[] args){
        ArrayList<String> textArrayList = new ArrayList<>();
        String[] textArray = {"Hallo", "dies", "ist", "ein", "Test-", "Text"};
        for (String elem: textArray) {
            textArrayList.add(elem);
        }

        FileWriter output;
        BufferedWriter bufferedWriter;
        try{
            output = new FileWriter("test.txt",false);
            bufferedWriter = new BufferedWriter(output);

            for (String text:textArrayList) {
                bufferedWriter.newLine();
                bufferedWriter.write(text);
            }

            bufferedWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }


        FileReader fileReader;
        BufferedReader bufferedReader;
        try{
            fileReader= new FileReader("test.txt");
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line=bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            bufferedReader.close();
        }catch (IOException e2){
            e2.printStackTrace();
        }



    }


}
