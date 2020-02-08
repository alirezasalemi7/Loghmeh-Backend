package structures;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    private double _x;
    private double _y;

    public Location(double x,double y){
        this._x = x;
        this._y = y;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public Location(){}

    public double getDistance(Location location){
        return -1;
    }
}

