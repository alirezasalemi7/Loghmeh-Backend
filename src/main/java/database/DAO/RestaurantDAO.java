package database.DAO;

import models.Location;

public class RestaurantDAO {

    private String name;
    private String logoAddress;
    private Location location;
    private String id;

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public RestaurantDAO(String name, String logoAddress, Location location, String id) {
        this.name = name;
        this.logoAddress = logoAddress;
        this.location = location;
        this.id = id;
    }

    public RestaurantDAO() {}

    public String getName() {
        return name;
    }

    public String getLogoAddress() {
        return logoAddress;
    }
}
