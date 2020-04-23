package business.Domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataAccess.DAO.OrderState;
import business.exceptions.ServerInternalException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import business.ServiceManagers.OrderDeliveryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class OrderTimer {

    private String orderId;
    private Date arrivalDate;
    private Location userLocation;
    private Location restaurantLocation;
    private Timer timer = new Timer();

    public OrderTimer(String orderId,Location userLocation,Location restaurantLocation){
        this.orderId = orderId;
        this.restaurantLocation = restaurantLocation;
        this.userLocation = userLocation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setArrivalDate(Date date){
        arrivalDate = date;
    }

    private class DeliveryTask extends TimerTask{
        @Override
        public void run() {
            try {
                OrderDeliveryManager.getInstance().updateOrderState(orderId, OrderState.Delivered, arrivalDate);
                OrderDeliveryManager.getInstance().clearOrderTimer(orderId);
            }
            catch (ServerInternalException e){
                timer.schedule(new DeliveryTask(), 1000);
            }
        }
    }

    public void setDeliveryTask(long time){
        timer.schedule(new OrderTimer.DeliveryTask(), time);
    }

    public void start(){
        timer.schedule(new FindDeliveryTask(), 1000);
    }

    private void changeStateToInRoad(int deliveryTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, deliveryTime);
        arrivalDate = calendar.getTime();
        try {
            OrderDeliveryManager.getInstance().updateOrderState(orderId, OrderState.InRoad, arrivalDate);
            timer.schedule(new DeliveryTask(), 1000*deliveryTime);
        }
        catch (ServerInternalException e){
            timer.schedule(new FindDeliveryTask(), 30000);
        }
    }

    protected class FindDeliveryTask extends TimerTask {

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
                double minTime = Double.MAX_VALUE;
                for (DeliveryMan deliveryMan : deliveryMEN){
                    double distance = deliveryMan.getLocation().getDistance(restaurantLocation)+restaurantLocation.getDistance(userLocation);
                    double tempTime = distance/deliveryMan.getVelocity();
                    if(tempTime<minTime){
                        minTime = tempTime;
                    }
                }
                changeStateToInRoad((int)minTime);
            }
            catch (IOException e){
                System.err.println("external server not responding.\nexit.");
            }
        }
    }
}
