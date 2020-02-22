package models;

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
    private HashMap<String, NormalFood> _normalMenu = new HashMap<>();
    private HashMap<String, SpecialFood> _specialMenu = new HashMap<>();
    private double _averagePopularity = 0;

    public ArrayList<Food> getMenu() {
        ArrayList<Food> allFoods = new ArrayList<Food>(this._normalMenu.values());
        allFoods.addAll(this._specialMenu.values());
        return allFoods;
    }

    public HashMap<String, NormalFood> getNormalMenu() {
        return this._normalMenu;
    }

    public HashMap<String, SpecialFood> getSpecialMenu() {
        return this._specialMenu;
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
        if (food instanceof NormalFood) {
            if (this._normalMenu.containsKey(food.getName()))
                throw new FoodIsRegisteredException(food.getName() + " is already registered in " + this.getName());
            this._normalMenu.put(food.getName(), (NormalFood) food);
        } else if (food instanceof SpecialFood) {
            if (this._specialMenu.containsKey(food.getName()))
                throw new FoodIsRegisteredException(food.getName() + " is already registered in " + this.getName());
            this._specialMenu.put(food.getName(), (SpecialFood) food);
        }
        int numberOfFoods = this._specialMenu.size() + this._normalMenu.size();
        this.setAveragePopularity((this._averagePopularity * (numberOfFoods - 1) + food.getPopularity()) / (numberOfFoods));
    }

    public void removeFood(Food food) {
        boolean restaurantHasFood = true;
        if (food instanceof NormalFood)
            if (_normalMenu.remove(food.getName()) == null)
                restaurantHasFood = false;
        else
            if(_specialMenu.remove(food.getName()) == null)
                restaurantHasFood = false;
        if (restaurantHasFood) {
            int numberOfFoods = this._specialMenu.size() + this._normalMenu.size();
            double prevAveragePopularity = ((numberOfFoods + 1) * this._averagePopularity - food.getPopularity()) / numberOfFoods;
            this.setAveragePopularity(prevAveragePopularity);
        }
    }

    public Food getFoodByName(String name) throws FoodDoesntExistException {
        if (!_normalMenu.containsKey(name)) {
            if (!_specialMenu.containsKey(name))
                throw new FoodDoesntExistException(name + "Doesn't exist in the menu of " + this.getName() + " restaurant");
            return _specialMenu.get(name);
        }
        return _normalMenu.get(name);
    }

    private class RestaurantSerializer extends StdSerializer<Restaurant>{

        protected RestaurantSerializer(Class<Restaurant> t) {
            super(t);
        }

        @Override
        public void serialize(Restaurant restaurant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            ArrayList<Food> foodList = new ArrayList<Food>(_normalMenu.values());
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", _name);
            jsonGenerator.writeObjectField("id", _id);
            jsonGenerator.writeObjectField("logo", _logoAddress);
//            jsonGenerator.writeStringField("description",_description);
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
//                String description = root.get("description").asText();
                JsonNode locationNode = root.get("location");
                ObjectMapper mapper = new ObjectMapper();
                Location location = mapper.readValue(locationNode.toString(), Location.class);
                JsonNode menuNode = root.get("menu");
                Restaurant restaurant = new Restaurant(name, id, logo, "",location);
                if(menuNode.isArray()){
                    for(JsonNode node : menuNode){
                        Food food;
                        if(node.get("oldPrice")==null){
                            food = mapper.readValue(node.toString(), NormalFood.class);
                        }
                        else {
                            food = mapper.readValue(node.toString(), SpecialFood.class);
                        }
                        food.setRestaurantId(id);
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
