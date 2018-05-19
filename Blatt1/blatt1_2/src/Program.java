public class Program {

    public static void main(String args[]){
        GeomCalculation[] geoArr = new GeomCalculation[10];

        geoArr[0]=new Circle(50);
        geoArr[1]=new Pentagon(20);
        geoArr[2]=new Circle(32);
        geoArr[3]=new Pentagon(56);
        geoArr[4]=new Circle(1);
        geoArr[5]=new Pentagon(22);
        geoArr[6]=new Circle(69);
        geoArr[7]=new Pentagon(13);
        geoArr[8]=new Circle(33);
        double wholePerimeter = 0;
        double wholeArea = 0;
        for(int i=0; i<geoArr.length;i++){
            if(geoArr[i]!=null){
                //System.out.println("Object " + i + "; Area: " + geoArr[i].getArea() + "; Perimeter: " + geoArr[i].getPerimeter());
                wholePerimeter+=geoArr[i].getPerimeter();
                wholeArea+=geoArr[i].getArea();

            }else i=geoArr.length;


        }
        System.out.println("Area: " + wholeArea + "; Perimeter: " + wholePerimeter);


    }

}