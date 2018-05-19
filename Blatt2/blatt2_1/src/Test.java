import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String args[]){

        /*Aufgabe 2 b)
        *zu Testzwecken werden beliebige Objekte vom Typ MyDate in eine Liste eingefügt.
        * Anschliessend wird die gesamte Liste sortiert.
        *
        * */
        List<MyDate> dates = new LinkedList<MyDate>();
        dates.add(new MyDate(15,3,2019));
        dates.add(new MyDate(15,3,2019));
        dates.add(new MyDate(19,1,1825));
        dates.add(new MyDate(30,4,2010));
        dates.add(new MyDate(11,12,1997));
        dates.add(new MyDate(5,7,2019));
        dates.add(new MyDate(5,10,2000));
        dates.add(new MyDate(5,10,2000));
        dates.add(new MyDate(7,12,1));

        Collections.sort(dates);

        System.out.println("Objekte in der Liste:");
        for (MyDate o:dates) {
            System.out.println(o);
        }

        /*Aufgabe 2 c)
        * Die Objekte aus der Liste werden einzelnd in den Hash überführt.
        * Vorher wird überprüft, ob das Objekt schon im Hash vorhanden ist.
        * */
        System.out.println("\nDoppellungen:");
        HashSet set = new HashSet();

        for (Object o:dates) {
            if(set.contains(o))
                System.out.println(o);
            else set.add(o);
        }
    }
}
