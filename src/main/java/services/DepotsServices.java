package services;

import interfaces.DepotsInterface;
import models.Depots;
import utils.Connexion;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DepotsServices implements DepotsInterface <Depots> {

    Connection connection;

    public DepotsServices() {
        this.connection = Connexion.getInstance().getCon();
    }
    @Override
    public void add(Depots d) {
        String query = "INSERT INTO depot (nom,adresse,capacite,image) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNom());
            ps.setString(2, d.getAdresse());
            ps.setInt(3, d.getCapacite());
            ps.setString(4, d.getImage());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getInt(1));
                }
            }
        }catch (SQLException e) {
            System.out.println("Erreur Ajout " + e.getMessage());
        }
    }

    @Override
    public void update(Depots item) {
        String query = "UPDATE depot SET nom = ?, adresse = ?, capacite = ?, image = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, item.getNom());
            ps.setString(2, item.getAdresse());
            ps.setInt(3, item.getCapacite());
            ps.setString(4, item.getImage());
            ps.setInt(5, item.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Depot updated successfully.");
            } else {
                System.out.println("No Depot found with ID: " + item.getId());
            }

        } catch (SQLException e) {
            System.out.println("Erreur Update: " + e.getMessage());
        }
    }


    @Override
    public void delete(Depots d) {
        String query = "DELETE FROM depot WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, d.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Depot with ID " + d.getId() + " was deleted successfully.");
            } else {
                System.out.println("No Depot found with ID " + d.getId() + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erreur Suppression: " + e.getMessage());
        }
    }
    @Override
    public List<Depots> select() throws SQLException{
        List<Depots> d =new ArrayList<>();
        String sql="select * from depot";
        Statement statement=connection.createStatement();
        ResultSet resultSet= statement.executeQuery(sql);
        while (resultSet.next()){
            Depots dep =new Depots();
            dep.setId(resultSet.getInt("id"));
            dep.setNom(resultSet.getString("nom"));
            dep.setAdresse(resultSet.getString("adresse"));
            dep.setCapacite(resultSet.getInt("capacite"));
            dep.setImage(resultSet.getString("image"));
            d.add(dep);
        }
        return d;
    }


    public Depots getDepotById(int depotId) {
        String sql = "SELECT * FROM depot WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, depotId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Depots depot = new Depots();
                depot.setId(rs.getInt("id"));
                depot.setNom(rs.getString("nom"));
                depot.setAdresse(rs.getString("adresse"));
                depot.setCapacite(rs.getInt("capacite"));
                depot.setImage(rs.getString("image"));
                return depot;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du dépôt: " + e.getMessage());
        }
        return null;
    }

}
