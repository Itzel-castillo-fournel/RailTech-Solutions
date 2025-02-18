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
    @FXML private Label heuresLabel; // Nouveau label pour afficher le nombre d'heures
    @FXML private AnchorPane heuresAnchorPane;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void setUser(UtilisateurModel utilisateur) {
        prenomNomLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        emailLabel.setText(utilisateur.getEmail());
        roleLabel.setText(utilisateur.getRole().toString());

        // Afficher le nombre de trajets si l'utilisateur est un administrateur
        if (utilisateur.getRole() == Role.CONDUCTEUR) {
            int nombreTrajets = utilisateurDAO.getNombreTrajetsCeMois(utilisateur.getId());
            trajetsLabel.setText(String.valueOf(nombreTrajets));
            trajetsLabel.setVisible(true);
            trajetsAnchorPane.setVisible(true);
            // Afficher le nombre d'heures travaillées pour tous les utilisateurs
            int nombreHeures = utilisateurDAO.getNombreHeuresTravailleesCeMois(utilisateur.getId());
            heuresLabel.setText(String.valueOf(nombreHeures));
            heuresAnchorPane.setVisible(true);
        } else {
            trajetsAnchorPane.setVisible(false); // Cacher l'AnchorPane pour les autres rôles
        }
    }
}