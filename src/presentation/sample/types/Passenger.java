package presentation.sample.types;

public class Passenger {
    private int id;
    private int floor;

    public Passenger(int id, int floor) {
        this.id = id;
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

}
