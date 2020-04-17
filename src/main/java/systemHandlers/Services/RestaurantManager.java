package systemHandlers.Services;

public class RestaurantServices {

    private RestaurantServices instance;

    private RestaurantServices() {}

    public RestaurantServices getInstance() {
        if (instance == null)
            instance = new RestaurantServices();
        return instance;
    }



}
