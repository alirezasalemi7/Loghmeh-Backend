package database.Mappers;

import database.ConnectionPool;
import database.DAO.OrderItemDAO;
import models.OrderItem;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderItemMapper extends Mapper<OrderItemDAO, Quartet<String,String,String,Boolean>> {

    private String tableName = "Order_items";

    OrderItemMapper() throws SQLException{
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
        if(!rs.next()){
            return null;
        }
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
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where order_id=\""+id+"\";");
        ArrayList<OrderItemDAO> items = new ArrayList<>();
        while (true){
            OrderItemDAO temp = getObject(rs);
            if(temp!=null){
                items.add(temp);
            }
            else break;
        }
        rs.close();
        statement.close();
        connection.close();
        return items;
    }

    private String concatValues(ArrayList<OrderItemDAO> items){
        String concat = "";
        for (OrderItemDAO item : items){
            concat+="("+item.getOrderId()+","+item.getFoodName()+","+item.getRestaurantId()+","+item.getCount()+","+item.isSpecial()+","+item.getCost()+"),";
        }
        return concat.substring(0, concat.length()-2);
    }

    public void addAllOrderItems(ArrayList<OrderItemDAO> items) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        String sql = "insert into "+tableName+"(order_id,food_name,restaurant_id,count,special,cost) values "+concatValues(items)+";";
        statement.executeQuery(sql);
        statement.close();
        connection.close();
    }
}
