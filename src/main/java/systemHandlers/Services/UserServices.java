package systemHandlers.Services;

import database.DAO.*;
import database.UserMapper;
import exceptions.*;
import models.Order;
import models.OrderItem;
import models.Restaurant;
import models.User;
import org.apache.commons.lang.RandomStringUtils;
import restAPI.DTO.Cart.CartDTO;
import restAPI.DTO.Cart.CartItemDTO;
import restAPI.DTO.Order.OrderDTO;
import restAPI.DTO.Order.OrderDetailDTO;
import restAPI.DTO.Order.OrderItemDTO;
import restAPI.DTO.User.UserProfileDTO;
import systemHandlers.DataHandler;
import systemHandlers.Repositories.OrderRepository;
import systemHandlers.Repositories.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserServices {

    private static UserServices instance;

    public static UserServices getInstance(){
        if(instance==null){
            instance = new UserServices();
        }
        return instance;
    }

    private UserServices(){}

    public UserProfileDTO getUserProfile(String id) throws UserDoesNotExistException {
        UserDAO user = UserRepository.getInstance().getUser(id);
        UserProfileDTO dto = new UserProfileDTO();
        dto.setCredit(user.getCredit());
        dto.setEmail(user.getEmail());
        dto.setFamily(user.getFamily());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    public void increaseCredit(String userId, Double chargeAmount) throws NegativeChargeAmountException,UserDoesNotExistException {
        if (chargeAmount <= 0) {
            throw new NegativeChargeAmountException("Your charge amount must be positive.");
        }
        UserDAO user = UserRepository.getInstance().getUser(userId);
        user.setCredit(user.getCredit() + chargeAmount);
        UserRepository.getInstance().updateCredit(user);
    }

    private OrderDetailDTO makeOrderDetailDTO(OrderDAO order){
        OrderDetailDTO detailDTO = new OrderDetailDTO();
        detailDTO.setTotalCost(order.getTotalCost());
        ArrayList<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for(OrderItemDAO item : order.getItems()){
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setCost(item.getCost());
            itemDTO.setCount(item.getCount());
            itemDTO.setName(item.getFoodName());
            orderItemDTOS.add(itemDTO);
        }
        detailDTO.setOrder(orderItemDTOS);
        return detailDTO;
    }

    public ArrayList<OrderDTO> getAllOrders(String userId) throws UserDoesNotExistException{
        if(UserRepository.getInstance().isUserExists(userId)){
            throw new UserDoesNotExistException();
        }
        ArrayList<OrderDAO> orders = OrderRepository.getInstance().getOrdersOfUser(userId);
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        for(OrderDAO order:orders){
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setOrderStatus(order.getState());
            dto.setRestaurantName(order.getRestaurantName());
            dto.setDetails(makeOrderDetailDTO(order));
        }
        return orderDTOS;
    }

    public OrderDetailDTO getOrder(String oid) throws OrderDoesNotExist {
        OrderDAO orderDAO = OrderRepository.getInstance().getOrder(oid);
        return makeOrderDetailDTO(orderDAO);
    }

    public CartDTO getUserCart(String userId) throws UserDoesNotExistException{
        if(UserRepository.getInstance().isUserExists(userId)){
            throw new UserDoesNotExistException();
        }
        CartDAO cartDAO = UserRepository.getInstance().getUserCart(userId);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCost(cartDAO.getSumOfPrices());
        ArrayList<CartItemDTO> itemDTOS = new ArrayList<>();
        for (CartItemDAO itemDAO : cartDAO.getItems().values()){
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCost(itemDAO.getCost());
            cartItemDTO.setCount(itemDAO.getCount());
            cartItemDTO.setFood(itemDAO.getFoodName());
            cartItemDTO.setRestaurant(itemDAO.getRestaurantId());
            cartItemDTO.setSpecial(itemDAO.isSpecial());
            itemDTOS.add(cartItemDTO);
        }
        cartDTO.setOrders(itemDTOS);
        return cartDTO;
    }

    public void addToCart(String userId,String foodName,String RestaurantId,boolean special) throws UserDoesNotExistException{

    }

    public void removeFromCart(String userId,String foodName,String RestaurantId,boolean special) throws UserDoesNotExistException{

    }

    public OrderDetailDTO finalizeCart(String userId) throws CartIsEmptyException,UserDoesNotExistException,CreditIsNotEnoughException{
        UserDAO user = UserRepository.getInstance().getUser(userId);
        CartDAO cart = UserRepository.getInstance().getUserCart(userId);
        if (cart.getItems().size() == 0) {
            throw new CartIsEmptyException("There isn't any order in your Cart.");
        } else if (cart.getSumOfPrices() > user.getCredit()) {
            throw new CreditIsNotEnoughException("Your credit is not enough.");
        }
        user.setCredit(user.getCredit() - cart.getSumOfPrices());
        try { UserRepository.getInstance().updateCredit(user); } catch (NegativeChargeAmountException e){}
        UserRepository.getInstance().emptyCart(userId);
        OrderDAO order = new OrderDAO();
        order.setState(OrderState.DeliveryManFinding);
        order.setId(RandomStringUtils.randomAlphanumeric(50));
        order.setRestaurantId(cart.getRestaurantId());
        order.setUserId(userId);
        order.setItems(new ArrayList(cart.getItems().values()));
        OrderRepository.getInstance().addOrder(order);
        // todo: get restaurant location
        OrderDeliveryManager.getInstance().addOrderToDeliver(order, null, user.getLocation());
        return makeOrderDetailDTO(order);
    }

}
