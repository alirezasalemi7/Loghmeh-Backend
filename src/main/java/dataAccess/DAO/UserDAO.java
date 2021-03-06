package dataAccess.DAO;

import business.Domain.Location;

public class UserDAO {

    private String name;
    private String family;
    private String phoneNumber;
    private String email;
    private Double credit;
    private String id;
    private Location location;
    private long password;

    public UserDAO() {}

    public UserDAO(String name, String family, String phoneNumber, String email, Double credit, String id, Location location, long password) {
        this.name = name;
        this.family = family;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.credit = credit;
        this.id = id;
        this.location = location;
        this.password = password;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
