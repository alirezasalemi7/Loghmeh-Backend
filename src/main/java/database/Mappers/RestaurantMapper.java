package database.Mappers;

import database.ConnectionPool;
import database.DAO.RestaurantDAO;
import models.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                "   locy int not null,\n" +
                ");";
        Statement stmt = connection.createStatement();
        stmt.execute(createTable);
        stmt.close();
        connection.close();
    }

    @Override
    protected String getFindStatement(String id) {
        return "select * from " + tableName + " where id = \"" + id + "\";";
    }

    @Override
    protected String getInsertStatement(RestaurantDAO obj) {
        return "insert into " + tableName + "(id, name, logo, locx, locy) " +
                "values (\"" + obj.getId() + "\", \"" + obj.getName() + "\", \"" + obj.getLogoAddress() +
                 "\", " + obj.getLocation().getX() + ", " + obj.getLocation().getY() + ");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from " + tableName + " where id = \"" + id + "\";";
    }

    @Override
    protected RestaurantDAO getObject(ResultSet rs) throws SQLException {
        if (!rs.next())
            return null;
        return new RestaurantDAO(rs.getString("name"), rs.getString("logo"), new Location(rs.getInt("locx"), rs.getInt("locy")), rs.getString("id"));
    }

    private String concatValues(ArrayList<RestaurantDAO> restaurants) {
        StringBuilder result = new StringBuilder();
        for (RestaurantDAO restaurant : restaurants)
            result.append(",(" + restaurant.getId() + "," + restaurant.getName() + "," + restaurant.getLogoAddress()
                    + "," + restaurant.getLocation().getX() + "," + restaurant.getLocation().getY() + ")");
        return result.toString().substring(1);
    }

    public void insertAllRestaurants(ArrayList<RestaurantDAO> restaurants) throws SQLException {
        String content = this.concatValues(restaurants);
        String query = "insert into " + tableName + "(id, name, logo, locx, locy) values " + content + ";";
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.close();
    }

    public ArrayList<RestaurantDAO> getAllRestaurantsInfo() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id, name, logo, locx, locy from " + tableName + ";";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        while (rs.next())
            restaurants.add(getObject(rs));
        rs.close();
        statement.close();
        connection.close();
        return restaurants;
    }

    public HashMap<String, Boolean> getRestaurantsId() throws SQLException {
        Connection connection = ConnectionPool.getConnection();
        String query = "select id from " + tableName + ";";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        HashMap<String, Boolean> ids = new HashMap<>();
        while (rs.next())
            ids.put(rs.getString("id"), true);
        rs.close();
        stmt.close();
        connection.close();
        return ids;
    }

}
