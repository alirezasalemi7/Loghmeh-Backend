package systemHandlers.Repositories;

import database.DAO.CartDAO;
import database.DAO.CartItemDAO;
import database.DAO.UserDAO;
import database.Mappers.CartItemMapper;
import database.Mappers.CartMapper;
import database.Mappers.UserMapper;
import exceptions.ServerInternalException;
import exceptions.UserDoesNotExistException;
import org.javatuples.Quartet;

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

    public UserDAO getUser(String id) throws UserDoesNotExistException, ServerInternalException {
        try {
            UserMapper mapper = new UserMapper();
            UserDAO user = mapper.find(id);
            if(user==null){
                throw new UserDoesNotExistException();
            }
            return user;
        }catch (SQLException e){
            throw new ServerInternalException();
        }
   }

    public void addCartItemToCart(CartItemDAO item) throws ServerInternalException {
        try {
            CartItemMapper mapper = new CartItemMapper();
            mapper.insert(item);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void updateCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            CartItemMapper mapper = new CartItemMapper();
            mapper.updateItem(item);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void updateCart(CartDAO cart) throws ServerInternalException {
        try {
            CartMapper mapper = new CartMapper();
            mapper.updateRestaurantIdOfCart(cart.getUserId(), cart.getUserId());
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void removeCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            CartItemMapper mapper = new CartItemMapper();
            mapper.delete(new Quartet<>(item.getCartId(), item.getFoodName(), item.getRestaurantId(), item.isSpecial()));
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public boolean isUserExists(String userId) throws ServerInternalException {
        try {
            UserDAO user = this.getUser(userId);
            return true;
        }
        catch (UserDoesNotExistException e){
            return false;
        }
    }

    public void updateCredit(UserDAO user) throws ServerInternalException {
        try {
            UserMapper mapper = new UserMapper();
            mapper.updateCredit(user.getId(), user.getCredit());
        }catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void resetCart(String userId) throws ServerInternalException {
        try {
            CartMapper mapper = new CartMapper();
            mapper.resetCart(userId);
            CartItemMapper itemMapper = new CartItemMapper();
            itemMapper.RemoveAllItemsOfCart(userId);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public CartDAO getUserCart(String userId) throws UserDoesNotExistException, ServerInternalException {
        try {
            CartMapper mapper = new CartMapper();
            CartDAO cart = mapper.find(userId);
            if(cart == null){
                throw new UserDoesNotExistException();
            }
            return cart;
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }


}
