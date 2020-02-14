package models;

import exceptions.CartIsEmptyException;
import exceptions.CreditIsNotEnoughException;
import exceptions.UnregisteredOrderException;

import java.util.ArrayList;

public class User {
    private Cart _cart;
    private Location _location;
    private String _name;
    private String _family;
    private String _phoneNumber;
    private String _email;
    private Double _credit;
    private String _id;

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

    public ArrayList<OrderItem> finalizeOrder() throws CartIsEmptyException, CreditIsNotEnoughException {
        if (_cart.getOrders().size() == 0) {
            throw new CartIsEmptyException("There isn't any order in your Cart.");
        } else if (_cart.getSumOfPrices() > _credit) {
            throw new CreditIsNotEnoughException("Your credit is not enough.");
        }
        _credit -= _cart.getSumOfPrices();
        ArrayList<OrderItem> orders = _cart.getOrders();
        _cart.clearCart();
        return orders;
    }

}
