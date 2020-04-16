package systemHandlers.Services;

import database.DAO.OrderDAO;
import database.DAO.OrderItemDAO;
import database.DAO.UserDAO;
import database.UserMapper;
import exceptions.*;
import models.Order;
import models.OrderItem;
import models.Restaurant;
import models.User;
import restAPI.DTO.Order.OrderDTO;
import restAPI.DTO.Order.OrderDetailDTO;
import restAPI.DTO.Order.OrderItemDTO;
import restAPI.DTO.User.UserProfileDTO;
import systemHandlers.DataHandler;
import systemHandlers.Repositories.OrderRepository;
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

    public UserProfileDTO getUserProfile(String id) {
        UserDAO user = UserRepository.getInstance().getUser(id);
        UserProfileDTO dto = new UserProfileDTO();
        dto.setCredit(user.getCredit());
        dto.setEmail(user.getEmail());
        dto.setFamily(user.getFamily());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    public void increaseCredit(String userId, Double chargeAmount) throws NegativeChargeAmountException {
        if (chargeAmount <= 0) {
            throw new NegativeChargeAmountException("Your charge amount must be positive.");
        }
        UserDAO user = UserRepository.getInstance().getUser(userId);
        user.setCredit(user.getCredit() + chargeAmount);
        UserRepository.getInstance().updateCredit(user);
    }

    public ArrayList<OrderDTO> getAllOrders(String userId){
        ArrayList<OrderDAO> orders = OrderRepository.getInstance().getOrdersOfUser(userId);
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        for(OrderDAO order:orders){
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setOrderStatus(order.getState());
            dto.setRestaurantName(order.getRestaurantName());
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
            dto.setDetails(detailDTO);
        }
        return orderDTOS;
    }

    public OrderDetailDTO getSpecialOrder(String oid) throws OrderDoesNotExist {
        OrderDAO orderDAO = OrderRepository.getInstance().getOrder(oid);
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setTotalCost(orderDAO.getTotalCost());
        ArrayList<OrderItemDTO> itemDTOS = new ArrayList<>();
        for(OrderItemDAO item : orderDAO.getItems()){
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setCost(item.getCost());
            itemDTO.setCount(item.getCount());
            itemDTO.setName(item.getFoodName());
            itemDTOS.add(itemDTO);
        }
        dto.setOrder(itemDTOS);
        return dto;
    }

    public void addToCart(String foodName,String RestaurantId,boolean special){

    }

    public void removeFromCart(String foodName,boolean special){

    }

    public void finalizeCart(String userId){

    }

}
