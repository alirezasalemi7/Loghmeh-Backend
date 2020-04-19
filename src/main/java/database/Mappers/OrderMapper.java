package database.Mappers;

import database.ConnectionPool;
import database.DAO.OrderDAO;
import database.DAO.OrderState;
import database.Mappers.Mapper;
import models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class OrderMapper extends Mapper<OrderDAO,String> {

    private final String tableName = "Orders";

    public OrderMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "id varchar(50),"+
                        "state varchar(20) not null," +
                        "delivery_time timestamp," +
                        "user_id varchar(50)," +
                        "restaurant_id varchar(100) not null," +
                        "restaurant_name varchar(100) not null," +
                        "primary key(id)," +
                        "foreign key(user_id) references Users(id)"+
                        ");"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(String id) {
        return "select * from "+tableName+" where id=\""+id+"\";";
    }


    @Override
    protected String getInsertStatement(OrderDAO obj) {
        return null;
    }

    @Override
    public void insert(OrderDAO obj) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement("insert into "+tableName+" (id,state,delivery_time,user_id,restaurant_id,restaurant_name)" +
                        " values (?,?,?,?,?,?);")
        ) {
            stmt.setString(1, obj.getId());
            stmt.setString(2, obj.getState().toString());
            stmt.setNull(3, Types.TIMESTAMP);
            stmt.setString(4, obj.getUserId());
            stmt.setString(5, obj.getRestaurantId());
            stmt.setString(6, obj.getRestaurantName());
            stmt.executeUpdate();
            OrderItemMapper mapper = new OrderItemMapper();
            mapper.addAllOrderItems(obj.getItems());
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id=\""+id+"\";";
    }

    @Override
    protected OrderDAO getObject(ResultSet rs) throws SQLException {
        OrderDAO dao = new OrderDAO();
        dao.setUserId(rs.getString("user_id"));
        dao.setRestaurantId(rs.getString("restaurant_id"));
        dao.setRestaurantName(rs.getString("restaurant_name"));
        dao.setId(rs.getString("id"));
        dao.setState(OrderState.valueOf(rs.getString("state")));
        Timestamp timestamp = rs.getTimestamp("delivery_time");
        if(rs.wasNull()){
            dao.setArrivalDate(null);
        }
        else{
            dao.setArrivalDate(Date.from(rs.getTimestamp("delivery_time").toInstant()));
        }
        OrderItemMapper mapper = new OrderItemMapper();
        dao.setItems(mapper.getAllItemsOfOrder(dao.getId()));
        return dao;
    }

    public ArrayList<OrderDAO> getUserOrders(String uid) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where user_id=\""+uid+"\";");
        ArrayList<OrderDAO> items = new ArrayList<>();
        while (rs.next())
            items.add(getObject(rs));
        rs.close();
        statement.close();
        connection.close();
        return items;
    }

    public ArrayList<OrderDAO> getNotDeliveredOrders() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+tableName+" where state!=\""+OrderState.Delivered.toString()+"\";");
        ArrayList<OrderDAO> items = new ArrayList<>();
        while (rs.next())
            items.add(getObject(rs));
        rs.close();
        statement.close();
        connection.close();
        return items;
    }

    public void updateStateAndDate(String oid, OrderState state, Date date) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("update "+tableName+" set state=? , delivery_time=? where id=?;");
        statement.setString(1, state.toString());
        statement.setTimestamp(2, new Timestamp(date.getTime()));
        statement.setString(3, oid);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
