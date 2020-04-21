package systemHandlers.Services;

import database.DAO.*;
import exceptions.*;
import models.Location;
import org.apache.commons.lang.RandomStringUtils;
import restAPI.DTO.Cart.CartDTO;
import restAPI.DTO.Cart.CartItemDTO;
import restAPI.DTO.Order.OrderDTO;
import restAPI.DTO.Order.OrderDetailDTO;
import restAPI.DTO.Order.OrderItemDTO;
import restAPI.DTO.User.UserProfileDTO;
import systemHandlers.Repositories.OrderRepository;
import systemHandlers.Repositories.RestaurantRepository;
import systemHandlers.Repositories.UserRepository;
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

    public UserProfileDTO getUserProfile(String id) throws UserDoesNotExistException ,ServerInternalException{
        UserDAO user = UserRepository.getInstance().getUser(id);
        UserProfileDTO dto = new UserProfileDTO();
        dto.setCredit(user.getCredit());
        dto.setEmail(user.getEmail());
        dto.setFamily(user.getFamily());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    public void increaseCredit(String userId, Double chargeAmount) throws NegativeChargeAmountException,UserDoesNotExistException,ServerInternalException{
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

    public ArrayList<OrderDTO> getAllOrders(String userId) throws UserDoesNotExistException,ServerInternalException{
        if(!UserRepository.getInstance().isUserExists(userId)) {
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
            orderDTOS.add(dto);
        }
        return orderDTOS;
    }

    public OrderDetailDTO getOrder(String oid) throws OrderDoesNotExist,ServerInternalException {
        OrderDAO orderDAO = OrderRepository.getInstance().getOrder(oid);
        return makeOrderDetailDTO(orderDAO);
    }

    public CartDTO getUserCart(String userId) throws UserDoesNotExistException,ServerInternalException{
        if(!UserRepository.getInstance().isUserExists(userId)){
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

    public int addToCart(String userId,String foodName,String RestaurantId,boolean special,int count) throws UserDoesNotExistException , RestaurantDoesntExistException ,FoodDoesntExistException ,RestaurantOutOfRangeException, FoodCountIsNegativeException, UnregisteredOrderException,ServerInternalException{
        CartDAO cart = UserRepository.getInstance().getUserCart(userId);
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(RestaurantId, foodName, special);
        UserDAO user = UserRepository.getInstance().getUser(userId);
        if(!special &&!RestaurantManager.getInstance().isInRange(user.getLocation(),RestaurantManager.getInstance().getRestaurantLocation(RestaurantId))){
            throw new RestaurantOutOfRangeException();
        }
        if(cart.getRestaurantId()==null || RestaurantId.equals(cart.getRestaurantId())){
            if(cart.getRestaurantId()==null){
                cart.setRestaurantId(RestaurantId);
            }
            String id = food.getName();
            if(special){
                id = id + "@";
                if(food.getCount()>=count){
                    RestaurantManager.getInstance().setFoodCount(RestaurantId, foodName, food.getCount()-count);
                }
                else throw new FoodCountIsNegativeException("Food Count must be positive.");
            }
            if(cart.getItems().containsKey(id)){
                CartItemDAO cartItem = cart.getItems().get(id);
                cartItem.setCount(cartItem.getCount()+count);
                cartItem.setCost(cartItem.getCost()+count*food.getPrice());
                UserRepository.getInstance().updateCartItem(cartItem);
            }
            else{
                CartItemDAO cartItem = new CartItemDAO();
                cartItem.setCartId(cart.getUserId());
                cartItem.setFoodName(food.getName());
                cartItem.setRestaurantId(RestaurantId);
                cartItem.setCount(count);
                cartItem.setCost(count*food.getPrice());
                cartItem.setSpecial(special);
                UserRepository.getInstance().addCartItemToCart(cartItem);
            }
            UserRepository.getInstance().updateCart(cart);
            return (special)?food.getCount()-count:Integer.MAX_VALUE;
        }
        else throw new UnregisteredOrderException("You have some orders in your cart.");
    }

    public int removeFromCart(String userId,String foodName,String RestaurantId,boolean special) throws UserDoesNotExistException,RestaurantOutOfRangeException,RestaurantDoesntExistException,FoodDoesntExistException,FoodNotExistInCartException,ServerInternalException{
        CartDAO cart = UserRepository.getInstance().getUserCart(userId);
        FoodDAO food = null;
        if(special){
            try {
                food = RestaurantRepository.getInstance().getFoodById(RestaurantId, foodName, special);
            }
            catch (FoodDoesntExistException e){
                food = RestaurantRepository.getInstance().getFoodById(RestaurantId, foodName, !special);
            }
        }
        else food = RestaurantRepository.getInstance().getFoodById(RestaurantId, foodName, special);
        UserDAO user = UserRepository.getInstance().getUser(userId);
        if(!special &&!RestaurantManager.getInstance().isInRange(user.getLocation(),RestaurantManager.getInstance().getRestaurantLocation(RestaurantId))){
            throw new RestaurantOutOfRangeException();
        }
        String id = foodName;
        if(special){
            id += "@";
        }
        int count = Integer.MAX_VALUE;
        if(cart.getItems().containsKey(id)){
            CartItemDAO item = cart.getItems().get(id);
            if(special && food.isSpecial()){
                RestaurantManager.getInstance().setFoodCount(RestaurantId, foodName, food.getCount()+1);
                count = food.getCount()+1;
                if(item.getCount()==1){
                    UserRepository.getInstance().removeCartItem(item);
                    if(cart.getItems().size()==1){
                        UserRepository.getInstance().resetCart(userId);
                    }
                }
                else{
                    item.setCount(item.getCount()-1);
                    item.setCost(item.getCost()-food.getPrice());
                    UserRepository.getInstance().updateCartItem(item);
                }
            }
            else if(special && !food.isSpecial()){
                count = 0;
                if(item.getCount()==1){
                    UserRepository.getInstance().removeCartItem(item);
                    if(cart.getItems().size()==1){
                        UserRepository.getInstance().resetCart(userId);
                    }
                }
                else{
                    item.setCost(item.getCost()-(item.getCost()/item.getCount()));
                    item.setCount(item.getCount()-1);
                    UserRepository.getInstance().updateCartItem(item);
                }
            }
            else if (!special && !food.isSpecial()){
                if(item.getCount()==1){
                    UserRepository.getInstance().removeCartItem(item);
                    if(cart.getItems().size()==1){
                        UserRepository.getInstance().resetCart(userId);
                    }
                }
                else{
                    item.setCount(item.getCount()-1);
                    item.setCost(item.getCost()-food.getPrice());
                    UserRepository.getInstance().updateCartItem(item);
                }
            }
            return count;
        }
        else throw new FoodNotExistInCartException();
    }

    public OrderDetailDTO finalizeCart(String userId) throws CartIsEmptyException,UserDoesNotExistException,CreditIsNotEnoughException, ServerInternalException{
        UserDAO user = UserRepository.getInstance().getUser(userId);
        CartDAO cart = UserRepository.getInstance().getUserCart(userId);
        if (cart.getItems().size() == 0) {
            throw new CartIsEmptyException("There isn't any order in your Cart.");
        } else if (cart.getSumOfPrices() > user.getCredit()) {
            throw new CreditIsNotEnoughException("Your credit is not enough.");
        }
        user.setCredit(user.getCredit() - cart.getSumOfPrices());
        UserRepository.getInstance().updateCredit(user);
        UserRepository.getInstance().resetCart(userId);
        OrderDAO order = new OrderDAO();
        order.setState(OrderState.DeliveryManFinding);
        order.setId(RandomStringUtils.randomAlphanumeric(50));
        order.setRestaurantId(cart.getRestaurantId());
        order.setUserId(userId);
        ArrayList<OrderItemDAO> items = new ArrayList<>();
        for (CartItemDAO item : cart.getItems().values()){
            OrderItemDAO orderItem = new OrderItemDAO();
            orderItem.setCount(item.getCount());
            orderItem.setCost(item.getCost());
            orderItem.setSpecial(item.isSpecial());
            orderItem.setOrderId(order.getId());
            orderItem.setFoodName(item.getFoodName());
            orderItem.setRestaurantId(item.getRestaurantId());
            items.add(orderItem);
        }
        order.setItems(items);
        Location restaurantLocation = null;
        RestaurantDAO restaurant = null;
        try {
            restaurantLocation = RestaurantManager.getInstance().getRestaurantLocation(order.getRestaurantId());
            restaurant = RestaurantRepository.getInstance().getRestaurantById(cart.getRestaurantId());
        } catch (RestaurantDoesntExistException e) {
            // never reaches here
        }
        order.setRestaurantName(restaurant.getName());
        OrderRepository.getInstance().addOrder(order);
        OrderDeliveryManager.getInstance().addOrderToDeliver(order, restaurantLocation, user.getLocation());
        return makeOrderDetailDTO(order);
    }

}
