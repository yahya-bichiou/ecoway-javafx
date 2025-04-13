package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {
    private final String URL = "jdbc:mysql://localhost:3306/java";
    private final String username = "root";
    private final String password = "";

    static MaConnexion instance;

    Connection connection;

    public MaConnexion() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Connexion établie avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static MaConnexion getInstance() {
        if (instance == null) {
            instance = new MaConnexion();
        }
        return instance;
    }

    public Connection getConn() {
        return connection;
    }
}

