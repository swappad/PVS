public class Circle implements GeomCalculation {
    int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public double getPerimeter() {
        return 2*radius*Math.PI;
    }

    @Override
    public double getArea() {
        return radius*radius*Math.PI;
    }
}
