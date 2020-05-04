package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.OrderItemDAO;
import org.javatuples.Quartet;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class OrderItemMapper extends Mapper<OrderItemDAO, Quartet<String,String,String,Boolean>> {

    private String tableName = "Order_items";

    public OrderItemMapper() throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "order_id varchar(50),"+
                        "food_name varchar(100) not null ," +
                        "restaurant_id varchar(100) not null ," +
                        "count int not null," +
                        "special int not null," +
                        "cost real not null," +
                        "foreign key(order_id) REFERENCES Orders(id)," +
                        "primary key(order_id,food_name,restaurant_id,special)"+
                        ");"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected PreparedStatement getFindStatement(Connection connection, Quartet<String,String,String,Boolean> id) throws SQLException {
        String query = "select * from "+tableName+" where order_id = ? AND food_name = ? AND restaurant_id = ? AND special = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id.getValue0());
        statement.setString(2, id.getValue1());
        statement.setString(3, id.getValue2());
        statement.setInt(4, id.getValue3() ? 1 : 0);
        return statement;
    }

    @Override
    protected PreparedStatement getInsertStatement(Connection connection, OrderItemDAO obj) throws SQLException {
        String query = "insert into "+tableName+"(order_id, food_name, restaurant_id, count, special, cost) values (?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, obj.getOrderId());
        statement.setString(2, obj.getFoodName());
        statement.setString(3, obj.getRestaurantId());
        statement.setInt(4, obj.getCount());
        statement.setInt(5, obj.isSpecial() ? 1 : 0);
        statement.setDouble(6, obj.getCost());
        return statement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection, Quartet<String, String, String, Boolean> id) throws SQLException {
        String query = "delete from "+tableName+" where order_id = ? AND food_name = ? AND restaurant_id = ? AND special = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id.getValue0());
        statement.setString(2, id.getValue1());
        statement.setString(3, id.getValue2());
        statement.setInt(4, id.getValue3() ? 1 : 0);
        return statement;
    }

    @Override
    protected OrderItemDAO getObject(ResultSet rs) throws SQLException {
        OrderItemDAO dao = new OrderItemDAO();
        dao.setOrderId(rs.getString("order_id"));
        dao.setFoodName(rs.getString("food_name"));
        dao.setRestaurantId(rs.getString("restaurant_id"));
        dao.setSpecial(rs.getInt("special")==1);
        dao.setCost(rs.getDouble("cost"));
        dao.setCount(rs.getInt("count"));
        return dao;
    }

    public ArrayList<OrderItemDAO> getAllItemsOfOrder(String id) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where order_id = ?;");
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        ArrayList<OrderItemDAO> items = new ArrayList<>();
        while (rs.next())
            items.add(getObject(rs));
        rs.close();
        statement.close();
        connection.close();
        return items;
    }

    private String concatValues(ArrayList<OrderItemDAO> items){
        String concat = "";
        for (OrderItemDAO item : items){
            concat+=",(\""+item.getOrderId()+"\",\""+item.getFoodName()+"\",\""+item.getRestaurantId()+"\","+item.getCount()+","+(item.isSpecial()?1:0)+","+item.getCost()+")";
        }
        return concat.substring(1);
    }

    public void addAllOrderItems(ArrayList<OrderItemDAO> items) throws SQLException{
        if (items.size() < 1)
            return;
        String query = "insert into "+tableName+" (order_id,food_name,restaurant_id,count,special,cost) values "+concatValues(items)+";";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
