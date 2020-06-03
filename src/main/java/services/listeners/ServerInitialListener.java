package services.listeners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataAccess.DAO.FoodDAO;
import dataAccess.DAO.RestaurantDAO;
import business.exceptions.InvalidJsonInputException;
import business.exceptions.ServerInternalException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import dataAccess.Repositories.RestaurantRepository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

@WebListener
public class ServerInitialListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("starting ...");
            fetchFromExternalServer();
            System.out.println("started.");
        } catch (ServerInternalException e) {
            System.err.println("An internal server error happened. maybe the sql connection is lost.\nexiting...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private String sendGetRequestToGetDataOnStart(){
        StringBuilder jsonBody = new StringBuilder();
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("http://138.197.181.131:8080/restaurants");
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode()!=200){
                System.out.println("cannot get data from external server. terminate.");
                System.exit(1);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = reader.readLine()) != null){
                jsonBody.append(line);
            }
            return jsonBody.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    private ArrayList<RestaurantDAO> externalServerBodyParser(String jsonBody){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(jsonBody);
            if(root.isArray()){
                for(JsonNode restaurant : root){
                    String utf8 = new String(restaurant.toString().getBytes(),"UTF-8");
                    RestaurantDAO encodedObject = RestaurantDAO.deserializeFromJson(utf8);
                    restaurants.add(encodedObject);
                }
            }
            return restaurants;
        }
        catch (IOException | InvalidJsonInputException e){
            System.err.println("invalid input from external server. terminated.");
            System.exit(1);
            return null;
        }
    }

    private void fetchFromExternalServer() throws ServerInternalException {
        ArrayList<RestaurantDAO> restaurants = externalServerBodyParser(sendGetRequestToGetDataOnStart());
        HashMap<String, Boolean> systemRestaurants = RestaurantRepository.getInstance().getAllRestaurantIds();
        ArrayList<RestaurantDAO> newRestaurants = new ArrayList<>();
        ArrayList<FoodDAO> newFoods = new ArrayList<>();
        for (RestaurantDAO restaurant : restaurants) {
            HashMap<String, Boolean> restaurantFoods = RestaurantRepository.getInstance().getNormalFoodIds(restaurant.getId());
            for (FoodDAO food : restaurant.getMenu())
                if (!restaurantFoods.getOrDefault(food.getName(), false))
                    newFoods.add(food);
            if (!systemRestaurants.getOrDefault(restaurant.getId(), false))
                newRestaurants.add(restaurant);
        }
        RestaurantRepository.getInstance().addRestaurants(newRestaurants);
        RestaurantRepository.getInstance().addFoods(newFoods);
    }
}
