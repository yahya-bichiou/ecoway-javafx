package services;

import interfaces.LivraisonInterface;
import models.Livraisons;
import utils.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivraisonService implements LivraisonInterface <Livraisons> {

    Connection connection;

    public LivraisonService() {
        this.connection = Connexion.getInstance().getCon();
    }
    @Override
    public void add(Livraisons l) {
        String query = "INSERT INTO livraison (commande_id,livreur,adresse,date,status,mode,prix) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, l.getCommandeId());
            ps.setString(2, l.getLivreur());
            ps.setString(3, l.getAdresse());
            ps.setTimestamp(4, Timestamp.valueOf(l.getDate()));
            ps.setString(5, l.getStatus());
            ps.setString(6, l.getMode());
            ps.setDouble(7, l.getPrix());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    l.setId(rs.getInt(1));
                }
            }
        }catch (SQLException e) {
            System.out.println("Erreur Ajout " + e.getMessage());
        }
    }

    @Override
    public void update(Livraisons l) {
        String query = "UPDATE livraison SET commande_id = ?, livreur = ?, adresse = ?, date = ?, status = ?, mode = ?, prix = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, l.getCommandeId());
            ps.setString(2, l.getLivreur());
            ps.setString(3, l.getAdresse());
            ps.setTimestamp(4, Timestamp.valueOf(l.getDate()));
            ps.setString(5, l.getStatus());
            ps.setString(6, l.getMode());
            ps.setDouble(7, l.getPrix());
            ps.setInt(8, l.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Aucune livraison trouvée avec l'id: " + l.getId());
            } else {
                System.out.println("Livraison mise à jour avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur Mise à jour: " + e.getMessage());
        }
    }


    @Override
    public void delete(int id) {
        String query = "DELETE FROM livraison WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Aucune livraison trouvée avec l'id: " + id);
            } else {
                System.out.println("Livraison supprimée avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur Suppression: " + e.getMessage());
        }
    }

    @Override
    public List<Livraisons> getAll() {
        List<Livraisons> list = new ArrayList<>();
        String query = "SELECT * FROM livraison";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Livraisons l = new Livraisons();
                l.setId(rs.getInt("id"));
                l.setCommandeId(rs.getInt("commande_id"));
                l.setLivreur(rs.getString("livreur"));
                l.setAdresse(rs.getString("adresse"));
                l.setDate(rs.getTimestamp("date").toLocalDateTime());
                l.setStatus(rs.getString("status"));
                l.setMode(rs.getString("mode"));
                l.setPrix(rs.getFloat("prix"));

                list.add(l);
            }
        } catch (SQLException e) {
            System.out.println("Erreur Lecture: " + e.getMessage());
        }

        return list;
    }

}
