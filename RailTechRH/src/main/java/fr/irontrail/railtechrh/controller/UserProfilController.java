package fr.irontrail.railtechrh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import fr.irontrail.railtechrh.model.UtilisateurModel;

public class UserProfilController {
    @FXML private Label prenomLabel;
    @FXML private Label nomLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;

    public void setUser(UtilisateurModel utilisateur) {
        prenomLabel.setText("Prénom: " + utilisateur.getPrenom());
        nomLabel.setText("Nom: " + utilisateur.getNom());
        emailLabel.setText("Email: " + utilisateur.getEmail());
        roleLabel.setText("Rôle: " + utilisateur.getRole().toString());
    }
}