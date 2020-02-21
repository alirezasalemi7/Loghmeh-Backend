package models;

import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;

public class SpecialFood extends Food {
    private double _currentPrice;
    int _count;

    public double getCurrentPrice() {
        return _currentPrice;
    }

    public void setCurrentPrice(double _currentPrice) {
        this._currentPrice = _currentPrice;
    }

    public int getCount() {
        return _count;
    }

    public void setCount(int _count) {
        this._count = _count;
    }

    public SpecialFood() {
        super();
    }

    @Override
    public String toJson() {
        return null;
    }

    @Override
    public Food deserializeFromJson(String jsonData) {
        return null;
    }

    public SpecialFood(String name, String description, double popularity, double price, String imageAddress, String restaurantId, double olderPrice, int count)
            throws InvalidPopularityException, InvalidPriceException {
        super(name, description, popularity, olderPrice, imageAddress, restaurantId);
        this._currentPrice = price;
        this._count = count;
    }

    public NormalFood changeToNormalFood() throws InvalidPopularityException, InvalidPriceException {
        return new NormalFood(super.getName(), super.getDescription(), super.getPopularity(), super.getPrice(), super.getImageAddress(), super.getRestaurantId());
    }

}
