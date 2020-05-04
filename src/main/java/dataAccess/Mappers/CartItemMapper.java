package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.CartItemDAO;
import org.javatuples.Quartet;

import java.sql.*;
import java.util.ArrayList;

public class CartItemMapper extends Mapper<CartItemDAO, Quartet<String,String,String,Boolean>> {

    private String tableName = "Cart_Items";

    public CartItemMapper() throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "cart_id varchar(50),"+
                        "food_name varchar(100) not null ," +
                        "restaurant_id varchar(100) not null," +
                        "count int not null," +
                        "special int not null ," +
                        "cost real not null," +
                        "primary key(cart_id,food_name,restaurant_id,special)," +
                        "foreign key(cart_id) REFERENCES Carts(id)"+
                        ");"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(Quartet<String,String,String,Boolean> id) {
        return "select * from "+tableName+" where cart_id=\""+id.getValue0()+
                "\" AND food_name=\""+id.getValue1()+"\" AND restaurant_id=\""+id.getValue2()+
                "\" AND special="+((id.getValue3())?1:0)+";";
    }

    @Override
    protected String getInsertStatement(CartItemDAO obj) {
        return "insert into "+tableName+"(cart_id,food_name,restaurant_id,count,special,cost) values (\"" +
                obj.getCartId()+"\",\""+obj.getFoodName()+"\",\""+obj.getRestaurantId()+
                "\","+obj.getCount()+","+((obj.isSpecial())?1:0)+","+obj.getCost()+");";
    }

    @Override
    protected String getDeleteStatement(Quartet<String,String,String,Boolean> id) {
        return "delete from "+tableName+" where cart_id=\""+id.getValue0()+"\" AND food_name=\""+id.getValue1()+"\" AND restaurant_id=\""+id.getValue2()+"\" AND special="+((id.getValue3())?1:0)+";";
    }

    @Override
    protected CartItemDAO getObject(ResultSet rs) throws SQLException {
        CartItemDAO cartItem = new CartItemDAO();
        cartItem.setCost(rs.getDouble("cost"));
        cartItem.setCount(rs.getInt("count"));
        cartItem.setSpecial(rs.getInt("special")==1);
        cartItem.setFoodName(rs.getString("food_name"));
        cartItem.setRestaurantId(rs.getString("restaurant_id"));
        cartItem.setCartId(rs.getString("cart_id"));
        return cartItem;
    }

    public ArrayList<CartItemDAO> getAllItemsOfCart(String id) throws SQLException{
        String query = "select * from " + tableName + " where cart_id = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        ArrayList<CartItemDAO> items = new ArrayList<>();
        while (rs.next())
            items.add(getObject(rs));
        rs.close();
        statement.close();
        connection.close();
        return items;
    }

    public void RemoveAllItemsOfCart(String cartId) throws SQLException{
        String query = "delete from "+tableName+" where cart_id = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, cartId);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public void updateItem(CartItemDAO item) throws SQLException{
        String query = "update "+tableName+" set cost = ?, count = ? where cart_id = ? AND food_name = ? AND restaurant_id = ? AND special = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, item.getCost());
        statement.setInt(2, item.getCount());
        statement.setString(3, item.getCartId());
        statement.setString(4, item.getFoodName());
        statement.setString(5, item.getRestaurantId());
        statement.setInt(6, ((item.isSpecial())?1:0));
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
