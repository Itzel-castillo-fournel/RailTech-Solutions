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
    @FXML private Label trajetsLabel;
    @FXML private Label specialiteTitle;
    @FXML private Label specialiteLabel;
    @FXML private AnchorPane trajetsAnchorPane;
    @FXML private Label heuresLabel;
    @FXML private AnchorPane heuresAnchorPane;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void setUser(UtilisateurModel utilisateur) {
        prenomNomLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        emailLabel.setText(utilisateur.getEmail());
        roleLabel.setText(utilisateur.getRole().toString());

        if (utilisateur.getRole() == Role.CONDUCTEUR) {
            int nombreTrajets = utilisateurDAO.getNombreTrajetsCeMois(utilisateur.getId());
            trajetsLabel.setText(String.valueOf(nombreTrajets));
            trajetsLabel.setVisible(true);
            trajetsAnchorPane.setVisible(true);
            int nombreHeures = utilisateurDAO.getNombreHeuresTravailleesCeMois(utilisateur.getId());
            heuresLabel.setText(String.valueOf(nombreHeures));
            heuresAnchorPane.setVisible(true);
        } else {
            trajetsAnchorPane.setVisible(false);
        }

        if (utilisateur.getRole() == Role.TECHNICIEN) {
            String specialite = utilisateurDAO.getSpecialiteTechnicien(utilisateur.getId());
            specialiteLabel.setText(specialite);
        } else {
            specialiteLabel.setVisible(false);
            specialiteTitle.setVisible(false);
        }
    }
}