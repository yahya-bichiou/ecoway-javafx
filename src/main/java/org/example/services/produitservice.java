package org.example.services;

import org.example.Interfaces.Iproduit;
import org.example.models.produit;
import org.example.utile.MaConnexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class produitservice implements Iproduit<produit> {

    Connection con;

    public produitservice() {
        this.con = MaConnexion.getInstance().getConn();
    }

    @Override
    public boolean addproduit(produit p) {
        String query = "INSERT INTO produit (nom, description, qualite, quantite_disponible, prix, date_ajout, image, categorie_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, p.getNom());
            st.setString(2, p.getDescription());
            st.setString(3, p.getQualite());
            st.setInt(4, p.getQuantiteDisponible());
            st.setDouble(5, p.getPrix());
            st.setTimestamp(6, Timestamp.valueOf(p.getDateAjout() != null ? p.getDateAjout() : LocalDateTime.now()));
            st.setString(7, p.getImage());
            st.setInt(8, p.getCategorieId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
        return false;
    }

    // READ
    @Override
    public List<produit> getAll() {
        return getAll(1, Integer.MAX_VALUE); // Retourne tous les produits sans pagination
    }

    // Nouvelle méthode pour la pagination
    public List<produit> getAll(int page, int itemsPerPage) {
        List<produit> produits = new ArrayList<>();
        int offset = (page - 1) * itemsPerPage;

        String query = "SELECT * FROM produit LIMIT ? OFFSET ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, itemsPerPage);
            st.setInt(2, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération paginée des produits : " + e.getMessage());
        }
        return produits;
    }

    // Méthode pour filtrer par catégorie avec pagination
    public List<produit> getProduitsByCategorie(int categorieId, int page, int itemsPerPage) {
        List<produit> produits = new ArrayList<>();
        int offset = (page - 1) * itemsPerPage;

        String query = "SELECT * FROM produit WHERE categorie_id = ? LIMIT ? OFFSET ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, categorieId);
            st.setInt(2, itemsPerPage);
            st.setInt(3, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits par catégorie : " + e.getMessage());
        }
        return produits;
    }

    // Méthode utilitaire pour extraire un produit d'un ResultSet
    private produit extractProduitFromResultSet(ResultSet rs) throws SQLException {
        produit p = new produit();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setDescription(rs.getString("description"));
        p.setQualite(rs.getString("qualite"));
        p.setQuantiteDisponible(rs.getInt("quantite_disponible"));
        p.setPrix(rs.getDouble("prix"));
        p.setDateAjout(rs.getTimestamp("date_ajout").toLocalDateTime());
        p.setImage(rs.getString("image"));
        p.setCategorieId(rs.getInt("categorie_id"));
        return p;
    }

    // Comptage des produits
    public int countAllProduits() {
        String query = "SELECT COUNT(*) FROM produit";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des produits : " + e.getMessage());
            return 0;
        }
    }

    // Comptage des produits par catégorie
    public int countProduitsByCategorie(int categorieId) {
        String query = "SELECT COUNT(*) FROM produit WHERE categorie_id = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, categorieId);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des produits par catégorie : " + e.getMessage());
            return 0;
        }
    }

    // UPDATE
    @Override
    public void updateproduit(produit p) {
        String query = "UPDATE produit SET nom=?, description=?, qualite=?, quantite_disponible=?, prix=?, date_ajout=?, image=?, categorie_id=? WHERE id=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, p.getNom());
            st.setString(2, p.getDescription());
            st.setString(3, p.getQualite());
            st.setInt(4, p.getQuantiteDisponible());
            st.setDouble(5, p.getPrix());
            st.setTimestamp(6, Timestamp.valueOf(p.getDateAjout()));
            st.setString(7, p.getImage());
            st.setInt(8, p.getCategorieId());
            st.setInt(9, p.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }

    // DELETE
    @Override
    public void deleteproduit(produit p) {
        String query = "DELETE FROM produit WHERE id=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, p.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }
}