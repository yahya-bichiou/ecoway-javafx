package org.example.Interfaces;
import java.util.List;
public interface Icategorie <C>{
    void addcategorie(C c);
    void updatecategorie(C c);
    void deletecategorie(C c);
    List<C> getAll();
}
