package systemHandlers.Repositories;

import database.UserMapper;
import exceptions.NegativeChargeAmountException;
import models.User;

import java.sql.SQLException;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository(){}

    public static UserRepository getInstance(){
        if(instance==null){
            instance = new UserRepository();
        }
        return instance;
    }

    public User getUser(String id) {
        try {
            UserMapper mapper = new UserMapper();
            User user = mapper.find(id);
            return user;
        }
        catch (SQLException e){
            return null;
        }
    }

    public void increaseCredit(String userId, Double chargeAmount) throws NegativeChargeAmountException {
        try {
            User user = getUser(userId);
            UserMapper mapper = new UserMapper();
            user.setCredit(user.getCredit() + chargeAmount);
            mapper.updateCredit(user);
        }
        catch (SQLException e){

        }
    }
}
