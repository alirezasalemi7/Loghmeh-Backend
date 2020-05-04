package dataAccess.Mappers;

import dataAccess.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Mapper<T, I> implements IMapper<T, I> {

    abstract protected PreparedStatement getFindStatement(Connection connection, I id) throws SQLException;

    abstract protected PreparedStatement getInsertStatement(Connection connection, T obj) throws SQLException;

    abstract protected PreparedStatement getDeleteStatement(Connection connection, I id) throws SQLException;

    abstract protected T getObject(ResultSet rs) throws SQLException;


    @Override
    public T find(I id) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = getFindStatement(connection, id)
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
                PreparedStatement stmt = getInsertStatement(connection, obj)
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
                PreparedStatement stmt = getDeleteStatement(connection, id)
                ) {
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }
}
