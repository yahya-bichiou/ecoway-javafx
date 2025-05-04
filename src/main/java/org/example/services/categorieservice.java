package org.example.services;

import org.example.Interfaces.Icategorie;
import org.example.models.categorie;
import org.example.utile.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class categorieservice implements Icategorie<categorie> {

    private Connection con;

    public categorieservice() {
        this.con = MaConnexion.getInstance().getConn();
    }

    // CREATE
    @Override
    public void addcategorie(categorie c) {
        String query = "INSERT INTO categorie (nom, description) VALUES (?, ?)";
        try (PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, c.getNom());
            st.setString(2, c.getDescription());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            throw new RuntimeException("Échec de l'ajout de la catégorie", e);
        }
    }

    // READ
    @Override
    public List<categorie> getAll() {
        List<categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                categories.add(extractCategorieFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories : " + e.getMessage());
            throw new RuntimeException("Échec de la récupération des catégories", e);
        }
        return categories;
    }

    // GET BY ID
    public categorie getById(int id) {
        String query = "SELECT * FROM categorie WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return extractCategorieFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
            throw new RuntimeException("Échec de la récupération de la catégorie", e);
        }
        return null;
    }

    // UPDATE
    @Override
    public void updatecategorie(categorie c) {
        String query = "UPDATE categorie SET nom=?, description=? WHERE id=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, c.getNom());
            st.setString(2, c.getDescription());
            st.setInt(3, c.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la catégorie : " + e.getMessage());
            throw new RuntimeException("Échec de la mise à jour de la catégorie", e);
        }
    }

    // DELETE
    @Override
    public void deletecategorie(int id) {
        String query = "DELETE FROM categorie WHERE id=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
            throw new RuntimeException("Échec de la suppression de la catégorie", e);
        }
    }
    // Méthode pour récupérer une catégorie par son nom
    public categorie getCategorieByName(String name) {
      for (categorie cat : getAll()) {
            if (cat.getNom().equalsIgnoreCase(name)) {
                return cat;
            }
        }
        return null; // Si la catégorie n'est pas trouvée
    }
    // Méthode utilitaire pour extraire une catégorie d'un ResultSet
    private categorie extractCategorieFromResultSet(ResultSet rs) throws SQLException {
        categorie c = new categorie();
        c.setId(rs.getInt("id"));
        c.setNom(rs.getString("nom"));
        c.setDescription(rs.getString("description"));
        return c;
    }
    public categorie getCategorieById(int id) {
        categorie c = null;
        try {
            String req = "SELECT * FROM categorie WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new categorie();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

}