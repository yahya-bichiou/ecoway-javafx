package interfaces;

import java.sql.SQLException;
import java.util.List;

public interface CollectesInterface <T>{
    public void add(T item);
    public void update(T item);
    public void delete(T item);
    List<T> select() throws SQLException;
}

