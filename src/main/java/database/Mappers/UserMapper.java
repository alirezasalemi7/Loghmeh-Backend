package database.Mappers;

import database.ConnectionPool;
import database.DAO.UserDAO;
import models.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    protected String getFindStatement(String id) {
        return "select * from "+tableName+" where id=\""+id+"\";";
    }

    @Override
    protected String getInsertStatement(UserDAO obj) {
        return "insert into "+tableName+" (name,family,phone,email,id,credit,locx,locy) values ("+
                "\""+obj.getName()+"\",\""+obj.getFamily()+"\",\""+obj.getPhoneNumber()+"\",\""+
                obj.getEmail()+"\",\""+ obj.getId()+"\","+obj.getCredit()+","+obj.getLocation().getX()+","+obj.getLocation().getY()+
                ");";
    }

    @Override
    protected String getDeleteStatement(String id) {
        return "delete from "+tableName+" where id=\""+id+"\";";
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
        return dao;
    }


    public void updateCredit(String id,double credit) throws SQLException{
        Connection connection = ConnectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery("update "+tableName+ "set credit="+credit+"where id=\""+id+"\";");
        if(statement!=null && !statement.isClosed()){
            statement.close();
        }
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }

}
