package dataAccess.Mappers;

import java.sql.SQLException;

public interface IMapper<T, I> {

    T find(I id) throws SQLException;

    void insert(T obj) throws SQLException;

    void delete(I id) throws SQLException;
}
