package web.server;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;
import systemHandlers.SystemManager;

public class LoghmehServer {

    private static SystemManager _system;
    private Javalin _server;
    private boolean _started;
    private int _port = 8080;

    public LoghmehServer(){
        _started = false;
        _system = SystemManager.getInstance();
    }

    public LoghmehServer(int port){
        this();
        _port = port;
    }

    public void startServer(){
        if(_started){
            return;
        }
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
        _server.get("/profile/finilize", finalizeOrder());
    }

    private Handler getAllRestaurantsInRange(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {

            }
        };
    }

    private Handler getSpecificRestaurant(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String restaurantId = context.pathParam("id");

            }
        };
    }

    private Handler getUserProfile(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {

            }
        };
    }

    private Handler addUserCredit(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                int credit = Integer.parseInt(context.formParam("Credit"));
                // con.
                context.redirect("/profile");
            }
        };
    }

    private Handler addFoodToCart(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {
                String foodName = context.formParam("foodName");
                String restaurantId = context.formParam("restaurantId");
                // con.
                context.redirect("/restaurants/"+restaurantId);
            }
        };
    }

    private Handler getCart(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {

            }
        };
    }

    private Handler finalizeOrder(){
        return new Handler() {
            @Override
            public void handle(@NotNull Context context) throws Exception {

            }
        };
    }
}
