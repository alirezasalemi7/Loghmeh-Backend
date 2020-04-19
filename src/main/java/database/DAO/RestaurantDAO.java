package database.DAO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import exceptions.FoodIsRegisteredException;
import exceptions.InvalidJsonInputException;
import models.*;

import java.io.IOException;
import java.util.ArrayList;

public class RestaurantDAO {

    private String name;
    private String logoAddress;
    private Location location;
    private String id;
    private ArrayList<FoodDAO> menu;

    public ArrayList<FoodDAO> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<FoodDAO> menu) {
        this.menu = menu;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public RestaurantDAO(String name, String logoAddress, Location location, String id) {
        this.name = name;
        this.logoAddress = logoAddress;
        this.location = location;
        this.id = id;
    }

    public RestaurantDAO() {}

    public String getName() {
        return name;
    }

    public String getLogoAddress() {
        return logoAddress;
    }

    private static class RestaurantDeserializer extends StdDeserializer<RestaurantDAO> {

        protected RestaurantDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public RestaurantDAO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            try {
                ObjectCodec codec = jsonParser.getCodec();
                JsonNode root = codec.readTree(jsonParser);
                String name = root.get("name").asText();
                String id = root.get("id").asText();
                String logo = root.get("logo").asText();
                JsonNode locationNode = root.get("location");
                ObjectMapper mapper = new ObjectMapper();
                Location location = mapper.readValue(locationNode.toString(), Location.class);
                JsonNode menuNode = root.get("menu");
                RestaurantDAO restaurant = new RestaurantDAO(name,logo,location,id);
                ArrayList<FoodDAO> foods = new ArrayList<>();
                if(menuNode.isArray()){
                    for(JsonNode node : menuNode){
                        FoodDAO food;
                        food = mapper.readValue(node.toString(), FoodDAO.class);
                        food.setSpecial(node.get("oldPrice")!=null);
                        food.setRestaurantId(id);
                        food.setRestaurantName(name);
                        foods.add(food);
                    }

                }
                else throw new JsonParseException(jsonParser, "invalid menu");
                restaurant.setMenu(foods);
                return restaurant;
            }
            catch (NullPointerException e){
                throw new JsonParseException(jsonParser, "invalid");
            }
        }
    }

    public static RestaurantDAO deserializeFromJson(String jsonData) throws InvalidJsonInputException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RestaurantDAO.class, new RestaurantDAO.RestaurantDeserializer(RestaurantDAO.class));
        mapper.registerModule(module);
        try {
            return mapper.readValue(jsonData, RestaurantDAO.class);
        }
        catch (JsonMappingException e){
            throw new InvalidJsonInputException();
        }
        catch (IOException e){
            throw new InvalidJsonInputException();
        }
    }
}
