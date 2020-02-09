package org.kharchal.co;

import exceptions.InvalidInputInstructionException;
import structures.Restaurant;
import systemHandlers.SystemManager;

import java.util.Scanner;

public class Main {

    static SystemManager _systemManager = SystemManager.getInstance();

    public static void main( String[] args ) {

    }

    private static void addRestaurant(String jsonData) throws Exception{
        Restaurant restaurant = Restaurant.deserializeFromJson(jsonData);
        _systemManager.addRestaurant(restaurant);
    }

}
