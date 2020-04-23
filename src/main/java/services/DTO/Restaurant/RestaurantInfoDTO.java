package services.DTO.Restaurant;

public class RestaurantInfoDTO {

    private String name;
    private String logoAddress;
    private String id;

    public RestaurantInfoDTO() {}

    public RestaurantInfoDTO(String name, String logoAddress, String id) {
        this.name = name;
        this.logoAddress = logoAddress;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoAddress() {
        return logoAddress;
    }

    public void setLogoAddress(String logoAddress) {
        this.logoAddress = logoAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
