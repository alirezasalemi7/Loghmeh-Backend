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
    private UserMapper userMapper;
    private CartMapper cartMapper;
    private CartItemMapper cartItemMapper;

    private UserRepository() throws ServerInternalException{
        try {
            userMapper = new UserMapper();
            cartMapper = new CartMapper();
            cartItemMapper = new CartItemMapper();
        }
        catch (SQLException e){
            throw new  ServerInternalException();
        }
    }

    public static UserRepository getInstance() throws ServerInternalException{
        if(instance==null){
            instance = new UserRepository();
        }
        return instance;
    }

    public UserDAO getUser(String id) throws UserDoesNotExistException, ServerInternalException {
        try {
            UserDAO user = userMapper.find(id);
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
            cartItemMapper.insert(item);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void updateCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            cartItemMapper.updateItem(item);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void updateCart(CartDAO cart) throws ServerInternalException {
        try {
            cartMapper.updateRestaurantIdOfCart(cart.getUserId(), cart.getUserId());
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void removeCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            cartItemMapper.delete(new Quartet<>(item.getCartId(), item.getFoodName(), item.getRestaurantId(), item.isSpecial()));
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
            userMapper.updateCredit(user.getId(), user.getCredit());
        }catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public void resetCart(String userId) throws ServerInternalException {
        try {
            cartMapper.resetCart(userId);
            cartItemMapper.RemoveAllItemsOfCart(userId);
        }
        catch (SQLException e){
            throw new ServerInternalException();
        }
    }

    public CartDAO getUserCart(String userId) throws UserDoesNotExistException, ServerInternalException {
        try {
            CartDAO cart = cartMapper.find(userId);
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
