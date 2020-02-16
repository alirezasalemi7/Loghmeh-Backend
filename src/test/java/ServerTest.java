import models.OrderItem;
import models.Restaurant;
import models.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;
import web.html.HtmlPageMaker;
import web.server.LoghmehServer;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ServerTest {

    private static LoghmehServer server;
    private String restaurantId1,restaurantId2,restaurantOutOfBoundId;
    private Restaurant restaurant1,restaurant2,restaurantOutOfBound;

    @BeforeClass
    public static void initial(){
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
        User user = DataHandler.getInstance().getUser();
        for(Restaurant restaurant : restaurants){
            if(user.getLocation().getDistance(restaurant.getLocation())>=170){
                restaurantOutOfBound = restaurant;
                restaurantOutOfBoundId = restaurant.getId();
                break;
            }
        }
    }

    @Test
    public void restaurantInfoTest1(){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/restaurants/"+restaurantId1);
        try {
            HttpResponse response = client.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String temp = null;
            while ((temp = reader.readLine())!=null){
                builder.append(temp);
            }
            String html = builder.toString().trim();
            HtmlPageMaker pageMaker = new HtmlPageMaker();
            String expectedPage = pageMaker.makeRestaurantPage(restaurant1);
            assertEquals(expectedPage.replaceAll("[ \t\n]", ""), html.replaceAll("[ \t\n]", ""));
        }
        catch (IOException e){
            assertTrue(false);
        }
    }

    @Test
    public void restaurantInfoTest2(){
        if(restaurantOutOfBound==null){
            return;
        }
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/restaurants/"+restaurantOutOfBoundId);
        try {
            HttpResponse response = client.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String temp = null;
            while ((temp = reader.readLine())!=null){
                builder.append(temp);
            }
            String html = builder.toString().trim();
            HtmlPageMaker pageMaker = new HtmlPageMaker();
            String expectedPage = pageMaker.makeInvalidRestaurantAccessPage(restaurantOutOfBoundId);
            assertEquals(expectedPage.replaceAll("[ \t\n]", ""), html.replaceAll("[ \t\n]", ""));
        }
        catch (IOException e){
            assertTrue(false);
        }
    }

    @Test
    public void finalizeOrderTest() throws UnsupportedEncodingException, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost addCreditRequest = new HttpPost("http://127.0.0.1:8080/profile/addcredit");
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("credit","220000"));
        addCreditRequest.setEntity(new UrlEncodedFormEntity(parameters));
        HttpResponse response = client.execute(addCreditRequest);
        assertEquals(221000.0, DataHandler.getInstance().getUser().getCredit(),0);
        double totalPrice = 0;
        HttpPost addToCartRequest1 = new HttpPost("http://127.0.0.1:8080/profile/addtocart");
        List<NameValuePair> parameters2 = new ArrayList<>();
        parameters2.add(new BasicNameValuePair("foodName", restaurant1.getMenu().get(0).getName()));
        parameters2.add(new BasicNameValuePair("restaurantId", restaurantId1));
        addToCartRequest1.setEntity(new UrlEncodedFormEntity(parameters2,"UTF-8"));
        HttpResponse httpResponse = client.execute(addToCartRequest1);
        assertEquals(1, DataHandler.getInstance().getUser().getCart().getOrders().size());
        assertEquals(restaurant1.getMenu().get(0).getName(), DataHandler.getInstance().getUser().getCart().getOrders().get(0).getFoodName());
        totalPrice += restaurant1.getMenu().get(0).getPrice();
        HttpPost addToCartRequest2 = new HttpPost("http://127.0.0.1:8080/profile/addtocart");
        List<NameValuePair> parameters3 = new ArrayList<>();
        parameters3.add(new BasicNameValuePair("foodName", restaurant2.getMenu().get(0).getName()));
        parameters3.add(new BasicNameValuePair("restaurantId", restaurantId2));
        addToCartRequest2.setEntity(new UrlEncodedFormEntity(parameters3,"UTF-8"));
        HttpResponse response1 = client.execute(addToCartRequest2);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
        StringBuilder builder1 = new StringBuilder();
        String temp1 = null;
        while ((temp1 = reader1.readLine())!=null){
            builder1.append(temp1);
        }
        String html1 = builder1.toString().trim();
        HtmlPageMaker pageMaker = new HtmlPageMaker();
        String expectedPage1 = pageMaker.makeMultipleRestaurantAddToCartErrorPage(DataHandler.getInstance().getUser());
        assertEquals(expectedPage1.replaceAll("[ \t\n]", ""), html1.replaceAll("[ \t\n]", ""));
        HttpGet finalizeRequest = new HttpGet("http://127.0.0.1:8080/profile/finalize");
        ArrayList<OrderItem> items =(ArrayList<OrderItem>) DataHandler.getInstance().getUser().getCart().getOrders().clone();
        HttpResponse response2 = client.execute(finalizeRequest);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(response2.getEntity().getContent()));
        StringBuilder builder2 = new StringBuilder();
        String temp2 = null;
        while ((temp2 = reader2.readLine())!=null){
            builder2.append(temp2);
        }
        String html2 = builder2.toString().trim();
        String expectedPage2 = pageMaker.makeOrderFinalizedPage(items,DataHandler.getInstance().getUser(),restaurant1.getName());
        assertEquals(expectedPage2.replaceAll("[ \t\n]", ""), html2.replaceAll("[ \t\n]", ""));
        assertEquals(221000.0-totalPrice, DataHandler.getInstance().getUser().getCredit(),0);
    }
}