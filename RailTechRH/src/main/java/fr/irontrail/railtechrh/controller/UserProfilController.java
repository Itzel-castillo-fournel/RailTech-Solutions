package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.scene.layout.AnchorPane;

public class UserProfilController {
    @FXML private Label prenomNomLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private Label trajetsLabel; // Nouveau label pour afficher le nombre de trajets
    @FXML private AnchorPane trajetsAnchorPane;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void setUser(UtilisateurModel utilisateur) {
        prenomNomLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        emailLabel.setText(utilisateur.getEmail());
        roleLabel.setText(utilisateur.getRole().toString());

        // Afficher le nombre de trajets si l'utilisateur est un administrateur
        if (utilisateur.getRole() == Role.CONDUCTEUR) {
            int nombreTrajets = utilisateurDAO.getNombreTrajetsCeMois(utilisateur.getId());
            trajetsLabel.setText(String.valueOf(nombreTrajets));
            trajetsAnchorPane.setVisible(true); // Rendre l'AnchorPane visible
        } else {
            trajetsAnchorPane.setVisible(false); // Cacher l'AnchorPane pour les autres rôles
        }
    }
}