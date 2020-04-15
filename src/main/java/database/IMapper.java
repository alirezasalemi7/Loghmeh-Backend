package database;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IMapper<T, I> {
    T find(I id) throws SQLException;
    ArrayList<T> getInfo(ArrayList<String> columnNames) throws SQLException;
    void insert(T obj) throws SQLException;
    void delete(I id) throws SQLException;
}
