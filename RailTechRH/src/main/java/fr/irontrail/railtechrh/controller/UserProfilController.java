package fr.irontrail.railtechrh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import fr.irontrail.railtechrh.model.UtilisateurModel;

public class UserProfilController {
    @FXML private Label prenomNomLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;

    public void setUser(UtilisateurModel utilisateur) {
        prenomNomLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        emailLabel.setText(utilisateur.getEmail());
        roleLabel.setText(utilisateur.getRole().toString());
    }
}