package models;

import exceptions.InvalidPopularityException;
import exceptions.InvalidPriceException;

public class NormalFood extends Food {

    public NormalFood() {
        super();
    }

    public NormalFood(String name, String description, double popularity, double price, String imageAddress, String restaurantId) throws InvalidPopularityException, InvalidPriceException {
        super(name, description, popularity, price, imageAddress, restaurantId);
    }

    @Override
    public String toJson() {
        return null;
    }

    @Override
    public Food deserializeFromJson(String jsonData) {
        return null;
    }

}
