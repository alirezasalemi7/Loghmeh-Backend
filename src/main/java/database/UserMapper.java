package database;

import models.Cart;
import models.Location;
import models.Order;
import models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class UserMapper extends Mapper<User,String> {

    private final String tableName = "Users";

    public UserMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "name varchar(100)," +
                        "family varchar(100)," +
                        "phone VARCHAR(11)," +
                        "email varchar(240)," +
                        "id varchar(50) primary key,"+
                        "credit real," +
                        "locx int," +
                        "locy int,"+
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
    protected String getInsertStatement(User obj) {
        return "insert into "+tableName+" (name,family,phone,email,id,credit,locx,locy) values ("+
                obj.getName()+","+obj.getFamily()+","+obj.getPhoneNumber()+","+
                obj.getEmail()+","+ obj.getEmail()+","+obj.getCredit()+obj.getLocation().getX()+","+obj.getLocation().getY()+
                ");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id="+id+";";
    }

    private User fetchPrimitives(ResultSet rs) throws SQLException{
        String name = rs.getString("name");
        String family = rs.getString("family");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        Double credit = rs.getDouble("credit");
        Location location = new Location(rs.getInt("locx"), rs.getInt("locy"));
        String id = rs.getString("id");
        return new User(location,id,name,null,family,phone,email, credit, null);
    }

    @Override
    protected User getObject(ResultSet rs) throws SQLException {
        ArrayList<String> fields =  new ArrayList<String>();
        fields.add("cart");
        fields.add("orders");
        return this.getPartialObject(rs,fields);
    }

    @Override
    protected User getPartialObject(ResultSet rs, ArrayList<String> columnNames) throws SQLException {
        User user = fetchPrimitives(rs);
        if(columnNames.contains("cart")){
            CartMapper mapper = new CartMapper();
            Cart cart = mapper.find(user.getId());
            user.setCart(cart);
        }
        if(columnNames.contains("orders")){
            OrderMapper orderMapper = new OrderMapper();
            ArrayList<Order> orders = orderMapper.getUserOrders(user.getId());
            HashMap<String,Order> orderHashMap = new HashMap<>();
            for (Order order: orders){
                orderHashMap.put(order.getId(), order);
            }
            user.setOrders(orderHashMap);
        }
        return user;
    }

    public void updateCredit(User user) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery("update "+tableName+ "set credit="+user.getCredit()+"where id="+user.getId()+";");
        statement.close();
        connection.close();
    }

}
