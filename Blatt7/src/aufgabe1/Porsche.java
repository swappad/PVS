package aufgabe1;

public class Porsche extends Car {
    private static long serialVersionUID;
    private String model;
    private int ps;

    public Porsche() {
        super();
    }

    public Porsche(String licensePlate, String productionDate, int numberPassenger, int numberWhels, int numberDoors, String model, int ps) {
        super(licensePlate, productionDate, numberPassenger, numberWhels, numberDoors);
        this.model = model;
        this.ps = ps;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        Porsche.serialVersionUID = serialVersionUID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    @Override
    public String toString() {
        return "aufgabe1.Porsche," + getModel();
    }
}
