package database;

import models.OrderItem;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderItemMapper extends Mapper<OrderItem, Triplet<String,String,String>> {

    private String tableName = "order_items";

    OrderItemMapper() throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "order_id varchar(50) foreign key REFERENCES Orders(id),"+
                        "food_name varchar(100) foreign key REFERENCES Foods(food_name)," +
                        "restaurant_id varchar(100) foreign key REFERENCES Foods(restaurant_id)," +
                        "count int ," +
                        "primary key(order_id,food_name,restaurant_id)"+
                        ");"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(Triplet<String,String,String> id) {
        return "select * from "+tableName+" where order_id="+id.getValue0()+" AND food_name="+id.getValue1()+" AND restaurant_id="+id.getValue2()+";";
    }

    @Override
    protected String getFindStatement(Triplet<String,String,String> id, ArrayList<String> columnNames) {
        return this.getFindStatement(id);
    }

    @Override
    protected String getInsertStatement(OrderItem obj) {
        return "insert into "+tableName+"(order_id,food_name,restaurant_id,count) values (" +
                obj.getParentId()+","+obj.getFoodName()+","+obj.getRestaurantId()+","+obj.getCount()+");";
    }

    @Override
    protected String getDeleteStatement(Triplet<String,String,String> id) {
        return "delete from "+tableName+" where order_id="+id.getValue0()+" AND food_name="+id.getValue1()+" AND restaurant_id="+id.getValue2()+";";
    }

    @Override
    protected OrderItem getObject(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected OrderItem getPartialObject(ResultSet rs, ArrayList<String> columnNames) throws SQLException {
        return null;
    }

    public ArrayList<OrderItem> getAllItemsOfOrder(String id) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where order_id="+id+";");
        ArrayList<OrderItem> items = new ArrayList<>();
        while (rs.next()){
            items.add(getObject(rs));
        }
        rs.close();
        return items;
    }
}
