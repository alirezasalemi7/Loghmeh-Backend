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
        try {
            UserMapper mapper = new UserMapper();
            User user = mapper.find(id);
            return user;
        }
        catch (SQLException e){
            return null;
        }
    }

    public void updateCredit(UserDAO user) throws NegativeChargeAmountException {
        try {
            UserMapper mapper = new UserMapper();
            mapper.updateCredit(user);
        }
        catch (SQLException e){

        }
    }

    public Cart getUserCart(String userId){
        return null;
    }


}
