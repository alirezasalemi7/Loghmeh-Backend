package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.RestaurantDAO;
import business.Domain.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMapper extends Mapper<RestaurantDAO, String> {

    private final String tableName = "Restaurants";

    public RestaurantMapper() throws SQLException{
        Connection connection = ConnectionPool.getConnection();

        String createTable = "create table if not exists " + tableName + " (\n" +
                "   id varchar(100) primary key,\n" +
                "   name varchar(100) not null,\n" +
                "   logo varchar(255) not null,\n" +
                "   locx int not null,\n" +
                "   locy int not null\n" +
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
    protected PreparedStatement getFindStatement(Connection connection, String id) throws SQLException {
        String query = "select * from " + tableName + " where id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        return statement;
    }

    @Override
    protected PreparedStatement getInsertStatement(Connection connection, RestaurantDAO obj) throws SQLException {
        String query = "insert into " + tableName + "(id, name, logo, locx, locy) " + "values (?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, obj.getId());
        statement.setString(2, obj.getName());
        statement.setString(3, obj.getLogoAddress());
        statement.setDouble(4, obj.getLocation().getX());
        statement.setDouble(5, obj.getLocation().getY());
        return statement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(Connection connection, String id) throws SQLException {
        String query = "delete from " + tableName + " where id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        return statement;
    }

    @Override
    protected RestaurantDAO getObject(ResultSet rs) throws SQLException {
        return new RestaurantDAO(rs.getString("name"), rs.getString("logo")
                , new Location(rs.getInt("locx"), rs.getInt("locy")), rs.getString("id"));
    }

    private String concatValues(ArrayList<RestaurantDAO> restaurants) {
        StringBuilder result = new StringBuilder();
        for (RestaurantDAO restaurant : restaurants)
            result.append(",(\"" + restaurant.getId() + "\",\"" + restaurant.getName() + "\",\"" + restaurant.getLogoAddress()
                    + "\"," + restaurant.getLocation().getX() + "," + restaurant.getLocation().getY() + ")");
        return result.toString().substring(1);
    }

    public void insertAllRestaurants(ArrayList<RestaurantDAO> restaurants) throws SQLException {
        if (restaurants.size() < 1)
            return;
        String content = this.concatValues(restaurants);
        String query = "insert into " + tableName + "(id, name, logo, locx, locy) values " + content + ";";
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

    public ArrayList<RestaurantDAO> getAllRestaurantsInfo() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id, name, logo, locx, locy from " + tableName + ";";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        while (rs.next())
            restaurants.add(getObject(rs));
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return restaurants;
    }

    public ArrayList<RestaurantDAO> getAllRestaurantsInRangePageByPage(int pageSize,int pageNumber,double range,Location location) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id, name, logo, locx, locy from " + tableName + " where POWER(locx - ?, 2) + POWER(locy - ?, 2) <= ? ORDER by id ASC LIMIT ?, ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, location.getX());
        statement.setDouble(2, location.getY());
        statement.setDouble(3, range * range);
        statement.setInt(4, pageNumber * pageSize);
        statement.setInt(5, pageNumber);
        ResultSet rs = statement.executeQuery();
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        while (rs.next())
            restaurants.add(getObject(rs));
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return restaurants;
    }

    public int getAllRestaurantsInRangeCount(double range,Location location) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select count(*) as count from " + tableName + " where POWER(locx - ?, 2) + POWER(locy - ?, 2) <= ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, location.getX());
        statement.setDouble(2, location.getY());
        statement.setDouble(3, range * range);
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

    public ArrayList<RestaurantDAO> getAllRestaurantsMatchNameAndInRange(String name,double range,Location location,int pageNumber,int pageSize) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id, name, logo, locx, locy from " + tableName + " where name LIKE %?% and POWER(locx - ?, 2) + POWER(locy - ?, 2) <= ? order by id limit ?, ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setDouble(2, location.getX());
        statement.setDouble(3, location.getY());
        statement.setDouble(4, range * range);
        statement.setInt(5, pageNumber * pageSize);
        statement.setInt(6, pageNumber);
        ResultSet rs = statement.executeQuery();
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        while (rs.next())
            restaurants.add(getObject(rs));
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return restaurants;
    }

    public int getAllRestaurantsMatchNameAndInRangeCount(String name,double range,Location location) throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select count(*) as count from " + tableName + " where name LIKE %?% and POWER(locx - ?, 2) + POWER(locy - ?, 2) <= ?;";
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

    public HashMap<String, Boolean> getRestaurantsId() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id from " + tableName + ";";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        HashMap<String, Boolean> ids = new HashMap<>();
        while (rs.next())
            ids.put(rs.getString("id"), true);
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(stmt!=null && !stmt.isClosed()){
            stmt.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return ids;
    }

}
