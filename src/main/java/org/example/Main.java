package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Commandes;
import models.Livraisons;
import services.CommandeService;
import services.LivraisonService;
import utils.Connexion;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Connection con;
        con = Connexion.getInstance().getCon();

        LivraisonService cs = new LivraisonService();

    }
}

