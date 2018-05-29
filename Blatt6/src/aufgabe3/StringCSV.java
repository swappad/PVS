package aufgabe3;

import java.io.*;
import java.util.Iterator;

public class StringCSV {

    public static void main(String[] args){
        String input = "Josef Ulm josef@ulm.de;Flo Kuchen flo@ulm.de;Jens Senden jens@ulm.de";
        String[] splittedInput = input.split(";");

        FileWriter fileWriter;
        BufferedWriter bufferedWriter;
        try{
            fileWriter=new FileWriter("Kontakte.csv",false);
            bufferedWriter= new BufferedWriter(fileWriter);
            for (String line: splittedInput) {
                bufferedWriter.newLine();
                String[] elements = line.split(" ");
                for (int i=0; i < elements.length; i++) {
                    bufferedWriter.write(i < elements.length - 1 ? elements[i] + "," : elements[i]);
                }
            }
            bufferedWriter.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        FileReader fileReader;
        BufferedReader bufferedReader;
        try{
            fileReader= new FileReader("Kontakte.csv");
            bufferedReader= new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            bufferedReader.close();
        }catch(IOException e2){
            e2.printStackTrace();
        }

    }
}
