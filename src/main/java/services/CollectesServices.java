package services;

import interfaces.CollectesInterface;
import models.Collectes;
import models.Depots;
import utils.Connexion;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CollectesServices implements CollectesInterface<Collectes> {

        Connection connection;

        public CollectesServices() {
            this.connection = Connexion.getInstance().getCon();
        }
        @Override
        public void add(Collectes c) {
            String query = "INSERT INTO collecte (depot_id,quantite,date,responsable) VALUES (?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, c.getDepot_id());
                ps.setDouble(2, c.getQuantite());
                LocalDate localDate = c.getDate();
                LocalDateTime localDateTime = localDate.atStartOfDay();
                ps.setTimestamp(3, Timestamp.valueOf(localDateTime));
                ps.setString(4, c.getResponsable());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setId(rs.getInt(1));
                    }
                }
            }catch (SQLException e) {
                System.out.println("Erreur Ajout " + e.getMessage());
            }
        }

    @Override
    public void update(Collectes item) {
        String query = "UPDATE collecte SET depot_id = ?, quantite = ?, date = ?, responsable = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, item.getDepot_id());
            ps.setDouble(2, item.getQuantite());
            LocalDate localDate = item.getDate();  // assuming it's LocalDate
            LocalDateTime localDateTime = localDate.atStartOfDay();  // set time to 00:00
            ps.setTimestamp(3, Timestamp.valueOf(localDateTime));
            ps.setString(4, item.getResponsable());
            ps.setInt(5, item.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Collecte updated successfully.");
            } else {
                System.out.println("No Collecte found with ID: " + item.getId());
            }

        } catch (SQLException e) {
            System.out.println("Erreur Update: " + e.getMessage());
        }
    }


    @Override
    public void delete(Collectes c) {
        String query = "DELETE FROM collecte WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, c.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Collecte with ID " + c.getId() + " was deleted successfully.");
            } else {
                System.out.println("No Collecte found with ID " + c.getId() + ".");
            }
        } catch (SQLException e) {
            System.out.println("Erreur Suppression: " + e.getMessage());
        }
    }

        @Override
        public List<Collectes> select() throws SQLException{
            List<Collectes> c =new ArrayList<>();
            String sql="select * from collecte";
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery(sql);
            while (resultSet.next()){
                Collectes collecte =new Collectes();
                collecte.setId(resultSet.getInt("id"));
                collecte.setDepot_id(resultSet.getInt("depot_id"));
                collecte.setQuantite(resultSet.getDouble("quantite"));
                collecte.setDate(resultSet.getTimestamp("date").toLocalDateTime().toLocalDate());
                collecte.setResponsable(resultSet.getString("responsable"));
                c.add(collecte);
            }
            return c;
        }

}
