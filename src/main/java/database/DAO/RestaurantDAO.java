package database.DAO;

import models.Location;

import java.util.HashMap;

public class RestaurantDAO {

    private String name;
    private String logoAddress;
    private Location location;
    private String id;
    private HashMap<String, Boolean> menu = new HashMap<>();

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

//    public RestaurantDAO(String name, String logoAddress, Location location, String id, HashMap<String, Boolean> menu) {
//        this.name = name;
//        this.logoAddress = logoAddress;
//        this.location = location;
//        this.id = id;
//        this.menu = menu;
//    }

    public RestaurantDAO() {}

    public String getName() {
        return name;
    }

    public HashMap<String, Boolean> getMenu() {
        return menu;
    }

    public void setMenu(HashMap<String, Boolean> menu) {
        this.menu = menu;
    }

    public String getLogoAddress() {
        return logoAddress;
    }
}
