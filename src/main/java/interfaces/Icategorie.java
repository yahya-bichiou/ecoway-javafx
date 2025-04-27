package interfaces;
import java.util.List;
public interface Icategorie <C>{
    void addcategorie(C c);
    void updatecategorie(C c);
    void deletecategorie(int id);
    List<C> getAll();
}
