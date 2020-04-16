package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Mapper<T, I> implements IMapper<T, I> {

    abstract protected String getFindStatement(I id);

    abstract protected String getFindStatement(I id, ArrayList<String> columnNames);

    abstract protected String getInsertStatement(T obj);

    abstract protected String getDeleteStatement(I id);

    abstract protected T getObject(ResultSet rs) throws SQLException;

    abstract protected T getPartialObject(ResultSet rs, ArrayList<String> columnNames) throws SQLException;

    @Override
    public T find(I id, ArrayList<String> columnNames) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getFindStatement(id, columnNames))
        ) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return getPartialObject(rs, columnNames);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public T find(I id) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getFindStatement(id))
        ) {
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return getObject(rs);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(T obj) throws SQLException {
        try (
                Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getInsertStatement(obj))
                ) {
            stmt.executeUpdate();
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
        } catch (SQLException e) {
            throw e;
        }
    }
}
