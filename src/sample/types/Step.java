package sample.types;

import javafx.geometry.Point2D;

public class Step {
    private Point2D beg, end;
    private int duration;
    private int floor;

    public Step() {
        this.beg = new Point2D(0, 0);
        this.end = new Point2D(0, 0);
        this.duration = 1000;
        this.floor = -1;
    }

    public Step(int duration) {
        this.beg = new Point2D(0, 0);
        this.end = new Point2D(0, 0);
        this.duration = duration;
        this.floor = -1;
    }

    public Step(Point2D beg, Point2D end, int duration) {
        this.beg = beg;
        this.end = end;
        this.duration = duration;
        this.floor = -1;
    }

    public Point2D getBeg() {
        return beg;
    }

    public void setBeg(Point2D beg) {
        this.beg = beg;
    }

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(Point2D end) {
        this.end = end;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
