package models;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Order {

    public static enum OrderState {DeliveryManFinding,InRoad,Delivered}
    private String id;
    private OrderState state;
    private DeliveryMan deliveryMan;
    private ArrayList<OrderItem> items;
    private User user;
    private Restaurant restaurant;
    private Timer timer = new Timer();
    private Date arrivalDate = null;

    public Order(ArrayList<OrderItem> items,User user,Restaurant restaurant,String id){
        this.id = id;
        this.state = OrderState.DeliveryManFinding;
        this.deliveryMan = null;
        this.items = items;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Order(ArrayList<OrderItem> items,User user,Restaurant restaurant){
        this.id = RandomStringUtils.randomAlphanumeric(50);
        this.state = OrderState.DeliveryManFinding;
        this.deliveryMan = null;
        this.items = items;
        this.user = user;
        this.restaurant = restaurant;
    }


    public String getId() {
        return id;
    }

    public OrderState getState() {
        return state;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    private void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
        changeStateToInRoad();
    }



    public void searchForDelivery(){
        if(deliveryMan==null){
            timer.schedule(new FindDeliveryTask(), 0);
        }
    }

    protected class FindDeliveryTask extends TimerTask{

        @Override
        public void run() {
            StringBuilder jsonBody = new StringBuilder();
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("http://138.197.181.131:8080/deliveries");
            try {
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
                ArrayList<DeliveryMan> deliveryMEN = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode nodes = mapper.readTree(jsonBody.toString());
                for(JsonNode node : nodes){
                    DeliveryMan deliveryMan = mapper.readValue(node.toString(), DeliveryMan.class);
                    deliveryMEN.add(deliveryMan);
                }
                if(deliveryMEN.size()==0){
                    timer.schedule(new FindDeliveryTask(), 30000);
                    return;
                }
                DeliveryMan minDistanceDeliveryMan = null;
                double minTime = Double.MAX_VALUE;
                for (DeliveryMan deliveryMan : deliveryMEN){
                    double distance = deliveryMan.getLocation().getDistance(restaurant.getLocation())+restaurant.getLocation().getDistance(user.getLocation());
                    double tempTime = distance/deliveryMan.getVelocity();
                    if(tempTime<minTime){
                        minDistanceDeliveryMan = deliveryMan;
                    }
                }
                setDeliveryMan(minDistanceDeliveryMan);
            }
            catch (IOException e){
                System.err.println("external server not responding.\nexit.");
            }
        }
    }

    private class DeliveryTask extends TimerTask{

        @Override
        public void run() {
            state = OrderState.Delivered;
        }
    }

    private void changeStateToInRoad(){
        this.state = OrderState.InRoad;
        double distance = restaurant.getLocation().getDistance(user.getLocation())+restaurant.getLocation().getDistance(deliveryMan.getLocation());
        int deliveryTime = (int) (distance/deliveryMan.getVelocity());
        timer.schedule(new DeliveryTask(), 1000*deliveryTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, deliveryTime);
        this.arrivalDate = calendar.getTime();
    }

    public Restaurant getRestaurant(){
        return this.restaurant;
    }

    public User getUser(){
        return this.user;
    }

    public double getTotalCost(){
        double cost = 0;
        for(OrderItem item : items){
            cost += item.getPrice();
        }
        return cost;
    }

    public String toJson(){
        return this.toJsonNode().asText();
    }

    public ObjectNode toJsonNode() {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("totalCost", this.getTotalCost());
        ArrayNode orders = JsonNodeFactory.instance.arrayNode();
        for(OrderItem orderItem : this.items){
            ObjectNode orderItemJson = JsonNodeFactory.instance.objectNode();
            orderItemJson.put("name", orderItem.getFood().getName());
            orderItemJson.put("count", orderItem.getCount());
            orderItemJson.put("cost", orderItem.getPrice());
            orders.add(orderItemJson);
        }
        json.put("order",orders);
        return json;
    }
}
