package org.example.service;

import org.example.MaConnexion;
import org.example.model.evenement;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class evenementService {
    Connection connection;
    private final DateTimeFormatter dbDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public evenementService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // CREATE
    public void createEvenement(evenement event) throws SQLException {
        String query = "INSERT INTO evenement (titre, description, contact, localisation, date_d, recomponse) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, event.getTitre());
            st.setString(2, event.getDescription());
            st.setString(3, event.getContact());
            st.setString(4, event.getLocalisation());
            st.setString(5, event.getDate_d().format(dbDateFormatter)); // Conversion LocalDate -> String
            st.setInt(6, event.getRecomponse());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) event.setId(rs.getInt(1));
            }
        }
    }

    // READ
    public List<evenement> getAllevenement() throws SQLException {
        List<evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                evenement event = new evenement(
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("contact"),
                        rs.getString("localisation"),
                        LocalDate.parse(rs.getString("date_d"), dbDateFormatter), // Conversion String -> LocalDate
                        rs.getInt("recomponse")
                );
                event.setId(rs.getInt("id"));
                events.add(event);
            }
        }
        return events;
    }

    // UPDATE
    public void updateEvenement(evenement event) throws SQLException {
        String query = "UPDATE evenement SET titre=?, description=?, contact=?, localisation=?, date_d=?, recomponse=? WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, event.getTitre());
            st.setString(2, event.getDescription());
            st.setString(3, event.getContact());
            st.setString(4, event.getLocalisation());
            st.setString(5, event.getDate_d().format(dbDateFormatter)); // Conversion LocalDate -> String
            st.setInt(6, event.getRecomponse());
            st.setInt(7, event.getId());
            st.executeUpdate();
        }
    }

    // DELETE (inchangé)
    public void deleteEvenement(int id) throws SQLException {
        String query = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }
}