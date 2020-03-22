package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;

import java.io.IOException;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
public class SpecialFood extends Food {

    @JsonProperty("oldPrice")
    private double _oldPrice;
    @JsonProperty("count")
    private int _count;

    public double getOldPrice() {
        return _oldPrice;
    }

    public void setOldPrice(double _oldPrice) {
        this._oldPrice = _oldPrice;
    }

    public int getCount() {
        return _count;
    }

    public void setCount(int _count) throws FoodCountIsNegativeException {
        if (_count < 0) throw new FoodCountIsNegativeException("Food Count must be positive.");
        this._count = _count;
    }

    public SpecialFood() {
        super();
    }

    @Override
    public String toJson() throws InvalidToJsonException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e){
            throw new InvalidToJsonException();
        }
    }

    @Override
    public JsonNode toJsonNode() throws InvalidToJsonException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(this.toJson());
        } catch (IOException e) {
            throw new InvalidToJsonException();
        }
    }

    @Override
    public Food deserializeFromJson(String jsonData) throws InvalidJsonInputException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData, SpecialFood.class);
        }
        catch (JsonMappingException e){
            throw new InvalidJsonInputException();
        }
        catch (IOException e){
            throw new InvalidJsonInputException();
        }
    }

    public SpecialFood(String name, String description, double popularity, double price, String imageAddress, String restaurantId, double olderPrice, int count)
            throws InvalidPopularityException, InvalidPriceException {
        super(name, description, popularity, price, imageAddress, restaurantId);
        this._oldPrice = olderPrice;
        this._count = count;
    }

    public NormalFood changeToNormalFood() throws InvalidPopularityException, InvalidPriceException {
        return new NormalFood(super.getName(), super.getDescription(), super.getPopularity(), this.getOldPrice(), super.getImageAddress(), super.getRestaurantId());
    }

}
