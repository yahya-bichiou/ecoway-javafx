package org.example.services;

import org.example.models.post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class postService {
    private Connection connection;

    // Constructeur de la classe postService
    public postService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // CREATE - Ajout d'un post
    public void createPost(post post) throws SQLException {
        // Vérifier si le titre, la description ou le créateur sont vides ou nuls
        if (post.getTitre() == null || post.getTitre().isEmpty()) {
            throw new SQLException("Le titre du post ne peut pas être vide.");
        }
        if (post.getDescription() == null || post.getDescription().isEmpty()) {
            throw new SQLException("La description du post ne peut pas être vide.");
        }
        if (post.getCreateur() == null || post.getCreateur().isEmpty()) {
            throw new SQLException("Le créateur du post ne peut pas être vide.");
        }

        String query = "INSERT INTO post (titre, description, createur, image) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, post.getTitre());
            st.setString(2, post.getDescription());
            st.setString(3, post.getCreateur());
            st.setString(4, post.getImage());  // Si image est nullable, vérifiez cela dans le modèle aussi

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du post, aucune ligne affectée.");
            }

            // Récupérer l'ID auto-généré du post
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1)); // Récupère l'ID auto-généré
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la création du post : " + e.getMessage());
            throw e;  // Propager l'exception
        }
    }

    // READ - Récupérer tous les posts
    public List<post> getAllPosts() throws SQLException {
        List<post> posts = new ArrayList<>();
        String query = "SELECT * FROM post";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                post post = new post(
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("createur"),
                        rs.getString("image")
                );
                post.setId(rs.getInt("id"));  // Définir l'ID du post depuis la base de données
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des posts : " + e.getMessage());
            throw e;  // Propager l'exception
        }
        return posts;
    }

    // UPDATE - Mettre à jour un post
    public void updatePost(post post) throws SQLException {
        // Vérification de la validité des données
        if (post.getId() <= 0) {
            throw new SQLException("L'ID du post est invalide.");
        }

        String query = "UPDATE post SET titre=?, description=?, createur=?, image=? WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, post.getTitre());
            st.setString(2, post.getDescription());
            st.setString(3, post.getCreateur());
            st.setString(4, post.getImage());
            st.setInt(5, post.getId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucun post mis à jour, vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour du post : " + e.getMessage());
            throw e;  // Propager l'exception
        }
    }

    // DELETE - Supprimer un post
    public void deletePost(int id) throws SQLException {
        if (id <= 0) {
            throw new SQLException("L'ID du post est invalide.");
        }

        String query = "DELETE FROM post WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucun post supprimé, vérifiez l'ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression du post : " + e.getMessage());
            throw e;  // Propager l'exception
        }
    }
    // ✅ Récupérer le titre d’un post par son ID
    public String getPostTitleById(int postId) throws SQLException {
        String query = "SELECT titre FROM post WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, postId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("titre");
                } else {
                    throw new SQLException("Aucun post trouvé avec l'ID : " + postId);
                }
            }
        }
    }

}
