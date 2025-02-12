package fr.irontrail.railtechrh.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {
    @FXML private VBox menuContainer;
    @FXML private StackPane contentContainer;

    public void setRole(String role) {
        menuContainer.getChildren().clear(); // Vide le menu

        switch (role) {
            case "ADMIN":
                addMenuButton("Mon Profil", "/views/admin/AdminDashboard.fxml");
                addMenuButton("Utilisateurs", "/views/admin/GestionUtilisateurs.fxml");
                addMenuButton("Notifications", "/views/admin/Notifications.fxml");
                break;
            case "TECHNICIEN":
                addMenuButton("Mon Profil", "/views/technicien/TechnicienDashboard.fxml");
                addMenuButton("Maintenance", "/views/technicien/MaintenanceList.fxml");
                addMenuButton("Notifications", "/views/technicien/Notifications.fxml");
                break;
            case "CONDUCTEUR":
                addMenuButton("Mon Profil", "/views/conducteur/ConducteurDashboard.fxml");
                addMenuButton("Mon Planning", "/views/conducteur/Planning.fxml");
                addMenuButton("Signaler un incident", "/views/conducteur/IncidentForm.fxml");
                addMenuButton("Notifications", "/views/conducteur/Notifications.fxml");
                break;
//            case "OPERATEUR":
//                addMenuButton("Mon Profile", "/views/operateur/OperateurDashboard.fxml");
//                addMenuButton("Planning Conducteurs", "/views/operateur/Planning.fxml");
//                addMenuButton("Trajets", "/views/operateur/TrajetList.fxml");
//                addMenuButton("Maintenance", "/views/operateur/MaintenanceList.fxml");
//                addMenuButton("Notifications", "/views/operateur/Notifications.fxml");
//                break;
        }
    }

    private void addMenuButton(String text, String fxmlFile) {
        Button button = new Button(text);
        button.setOnAction(e -> loadContent(fxmlFile));
        menuContainer.getChildren().add(button);
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
}