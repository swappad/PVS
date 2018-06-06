package aufgabe1;

import java.io.Serializable;

public class Car implements Serializable {
    private static long serialVersionUID;
    protected String licensePlate;
    protected String productionDate;
    protected int numberPassenger;
    protected int numberWhels;
    protected int numberDoors;

    public Car() {
    }

    public Car(String licensePlate, String productionDate, int numberPassenger, int numberWhels, int numberDoors) {
        this.licensePlate = licensePlate;
        this.productionDate = productionDate;
        this.numberPassenger = numberPassenger;
        this.numberWhels = numberWhels;
        this.numberDoors = numberDoors;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        Car.serialVersionUID = serialVersionUID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public int getNumberPassenger() {
        return numberPassenger;
    }

    public void setNumberPassenger(int numberPassenger) {
        this.numberPassenger = numberPassenger;
    }

    public int getNumberWhels() {
        return numberWhels;
    }

    public void setNumberWhels(int numberWhels) {
        this.numberWhels = numberWhels;
    }

    public int getNumberDoors() {
        return numberDoors;
    }

    public void setNumberDoors(int numberDoors) {
        this.numberDoors = numberDoors;
    }
}
