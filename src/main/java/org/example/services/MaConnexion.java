package org.example.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {
    private static MaConnexion instance;
    private Connection connection;

    // Détails de connexion (à déplacer dans un fichier de configuration si possible)
    private final String URL = "jdbc:mysql://localhost:3306/ecowaysinapropriate";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    // Constructeur privé pour garantir qu'il n'y a qu'une seule instance
    private MaConnexion() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion à la base de données établie avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    // Accès à l'instance unique
    public static synchronized MaConnexion getInstance() {
        if (instance == null) {
            instance = new MaConnexion();
        }
        return instance;
    }

    // Accès à la connexion
    public Connection getConn() {
        return connection;
    }

    // Méthode pour fermer la connexion, si nécessaire
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
