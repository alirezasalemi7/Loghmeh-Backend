package org.kharchal.co;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.FoodDoesntExistException;
import exceptions.InvalidInputInstructionException;
import exceptions.InvalidToJsonException;
import exceptions.RestaurantDoesntExistException;
import structures.Food;
import structures.Restaurant;
import systemHandlers.SystemManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static SystemManager _systemManager = SystemManager.getInstance();

    public static void main( String[] args ) {

    }

    private static void addRestaurant(String jsonData) throws Exception{
        Restaurant restaurant = Restaurant.deserializeFromJson(jsonData);
        _systemManager.addRestaurant(restaurant);
    }

    private static void addFood(String jsonData) throws Exception{
        Food food = Food.deserializeFromJson(jsonData);
        _systemManager.addFood(food);
    }

    private static void getRestaurants(){
        ArrayList<String> names = _systemManager.getAllRestaurants();
        for (String name : names){
            System.out.println(name);
        }
    }

    private static void getRestaurant(String jsonData) throws IOException, RestaurantDoesntExistException, InvalidToJsonException {
        JsonNode node = (new ObjectMapper()).readTree(jsonData);
        String name = node.get("name").asText().trim();
        Restaurant restaurant = _systemManager.getRestaurantByName(name);
        System.out.println(restaurant.toJson());
    }

    private static void getFood(String jsonData) throws IOException, InvalidToJsonException, RestaurantDoesntExistException, FoodDoesntExistException {
        JsonNode node = (new ObjectMapper()).readTree(jsonData);
        String foodName = node.get("foodName").asText().trim();
        String restaurantName = node.get("restaurantName").asText().trim();
        Food food = _systemManager.getFood(restaurantName, foodName);
        System.out.println(food.toJson());
    }

}
