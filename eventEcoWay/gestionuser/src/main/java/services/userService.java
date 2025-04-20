package services;

import interfaces.iService;
import models.user;
import utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userService implements iService<user> {

    Connection cnx = MaConnexion.getInstance().getConn();

    @Override
    public void add(user user) {
        String defaultRole = "User"; // Default role
        String SQL = "INSERT INTO user (name, email, password, imageProfile, role) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement st = cnx.prepareStatement(SQL);
            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.setString(4, user.getImageProfile());
            st.setString(5, defaultRole); // Enforce default role
            st.executeUpdate();
            System.out.println("User added");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void update(user user) {
        String SQL = "UPDATE user SET name = ?, email = ?, password = ?, imageProfile = ? , role = ?  WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(SQL)){
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getImageProfile());
            ps.setString(5, user.getRole());
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
                u.setImageProfile(res.getString("imageProfile"));
                u.setRole(res.getString("role"));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public boolean validateCredentials(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if credentials are valid.
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        }

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

    public boolean promoteToAdmin(int userId) {


        String query = "UPDATE user SET role = 'Admin' WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean addUser(String name, String email, String password, String imageProfile,String role) {

        String query = "INSERT INTO user (name, email, password, imageProfile,role) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, imageProfile);
            statement.setString(5, role);
            statement.executeUpdate();
            return true; // Returns true if the user is successfully added.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // teba3 sesion Event
    public user getUserByEmail(String email) {
    String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new user(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("imageProfile"),
                    resultSet.getString("role")
            );
        }
    } catch (SQLException e) {
        System.err.println("Error fetching user by email: " + e.getMessage());
    }
        return null;
}

}
