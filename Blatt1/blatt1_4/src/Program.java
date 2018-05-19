import java.util.LinkedList;
import java.util.List;

public class Program {

    public static void main(String args[]){
        List<GeomCalculation> geomList = new LinkedList<GeomCalculation>();

        geomList.add(new Circle(50));
        geomList.add(new Pentagon(20));
        geomList.add(new Circle(32));
        geomList.add(new Pentagon(56));
        geomList.add(new Circle(1));
        geomList.add(new Pentagon(22));
        geomList.add(new Circle(69));
        geomList.add(new Pentagon(13));
        geomList.add(new Circle(33));

        double wholePerimeter = 0;
        double wholeArea = 0;
        for (GeomCalculation obj: geomList) {
            wholePerimeter += obj.getPerimeter();
            wholeArea +=obj.getArea();
        }
        System.out.println("Area: " + wholeArea + "; Perimeter: " + wholePerimeter);
    }

}

