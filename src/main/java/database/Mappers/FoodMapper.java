package database.Mappers;

import database.ConnectionPool;
import database.DAO.FoodDAO;
import org.javatuples.Triplet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        if(rs.next()) {
            rs.close();
            return null;
        }
        FoodDAO food;
        if (rs.getInt("special") == 1)
            food = new FoodDAO(rs.getString("restaurant_id"), rs.getString("restaurant_name"), rs.getString("logo"), rs.getDouble("popularity"), rs.getString("name")
                    , rs.getDouble("price"), rs.getString("description"), rs.getInt("count"), rs.getDouble("old_price"));
        else food = new FoodDAO(rs.getString("restaurant_id"), rs.getString("restaurant_name"), rs.getString("logo"), rs.getDouble("popularity"), rs.getString("name")
                    , rs.getDouble("price"), rs.getString("description"));
        rs.close();
        return food;
    }
}
