package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.NotificationDAO;
import fr.irontrail.railtechrh.model.NotificationModel;
import fr.irontrail.railtechrh.model.enums.Role;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationsController {

    @FXML private VBox notificationsContainer;

    private NotificationDAO notificationDAO;
    private int utilisateurId;
    private Role roleUtilisateur;
    private MainController mainController;

    public void initialize() {
        notificationDAO = new NotificationDAO();
        notificationsContainer.setSpacing(15);
        notificationsContainer.setPadding(new Insets(20));
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
        this.roleUtilisateur = notificationDAO.getRoleUtilisateur(utilisateurId);
        chargerNotifications();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void chargerNotifications() {
        notificationsContainer.getChildren().clear();

        List<NotificationModel> notifications;

        if (roleUtilisateur == Role.TECHNICIEN) {
            notifications = notificationDAO.getNotificationsTechniciens();
        } else {
            notifications = notificationDAO.getNotificationsUtilisateur(utilisateurId);
        }

        if (notifications.isEmpty()) {
            Label emptyLabel = new Label("Aucune notification");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d; -fx-padding: 20px;");
            notificationsContainer.getChildren().add(emptyLabel);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (NotificationModel notification : notifications) {
            VBox notificationCard = creerNotificationCard(notification, formatter);
            notificationsContainer.getChildren().add(notificationCard);
        }
    }

    private VBox creerNotificationCard(NotificationModel notification, DateTimeFormatter formatter) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setPadding(new Insets(14));
        card.setMaxWidth(600); // Largeur augmentée

        String titre = notification.getTitre().toLowerCase();
        String borderColor = "#2980b9"; // bleu par défaut
        String titreColor = "#1c287c";

        if (titre.contains("assigné")) {
            borderColor = "#28a745"; // vert
            titreColor = "#28a745";
        } else if (titre.contains("modifié")) {
            borderColor = "#f39c12"; // orange
            titreColor = "#f39c12";
        } else if (titre.contains("annulé") || titre.contains("retiré")) {
            borderColor = "#e74c3c"; // rouge
            titreColor = "#e74c3c";
        }

        card.setStyle("-fx-background-color: #f4f7fa; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-border-width: 1.5px; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");

        // En-tête avec titre et bouton supprimer
        HBox header = new HBox();
        header.setAlignment(Pos.TOP_RIGHT);
        header.setSpacing(10);

        Label titreLabel = new Label(notification.getTitre());
        titreLabel.setFont(Font.font("System", FontWeight.BOLD, 16)); // taille augmentée
        titreLabel.setTextFill(Color.web(titreColor));
        titreLabel.setWrapText(true);
        HBox.setHgrow(titreLabel, Priority.ALWAYS);

        Button supprimerBtn = new Button("✕");
        supprimerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px;");
        supprimerBtn.setOnAction(e -> {
            boolean succes = notificationDAO.masquerNotification(notification.getId());
            if (succes) {
                notificationsContainer.getChildren().remove(card);
            } else {
                System.err.println("Échec de la suppression de la notification id=" + notification.getId());
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titreLabel, spacer, supprimerBtn);
        card.getChildren().add(header);

        // Date
        if (notification.getDate() != null) {
            Label dateLabel = new Label("Date : " + notification.getDate().format(formatter));
            dateLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
            card.getChildren().add(dateLabel);
        }

        return card;
    }

    @FXML
    public void rafraichirNotifications() {
        chargerNotifications();
    }
}
