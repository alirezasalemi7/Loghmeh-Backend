package structures;

import exceptions.InvalidToJsonException;
import exceptions.UnregisteredOrderException;

public class User {
    private Cart _cart;
    private Location _location;
    private String _name;
    private String _family;
    private String _phoneNumber;
    private String _email;
    private Double _credit;

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getFamily() {
        return _family;
    }

    public void setFamily(String _family) {
        this._family = _family;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public Double getCredit() {
        return _credit;
    }

    public void setCredit(Double _credit) {
        this._credit = _credit;
    }

    public User(Location location, String name, String family, String phoneNumber, String email, Double credit) {
        this._cart = new Cart();
        this._location = location;
        this._name = name;
        this._family = family;
        this._phoneNumber = phoneNumber;
        this._email = email;
        this._credit = credit;
    }

    public void addToCart(Food food, String restaurantId) throws UnregisteredOrderException {
        _cart.addOrder(food ,restaurantId);
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
