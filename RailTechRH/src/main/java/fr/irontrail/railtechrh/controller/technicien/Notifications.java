package fr.irontrail.railtechrh.controller.technicien;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.dao.TechnicienDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class Notifications implements Initializable {

    @FXML
    private VBox incidentContainer;

    @FXML
    private ChoiceBox<String> graviteFilter;

    private final TechnicienDAO technicienDAO = new TechnicienDAO();

    // Permet de stocker les Id des incidents masqués sans permettre les doublons
    private final Set<Integer> masqueIncidentIds = new HashSet<>();

    // ID du technicien connecté
    private int technicienId;

    // Un indiquateur pour savoir si les incidents ont déjà été chargés
    private boolean incidentsLoaded = false;

    private MainController mainController;

    // Permet de définir le contrôleur principal pour gérer les transitions entre les vues.
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Constructeur sans arguments requis par JavaFX
    public Notifications() {
    }

    // Méthode pour définir l'ID du technicien
    public void setTechnicienId(int technicienId) {
        this.technicienId = technicienId;

        // Si les données ont déjà été initialisées mais que l'ID change, rechargez les incidents
        if (incidentContainer != null && !incidentsLoaded) {
            loadIncidents();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ajouter "Tous" comme première option
        graviteFilter.getItems().add("Tous");

        // Ajouter les valeurs de l'énumération Gravite
        for (Gravite gravite : Gravite.values()) {
            graviteFilter.getItems().add(gravite.toString());
        }

        // Sélectionner "Tous" par défaut
        graviteFilter.setValue("Tous");

        // Ajouter un écouteur pour détecter les changements de sélection
        graviteFilter.setOnAction(event -> filterIncidentsByGravite());
    }

    /**
     * Chargement des Incidents : Lorsque l'ID du technicien est défini, les incidents sont chargés depuis la base de données
     * et affichés dans l'interface utilisateur.
     */
    private void loadIncidents() {
        if (technicienId <= 0) {
            System.err.println("Impossible de charger les incidents: technicienId non défini");
            return;
        }

        // Efface les incidents précédemment affichés
        incidentContainer.getChildren().clear();

        // Charge les notifications masquées depuis les préférences
        loadMasqueIncidentIds();

        // Charge les données des incidents
        List<IncidentModel> incidents = technicienDAO.getIncidents();

        for (IncidentModel incident : incidents) {
            // Vérifie si l'incident a déjà été masqué
            if (masqueIncidentIds.contains(incident.getId())) {
                continue; // Ne pas afficher cet incident
            }

            // Crée un Pane pour chaque incident
            Pane incidentPane = createIncidentPane(incident);

            // Ajoute le Pane au VBox
            incidentContainer.getChildren().add(incidentPane);
        }

        incidentsLoaded = true;
    }

    /**
     * Filtrage : L'utilisateur peut filtrer les incidents par gravité en utilisant le filtre déroulant.
     */
    private void filterIncidentsByGravite() {
        String selectedGravite = graviteFilter.getValue();

        // Efface les incidents précédemment affichés
        incidentContainer.getChildren().clear();

        // Charge les données des incidents
        List<IncidentModel> incidents = technicienDAO.getIncidents();

        for (IncidentModel incident : incidents) {
            // Vérifie si l'incident a déjà été masqué
            if (masqueIncidentIds.contains(incident.getId())) {
                continue; // Ne pas afficher cet incident
            }

            // Vérifie si l'incident correspond à la gravité sélectionnée
            if (!selectedGravite.equals("Tous") && !incident.getGravite().toString().equals(selectedGravite)) {
                continue; // Ne pas afficher cet incident
            }

            // Crée un Pane pour chaque incident
            Pane incidentPane = createIncidentPane(incident);

            // Ajoute le Pane au VBox
            incidentContainer.getChildren().add(incidentPane);
        }
    }

    private Pane createIncidentPane(IncidentModel incident) {
        Pane incidentPane = new Pane();

        // Définir la couleur en fonction de la gravité
        String borderColor;
        switch (incident.getGravite()) {
            case MAJEUR:
                borderColor = "#FF9A61";
                break;
            case CRITIQUE:
                borderColor = "#FF0F3C";
                break;
            case MODERE:
                borderColor = "#170FFF";
                break;
            default:
                borderColor = "black"; // Couleur par défaut si la gravité n'est pas reconnue
        }

        incidentPane.setStyle("-fx-border-color: " + borderColor + "; -fx-border-radius: 20px;");
        incidentPane.setPrefHeight(127.0);
        incidentPane.setPrefWidth(686.0);

        // Crée l'icône SVG de l'outil
        SVGPath toolIcon = new SVGPath();
        toolIcon.setContent("M15.936 2.50098L21.501 8.06595V15.936L15.936 21.501H8.06595L2.50098 15.936V8.06595L8.06595 2.50098H15.936ZM15.1076 4.50098H8.89437L4.50098 8.89437V15.1076L8.89437 19.501H15.1076L19.501 15.1076V8.89437L15.1076 4.50098ZM11.0002 15.0002H13.0002V17.0002H11.0002V15.0002ZM11.0002 7.00024H13.0002V13.0002H11.0002V7.00024Z");
        toolIcon.setLayoutX(13.0); // Position plus à gauche
        toolIcon.setLayoutY(15.0); // Position verticale centrée
        toolIcon.setScaleX(0.8); // Réduit la taille de l'icône
        toolIcon.setScaleY(0.8); // Réduit la taille de l'icône
        toolIcon.setStyle("-fx-fill: " + borderColor + ";"); // Couleur de l'icône correspondant à la gravité

        // Crée les labels pour chaque incident - ajuster les positions pour laisser de la place à l'icône
        Label descriptionLabel = new Label(incident.getDescription());
        descriptionLabel.setLayoutX(45.0); // Déplacer à droite pour laisser de la place à l'icône
        descriptionLabel.setLayoutY(14.0);
        descriptionLabel.setStyle("-fx-font-weight: bold");

        Label graviteLabel = new Label("Gravité: " + incident.getGravite().toString());
        graviteLabel.setLayoutX(45.0);
        graviteLabel.setLayoutY(62.0);

        Label trainLabel = new Label(incident.getTypeIncident() + " - " + incident.getTrainImmat().getImmatriculation());
        trainLabel.setLayoutX(45.0);
        trainLabel.setLayoutY(36.0);

        // Crée l'icône de croix
        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent("M11.9997 10.5865L16.9495 5.63672L18.3637 7.05093L13.4139 12.0007L18.3637 16.9504L16.9495 18.3646L11.9997 13.4149L7.04996 18.3646L5.63574 16.9504L10.5855 12.0007L5.63574 7.05093L7.04996 5.63672L11.9997 10.5865Z"); // Exemple de croix simple
        closeIcon.setStrokeWidth(2);
        closeIcon.setLayoutX(650.0);
        closeIcon.setLayoutY(10.0);
        closeIcon.setStyle("-fx-cursor: hand;");

        // Gestion de l'événement de clic sur l'icône de croix
        closeIcon.setOnMouseClicked(event -> {
            // Ajoute l'ID de l'incident à la liste des incidents masqués
            masqueIncidentIds.add(incident.getId());

            // Enregistre les notifications masquées dans les préférences
            saveMasqueIncidentIds();

            // Supprime l'incident de l'interface utilisateur
            incidentContainer.getChildren().remove(incidentPane);
        });

        // Crée le bouton "Ajouter"
        Button ajouterButton = new Button("Ajouter une maintenance");
        // Créez le SVGPath avec le chemin SVG fourni
        SVGPath svgIcon = new SVGPath();
        svgIcon.setContent("M5.32943 3.27158C6.56252 2.8332 7.9923 3.10749 8.97927 4.09446C10.1002 5.21537 10.3019 6.90741 9.5843 8.23385L20.293 18.9437L18.8788 20.3579L8.16982 9.64875C6.84325 10.3669 5.15069 10.1654 4.02952 9.04421C3.04227 8.05696 2.7681 6.62665 3.20701 5.39332L5.44373 7.63C6.02952 8.21578 6.97927 8.21578 7.56505 7.63C8.15084 7.04421 8.15084 6.09446 7.56505 5.50868L5.32943 3.27158ZM15.6968 5.15512L18.8788 3.38736L20.293 4.80157L18.5252 7.98355L16.7574 8.3371L14.6361 10.4584L13.2219 9.04421L15.3432 6.92289L15.6968 5.15512ZM8.97927 13.2868L10.3935 14.7011L5.09018 20.0044C4.69966 20.3949 4.06649 20.3949 3.67597 20.0044C3.31334 19.6417 3.28744 19.0699 3.59826 18.6774L3.67597 18.5902L8.97927 13.2868Z");

        // Définissez la taille et la couleur de l'icône SVG
        svgIcon.setScaleX(1.5); // Ajustez la taille selon vos besoins
        svgIcon.setScaleY(1.5); // Ajustez la taille selon vos besoins
        svgIcon.setScaleX(0.8); // Réduit la taille de l'icône
        svgIcon.setScaleY(0.8); // Réduit la taille de l'icône
        svgIcon.setFill(Color.WHITE); // Définissez la couleur de remplissage

        // Ajoutez l'icône SVG au bouton
        ajouterButton.setGraphic(svgIcon);
        ajouterButton.setLayoutX(45.0);
        ajouterButton.setLayoutY(90.0);
        ajouterButton.setStyle("-fx-background-color: #1C287C; -fx-text-fill: white; -fx-font-weight: bold;-fx-cursor: hand;");

        ajouterButton.setOnMouseEntered(event -> {
            ajouterButton.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-text-fill: #1C287C; " +
                            "-fx-font-weight: bold; " +
                            "-fx-cursor: hand;"
            );
            svgIcon.setFill(Color.BLUE);
        });

        ajouterButton.setOnMouseExited(event -> {
            ajouterButton.setStyle(
                    "-fx-background-color: #1C287C; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-cursor: hand;"
            );
            svgIcon.setFill(Color.WHITE);
        });

        // Gestion de l'événement de clic sur le bouton "Modifier"
        ajouterButton.setOnAction(event -> {
            try {
                // Charger la vue AjouterMaintenance.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/technicien/AjouterMaintenance.fxml"));
                AnchorPane maintenancePane = loader.load();

                // Obtenir le contrôleur de la vue
                AjouterMaintenance controller = loader.getController();

                // Passer les informations nécessaires au contrôleur
                controller.setImmatriculation(incident.getTrainImmat().getImmatriculation());
                controller.setTypeProbleme(incident.getTypeIncident().toString());
                controller.setIncidentId(incident.getId());

                // In the createIncidentPane method of Notifications class
                controller.setMainController(this.mainController);

                // Utiliser le contentContainer du MainController pour charger la nouvelle vue
                if (mainController != null) {
                    mainController.getContentContainer().getChildren().setAll(maintenancePane);
                } else {
                    // Fallback si mainController n'est pas disponible
                    System.err.println("MainController non disponible. Impossible de charger la vue.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Ajoute les labels, les icônes et le bouton au Pane
        incidentPane.getChildren().addAll(toolIcon, descriptionLabel, graviteLabel, trainLabel, closeIcon, ajouterButton);

        return incidentPane;
    }

    /**
     * Charge les IDs des incidents masqués depuis les préférences utilisateur.
     * Exemple : Lorsque loadMasqueIncidentIds() est appelée, elle récupère la chaîne "101,102,103" depuis les préférences utilisateur en utilisant la même clé.
     * Elle divise cette chaîne en un tableau d'IDs et les ajoute à la collection masqueIncidentIds. [101, 102, 103, 104]
     */
    private void loadMasqueIncidentIds() {
        masqueIncidentIds.clear(); // Vider la collection avant de charger

        Preferences prefs = Preferences.userNodeForPackage(Notifications.class);
        String masqueIdsString = prefs.get(getPrefsKeyForTechnicien(technicienId), "");

        if (!masqueIdsString.isEmpty()) {
            String[] ids = masqueIdsString.split(",");
            for (String id : ids) {
                try {
                    masqueIncidentIds.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Format invalide d'ID d'incident masqué: " + id);
                }
            }
        }
    }

    /**
     * Enregistre les IDs des incidents masqués dans les préférences utilisateur.
     * Exemple : Supposons que les IDs des incidents masqués pour ce technicien sont [101, 102, 103].
     * Lorsque saveMasqueIncidentIds() est appelée, elle convertit cette liste en une chaîne : "101,102,103".
     * Elle enregistre cette chaîne dans les préférences utilisateur sous la clé "masqueIncidentIds_technicien_456".
     */
    private void saveMasqueIncidentIds() {
        Preferences prefs = Preferences.userNodeForPackage(Notifications.class);
        String masqueIdsString = masqueIncidentIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        prefs.put(getPrefsKeyForTechnicien(technicienId), masqueIdsString);
    }

    /**
     * Génère une clé de préférence spécifique au technicien.
     * Exemple : Supposons que nous avons un technicien avec l'ID 456.
     * Lorsque getPrefsKeyForTechnicien(456) est appelée, elle retourne la clé : "masqueIncidentIds_technicien_456".
     */
    private String getPrefsKeyForTechnicien(int technicienId) {
        return "masqueIncidentIds_technicien_" + technicienId;
    }
}
