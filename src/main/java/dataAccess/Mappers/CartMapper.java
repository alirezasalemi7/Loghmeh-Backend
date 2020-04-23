package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.CartDAO;
import dataAccess.DAO.CartItemDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    protected String getFindStatement(String id) {
        return "select * from "+tableName+" where id=\""+id+"\";";
    }

    @Override
    protected String getInsertStatement(CartDAO obj) {
        return "insert into "+tableName+" (id,restaurant_id) values (\"" +
                obj.getUserId() + "\",\"" + obj.getRestaurantId()+
                "\");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id=\""+id+"\";";
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
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("update "+tableName+" set restaurant_id = \""+restaurantId+"\" where id=\""+cartId+"\";");
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public void resetCart(String id) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("update "+tableName+" set restaurant_id = null where id=\""+id+"\";");
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

}
