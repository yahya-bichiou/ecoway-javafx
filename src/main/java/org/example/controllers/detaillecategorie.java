package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class detaillecategorie {

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField descriptionTextField;

    public TextField getnomTextField() {
        return nomTextField;
    }

    public void setNomTextField(String nom) {
        this.nomTextField.setText(nom);
    }

    public void setDescriptionTextField(String description) {
        this.descriptionTextField.setText(description);
    }

    public TextField getNomTextField() {
        return nomTextField;
    }

    public TextField getDescriptionTextField() {
        return descriptionTextField;
    }
}
