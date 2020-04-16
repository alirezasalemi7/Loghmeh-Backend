package systemHandlers.Repositories;

import database.DAO.UserDAO;
import database.UserMapper;
import exceptions.NegativeChargeAmountException;
import models.Cart;
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

    public UserDAO getUser(String id) {
        return null;
    }

    public void updateCredit(UserDAO user) throws NegativeChargeAmountException {

    }

    public Cart getUserCart(String userId){
        return null;
    }


}
