package services;

import utils.MaConnexion;
import models.participant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class participantService {
    private Connection connection;

    public participantService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // Vérifie si le téléphone existe déjà
    public boolean telephoneExists(String telephone) throws SQLException {
        String query = "SELECT COUNT(*) FROM participant WHERE telephone = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, telephone);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    // Vérifie si le Email existe déjà
    public boolean EmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM participant WHERE email = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // CREATE avec vérification d'unicité
    public boolean createParticipant(participant participant) throws SQLException {
        // Vérifier d'abord si le téléphone existe
        if (telephoneExists(participant.getTelephone())) {
            return false;
        }

        String query = "INSERT INTO participant (nom, age, email, telephone, env_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, participant.getNom());
            st.setInt(2, participant.getAge());
            st.setString(3, participant.getEmail());
            st.setString(4, participant.getTelephone());
            st.setInt(5, participant.getEnv_id());

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du participant, aucune ligne affectée.");
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    participant.setId(rs.getInt(1));
                }
            }
        }
        return true;
    }

    // READ
    public List<participant> getAllparticipant() throws SQLException {
        List<participant> participants = new ArrayList<>();
        String query = "SELECT * FROM participant";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                participant participant = new participant(
                        rs.getString("nom"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getInt("env_id")
                );
                participant.setId(rs.getInt("id"));
                participants.add(participant);
            }
        }
        return participants;
    }

    // UPDATE avec vérification d'unicité
    public boolean updateParticipant(participant participant) throws SQLException {
        // Vérifier si le nouveau téléphone existe déjà pour un autre participant
        String checkQuery = "SELECT COUNT(*) FROM participant WHERE telephone = ? AND id != ?";
        try (PreparedStatement checkSt = connection.prepareStatement(checkQuery)) {
            checkSt.setString(1, participant.getTelephone());
            checkSt.setInt(2, participant.getId());
            try (ResultSet rs = checkSt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Téléphone existe déjà pour un autre participant
                }
            }
        }

        String query = "UPDATE participant SET nom=?, age=?, email=?, telephone=?, env_id=? WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, participant.getNom());
            st.setInt(2, participant.getAge());
            st.setString(3, participant.getEmail());
            st.setString(4, participant.getTelephone());
            st.setInt(5, participant.getEnv_id());
            st.setInt(6, participant.getId());
            st.executeUpdate();
        }
        return true;
    }

    // DELETE
    public void deleteParticipant(int id) throws SQLException {
        String query = "DELETE FROM participant WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }
// bech ne5ou el participant
    public List<Integer> getEventIdsByUserEmail(String email) throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String query = "SELECT env_id FROM participant WHERE email = ?";

        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                eventIds.add(rs.getInt("env_id"));
            }
        }
        return eventIds;
    }
}