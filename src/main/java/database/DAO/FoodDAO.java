package database.DAO;

public class FoodDAO {

    private String restaurantId;
    private String restaurantName;
    private String logo;
    private Double popularity;
    private String name;
    private Double price;
    private String description;
    private int count;
    private Double oldPrice;
    private boolean special;

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getLogo() {
        return logo;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public FoodDAO() {}

    public FoodDAO(String restaurantId, String restaurantName, String logo, Double popularity, String name, Double price, String description, int count, Double oldPrice) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.logo = logo;
        this.popularity = popularity;
        this.name = name;
        this.price = price;
        this.description = description;
        this.count = count;
        this.oldPrice = oldPrice;
    }

    public FoodDAO(String restaurantId, String restaurantName, String logo, Double popularity, String name, Double price, String description) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.logo = logo;
        this.popularity = popularity;
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
