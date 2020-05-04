package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.FoodDAO;
import business.Domain.Location;
import org.javatuples.Triplet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

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
    protected PreparedStatement getFindStatement(Connection connection, Triplet<String, String, Boolean> id) throws SQLException {
        String query = "select * from " + tableName + " where restaurant_id = ? and name = ? and special = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id.getValue0());
        statement.setString(2, id.getValue1());
        statement.setInt(3, id.getValue2() ? 1 : 0);
        return statement;
    }

    @Override
    protected PreparedStatement getInsertStatement(Connection connection, FoodDAO obj) throws SQLException {
        String query = "insert into " + tableName + "(restaurant_id, restaurant_name, name, logo, popularity, price, description, special, count, old_price) \n" + "\tvalues (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, obj.getRestaurantId());
        statement.setString(2, obj.getRestaurantName());
        statement.setString(3, obj.getName());
        statement.setString(4, obj.getLogo());
        statement.setDouble(5, obj.getPopularity());
        statement.setDouble(6, obj.getPrice());
        statement.setString(7, obj.getDescription());
        statement.setInt(8, obj.isSpecial() ? 1 : 0);
        statement.setInt(9, obj.getCount());
        statement.setDouble(10, obj.getOldPrice());
        return statement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection, Triplet<String, String, Boolean> id) throws SQLException {
        String query = "delete from Foods where restaurant_id = ? and name = ? and special = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id.getValue0());
        statement.setString(2, id.getValue1());
        statement.setInt(3, id.getValue2() ? 1 : 0);
        return statement;
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

    public HashMap<String, Boolean> getNormalFoodIds(String restaurantId) throws SQLException {
        String query = "select name from " + tableName + " where restaurant_id = ? and special = 0;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, restaurantId);
        ResultSet rs = statement.executeQuery();
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
        String query = "select * from " + tableName + " where restaurant_id = ? and special = 0;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, restaurantId);
        ResultSet rs = statement.executeQuery();
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
        String query = "update " + tableName + " set count = ? where special = 1 and restaurant_id = ? and name = ?;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, count);
        statement.setString(2, restaurantId);
        statement.setString(3, foodId);
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed())
            statement.close();
        if(connection!=null && !connection.isClosed())
            connection.close();
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

    public void insertAllFoods(ArrayList<FoodDAO> foods) throws SQLException {
        if (foods.size() < 1)
            return;
        String query = "insert into " + tableName + "(restaurant_id, restaurant_name, name, logo, popularity, price, description, special, count, old_price) values " + this.createStatement(foods.size(), 10) + ";";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < foods.size(); i++) {
            statement.setString(i + 1, foods.get(i).getRestaurantId());
            statement.setString(i + 2, foods.get(i).getRestaurantName());
            statement.setString(i + 3, foods.get(i).getName());
            statement.setString(i + 4, foods.get(i).getLogo());
            statement.setDouble(i + 5, foods.get(i).getPopularity());
            statement.setDouble(i + 6, foods.get(i).getPrice());
            statement.setString(i + 7, foods.get(i).getDescription());
            statement.setInt(i + 8, foods.get(i).isSpecial() ? 1 : 0);
            statement.setInt(i + 9, foods.get(i).getCount());
            statement.setDouble(i + 10, foods.get(i).getOldPrice());

        }
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public void changeSpecialFoodsToNormal() throws SQLException {
        String query = "update " + tableName + " set price = old_price, count = null, old_price = null, special = 0 where special = 1;";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public void deleteRedundantSpecialFoods() throws SQLException {
        String query = "delete F.* from " + tableName + " F, " + tableName + " F1 where ((F.special = 1) and (F1.special = 0) and (F.name = F1.name) and (F.restaurant_id = F1.restaurant_id));";
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

    public ArrayList<FoodDAO> getAllSpecialFoods() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select * from " + tableName + " where special = 1;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
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

    public ArrayList<FoodDAO> getFoodsMatchNameInUserRange(String name, Location location, double range, int pageNumber, int pageSize) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select * from " + tableName + " where special = 0 and name like %?% and " +
                "(select POWER(locx - ? ,2)+POWER(locy - ? ,2) from Restaurants " +
                " where id = restaurant_id) <= ? order by name asc limit ?, ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setDouble(2, location.getX());
        statement.setDouble(3, location.getY());
        statement.setDouble(4, range * range);
        statement.setInt(5, pageNumber * pageSize);
        statement.setInt(6, pageSize);
        ResultSet rs = statement.executeQuery();
        ArrayList<FoodDAO> foods = new ArrayList<>();
        while (rs.next())
            foods.add(getObject(rs));
        if(rs!=null && !rs.isClosed())
            rs.close();
        if(statement!=null && !statement.isClosed())
            statement.close();
        if(connection!=null && !connection.isClosed())
            connection.close();
        return foods;
    }

    public int getFoodsMatchNameInUserRangeCount(String name, Location location, double range) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select count(*) as count from " + tableName + " where special = 0 and name like %?% and " +
                "(select POWER(locx-?,2)+POWER(locy-?,2) from Restaurants where id = restaurant_id) <= ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setDouble(2, location.getX());
        statement.setDouble(3, location.getY());
        statement.setDouble(4, range * range);
        ResultSet rs = statement.executeQuery();
        int count = 0;
        while (rs.next())
            count = rs.getInt("count");
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return count;
    }

    public ArrayList<FoodDAO> getFoodsMatchNameAndRestaurantNameInUserRange(String foodName,String restaurantName, Location location, double range,int pageNumber,int pageSize) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select * from " + tableName + " where special = 0 and name like %?% and " +
                " restaurant_name like %?% and (select POWER(locx - ?, 2) + POWER(locy - ?, 2) from Restaurants " +
                " where id = restaurant_id) <= ? order by name asc limit ?, ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, foodName);
        statement.setString(2, restaurantName);
        statement.setDouble(3, location.getX());
        statement.setDouble(4, location.getY());
        statement.setDouble(5, range * range);
        statement.setInt(6, pageNumber * pageSize);
        statement.setInt(7, pageSize);
        ResultSet rs = statement.executeQuery();
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

    public int getFoodsMatchNameAndRestaurantNameInUserRangeCount(String foodName,String restaurantName, Location location, double range) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select count(*) as count from " + tableName + " where special = 0 and name like %?% and " +
                " restaurant_name like %?% and (select POWER(locx - ?, 2) + POWER(locy - ?, 2) from Restaurants " +
                " where id = restaurant_id) <= ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, foodName);
        statement.setString(2, restaurantName);
        statement.setDouble(3, location.getX());
        statement.setDouble(4, location.getY());
        statement.setDouble(5, range * range);
        ResultSet rs = statement.executeQuery();
        int count = 0;
        while (rs.next())
            count = rs.getInt("count");
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return count;
    }
}
