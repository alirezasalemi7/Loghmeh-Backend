package database;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IMapper<T, I> {

    T find(I id, ArrayList<String> columnNames) throws SQLException;

    T find(I id) throws SQLException;

    void insert(T obj) throws SQLException;

    void update(T obj) throws SQLException;

    void update(I id, T obj) throws SQLException;

    void delete(I id) throws SQLException;
}
