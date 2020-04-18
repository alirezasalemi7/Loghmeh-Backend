package database.Mappers;

import database.ConnectionPool;
import database.DAO.RestaurantDAO;
import models.Location;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        return "insert into " + tableName + "(id, name, logo, locx, locy, average_popularity) " +
                "values (\"" + obj.getId() + "\", \"" + obj.getName() + "\", \"" + obj.getLogoAddress() +
                 "\", " + obj.getLocation().getX() + ", " + obj.getLocation().getY() + ");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from " + tableName + " where id = \"" + id + "\";";
    }

    @Override
    protected RestaurantDAO getObject(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            rs.close();
            return null;
        }
        RestaurantDAO restaurant = new RestaurantDAO(rs.getString("name"), rs.getString("logo"), new Location(rs.getInt("locx")
                , rs.getInt("locy")), rs.getString("id"), null);
        rs.close();
        return restaurant;
    }
}
