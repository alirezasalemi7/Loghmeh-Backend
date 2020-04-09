package database;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionPool {

    private static  BasicDataSource source = new BasicDataSource();

    static {
        try{
            String dbName = "LoghmeDB";
            String username = "root";
            String password = "12345";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/",username,password);
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS "+dbName+";");
            connection.close();
            source.setDriverClassName("com.mysql.cj.jdbc.Driver");
            source.setUrl("jdbc:mysql://localhost/"+dbName);
            source.setUsername(username);
            source.setPassword(password);
            source.setMaxIdle(20);
            source.setMinIdle(1);
            source.setMaxOpenPreparedStatements(200);
        }
        catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
            System.err.println("cannot load database driver.\nexit.");
            System.exit(1);
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
            System.err.println("cannot connect to database.\nexit.");
            System.exit(1);
        }
    }

    public static Connection getConnection() throws SQLException{
        return source.getConnection();
    }

    private ConnectionPool(){

    }
}
