package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private final String URL="jdbc:mysql://localhost:3306/projet_dev";
    private final String USERNAME="root";
    private final String PASSWORD="";
    Connection con;

    static Connexion instance;

    public static Connexion getInstance(){
        if(instance==null){
            instance=new Connexion();
        }
        return instance;
    }

    public Connection getCon() {
        return con;
    }

    private Connexion() {
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion OK");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
