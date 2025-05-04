package interfaces;

import java.util.List;

public interface LivraisonInterface <T> {
    void add(T item);
    void update(T item);
    void delete(int id);
    List<T> getAll();

}
