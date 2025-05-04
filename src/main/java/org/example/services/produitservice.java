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
        String query = "INSERT INTO produit (catégorie_id,nom, description, qualite, quantite_disponible, prix, date_ajout, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, p.getCategorieId());
            st.setString(2, p.getNom());
            st.setString(3, p.getDescription());
            st.setString(4, p.getQualite());
            st.setInt(5, p.getQuantiteDisponible());
            st.setDouble(6, p.getPrix());
            st.setTimestamp(7, Timestamp.valueOf(p.getDateAjout() != null ? p.getDateAjout() : LocalDateTime.now()));
            st.setString(8, p.getImage());


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

    @Override
    public List<produit> getAllproduit() {
        return List.of();
    }

    @Override
    public List<produit> getAll() {
        return getAll(1, Integer.MAX_VALUE);
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
    public List<produit> getProduitsByCategorie(int catégorie_id, int page, int itemsPerPage) {
        List<produit> produits = new ArrayList<>();
        int offset = (page - 1) * itemsPerPage;

        String query = "SELECT * FROM produit WHERE catégorie_id = ? LIMIT ? OFFSET ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, catégorie_id);  // Utilisation correcte de categorie_id
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

    // Nouvelle méthode pour la recherche de tous les produits par mots-clés
    public List<produit> searchProducts(String keyword, int page, int itemsPerPage) {
        List<produit> produits = new ArrayList<>();
        int offset = (page - 1) * itemsPerPage;

        String query = "SELECT * FROM produit WHERE nom LIKE ? OR description LIKE ? LIMIT ? OFFSET ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            st.setString(1, searchTerm);
            st.setString(2, searchTerm);
            st.setInt(3, itemsPerPage);
            st.setInt(4, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de produits : " + e.getMessage());
        }
        return produits;
    }

    // Nouvelle méthode pour la recherche de produits par catégorie et mots-clés
    public List<produit> searchProductsByCategorie(int categorieId, String keyword, int page, int itemsPerPage) {
        List<produit> produits = new ArrayList<>();
        int offset = (page - 1) * itemsPerPage;

        String query = "SELECT * FROM produit WHERE catégorie_id = ? AND (nom LIKE ? OR description LIKE ?) LIMIT ? OFFSET ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            st.setInt(1, categorieId);
            st.setString(2, searchTerm);
            st.setString(3, searchTerm);
            st.setInt(4, itemsPerPage);
            st.setInt(5, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de produits par catégorie : " + e.getMessage());
        }
        return produits;
    }

    // Méthode pour compter les résultats de recherche
    public int countSearchResults(String keyword) {
        String query = "SELECT COUNT(*) FROM produit WHERE nom LIKE ? OR description LIKE ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            st.setString(1, searchTerm);
            st.setString(2, searchTerm);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des résultats de recherche : " + e.getMessage());
            return 0;
        }
    }

    // Méthode pour compter les résultats de recherche par catégorie
    public int countSearchResultsByCategorie(int categorieId, String keyword) {
        String query = "SELECT COUNT(*) FROM produit WHERE catégorie_id = ? AND (nom LIKE ? OR description LIKE ?)";
        try (PreparedStatement st = con.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            st.setInt(1, categorieId);
            st.setString(2, searchTerm);
            st.setString(3, searchTerm);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des résultats de recherche par catégorie : " + e.getMessage());
            return 0;
        }
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
        p.setCategorieId(rs.getInt("catégorie_id"));  // Assurez-vous que c'est bien "categorie_id" avec é
        p.setLikes(rs.getInt("likes"));

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
    public int countProduitsByCategorie(int catégorie_id) {
        String query = "SELECT COUNT(*) FROM produit WHERE \"catégorie_id\" = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, catégorie_id);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du comptage des produits par catégorie : " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void updateproduit(produit p) {
        String query = "UPDATE produit SET nom=?, description=?, qualite=?, quantite_disponible=?, prix=?, date_ajout=?, image=?, \"catégorie_id\"=? WHERE id=?";
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

    public void deleteproduit(int id) {
        String query = "DELETE FROM produit WHERE id=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }
    public void incrementLike(produit produit) {
        String query = "UPDATE produit SET likes = likes + 1 WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, produit.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'incrémentation du like : " + e.getMessage());
        }

    }
    public int getTotalLikes() {
        String query = "SELECT SUM(likes) FROM produit";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du total des likes : " + e.getMessage());
            return 0;
        }
    }
    public List<produit> getMostLikedProducts(int limit) {
        List<produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit ORDER BY likes DESC LIMIT ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, limit);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits les plus aimés : " + e.getMessage());
        }
        return produits;
    }
}