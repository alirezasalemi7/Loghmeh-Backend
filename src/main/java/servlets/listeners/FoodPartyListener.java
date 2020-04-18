package servlets.listeners;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import exceptions.*;
import models.Food;
import models.Restaurant;
import models.SpecialFood;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import systemHandlers.DataHandler;
import systemHandlers.Repositories.RestaurantRepository;
import systemHandlers.Services.RestaurantManager;
import systemHandlers.SystemManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class FoodPartyListener implements ServletContextListener {

    ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
//        TODO: change the period to 30 min at the end.
        scheduler.scheduleAtFixedRate(new FoodPartyInfoGetter(), 0, 3, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        scheduler.shutdown();
    }

    private class FoodPartyInfoGetter implements Runnable {

        @Override
        public void run() {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet("http://138.197.181.131:8080/foodparty");
            try {
                HttpResponse response = client.execute(getRequest);
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.err.println("Failed to obtain food Party information from external server. Error code: " + response.getStatusLine().getStatusCode());
                    System.exit(1);
                }
                RestaurantRepository.getInstance().terminatePreviousFoodParty();
                RestaurantManager.getInstance().setFoodPartyStartTime(Calendar.getInstance().getTime());
                HashMap<String, Boolean> systemRestaurants = RestaurantRepository.getInstance().getAllRestaurantIds();
                ArrayList<RestaurantDAO> newRestaurants = new ArrayList<>();
                ArrayList<FoodDAO> newFoods = new ArrayList<>();
                ArrayList<Restaurant> restaurants = externalServerBodyParser(getInputString(response.getEntity().getContent()));
                for (Restaurant restaurant : restaurants) {
                    for (SpecialFood food : restaurant.getSpecialMenu().values())
                        newFoods.add(new FoodDAO(food.getRestaurantId(), restaurant.getName(), food.getImageAddress(), food.getPopularity()
                                , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice()));
                    if (!systemRestaurants.getOrDefault(restaurant.getId(), false))
                        newRestaurants.add(new RestaurantDAO(restaurant.getName(), restaurant.getLogoAddress()
                                , restaurant.getLocation(), restaurant.getId()));
                }
                RestaurantRepository.getInstance().addFoods(newFoods);
                RestaurantRepository.getInstance().addRestaurants(newRestaurants);
            } catch (ClientProtocolException e) {
                System.err.println("A protocol error happened.\nexiting...");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("An IO error happened while getting response.\nexiting...");
                System.exit(1);
            } catch (ServerInternalException e) {
                System.err.println("An internal server error happened; database connection has been lost.\nexiting...");
                System.exit(1);
            }
        }

        private String getInputString(InputStream is) throws IOException {
            String line = "";
            StringBuilder input = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            while ((line = bufferedReader.readLine()) != null)
                input.append(line);
            return input.toString();
        }

        private ArrayList<Restaurant> externalServerBodyParser(String jsonBody){
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Restaurant> restaurants = new ArrayList<>();
            try {
                JsonNode root = mapper.readTree(jsonBody);
                if(root.isArray()) {
                    for(JsonNode restaurant : root) {
                        String utf8 = new String(restaurant.toString().getBytes(),"UTF_8");
                        Restaurant encodedObject = Restaurant.deserializeFromJson(utf8);
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

    }
}
