package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.NotificationDAO;
import fr.irontrail.railtechrh.dao.TechnicienDAO;
import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.NotificationModel;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.Role;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsController {

    @FXML private VBox notificationsContainer;

    private NotificationDAO notificationDAO;
    private TechnicienDAO technicienDAO;
    private int utilisateurId;
    private Role roleUtilisateur;
    private MainController mainController;

    // Map pour stocker les informations des incidents
    private Map<Integer, IncidentModel> incidentsCache;

    public void initialize() {
        notificationDAO = new NotificationDAO();
        technicienDAO = new TechnicienDAO();
        incidentsCache = new HashMap<>();

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

        // Précharger tous les incidents pour optimiser les performances
        List<IncidentModel> incidents = TechnicienDAO.getIncidents();
        for (IncidentModel incident : incidents) {
            incidentsCache.put(incident.getId(), incident);
        }

        List<NotificationModel> notifications;

        if (roleUtilisateur == Role.OPERATEUR) {
            notifications = notificationDAO.getNotificationsOperateurs();
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
            Pane notificationPane = creerNotificationPane(notification, formatter);
            notificationsContainer.getChildren().add(notificationPane);
        }
    }

    private Pane creerNotificationPane(NotificationModel notification, DateTimeFormatter formatter) {
        Pane notificationPane = new Pane();

        // Récupérer la gravité et les informations de l'incident si disponible
        Gravite gravite = Gravite.MODERE; // Valeur par défaut
        String trainImmat = "";
        String typeIncident = "";

        if (notification.getIncidentId() > 0) {
            // Récupérer l'incident depuis le cache ou la base de données
            IncidentModel incident = getIncidentFromId(notification.getIncidentId());
            if (incident != null) {
                gravite = incident.getGravite();
                trainImmat = incident.getTrainImmat().getImmatriculation();
                typeIncident = incident.getTypeIncident().toString();
            }
        } else {
            // Déterminer la gravité à partir du titre si pas d'incident associé
            gravite = determinerGravite(notification);
        }

        // Déterminer la couleur de bordure en fonction de la gravité
        String borderColor = determinerCouleurBordure(gravite);

        // Style similaire aux notifications technicien
        notificationPane.setStyle("-fx-border-color: " + borderColor + "; -fx-border-radius: 20px;");
        notificationPane.setPrefHeight(127.0);
        notificationPane.setPrefWidth(686.0);

        // Créer l'icône SVG d'alerte
        SVGPath alertIcon = new SVGPath();
        alertIcon.setContent("M15.936 2.50098L21.501 8.06595V15.936L15.936 21.501H8.06595L2.50098 15.936V8.06595L8.06595 2.50098H15.936ZM15.1076 4.50098H8.89437L4.50098 8.89437V15.1076L8.89437 19.501H15.1076L19.501 15.1076V8.89437L15.1076 4.50098ZM11.0002 15.0002H13.0002V17.0002H11.0002V15.0002ZM11.0002 7.00024H13.0002V13.0002H11.0002V7.00024Z");
        alertIcon.setLayoutX(13.0);
        alertIcon.setLayoutY(15.0);
        alertIcon.setScaleX(0.8);
        alertIcon.setScaleY(0.8);
        alertIcon.setStyle("-fx-fill: " + borderColor + ";");

        // Créer les labels pour chaque notification
        Label titreLabel = new Label(notification.getTitre());
        titreLabel.setLayoutX(45.0);
        titreLabel.setLayoutY(14.0);
        titreLabel.setStyle("-fx-font-weight: bold");

        // Ajouter le bouton supprimer (repris du premier fichier)
        Button supprimerBtn = new Button("✕");
        supprimerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill:  #4051b5; -fx-font-size: 14px;");
        supprimerBtn.setLayoutX(650.0);
        supprimerBtn.setLayoutY(10.0);
        supprimerBtn.setOnAction(e -> {
            boolean succes = notificationDAO.masquerNotification(notification.getId());
            if (succes) {
                notificationsContainer.getChildren().remove(notificationPane);
            } else {
                System.err.println("Échec de la suppression de la notification id=" + notification.getId());
            }
        });

        // Si c'est un incident, afficher les détails du train et du type d'incident
        Label infoLabel;
        if (!trainImmat.isEmpty() && !typeIncident.isEmpty()) {
            String infoText = formatTypeIncident(typeIncident) + " - " + trainImmat;
            infoLabel = new Label(infoText);
        } else {
            infoLabel = new Label("Notification générale");
        }
        infoLabel.setLayoutX(45.0);
        infoLabel.setLayoutY(36.0);

        // Ajouter le label de gravité pour les incidents
        Label graviteLabel = new Label("Gravité: " + gravite.toString());
        graviteLabel.setLayoutX(45.0);
        graviteLabel.setLayoutY(62.0);

        // Date de la notification
        Label dateLabel = new Label(notification.getDate() != null ? notification.getDate().format(formatter) : "Date non disponible");
        dateLabel.setLayoutX(45.0);
        dateLabel.setLayoutY(88.0);
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        // Ajouter tous les éléments au Pane
        notificationPane.getChildren().addAll(alertIcon, titreLabel, infoLabel, graviteLabel, dateLabel, supprimerBtn);

        return notificationPane;
    }

    /**
     * Récupère les informations d'un incident à partir de son ID
     */
    private IncidentModel getIncidentFromId(int incidentId) {
        // Vérifier si l'incident est déjà dans le cache
        if (incidentsCache.containsKey(incidentId)) {
            return incidentsCache.get(incidentId);
        }

        // Si non trouvé, parcourir tous les incidents pour rechercher celui avec cet ID
        List<IncidentModel> incidents = TechnicienDAO.getIncidents();
        for (IncidentModel incident : incidents) {
            if (incident.getId() == incidentId) {
                incidentsCache.put(incidentId, incident);
                return incident;
            }
        }

        return null;
    }

    /**
     * Formate le type d'incident pour affichage
     */
    private String formatTypeIncident(String typeIncident) {
        return typeIncident.replace("_", " ").replace("PANNE TECHNIQUE", "Panne technique")
                .replace("VOIE ENDOMMAGEE", "Voie endommagée");
    }

    /**
     * Détermine la gravité de la notification basée sur son contenu
     */
    private Gravite determinerGravite(NotificationModel notification) {
        String titreTexte = notification.getTitre().toLowerCase();

        if (titreTexte.contains("critique") || titreTexte.contains("urgent") ||
                titreTexte.contains("annulé") || titreTexte.contains("retiré") ||
                titreTexte.contains("erreur") || titreTexte.contains("danger")) {
            return Gravite.CRITIQUE;
        } else if (titreTexte.contains("modifié") || titreTexte.contains("important") ||
                titreTexte.contains("attention") || titreTexte.contains("alerte")) {
            return Gravite.MAJEUR;
        } else if (titreTexte.contains("assigné") || titreTexte.contains("information") ||
                titreTexte.contains("succès") || titreTexte.contains("terminé")) {
            return Gravite.MODERE;
        } else {
            return Gravite.MODERE; // Par défaut
        }
    }

    /**
     * Détermine la couleur de bordure en fonction de la gravité
     */
    private String determinerCouleurBordure(Gravite gravite) {
        switch (gravite) {
            case MAJEUR:
                return "#FF9A61";
            case CRITIQUE:
                return "#FF0F3C";
            case MODERE:
                return "#170FFF";
            case MINEUR:
                return "#00FF00";
            default:
                return "#170FFF"; // Couleur par défaut pour gravité modérée
        }
    }

    @FXML
    public void rafraichirNotifications() {
        chargerNotifications();
    }
}
