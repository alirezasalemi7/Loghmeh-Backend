package org.kharchal.co;

import exceptions.InvalidInputInstructionException;
import structures.Food;
import structures.Restaurant;
import systemHandlers.SystemManager;

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

}
