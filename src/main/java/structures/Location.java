package structures;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Location {

    @JsonProperty("x")
    private double _x;
    @JsonProperty("y")
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
        return Math.sqrt(Math.pow(_x-location._x,2)+Math.pow(_y-location._y,2));
    }
}

