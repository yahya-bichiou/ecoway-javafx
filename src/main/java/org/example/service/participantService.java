package org.example.service;

import org.example.MaConnexion;
import org.example.model.participant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class participantService  {
         private Connection connection;

    public participantService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }
    // CREATE
    public void createParticipant(participant participant) throws SQLException {
        // Validation des données avant insertion
        /*if (participant.getNom() == null || participant.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide");
        }*/

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

        // UPDATE
        public void updateParticipant(participant participant) throws SQLException {
            String query = "UPDATE participant  SET nom=?, age=? , email=?, telephone=?, env_id=? WHERE id=?";
            try (PreparedStatement st = connection.prepareStatement(query)) {
                st.setString(1, participant.getNom());
                st.setInt(2, participant.getAge());
                st.setString(3,participant.getEmail() );
                st.setString(4,participant.getTelephone() );
                st.setInt(5,participant.getEnv_id() );
                st.setInt(6, participant.getId());
                st.executeUpdate();
            }
        }

        // DELETE
        public void deleteParticipant(int id) throws SQLException {
            String query = "DELETE FROM Participant WHERE id=?";
            try (PreparedStatement st = connection.prepareStatement(query)) {
                st.setInt(1, id);
                st.executeUpdate();
            }
        }
    }