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
    private Location _location;
    private HashMap<String, Food> _menu = new HashMap<>();
    private double _averagePopularity = 0;

    public Restaurant(String name,String description,Location location){
        this._name = name;
        this._description = description;
        this._location = location;
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
        return null;
    }

    public String toJson() throws InvalidToJsonException {
        return null;
    }

    public static Restaurant deserializeFromJson(String jsonData) throws InvalidJsonInputException{
        return null;
    }
}
