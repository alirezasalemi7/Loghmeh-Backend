package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.CartDAO;
import dataAccess.DAO.CartItemDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CartMapper extends Mapper<CartDAO, String> {

    private final String tableName = "Carts";

    public CartMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "id varchar(50),"+
                        "restaurant_id varchar(100)," +
                        "primary key(id),"+
                        "foreign key(id) REFERENCES Users(id)"+
                        ");"
        );
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    @Override
    protected PreparedStatement getFindStatement(Connection connection, String id) throws SQLException {
        String query = "select * from "+tableName+" where id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        return statement;
    }

    @Override
    protected PreparedStatement getInsertStatement(Connection connection, CartDAO obj) throws SQLException {
        String query = "insert into " + tableName + " (id, restaurant_id) values (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, obj.getUserId());
        statement.setString(2, obj.getRestaurantId());
        return statement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection, String id) throws SQLException {
        String query = "delete from " + tableName + " where id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        return statement;
    }

    @Override
    protected CartDAO getObject(ResultSet rs) throws SQLException {
        CartDAO cart = new CartDAO();
        String restaurant_id = rs.getString("restaurant_id");
        if(rs.wasNull()){
            restaurant_id = null;
        }
        cart.setRestaurantId(restaurant_id);
        cart.setUserId(rs.getString("id"));
        CartItemMapper mapper = new CartItemMapper();
        ArrayList<CartItemDAO> items = mapper.getAllItemsOfCart(cart.getUserId());
        HashMap<String,CartItemDAO> itemHashMap = new HashMap<>();
        for(CartItemDAO item : items){
            if(item.isSpecial()){
                itemHashMap.put(item.getFoodName()+"@", item);
            }
            else{
                itemHashMap.put(item.getFoodName(), item);
            }
        }
        cart.setItems(itemHashMap);
        return cart;
    }

    public void updateRestaurantIdOfCart(String cartId,String restaurantId) throws SQLException{
        String query = "update " + tableName + " set restaurant_id = ? where id = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, restaurantId);
        statement.setString(2, cartId);
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public void resetCart(String id) throws SQLException{
        String query = "update " + tableName + " set restaurant_id = null where id = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        statement.executeUpdate(query);
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

}
