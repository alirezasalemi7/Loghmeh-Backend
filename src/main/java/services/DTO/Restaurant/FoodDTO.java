package services.DTO.Restaurant;

public class FoodDTO {

    private String restaurantId;
    private String restaurantName;
    private String logo;
    private Double popularity;
    private String name;
    private Double price;
    private String description;

    public FoodDTO() {}

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

    public FoodDTO(String restaurantId, String restaurantName, String logo, Double popularity, String name, Double price, String description) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.logo = logo;
        this.popularity = popularity;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
}
