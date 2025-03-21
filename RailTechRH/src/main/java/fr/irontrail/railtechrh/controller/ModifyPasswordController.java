package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ModifyPasswordController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button annulerButton;

    @FXML
    private Button enregistrerButton;

    private UtilisateurModel utilisateur;
    private MainController mainController;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public void setUtilisateur(UtilisateurModel utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAnnulerButton() {
        // Retour à la page profil
        if (mainController != null) {
            mainController.loadUserProfile();
        }
    }

    @FXML
    private void handleEnregistrerButton() {
        // Récupérer les valeurs des champs
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Vérifier que les champs ne sont pas vides
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Tous les champs doivent être remplis.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Vérifier que le nouveau mot de passe et la confirmation correspondent
        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("Le nouveau mot de passe et sa confirmation ne correspondent pas.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Vérifier que l'ancien mot de passe est correct
        if (!utilisateurDAO.verifyPassword(utilisateur.getId(), currentPassword)) {
            statusLabel.setText("Mot de passe actuel incorrect.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Mettre à jour le mot de passe
        boolean success = utilisateurDAO.updatePassword(utilisateur.getId(), newPassword);

        if (success) {
            statusLabel.setText("Mot de passe mis à jour avec succès !");
            statusLabel.setStyle("-fx-text-fill: green;");

            // Retour à la page profil après un court délai
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                if (mainController != null) {
                                    mainController.loadUserProfile();
                                }
                            });
                        }
                    },
                    1500 // Délai de 1.5 secondes
            );
        } else {
            statusLabel.setText("Erreur lors de la mise à jour du mot de passe.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}