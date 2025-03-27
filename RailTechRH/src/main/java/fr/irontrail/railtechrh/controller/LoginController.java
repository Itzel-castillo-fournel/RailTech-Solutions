package fr.irontrail.railtechrh.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.dao.UtilisateurDAO;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private Label errorLabel;

    @FXML
    private UtilisateurDAO utilisateurDAO;

    @FXML
    public void handleConnexion() {
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();
        utilisateurDAO = new UtilisateurDAO();
        UtilisateurModel utilisateur = utilisateurDAO.findByEmail(email);

        if (utilisateur != null && utilisateurDAO.verifyPassword(utilisateur.getId(), motDePasse)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/MainView.fxml"));
                Parent root = loader.load();
                MainController controller = loader.getController();
                controller.setCurrentUser(utilisateur);
                controller.setRole(String.valueOf(utilisateur.getRole()));

                Scene scene = new Scene(root, 1024, 768); // Même taille que dans App
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}
