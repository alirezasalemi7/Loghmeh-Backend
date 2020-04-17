package systemHandlers.Repositories;

import database.DAO.CartDAO;
import database.DAO.UserDAO;
import database.UserMapper;
import exceptions.NegativeChargeAmountException;
import exceptions.UserDoesNotExistException;
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

    public UserDAO getUser(String id) throws UserDoesNotExistException {
        return null;
    }

    public boolean isUserExists(String userId){
        return false;
    }

    public void updateCredit(UserDAO user) throws NegativeChargeAmountException {

    }

    public void emptyCart(String userId){

    }

    public CartDAO getUserCart(String userId){
        // todo: how to get cart;
        return null;
    }


}
