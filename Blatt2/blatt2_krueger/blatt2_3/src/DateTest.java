import java.time.LocalDate;
import java.time.temporal.IsoFields;

public class DateTest {

    public static void main(String args[]){

        System.out.println("Today: " + LocalDate.now());
        LocalDate date = LocalDate.now();
        System.out.println(date.minusWeeks(4));//Gibt das Datum von vor genau vier Wochen aus.
        System.out.println(date.minusMonths(1));//Gibt das Datum desselben Tages vom Vormonat aus.
        /*Die Daten der beiden obenstehenden Ausgaben sind nicht gleich.*/

        System.out.println("Woche: " + date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
    }
}
