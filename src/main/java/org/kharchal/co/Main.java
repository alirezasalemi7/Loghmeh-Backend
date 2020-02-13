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
import web.server.LoghmehServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main( String[] args ) {
        LoghmehServer server = new LoghmehServer();
        server.startServer();
    }
}
