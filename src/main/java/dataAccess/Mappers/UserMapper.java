package dataAccess.Mappers;

import dataAccess.ConnectionPool;
import dataAccess.DAO.UserDAO;
import business.Domain.Location;

import java.sql.*;
import java.util.Queue;

public class UserMapper extends Mapper<UserDAO,String> {

    private final String tableName = "Users";

    public UserMapper() throws SQLException{
        super();
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "create table if not exists "+tableName+"(" +
                        "name varchar(100) not null ," +
                        "family varchar(100) not null ," +
                        "phone varchar(11) not null ," +
                        "email varchar(240) not null ," +
                        "password int not null ,"+
                        "id varchar(50) primary key ,"+
                        "credit real not null ," +
                        "locx int not null ," +
                        "locy int not null"+
                        ");"
        );
        if(statement!=null && !statement.isClosed()){
            statement.close();
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
    protected PreparedStatement getInsertStatement(Connection connection, UserDAO obj) throws SQLException {
        String query = "insert into "+tableName+" (name,family,phone,email,id,credit,locx,locy,password) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, obj.getName());
        statement.setString(2, obj.getFamily());
        statement.setString(3, obj.getPhoneNumber());
        statement.setString(4, obj.getEmail());
        statement.setString(5, obj.getId());
        statement.setDouble(6, obj.getCredit());
        statement.setDouble(7, obj.getLocation().getX());
        statement.setDouble(8, obj.getLocation().getY());
        statement.setLong(9, obj.getPassword());
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
    protected UserDAO getObject(ResultSet rs) throws SQLException {
        UserDAO dao = new UserDAO();
        dao.setName(rs.getString("name"));
        dao.setFamily(rs.getString("family"));
        dao.setPhoneNumber(rs.getString("phone"));
        dao.setEmail(rs.getString("email"));
        dao.setCredit(rs.getDouble("credit"));
        Location location = new Location(rs.getInt("locx"), rs.getInt("locy"));
        dao.setLocation(location);
        dao.setId(rs.getString("id"));
        dao.setPassword(rs.getLong("password"));
        return dao;
    }

    public void updateCredit(String id,double credit) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("update " + tableName + " set credit = ? where id = ?;");
        statement.setDouble(1, credit);
        statement.setString(2, id);
        statement.executeUpdate();
        if(statement!=null && !statement.isClosed())
            statement.close();
        if(connection!=null && !connection.isClosed())
            connection.close();
    }

    public UserDAO getUserByEmail(String email) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from "+tableName+" where email=?;");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        if(!rs.next()){
            return null;
        }
        UserDAO user = getObject(rs);
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
        return user;
    }

}
