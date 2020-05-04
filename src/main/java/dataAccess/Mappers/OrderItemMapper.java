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
    protected String getFindStatement(Quartet<String,String,String,Boolean> id) {
        return "select * from "+tableName+" where order_id=\""+id.getValue0()+"\" AND food_name=\""+id.getValue1()+"\" AND restaurant_id=\""+id.getValue2()+"\" AND special="+((id.getValue3())?1:0)+";";
    }

    @Override
    protected String getInsertStatement(OrderItemDAO obj) {
        return "insert into "+tableName+"(order_id,food_name,restaurant_id,count,special,cost) values (" +
                obj.getOrderId()+","+obj.getFoodName()+","+obj.getRestaurantId()+","+obj.getCount()+","+obj.isSpecial()+","+obj.getCost()+");";
    }

    @Override
    protected String getDeleteStatement(Quartet<String,String,String,Boolean> id) {
        return "delete from "+tableName+" where order_id=\""+id.getValue0()+"\" AND food_name=\""+id.getValue1()+"\" AND restaurant_id=\""+id.getValue2()+"\" AND special="+((id.getValue3())?1:0)+";";
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
