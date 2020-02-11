package structures;

import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;

public class User {
    private Cart _cart;
    private Location _location;

    public User(Location location) {
        this._cart = new Cart();
        this._location = location;
    }

    public void addToCart(Food food, String restaurantName) throws UnregisteredOrderException {
        _cart.addOrder(food ,restaurantName);
    }

    public Cart getCart() {
        return _cart;
    }

    public Location getLocation(){
        return _location;
    }

    public String finalizeOrder() throws InvalidToJsonException {
        String json = _cart.toJson();
        _cart.clearCart();
        return json;
    }

}
