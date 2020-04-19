package database.Mappers;

import database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Mapper<T, I> implements IMapper<T, I> {

    abstract protected String getFindStatement(I id);

    abstract protected String getInsertStatement(T obj);

    abstract protected String getDeleteStatement(I id);

    abstract protected T getObject(ResultSet rs) throws SQLException;


    @Override
    public T find(I id) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getFindStatement(id))
        ) {
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
                return null;
            T object = getObject(rs);
            rs.close();
            stmt.close();
            connection.close();
            return object;
        } catch (SQLException e) {
            throw e;
        }
        finally {

        }
    }

    @Override
    public void insert(T obj) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getInsertStatement(obj))
                ) {
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void delete(I id) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getDeleteStatement(id))
                ) {
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }
}
