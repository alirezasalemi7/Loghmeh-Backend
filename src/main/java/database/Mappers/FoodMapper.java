package database.Mappers;

import database.ConnectionPool;
import database.DAO.FoodDAO;
import exceptions.FoodDoesntExistException;
import org.javatuples.Triplet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodMapper extends Mapper<FoodDAO, Triplet<String, String, Boolean>> {

    private final String tableName = "Foods";

    public FoodMapper() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String createTable = "create table if not exists " + tableName + " (\n" +
                "    restaurant_id varchar(100) not null,\n" +
                "    restaurant_name varchar(100) not null,\n" +
                "    name varchar(100) not null,\n" +
                "    logo varchar(255) not null,\n" +
                "    popularity real not null,\n" +
                "    price real not null,\n" +
                "    description text,\n" +
                "    special int not null,\n" +
                "    count int,\n" +
                "    old_price real,\n" +
                "    primary key (restaurant_id, name, special),\n" +
                "    foreign key (restaurant_id) references Restaurants(id) on delete cascade\n" +
                ");";
        Statement stmt = connection.createStatement();
        stmt.execute(createTable);
        if(stmt!=null && !stmt.isClosed()){
            stmt.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    @Override
    protected String getFindStatement(Triplet<String, String, Boolean> id) {
        return "select * from " + tableName + " where restaurant_id = \"" + id.getValue0() + "\" and name = \"" + id.getValue1() + "\" and special = " + (id.getValue2() ? 1 : 0) + ";";
    }

    @Override
    protected String getInsertStatement(FoodDAO obj) {
        return "insert into " + tableName + "(restaurant_id, restaurant_name, name, logo, popularity, price, description, special, count, old_price) \n" +
                "\tvalues (\"" + obj.getRestaurantId() + "\", \"" + obj.getRestaurantName() + "\", \"" + obj.getName() + "\", \"" + obj.getLogo() + "\", " + obj.getPopularity()
                    + ", " + obj.getPrice() + ", \"" + obj.getDescription() + "\", " + (obj.isSpecial() ? 1 : 0) + ", " + obj.getCount() + ", " + obj.getOldPrice() + ");";
    }

    @Override
    protected String getDeleteStatement(Triplet<String, String, Boolean> id) {
        return "delete from Foods where restaurant_id = \"" + id.getValue0() + "\" and name = \" " + id.getValue1() + " \" and special = " + (id.getValue2() ? 1 : 0) + ";";
    }

    @Override
    protected FoodDAO getObject(ResultSet rs) throws SQLException {
        FoodDAO food;
        if (rs.getInt("special") == 1)
            food = new FoodDAO(rs.getString("restaurant_id"), rs.getString("restaurant_name"), rs.getString("logo"), rs.getDouble("popularity"), rs.getString("name")
                    , rs.getDouble("price"), rs.getString("description"), rs.getInt("count"), rs.getDouble("old_price"));
        else food = new FoodDAO(rs.getString("restaurant_id"), rs.getString("restaurant_name"), rs.getString("logo"), rs.getDouble("popularity"), rs.getString("name")
                    , rs.getDouble("price"), rs.getString("description"));
        return food;
    }

    private void runQuery(String query) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public HashMap<String, Boolean> getNormalFoodIds(String restaurantId) throws SQLException {
        String query = "select name from " + tableName + " where restaurant_id = \"" + restaurantId + "\" and special = 0;";
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        HashMap<String, Boolean> ids = new HashMap<>();
        while(rs.next())
            ids.put(rs.getString("name"), true);
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return ids;
    }

    public ArrayList<FoodDAO> getNormalFoods(String restaurantId) throws SQLException {
        String query = "select * from " + tableName + " where restaurant_id = \"" + restaurantId + "\" and special = 0;";
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ArrayList<FoodDAO> foods = new ArrayList<>();
        while(rs.next())
            foods.add(this.getObject(rs));
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return foods;
    }

    public void updateFoodCount(String restaurantId, String foodId, int count) throws SQLException {
        String query = "update " + tableName + " set count = " + count + " where special = 1 and restaurant_id = \"" + restaurantId + "\" and name = \"" + foodId + "\";";
        this.runQuery(query);
    }

    private String concatValues(ArrayList<FoodDAO> foods) {
        StringBuilder concatenated = new StringBuilder();
        for (FoodDAO food : foods)
            concatenated.append(",(\"" + food.getRestaurantId() + "\",\"" + food.getRestaurantName() + "\",\"" + food.getName() + "\",\"" + food.getLogo() + "\"," + food.getPopularity()
                    + "," + food.getPrice() + ",\"" + food.getDescription() + "\"," + (food.isSpecial() ? 1 : 0) + "," + food.getCount() + "," + food.getOldPrice() + ")");
        return concatenated.toString().substring(1);
    }

    public void insertAllFoods(ArrayList<FoodDAO> foods) throws SQLException {
        if (foods.size() < 1)
            return;
        String content = this.concatValues(foods);
        String query = "insert into " + tableName + "(restaurant_id, restaurant_name, name, logo, popularity, price, description, special, count, old_price) values " + content + ";";
        this.runQuery(query);
    }

    public void changeSpecialFoodsToNormal() throws SQLException {
        String query = "update " + tableName + " set price = old_price, count = null, old_price = null, special = 0 where special = 1;";
        this.runQuery(query);
    }

    public void deleteRedundantSpecialFoods() throws SQLException {
        String query = "delete F.* from " + tableName + " F, " + tableName + " F1 where ((F.special = 1) and (F1.special = 0) and (F.name = F1.name) and (F.restaurant_id = F1.restaurant_id));";
        this.runQuery(query);
    }

    public ArrayList<FoodDAO> getAllSpecialFoods() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select * from " + tableName + " where special = 1;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ArrayList<FoodDAO> foods = new ArrayList<>();
        while (rs.next())
            foods.add(getObject(rs));
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return foods;
    }
}
