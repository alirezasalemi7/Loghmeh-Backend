package dataAccess.Repositories;

import dataAccess.DAO.CartDAO;
import dataAccess.DAO.CartItemDAO;
import dataAccess.DAO.UserDAO;
import dataAccess.Mappers.CartItemMapper;
import dataAccess.Mappers.CartMapper;
import dataAccess.Mappers.UserMapper;
import business.exceptions.ServerInternalException;
import business.exceptions.UserDoesNotExistException;
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
            e.printStackTrace();
            throw new  ServerInternalException();
        }
    }

    public static UserRepository getInstance() throws ServerInternalException{
        if(instance==null){
            instance = new UserRepository();
        }
        return instance;
    }

    public void AddUser(UserDAO user) throws ServerInternalException{
        try {
            userMapper.insert(user);
            CartDAO cart = new CartDAO();
            cart.setRestaurantId(null);
            cart.setUserId(user.getId());
            cartMapper.insert(cart);
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public UserDAO getUser(String id) throws UserDoesNotExistException, ServerInternalException {
        try {
            UserDAO user = userMapper.find(id);
            if(user == null)
                throw new UserDoesNotExistException();
            return user;
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerInternalException();
        }
   }

    public void addCartItemToCart(CartItemDAO item) throws ServerInternalException {
        try {
            cartItemMapper.insert(item);
        }
        catch (SQLException e){
            e.printStackTrace();
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void updateCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            cartItemMapper.updateItem(item);
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void updateCart(CartDAO cart) throws ServerInternalException {
        try {
            cartMapper.updateRestaurantIdOfCart(cart.getUserId(), cart.getRestaurantId());
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void removeCartItem(CartItemDAO item) throws ServerInternalException {
        try {
            cartItemMapper.delete(new Quartet<>(item.getCartId(), item.getFoodName(), item.getRestaurantId(), item.isSpecial()));
        }
        catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public void resetCart(String userId) throws ServerInternalException {
        try {
            cartMapper.resetCart(userId);
            cartItemMapper.RemoveAllItemsOfCart(userId);
        }
        catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }

    public UserDAO getUserByEmail(String email) throws UserDoesNotExistException,ServerInternalException{
        try {
            UserDAO user = userMapper.getUserByEmail(email);
            if(user == null)
                throw new UserDoesNotExistException();
            return user;
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerInternalException();
        }
    }
}
