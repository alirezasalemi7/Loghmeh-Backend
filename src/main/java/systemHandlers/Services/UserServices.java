package systemHandlers.Services;

import database.UserMapper;
import exceptions.CartIsEmptyException;
import exceptions.CreditIsNotEnoughException;
import exceptions.NegativeChargeAmountException;
import exceptions.RestaurantDoesntExistException;
import models.Order;
import models.OrderItem;
import models.Restaurant;
import models.User;
import systemHandlers.DataHandler;
import systemHandlers.Repositories.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserServices {

    private static UserServices instance;

    public static UserServices getInstance(){
        if(instance==null){
            instance = new UserServices();
        }
        return instance;
    }

    private UserServices(){}

    public User getUser(String id) {
        return UserRepository.getInstance().getUser(id);
    }

    public void increaseCredit(String userId, Double chargeAmount) throws NegativeChargeAmountException {
        if (chargeAmount <= 0) {
            throw new NegativeChargeAmountException("Your charge amount must be positive.");
        }
        UserRepository.getInstance().increaseCredit(userId,chargeAmount);
    }

}
