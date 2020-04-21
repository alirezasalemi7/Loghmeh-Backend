package systemHandlers.Services;


import database.DAO.FoodDAO;
import database.DAO.RestaurantDAO;
import database.DAO.UserDAO;
import exceptions.*;
import models.Location;
import org.javatuples.Pair;
import restAPI.DTO.Restaurant.*;
import restAPI.DTO.searchResults.SearchResultDTO;
import systemHandlers.Repositories.RestaurantRepository;
import systemHandlers.Repositories.UserRepository;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantManager {

    private static RestaurantManager instance;
    private Date foodPartyStartTime;

    private RestaurantManager() {}

    public static RestaurantManager getInstance() {
        if (instance == null)
            instance = new RestaurantManager();
        return instance;
    }

    public void setFoodPartyStartTime(Date date){
        this.foodPartyStartTime = date;
    }

    public Date getFoodPartyStartTime(){
        return foodPartyStartTime;
    }

    public AllRestaurantsPageDTO getInRangeRestaurants(String userId, int pageNumber, int pageSize) throws UserDoesNotExistException, ServerInternalException {
        UserDAO user = UserRepository.getInstance().getUser(userId);
        Pair<ArrayList<RestaurantDAO>,Integer> pair = RestaurantRepository.getInstance().getAllRestaurantsInRange(pageNumber,pageSize,user.getLocation());
        ArrayList<RestaurantDAO> restaurants = pair.getValue0();
        ArrayList<RestaurantInfoDTO> restaurantList = new ArrayList<>();
        for (RestaurantDAO restaurant : restaurants) {
            if (this.isInRange(user.getLocation(), restaurant.getLocation())) {
                restaurantList.add(new RestaurantInfoDTO(restaurant.getName(), restaurant.getLogoAddress(), restaurant.getId()));
            }
        }
        AllRestaurantsPageDTO dto = new AllRestaurantsPageDTO();
        dto.setRestaurants(restaurantList);
        dto.setTotalPages(pair.getValue1());
        dto.setCurrentPage(pageNumber);
        return dto;
    }

    public RestaurantDTO getNearbyRestaurantById(String restaurantId, String userId) throws RestaurantDoesntExistException, UserDoesNotExistException, OutOfRangeException, ServerInternalException {
        UserDAO user = UserRepository.getInstance().getUser(userId);
        RestaurantDAO restaurant = RestaurantRepository.getInstance().getRestaurantById(restaurantId);
        if (!this.isInRange(user.getLocation(), restaurant.getLocation()))
            throw new OutOfRangeException("Restaurant " + restaurant.getName() + "is not in range.");
        ArrayList<FoodDTO> foods = new ArrayList<>();
        for (FoodDAO food : restaurant.getMenu()) {
            foods.add(new FoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo()
                    , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription()));
        }
        return new RestaurantDTO(new RestaurantInfoDTO(restaurant.getName(), restaurant.getLogoAddress(), restaurant.getId()), foods);
    }

    public FoodDTO getFoodById(String restaurantId, String foodId) throws FoodDoesntExistException, ServerInternalException {
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(restaurantId, foodId, false);
        return new FoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo()
                , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription());
    }

    public SpecialFoodDTO getSpecialFoodById(String restaurantId, String foodId) throws FoodDoesntExistException, ServerInternalException {
        FoodDAO food = RestaurantRepository.getInstance().getFoodById(restaurantId, foodId, true);
        return new SpecialFoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo(), food.getPopularity()
                , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice());
    }

    public boolean isInRange(Location user, Location restaurant) {
        return user.getDistance(restaurant) <= 170;
    }

    public void setFoodCount(String restaurantId, String foodId, int newCount) throws ServerInternalException {
        RestaurantRepository.getInstance().setFoodCount(restaurantId, foodId, newCount);
    }

    public int getFoodCount(String restaurantId, String foodId) throws ServerInternalException {
        return RestaurantRepository.getInstance().getFoodCount(restaurantId, foodId);
    }

    public ArrayList<SpecialFoodDTO> getAllSpecialFoods() throws ServerInternalException {
        ArrayList<FoodDAO> foods = RestaurantRepository.getInstance().getSpecialFoods();
        ArrayList<SpecialFoodDTO> specialFoods = new ArrayList<>();
        for (FoodDAO food : foods)
            specialFoods.add(new SpecialFoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo(), food.getPopularity()
                    , food.getName(), food.getPrice(), food.getDescription(), food.getCount(), food.getOldPrice()));
        return specialFoods;
    }

    public Location getRestaurantLocation(String restaurantId) throws ServerInternalException, RestaurantDoesntExistException {
        return RestaurantRepository.getInstance().getRestaurantLocation(restaurantId);
    }

    public SearchResultDTO findRestaurantsByNameMatch(String userId,String name,int pageNumber,int pageSize) throws UserDoesNotExistException,ServerInternalException{
        UserDAO user = UserRepository.getInstance().getUser(userId);
        ArrayList<RestaurantDAO> restaurants = RestaurantRepository.getInstance().getRestaurantsMatchNameInRange(pageNumber, pageSize, user.getLocation(), name);
        ArrayList<RestaurantInfoDTO> results = new ArrayList<>();
        for(RestaurantDAO restaurant : restaurants)
            results.add(new RestaurantInfoDTO(restaurant.getName(), restaurant.getLogoAddress(), restaurant.getId()));
        return new SearchResultDTO(results, new ArrayList<>());
    }

    public SearchResultDTO findFoodsByNameMatch(String userId,String name,int pageNumber,int pageSize) throws UserDoesNotExistException,ServerInternalException{
        UserDAO user = UserRepository.getInstance().getUser(userId);
        ArrayList<FoodDAO> foods = RestaurantRepository.getInstance().getFoodsMatchNameInRange(pageNumber, pageSize, user.getLocation(), name);
        ArrayList<FoodDTO> results = new ArrayList<>();
        for(FoodDAO food : foods)
            results.add(new FoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo()
                    , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription()));
        return new SearchResultDTO(new ArrayList<>(), results);
    }

    public SearchResultDTO findFoodsByNameAndRestaurantNameMatch(String userId,String foodName,String restaurantName,int pageNumber,int pageSize) throws UserDoesNotExistException,ServerInternalException{
        UserDAO user = UserRepository.getInstance().getUser(userId);
        ArrayList<FoodDAO> foods = RestaurantRepository.getInstance().getFoodsMatchNameAndRestaurantNameInRange(pageNumber, pageSize, user.getLocation(), foodName, restaurantName);
        ArrayList<FoodDTO> results = new ArrayList<>();
        for(FoodDAO food : foods)
            results.add(new FoodDTO(food.getRestaurantId(), food.getRestaurantName(), food.getLogo()
                    , food.getPopularity(), food.getName(), food.getPrice(), food.getDescription()));
        return new SearchResultDTO(new ArrayList<>(), results);
    }

}
