package servlets.listeners;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import models.Food;
import models.Restaurant;
import models.SpecialFood;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
                terminatePreviousFoodParty();
                SystemManager.getInstance().setFoodPartStartTime(java.util.Calendar.getInstance().getTime());
                HashMap<String, Restaurant> systemRestaurants = DataHandler.getInstance().getAllRestaurant();
                ArrayList<Restaurant> restaurants = externalServerBodyParser(getInputString(response.getEntity().getContent()));
                for (Restaurant restaurant : restaurants) {
                    Restaurant equivalent = systemRestaurants.getOrDefault(restaurant.getId(), restaurant);
                    try {
                        SystemManager.getInstance().addRestaurant(equivalent);
                    } catch (RestaurantIsRegisteredException e) {
                        for (SpecialFood food : restaurant.getSpecialMenu().values()) {
                            equivalent.addFood(food);
                        }
                    }
                }
            } catch (Exception e) {
                //
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
                        String utf8 = new String(restaurant.toString().getBytes(),"UTF-8");
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

        private void terminatePreviousFoodParty(){
            HashMap<String, Restaurant> restaurants = DataHandler.getInstance().getAllRestaurant();
            for (Map.Entry<String, Restaurant> entry : restaurants.entrySet()) {
                Restaurant restaurant = entry.getValue();
                for (SpecialFood food : restaurant.getSpecialMenu().values()) {
                    try {
                        restaurant.addFood(food.changeToNormalFood());
                    } catch (FoodIsRegisteredException | InvalidPopularityException | InvalidPriceException e) {
                        // nothing
                    } finally {
                        restaurant.removeFood(food);
                    }
                }
            }
        }

    }
}
