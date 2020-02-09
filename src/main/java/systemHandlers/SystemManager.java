package systemHandlers;

import exceptions.FoodIsRegisteredException;
import exceptions.RestaurantDoesntExistException;
import exceptions.RestaurantIsRegisteredException;
import structures.Food;
import structures.Restaurant;

import java.util.ArrayList;

public class SystemManager {


    private static SystemManager _instance;
    private DataHandler _dataHandler;

    private SystemManager() {
        _dataHandler = DataHandler.getInstance();
    }

    public static SystemManager getInstance() {
        if (_instance == null) {
            _instance = new SystemManager();
        }
        return _instance;
    }

}
