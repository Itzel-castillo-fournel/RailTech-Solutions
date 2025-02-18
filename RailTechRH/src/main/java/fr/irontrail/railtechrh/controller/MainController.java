package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.io.InputStream;

public class MainController {
    @FXML private VBox menuContainer;
    @FXML private StackPane contentContainer;

    public void setRole(String role) {
        menuContainer.getChildren().clear(); // Vide le menu

        // Ajouter le bouton pour afficher le profil de l'utilisateur
        addMenuButton("Mon Profil", "/fr/irontrail/railtechrh/UserProfilView.fxml", "profile-icon.png");

        switch (role) {
            case "ADMIN":
                addMenuButton("Utilisateurs", "/fr/irontrail/railtechrh/admin/GestionUtilisateurs.fxml", "people-icon.png");
                addMenuButton("Notifications", "/fr/irontrail/railtechrh/admin/Notifications.fxml", "notification-icon.png");
                break;
            case "TECHNICIEN":
                addMenuButton("Maintenance", "/fr/irontrail/railtechrh/technicien/MaintenanceList.fxml", "tools-icon.png");
                addMenuButton("Notifications", "/fr/irontrail/railtechrh/technicien/Notifications.fxml", "profile-icon.png");
                break;
            case "CONDUCTEUR":
                addMenuButton("Mon Planning", "/fr/irontrail/railtechrh/conducteur/Planning.fxml", "planning-icon.png");
                addMenuButton("Signaler un incident", "/fr/irontrail/railtechrh/conducteur/IncidentForm.fxml", "alert-icon.png");
                addMenuButton("Notifications", "/fr/irontrail/railtechrh/conducteur/Notifications.fxml", "notification-icon.png");
                break;
            case "OPERATEUR":
                addMenuButton("Planning Conducteurs", "/fr/irontrail/railtechrh/operateur/Planning.fxml", "planning-icon.png");
                addMenuButton("Trajets", "/fr/irontrail/railtechrh/operateur/TrajetList.fxml", "train-icon.png");
                addMenuButton("Maintenance", "/fr/irontrail/railtechrh/operateur/MaintenanceList.fxml", "tools-icon.png");
                addMenuButton("Notifications", "/fr/irontrail/railtechrh/operateur/Notifications.fxml", "notification-icon.png");
                break;
        }
        // Ajouter le bouton "Se déconnecter" pour tous les rôles
        addLogoutButton();
    }

    private void addMenuButton(String text, String fxmlFile, String iconPath) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");

        // Charger l'icône
        InputStream iconStream = getClass().getResourceAsStream("/fr/irontrail/railtechrh/icons/" + iconPath);
        if (iconStream != null) {
            ImageView icon = new ImageView(new Image(iconStream));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            button.setGraphic(icon);
        } else {
            System.err.println("Icône non trouvée : " + iconPath);
        }


        // Appeler loadUserProfile pour le bouton "Mon Profil"
        if (text.equals("Mon Profil")) {
            button.setOnAction(e -> loadUserProfile());
        } else {
            button.setOnAction(e -> loadContent(fxmlFile));
        }
        menuContainer.getChildren().add(button);
    }

    private void addLogoutButton() {
        Button logoutButton = new Button("Se déconnecter");
        logoutButton.setOnAction(e -> logout());
        menuContainer.getChildren().add(logoutButton);
    }
    private void logout() {
        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/LoginView.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer par la scène de connexion
            Stage stage = (Stage) menuContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            contentContainer.getChildren().setAll(content); // Affiche le contenu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UtilisateurModel currentUser; // Utilisateur actuellement connecté

    // Définit l'utilisateur actuellement connecté
    public void setCurrentUser(UtilisateurModel user) {
        this.currentUser = user;
        System.out.println("Utilisateur défini : " + user.getPrenom() + " " + user.getNom()); // Debug
    }

    // Charge la vue du profil utilisateur
    private void loadUserProfile() {
        try {
            // Charge la vue FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/UserProfilView.fxml"));
            Parent content = loader.load();

            // Récupère le contrôleur de la vue
            UserProfilController controller = loader.getController();

            // Passe l'utilisateur au contrôleur
            controller.setUser(currentUser);

            // Affiche la vue dans le conteneur
            contentContainer.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}