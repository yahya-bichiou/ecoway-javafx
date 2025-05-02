package org.example.services;

import org.example.models.reaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class reactionService {
    private final Connection connection;

    public reactionService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // ➕ Ajouter une réaction (like ou dislike)
    public void addReaction(reaction reaction) throws SQLException {
        String query = "INSERT INTO reaction (post_id, type, utilisateur) VALUES (?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, reaction.getPostId());
            st.setString(2, reaction.getType());
            st.setString(3, reaction.getUtilisateur());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réaction : " + e.getMessage());
            throw e;
        }
    }

    // 🔢 Compter les réactions par type pour un post
    public int countReactionsByType(int postId, String type) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM reaction WHERE post_id = ? AND type = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            st.setString(2, type);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des réactions : " + e.getMessage());
            throw e;
        }
        return 0;
    }

    // 🔍 Vérifier si un utilisateur a déjà réagi à un post
    public boolean hasUserReacted(int postId, String utilisateur) throws SQLException {
        String query = "SELECT 1 FROM reaction WHERE post_id = ? AND utilisateur = ? LIMIT 1";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            st.setString(2, utilisateur);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la réaction utilisateur : " + e.getMessage());
            throw e;
        }
    }

    // 🗑 Supprimer une réaction spécifique
    public void deleteReaction(int reactionId) throws SQLException {
        String query = "DELETE FROM reaction WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, reactionId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réaction : " + e.getMessage());
            throw e;
        }
    }

    // 🗑 Supprimer toutes les réactions liées à un post
    public void deleteReactionsByPostId(int postId) throws SQLException {
        String query = "DELETE FROM reaction WHERE post_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des réactions du post : " + e.getMessage());
            throw e;
        }
    }

    // 📋 Récupérer toutes les réactions pour un post
    public List<reaction> getReactionsByPostId(int postId) throws SQLException {
        List<reaction> reactions = new ArrayList<>();
        String query = "SELECT * FROM reaction WHERE post_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    reaction r = new reaction(
                            rs.getInt("id"),
                            rs.getInt("post_id"),
                            rs.getString("type"),
                            rs.getString("utilisateur")
                    );
                    reactions.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réactions : " + e.getMessage());
            throw e;
        }
        return reactions;
    }
    // ✨ Mettre à jour le type de réaction d'un utilisateur pour un post
    public void updateReaction(int postId, String utilisateur, String newType) throws SQLException {
        String query = "UPDATE reaction SET type = ? WHERE post_id = ? AND utilisateur = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, newType);
            st.setInt(2, postId);
            st.setString(3, utilisateur);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réaction : " + e.getMessage());
            throw e;
        }
    }

}
