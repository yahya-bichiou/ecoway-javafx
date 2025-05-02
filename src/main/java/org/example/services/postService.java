package org.example.services;

import org.example.models.post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class postService {
    private final Connection connection;

    public postService() throws SQLException {
        this.connection = MaConnexion.getInstance().getConn();
    }

    // CREATE - Ajouter un post
    public void createPost(post post) throws SQLException {
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
            st.setString(4, post.getImage());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du post, aucune ligne affectée.");
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
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
                post.setId(rs.getInt("id"));
                posts.add(post);
            }
        }
        return posts;
    }

    // UPDATE - Mettre à jour un post
    public void updatePost(post post) throws SQLException {
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
        }
    }

    // Récupérer le titre d’un post par son ID
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

    // 🔥 Récupérer Top 5 Posts par nombre de commentaires
    public List<Object[]> getTop5PostsByComments() throws SQLException {
        List<Object[]> topPosts = new ArrayList<>();
        String query = """
                SELECT p.titre, COUNT(c.id) AS nb_comments
                FROM post p
                LEFT JOIN commentaire c ON p.id = c.post_id
                GROUP BY p.id
                ORDER BY nb_comments DESC
                LIMIT 5
                """;

        try (PreparedStatement st = connection.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Object[] postData = new Object[2];
                postData[0] = rs.getString("titre");        // Titre du post
                postData[1] = rs.getInt("nb_comments");      // Nombre de commentaires
                topPosts.add(postData);
            }
        }
        return topPosts;
    }
}
