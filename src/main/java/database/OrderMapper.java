package database;

import database.Mappers.Mapper;
import models.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderMapper extends Mapper<Order,String> {

    private final String tableName = "Orders";

    public OrderMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "id varchar(50) primary key,"+
                        "state varchar(20)," +
                        "delivery_time DATETIME ," +
                        "user_id varchar(50) foreign key references Users(id)"+
                        ")"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(String id) {
        return "select * from "+tableName+" where id="+id+";";
    }

    @Override
    protected String getFindStatement(String id, ArrayList<String> columnNames) {
        return this.getFindStatement(id);
    }

    @Override
    protected String getInsertStatement(Order obj) {
        return "insert into " + tableName + "(id,state,user_id) values (" +
                obj.getId()+","+obj.getState().toString()+","+obj.getUser().getId()+
                ");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id="+id+";";
    }

    @Override
    protected Order getObject(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected Order getPartialObject(ResultSet rs, ArrayList<String> columnNames) throws SQLException {
        return null;
    }

    public ArrayList<Order> getUserOrders(String uid) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where user_id="+uid+";");
        ArrayList<Order> items = new ArrayList<>();
        while (rs.next()){
            items.add(getObject(rs));
        }
        rs.close();
        return items;
    }

    public ArrayList<Order> getNotDeliveredOrders() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where state=\"Delivered\";");
        ArrayList<Order> items = new ArrayList<>();
        while (rs.next()){
            items.add(getObject(rs));
        }
        rs.close();
        return items;
    }
}
