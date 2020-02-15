package org.kharchal.co.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import structures.Restaurant;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;
import web.html.HtmlPageMaker;
import web.server.LoghmehServer;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ServerTest {

    private LoghmehServer server;
    private String restaurantId1,restaurantId2;
    private Restaurant restaurant1,restaurant2;

    @BeforeClass
    public void initial(){
        server = new LoghmehServer();
        server.startServer();
    }

    @Before
    public void setup(){
        SystemManager system = SystemManager.getInstance();
        ArrayList<Restaurant> restaurants = system.getInRangeRestaurants(DataHandler.getInstance().getUser());
        restaurant1 = restaurants.get(restaurants.size()-1);
        restaurant2 = restaurants.get(restaurants.size()-2);
        restaurantId1 = restaurant1.getId();
        restaurantId2 = restaurant2.getId();
    }

    @Test
    public void restaurantInfoTest(){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("127.0.0.1:8080/restaurants/"+restaurantId1);
        try {
            HttpResponse response = client.execute(httpGet);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            StringBuilder builder = new StringBuilder();
            char[] temp = new char[1024];
            while (reader.read(temp)!=-1){
                builder.append(temp);
            }
            String html = builder.toString().trim();
            HtmlPageMaker pageMaker = new HtmlPageMaker();
            String expectedPage = pageMaker.makeRestaurantPage(restaurant1);
            assertEquals(expectedPage.trim(), html);
        }
        catch (IOException e){
            assertTrue(false);
        }
    }
}
