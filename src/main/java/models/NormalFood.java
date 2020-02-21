package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidJsonInputException;
import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;
import exceptions.InvalidToJsonException;

import java.io.IOException;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY , getterVisibility = JsonAutoDetect.Visibility.NONE)
public class NormalFood extends Food {

    public NormalFood() {
        super();
    }

    public NormalFood(String name, String description, double popularity, double price, String imageAddress, String restaurantId) throws InvalidPopularityException, InvalidPriceException {
        super(name, description, popularity, price, imageAddress, restaurantId);
    }

    @Override
    public String toJson()throws InvalidToJsonException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e){
            throw new InvalidToJsonException();
        }
    }

    @Override
    public Food deserializeFromJson(String jsonData)throws InvalidJsonInputException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData, NormalFood.class);
        }
        catch (JsonMappingException e){
            throw new InvalidJsonInputException();
        }
        catch (IOException e){
            throw new InvalidJsonInputException();
        }
    }

}
