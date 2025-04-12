package services;

import interfaces.Cart;
import models.Commandes;
import utils.Connexion;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements Cart <Commandes> {

    Connection connection;

    public CommandeService() {
        this.connection = Connexion.getInstance().getCon();
    }
    @Override
    public void add(Commandes c) {
        String query = "INSERT INTO commande (client_id,status,mode_Paiement,date,prix,produits) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getClientId());
            ps.setString(2, c.getStatus());
            ps.setString(3, c.getModeDePaiement());
            LocalDate localDate = c.getDate(); // assuming c.getDate() returns a LocalDate
            LocalDateTime localDateTime = localDate.atStartOfDay(); // 00:00 time
            ps.setTimestamp(4, Timestamp.valueOf(localDateTime));
            ps.setDouble(5, c.getPrix());
            ps.setString(6, c.getProduits());
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
    public void update(Commandes item) {

    }

    @Override
    public void delete(Commandes item) {

    }

    @Override
    public List<Commandes> select() throws SQLException{
        List<Commandes> c =new ArrayList<>();
        String sql="select * from commande";
        Statement statement=connection.createStatement();
        ResultSet resultSet= statement.executeQuery(sql);
        while (resultSet.next()){
            Commandes commande =new Commandes();
            commande.setId(resultSet.getInt("id"));
            commande.setClientId(resultSet.getInt("client_id"));
            commande.setStatus(resultSet.getString("status"));
            commande.setModeDePaiement(resultSet.getString("mode_paiement"));
            commande.setDate(resultSet.getTimestamp("date").toLocalDateTime().toLocalDate());
            commande.setPrix(resultSet.getFloat("prix"));
            commande.setProduits(resultSet.getString("produits"));
            c.add(commande);
        }
        return c;
    }

}
