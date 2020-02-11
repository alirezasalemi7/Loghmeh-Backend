package structures;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import exceptions.FoodDoesntExistException;
import exceptions.FoodIsRegisteredException;
import exceptions.InvalidJsonInputException;
import exceptions.InvalidToJsonException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {

    private String _name;
    private String _description;
    private String _logoAddress;
    private Location _location;
    private String _id;
    private HashMap<String, Food> _menu = new HashMap<>();
    private double _averagePopularity = 0;

    public ArrayList<Food> getMenu() {
        return new ArrayList<Food>(this._menu.values());
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public Restaurant(String name, String id, String logoAddress, String description,Location location){
        this._name = name;
        this._id = id;
        this._logoAddress = logoAddress;
        this._description = description;
        this._location = location;
    }

    public String getLogoAddress() {
        return _logoAddress;
    }

    public void setLogoAddress(String logoAddress) {
        this._logoAddress = logoAddress;
    }

    public Location getLocation(){
        return _location;
    }

    public double getAveragePopularity() {
        return _averagePopularity;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public void setAveragePopularity(double popularity) {
        this._averagePopularity = popularity;
    }

    public void addFood(Food food) throws FoodIsRegisteredException {
        if (this._menu.containsKey(food.getName())) {
            throw new FoodIsRegisteredException(food.getName() + " is already registered in " + this.getName());
        }
        int numberOfFoods = this._menu.size();
        this.setAveragePopularity((this._averagePopularity * numberOfFoods + food.getPopularity()) / (numberOfFoods + 1));
        this._menu.put(food.getName(), food);
    }

    public Food getFoodByName(String name) throws FoodDoesntExistException {
        if (!_menu.containsKey(name)) {
            throw new FoodDoesntExistException(name + "Doesn't exist in the menu of " + this.getName() + " restaurant");
        }
        return _menu.get(name);
    }

    private class RestaurantSerializer extends StdSerializer<Restaurant>{

        protected RestaurantSerializer(Class<Restaurant> t) {
            super(t);
        }

        @Override
        public void serialize(Restaurant restaurant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            ArrayList<Food> foodList = new ArrayList<Food>(_menu.values());
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", _name);
            jsonGenerator.writeObjectField("id", _id);
            jsonGenerator.writeObjectField("logo", _logoAddress);
            jsonGenerator.writeStringField("description",_description);
            jsonGenerator.writeObjectField("location", _location);
            jsonGenerator.writeArrayFieldStart("menu");
            for(Food food : foodList){
                jsonGenerator.writeObject(food);
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }


    public String toJson() throws InvalidToJsonException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Restaurant.class,new RestaurantSerializer(Restaurant.class));
        mapper.registerModule(module);
        try {
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e){
            throw new InvalidToJsonException();
        }
    }

    private static class RestaurantDeserializer extends StdDeserializer<Restaurant>{

        protected RestaurantDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Restaurant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            try {
                ObjectCodec codec = jsonParser.getCodec();
                JsonNode root = codec.readTree(jsonParser);
                String name = root.get("name").asText();
                String id = root.get("id").asText();
                String logo = root.get("logo").asText();
                String description = root.get("description").asText();
                JsonNode locationNode = root.get("location");
                ObjectMapper mapper = new ObjectMapper();
                Location location = mapper.readValue(locationNode.toString(), Location.class);
                JsonNode menuNode = root.get("menu");
                Restaurant restaurant = new Restaurant(name, id, logo, description,location);
                if(menuNode.isArray()){
                    for(JsonNode node : menuNode){
                        Food food = mapper.readValue(node.toString(), Food.class);
                        food.setRestaurantName(name);
                        try {
                            restaurant.addFood(food);
                        } catch (FoodIsRegisteredException e) {
                            throw new JsonParseException(jsonParser, e.getMessage());
                        }
                    }
                }
                else throw new JsonParseException(jsonParser, "invalid menu");
                return restaurant;
            }
            catch (NullPointerException e){
                throw new JsonParseException(jsonParser, "invalid");
            }
        }
    }

    public static Restaurant deserializeFromJson(String jsonData) throws InvalidJsonInputException{
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Restaurant.class, new RestaurantDeserializer(Restaurant.class));
        mapper.registerModule(module);
        try {
            return mapper.readValue(jsonData, Restaurant.class);
        }
        catch (JsonMappingException e){
            throw new InvalidJsonInputException();
        }
        catch (IOException e){
            throw new InvalidJsonInputException();
        }
    }
}
