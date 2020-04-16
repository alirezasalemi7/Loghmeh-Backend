package database;

import models.Cart;
import models.OrderItem;
import models.SpecialFood;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class CartMapper extends Mapper<Cart , String> {

    private final String tableName = "Cart";

    CartMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "id varchar(50) foreign key REFERENCES Users(id),"+
                        "cur_restaurant varchar(100)," +
                        "sum_of_prices real ," +
                        "primary key(id)"+
                        ");"
        );
        statement.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(String id) {
        return "select * from "+tableName+" where id="+id+';';
    }

    @Override
    protected String getFindStatement(String id, ArrayList<String> columnNames) {
        return this.getFindStatement(id);
    }

    @Override
    protected String getInsertStatement(Cart obj) {
        return "insert into "+tableName+" (id,cur_restaurant,sum_of_prices) values (" +
                obj.getUserId() + "," + obj.getRestaurantId() + "," + obj.getSumOfPrices()+
                ");";
    }

    private Cart getPrimitives(ResultSet rs) throws SQLException{
        String uid = rs.getString("id");
        String restaurantId = rs.getString("cur_restaurant");
        if(rs.wasNull()){
            restaurantId = null;
        }
        double sum = rs.getDouble("sum_of_prices");
        return new Cart(uid, restaurantId, sum, null);
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id="+id+";";
    }

    @Override
    protected Cart getObject(ResultSet rs) throws SQLException {
        ArrayList<String> fields = new ArrayList();
        fields.add("orders");
        return this.getPartialObject(rs, fields);
    }

    @Override
    protected Cart getPartialObject(ResultSet rs, ArrayList<String> columnNames) throws SQLException {
        Cart cart = getPrimitives(rs);
        if(columnNames.contains("orders")){
            CartItemMapper mapper = new CartItemMapper();
            ArrayList<OrderItem> items = mapper.getAllItemsOfCart(cart.getUserId());
            HashMap<String,OrderItem> itemHashMap = new HashMap<>();
            for (OrderItem item : items){
                if(item.getFood() instanceof SpecialFood){
                    itemHashMap.put(item.getFoodName()+"@", item);
                }
                else{
                    itemHashMap.put(item.getFoodName(), item);
                }
            }
            cart.setItems(itemHashMap);
        }
        return cart;
    }
}
