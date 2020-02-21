package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DeliveryMan {

    private String id;
    private double velocity;
    private Location location;

    public String getId() {
        return id;
    }

    public double getVelocity() {
        return velocity;
    }

    public Location getLocation() {
        return location;
    }
}
