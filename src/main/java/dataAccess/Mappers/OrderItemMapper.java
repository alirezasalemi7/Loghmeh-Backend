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

    private String createStatement(int size, int length) {
        StringBuilder concatenated = new StringBuilder("(?");
        for (int i = 1; i < length; i++)
            concatenated.append(", ?");
        concatenated.append(")");
        for (int i = 1; i < size; i++) {
            concatenated.append(", (?");
            for (int j = 1; j < length; j++) {
                concatenated.append(", ?");
            }
            concatenated.append(")");
        }
        return concatenated.toString();
    }

    public void addAllOrderItems(ArrayList<OrderItemDAO> items) throws SQLException{
        if (items.size() < 1)
            return;
        String query = "insert into " + tableName + " (order_id,food_name,restaurant_id,count,special,cost) values " + createStatement(items.size(), 6) + ";";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < items.size(); i++) {
            statement.setString(6*i + 1, items.get(i).getOrderId());
            statement.setString(6*i + 2, items.get(i).getFoodName());
            statement.setString(6*i + 3, items.get(i).getRestaurantId());
            statement.setInt(6*i + 4, items.get(i).getCount());
            statement.setInt(6*i + 5, items.get(i).isSpecial() ? 1 : 0);
            statement.setDouble(6*i + 6, items.get(i).getCost());

        }
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
