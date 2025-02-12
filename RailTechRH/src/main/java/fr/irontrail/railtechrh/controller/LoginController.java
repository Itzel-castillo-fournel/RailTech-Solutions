package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.controller.MainController;
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
    @FXML private TextField emailField;
    @FXML private PasswordField motDePasseField;
    @FXML private Label errorLabel;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void handleConnexion() {
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();

        UtilisateurModel utilisateur = utilisateurDAO.findByEmail(email);

        if (utilisateur != null && utilisateur.getMdp().equals(motDePasse)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/MainView.fxml"));
                Parent root = loader.load();
                MainController controller = loader.getController();
                controller.setRole(utilisateur.getRole()); // Passe le rôle au contrôleur principal

                Scene scene = new Scene(root);
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}