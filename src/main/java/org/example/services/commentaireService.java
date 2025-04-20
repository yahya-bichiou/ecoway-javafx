package org.example.services;

import org.example.models.commentaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commentaireService {
    private Connection connection;

    public commentaireService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // CREATE
    public void createCommentaire(commentaire commentaire) throws SQLException {
        String query = "INSERT INTO commentaire (message, auteur, post_id) VALUES (?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, commentaire.getMessage());
            st.setString(2, commentaire.getAuteur());
            st.setInt(3, commentaire.getPostId());

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du commentaire, aucune ligne affectée.");
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    commentaire.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ
    public List<commentaire> getAllCommentaires() throws SQLException {
        List<commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                commentaire commentaire = new commentaire(
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getString("auteur"),
                        rs.getInt("post_id"),
                        rs.getInt("is_inappropriate")
                );
                commentaires.add(commentaire);
            }
        }
        return commentaires;
    }

    // READ by post_id
    public List<commentaire> getCommentairesByPostId(int postId) throws SQLException {
        List<commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE post_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    commentaire commentaire = new commentaire(
                            rs.getInt("id"),
                            rs.getString("message"),
                            rs.getString("auteur"),
                            rs.getInt("post_id"),
                            rs.getInt("is_inappropriate")
                    );
                    commentaires.add(commentaire);
                }
            }
        }
        return commentaires;
    }

    // UPDATE
    public void updateCommentaire(commentaire commentaire) throws SQLException {
        String query = "UPDATE commentaire SET message=?, auteur=?, post_id=? WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, commentaire.getMessage());
            st.setString(2, commentaire.getAuteur());
            st.setInt(3, commentaire.getPostId());
            st.setInt(4, commentaire.getId());
            st.executeUpdate();
        }
    }

    // DELETE
    public void deleteCommentaire(int id) throws SQLException {
        String query = "DELETE FROM commentaire WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            st.executeUpdate();
        }
    }

    // GET POST TITLE BY ID
    public String getPostTitleById(int postId) throws SQLException {
        String query = "SELECT titre FROM post WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("titre");
                }
            }
        }
        return "Post inconnu";
    }
}
