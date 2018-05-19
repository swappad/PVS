public class Pentagon implements GeomCalculation {
    private int length;

    public Pentagon(int length) {
        this.length = length;
    }

    @Override
    public double getPerimeter() {
        return 5*length;
    }

    @Override
    public double getArea() {
        return (length*length*0.25)*Math.sqrt(25+10*Math.sqrt(5));
    }
}
