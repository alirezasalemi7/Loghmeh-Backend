package web.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import models.Food;
import models.OrderItem;
import models.Restaurant;
import systemHandlers.DataHandler;
import systemHandlers.SystemManager;
import web.html.HtmlPageMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoghmehServer {

    private static SystemManager _system;
    private Javalin _server;
    private boolean _started;
    private int _port = 8080;
    private HtmlPageMaker _pageMaker;

    public LoghmehServer(){
        _started = false;
        _system = SystemManager.getInstance();
        _pageMaker = new HtmlPageMaker();
    }

    public LoghmehServer(int port){
        this();
        _port = port;
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

    public void startServer(){
        if(_started){
            return;
        }
        fetchFromExternalServer();
        _server = Javalin.create().start(_port);
        setRoutes();
    }

    private void setRoutes(){
        _server.get("/restaurants", getAllRestaurantsInRange());
        _server.get("/restaurants/:id", getSpecificRestaurant());
        _server.get("/profile", getUserProfile());
        _server.post("/profile/addcredit", addUserCredit());
        _server.post("/profile/addtocart", addFoodToCart());
        _server.get("/profile/cart", getCart());
        _server.get("/profile/finalize", finalizeOrder());
    }

    private Handler getAllRestaurantsInRange(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                ArrayList<Restaurant> restaurants = _system.getInRangeRestaurants(DataHandler.getInstance().getUser());
                String html = _pageMaker.makeAllRestaurantsPage(restaurants);
                context.status(200);
                context.html(html);
            }
        };
    }

    private Handler getSpecificRestaurant(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String restaurantId = context.pathParam("id");
                try {
                    boolean isInRange = _system.isRestaurantInRange(DataHandler.getInstance().getUser(), restaurantId);
                    if(isInRange){
                        Restaurant restaurant = _system.getRestaurantById(restaurantId);
                        String html = _pageMaker.makeRestaurantPage(restaurant);
                        context.status(200);
                        context.html(html);
                    }
                    else{
                        String html = _pageMaker.makeInvalidRestaurantAccessPage(restaurantId);
                        context.status(403);
                        context.html(html);
                    }
                }
                catch (RestaurantDoesntExistException e){
                    String html = _pageMaker.makeRestaurantNotFoundPage(restaurantId);
                    context.status(404);
                    context.html(html);
                }
            }
        };
    }

    private Handler getUserProfile(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String html = _pageMaker.makeProfilePage(DataHandler.getInstance().getUser(),false,false);
                context.status(200);
                context.html(html);
            }
        };
    }

    private Handler addUserCredit(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String creditString = context.formParam("credit");
                if(creditString==null){
                    context.status(400);
                    System.err.println(context.formParamMap());
                    context.html(_pageMaker.makeInvalidRequestPage("/profile/addcredit"));
                    return;
                }
                try {
                    double credit = Double.parseDouble(creditString);
                    _system.increaseCredit(DataHandler.getInstance().getUser(), credit);
                    context.status(200);
                    context.html(_pageMaker.makeProfilePage(DataHandler.getInstance().getUser(), false, true));
                }
                catch (NumberFormatException e){
                    context.status(400);
                    context.html(_pageMaker.makeInvalidRequestPage("/profile/addcredit"));
                }
                catch (NegativeChargeAmountException e){
                    context.status(400);
                    context.html(_pageMaker.makeProfilePage(DataHandler.getInstance().getUser(), true,false));
                }
            }
        };
    }

    private Handler addFoodToCart(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String foodName = context.formParam("foodName");
                String restaurantId = context.formParam("restaurantId");
                String restaurantName = "";
                if(foodName == null || restaurantId == null){
                    context.status(400);
                    context.html(_pageMaker.makeInvalidRequestPage("/profile/addtocart"));
                    return;
                }
                try {
                    if(!_system.isRestaurantInRange(DataHandler.getInstance().getUser(), restaurantId)){
                        String html = _pageMaker.makeInvalidRestaurantAccessPage(restaurantId);
                        context.status(403);
                        context.html(html);
                        return;
                    }
                    Restaurant restaurant = _system.getRestaurantById(restaurantId);
                    restaurantName = restaurant.getName();
                    Food food = restaurant.getFoodByName(foodName);
                    _system.addToCart(food, DataHandler.getInstance().getUser());
                    context.status(200);
                    context.redirect("/restaurants/"+restaurantId);
                }
                catch (RestaurantDoesntExistException e){
                    String html = _pageMaker.makeRestaurantNotFoundPage(restaurantId);
                    context.status(404);
                    context.html(html);
                }
                catch (FoodDoesntExistException e){
                    String html = _pageMaker.makeFoodNotFoundPage(foodName, restaurantName,restaurantId);
                    context.status(404);
                    context.html(html);
                }
                catch (UnregisteredOrderException e){
                    String html = _pageMaker.makeMultipleRestaurantAddToCartErrorPage(DataHandler.getInstance().getUser());
                    context.status(400);
                    context.html(html);
                }
            }
        };
    }

    private Handler getCart(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String restaurantId;
                String restaurantName = null;
                try {
                    restaurantId = DataHandler.getInstance().getUser().getCart().getRestaurantId();
                    if(restaurantId!=null){
                        restaurantName = _system.getRestaurantById(restaurantId).getName();
                    }
                }
                catch (RestaurantDoesntExistException e){
                    // never reach there
                }
                String html = _pageMaker.makeCartPage(DataHandler.getInstance().getUser(),restaurantName);
                context.status(200);
                context.html(html);
            }
        };
    }

    private Handler finalizeOrder(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String html;
                String restaurantId;
                String restaurantName = null;
                try {
                    restaurantId = DataHandler.getInstance().getUser().getCart().getRestaurantId();
                    if(restaurantId!=null){
                        restaurantName = _system.getRestaurantById(restaurantId).getName();
                    }
                }
                catch (RestaurantDoesntExistException e){
                    // never reach there
                }
                try {

                    ArrayList<OrderItem> orderItems = _system.finalizeOrder(DataHandler.getInstance().getUser());
                    html = _pageMaker.makeOrderFinalizedPage(orderItems, DataHandler.getInstance().getUser(), restaurantName);
                    context.status(200);
                    context.html(html);
                }
                catch (CartIsEmptyException e){
                    html = _pageMaker.makeCartEmptyErrorPage();
                    context.status(400);
                    context.html(html);
                }
                catch (CreditIsNotEnoughException e){
                    html = _pageMaker.makeNotEnoughCreditPage(DataHandler.getInstance().getUser());
                    context.status(400);
                    context.html(html);
                }
            }
        };
    }
}
