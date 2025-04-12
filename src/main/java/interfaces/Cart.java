package interfaces;

import models.Commandes;

import java.sql.SQLException;
import java.util.List;

public interface Cart <T> {
    public void add(T item);
    public void update(T item);
    public void delete(T item);
    List<Commandes> select() throws SQLException;
}
