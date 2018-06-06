package aufgabe1;

public class Mercedes extends Car {
    private static long serialVersionUID;
    private String model;
    private int capacity;

    public Mercedes() {
        super();
    }

    public Mercedes(String licensePlate, String productionDate, int numberPassenger, int numberWhels, int numberDoors, String model, int capacity) {
        super(licensePlate, productionDate, numberPassenger, numberWhels, numberDoors);
        this.model = model;
        this.capacity=capacity;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        Mercedes.serialVersionUID = serialVersionUID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return ("aufgabe1.Mercedes," + getModel());
    }
}
