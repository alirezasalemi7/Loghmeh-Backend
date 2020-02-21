package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidJsonInputException;
import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;
import exceptions.InvalidToJsonException;

import java.io.IOException;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonPropertyOrder({"name","description","popularity","price","image"})
public abstract class Food {

    @JsonProperty("name")
    private String _name;
    @JsonProperty("description")
    private String _description;
    @JsonProperty("popularity")
    private double _popularity;
    @JsonProperty("price")
    private double _price;
    @JsonProperty("image")
    private String _imageAddress;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String _restaurantId;

    public Food() {}

    public Food(String name, String description, double popularity, double price, String imageAddress, String restaurantId)
            throws InvalidPopularityException, InvalidPriceException {
        if (price < 0)
            throw new InvalidPriceException("Invalid price. Price is negative.");
        if (!(popularity >= 0 && popularity <= 1))
            throw new InvalidPopularityException("Popularity must be a number between 0 & 1");
        this._name = name;
        this._description = description;
        this._popularity = popularity;
        this._price = price;
        this._restaurantId = restaurantId;
        this._imageAddress = imageAddress;
    }

    public String getImageAddress() {
        return _imageAddress;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public double getPopularity() {
        return _popularity;
    }

    public double getPrice() {
        return _price;
    }

    public String getRestaurantId() {
        return _restaurantId;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public void setPopularity(double popularity) {
        this._popularity = popularity;
    }

    public void setPrice(double price) {
        this._price = price;
    }

    public void setRestaurantId(String restaurantId) {
        this._restaurantId = restaurantId;
    }

    public void setImageAddress(String _imageAddress) {
        this._imageAddress = _imageAddress;
    }

    public abstract String toJson();// throws InvalidToJsonException{
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.writeValueAsString(this);
//        }
//        catch (JsonProcessingException e){
//            throw new InvalidToJsonException();
//        }
//    }

    public abstract Food deserializeFromJson(String jsonData);// throws InvalidJsonInputException{
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.readValue(jsonData, Food.class);
//        }
//        catch (JsonMappingException e){
//            throw new InvalidJsonInputException();
//        }
//        catch (IOException e){
//            throw new InvalidJsonInputException();
//        }
//    }
}
