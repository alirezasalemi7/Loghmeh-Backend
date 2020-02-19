package servlets.listeners;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.DeliveryMan;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class DeliveryManInitialListener implements ServletContextListener {

    ScheduledExecutorService scheduler;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DeliveryManGetterFromServer(), 0, 30,TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        return;
    }

    private class DeliveryManGetterFromServer implements Runnable {

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
                DataHandler.getInstance().setDeliveries(deliveryMEN);
                SystemManager.getInstance().assignOrdersToDeliveries();
            }
            catch (IOException e){
                System.err.println("external server not responding.\nexit.");
                System.exit(1);
            }
        }
    }
}
