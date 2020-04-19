package database.DAO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
public class FoodDAO {

    private String restaurantId;
    private String restaurantName;
    @JsonProperty("image")
    private String logo;
    @JsonProperty("popularity")
    private Double popularity;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("description")
    private String description;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("oldPrice")
    private Double oldPrice;
    private boolean special;

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

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

    public Integer getCount() {
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
        this.special = true;
    }

    public FoodDAO(String restaurantId, String restaurantName, String logo, Double popularity, String name, Double price, String description) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.logo = logo;
        this.popularity = popularity;
        this.name = name;
        this.price = price;
        this.description = description;
        this.special = false;
        this.count = null;
        this.oldPrice = null;
    }
}
