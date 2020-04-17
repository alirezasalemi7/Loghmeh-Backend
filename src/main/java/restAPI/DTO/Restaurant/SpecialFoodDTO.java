package restAPI.DTO.Restaurant;

public class SpecialFoodDTO {

    private String id;
    private String restaurantId;
    private String restaurantName;
    private String logo;
    private Double popularity;
    private String name;
    private Double price;
    private String description;
    private int count;
    private Double oldPrice;

    public SpecialFoodDTO() {}

    public SpecialFoodDTO(String id, String restaurantId, String restaurantName, String logo, Double popularity, String name, Double price, String description, int count, Double oldPrice) {
        this.id = id;
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
}
