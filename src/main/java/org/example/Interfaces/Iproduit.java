package org.example.Interfaces;
import java.util.List;

public interface Iproduit<P> {
    boolean addproduit(P produit);
    void updateproduit(P produit);
    void deleteproduit(P produit);
    List<P> getAll();
}
