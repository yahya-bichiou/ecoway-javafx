package services;

import interfaces.iService;
import models.user;
import utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class userService implements iService<user> {

    Connection cnx = MaConnexion.getInstance().getConn();

    @Override
    public void add(user user) {
        String defaultRole = "User";
        String defaultImage = "@/assets/user.png";


        String profileImage = (user.getImageProfile() == null || user.getImageProfile().isEmpty())
                ? defaultImage
                : user.getImageProfile();

        String SQL = "INSERT INTO user (name, email, password, profile_picture, roles) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement st = cnx.prepareStatement(SQL);
            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.setString(4, profileImage);
            st.setString(5, defaultRole);
            st.executeUpdate();
            System.out.println("User added");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    @Override
    public void update(user user) {
        String SQL = "UPDATE user SET name = ?, email = ?, password = ?, profile_picture = ? , roles = ?  WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(SQL)){
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getImageProfile());
            ps.setString(5, user.getRoles());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    @Override
    public void delete(user user) {
        String SQL="DELETE FROM user WHERE id=?";
        try(PreparedStatement ps = cnx.prepareStatement(SQL)) {
            ps.setInt(1,user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void blockUser(user user) {
        String sql = "UPDATE user SET blocked = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setBoolean(1, user.isBlocked());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<user> getAll() {
        String SQL="SELECT * FROM user";
        List<user> users = new ArrayList<user>();
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(SQL);
            while (res.next()) {
                user u = new user();
                u.setId(res.getInt("id"));
                u.setName(res.getString("name"));
                u.setEmail(res.getString("email"));
                u.setPassword(res.getString("password"));
                u.setImageProfile(res.getString("profile_picture"));
                u.setRoles(res.getString("roles"));
                u.setBlocked(res.getBoolean("blocked"));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public int validateCredentials(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Check if account is blocked
                if (resultSet.getBoolean("blocked")) {
                    return 2; // Account is blocked
                }
                return 1; // Valid credentials and not blocked
            }
            return 0; // Invalid credentials
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return 0;
        }
    }

    public List<user> searchUsers(String searchTerm) {
        List<user> results = new ArrayList<>();
        String query = "SELECT * FROM user WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(roles) LIKE ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            String likeTerm = "%" + searchTerm.toLowerCase() + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user u = new user();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setImageProfile(rs.getString("profile_picture"));
                u.setRoles(rs.getString("roles"));
                u.setBlocked(rs.getBoolean("blocked"));
                results.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean emailExists(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if email is found.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean addUser(String name, String email, String password, String profile_picture,String roles) {

        String query = "INSERT INTO user (name, email, password, profile_picture,roles) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, profile_picture);
            statement.setString(5, roles);
            statement.executeUpdate();
            return true; // Returns true if the user is successfully added.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public user getUserByEmail(String email) {
        String SQL = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement st = cnx.prepareStatement(SQL)) {
            st.setString(1, email);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                user u = new user();
                u.setId(res.getInt("id"));
                u.setName(res.getString("name"));
                u.setEmail(res.getString("email"));
                u.setPassword(res.getString("password"));
                u.setImageProfile(res.getString("profile_picture"));
                u.setRoles(res.getString("roles"));
                u.setResetToken(res.getString("reset_token")); // Make sure to set the reset token
                return u;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String generateResetToken(String email) {
        String token = UUID.randomUUID().toString();
        String SQL = "UPDATE user SET reset_token = ? WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(SQL)) {
            ps.setString(1, token);
            ps.setString(2, email);
            ps.executeUpdate();
            return token;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        String SQL = "UPDATE user SET password = ?, reset_token = NULL WHERE reset_token = ?";
        try (PreparedStatement ps = cnx.prepareStatement(SQL)) {
            ps.setString(1, newPassword);
            ps.setString(2, token);
            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
