package servlets.listeners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidJsonInputException;
import exceptions.RestaurantIsRegisteredException;
import models.Restaurant;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import systemHandlers.SystemManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServerInitialListener implements ServletContextListener {

    private SystemManager _system = SystemManager.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        fetchFromExternalServer();
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

    private ArrayList<Restaurant> externalServerBodyParser(String jsonBody){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(jsonBody);
            if(root.isArray()){
                for(JsonNode restaurant : root){
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

    private void fetchFromExternalServer(){
        ArrayList<Restaurant> restaurants = externalServerBodyParser(sendGetRequestToGetDataOnStart());
        try {
            for (Restaurant restaurant : restaurants){
                _system.addRestaurant(restaurant);
            }
        }
        catch (RestaurantIsRegisteredException e){
            System.err.println("duplicated restaurant from external server. terminate.");
            System.exit(1);
        }
    }
}
