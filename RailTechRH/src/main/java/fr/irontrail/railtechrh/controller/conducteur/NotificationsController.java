package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.NotificationDAO;
import fr.irontrail.railtechrh.model.NotificationModel;
import fr.irontrail.railtechrh.model.enums.Role;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

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

        notificationsContainer.setSpacing(10);
        notificationsContainer.setPadding(new Insets(15));
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
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-padding: 20px;");
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
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1); -fx-padding: 10px; -fx-spacing: 5px;");

        Label titreLabel = new Label(notification.getTitre());
        titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        if (notification.getDate() != null) {
            Label dateLabel = new Label("Date: " + notification.getDate().format(formatter));
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

            String titreTexte = notification.getTitre().toLowerCase();
            if (titreTexte.contains("assigné")) {
                titreLabel.setTextFill(Paint.valueOf("#28a745"));
            } else if (titreTexte.contains("modifié")) {
                titreLabel.setTextFill(Paint.valueOf("#f39c12"));
            } else if (titreTexte.contains("annulé") || titreTexte.contains("retiré")) {
                titreLabel.setTextFill(Paint.valueOf("#e74c3c"));
            } else {
                titreLabel.setTextFill(Paint.valueOf("#2980b9"));
            }

            card.getChildren().addAll(titreLabel, dateLabel);
        } else {
            card.getChildren().add(titreLabel);
        }

        return card;
    }

    @FXML
    public void rafraichirNotifications() {
        chargerNotifications();
    }
}