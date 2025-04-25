package org.example.Interfaces;
import org.example.models.produit;

import java.util.List;

public interface Iproduit<P> {
    boolean addproduit(P produit);

    // READ
    List<produit> getAllproduit();

    void updateproduit(P produit);
    void deleteproduit(int p);
    List<P> getAll();
}
