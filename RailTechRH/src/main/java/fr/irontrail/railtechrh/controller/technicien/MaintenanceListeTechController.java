package fr.irontrail.railtechrh.controller.technicien;
import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.TechnicienDAO;
import fr.irontrail.railtechrh.model.MaintenanceModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;

public class MaintenanceListeTechController {

    @FXML
    private VBox maintenanceListVBox;

    @FXML
    private Label pannePercentageLabel;

    @FXML
    private Label maintenancePercentageLabel;

    @FXML
    private Label operationnelPercentageLabel;

    private TechnicienDAO technicienDAO;
    private MainController mainController;

    public MaintenanceListeTechController() {
        this.technicienDAO = new TechnicienDAO();
    }
    public void setMainController(MainController mainController) { this.mainController = mainController;}

    @FXML
    public void initialize() {
        loadMaintenanceDetails();
        updatePercentageLabels();
    }

    private void loadMaintenanceDetails() {
        try {
            List<MaintenanceModel> maintenanceDetails = technicienDAO.getMaintenanceDetails();
            Platform.runLater(() -> {
                maintenanceListVBox.getChildren().clear();

                //Filtre pour ne pas afficher les train opérationnel
                List<MaintenanceModel> filteredMaintenanceDetails = maintenanceDetails.stream()
                        .filter(maintenance -> !"OPERATIONNEL".equals(maintenance.getEtatMaintenance()))
                        .toList();

                for (int i = 0; i < filteredMaintenanceDetails.size(); i++) {
                    MaintenanceModel maintenance = filteredMaintenanceDetails.get(i);
                    Pane maintenancePane = createMaintenancePane(maintenance,
                            i == filteredMaintenanceDetails.size() - 1); // Check if it's the last item
                    maintenanceListVBox.getChildren().add(maintenancePane);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Label errorLabel = new Label("Erreur de chargement des maintenances");
                maintenanceListVBox.getChildren().add(errorLabel);
            });
        }
    }

    private void updatePercentageLabels() {
        try {
            Map<String, Integer> percentages = technicienDAO.getMaintenancePercentages();
            Platform.runLater(() -> {
                pannePercentageLabel.setText(percentages.get("PANNE") + "%");
                maintenancePercentageLabel.setText(percentages.get("MAINTENANCE") + "%");
                operationnelPercentageLabel.setText(percentages.get("OPÉRATIONNEL") + "%");
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Pane createMaintenancePane(MaintenanceModel maintenance, boolean isLastItem) {
        Pane pane = new Pane();
        pane.setPrefHeight(39.0);
        pane.setPrefWidth(700.0); // Ajustez la largeur pour inclure les nouvelles colonnes

        // Set background and base style
        String baseStyle = "-fx-background-color: transparent;";

        // Add blue bottom border for all items except the last one
        if (!isLastItem) {
            pane.setStyle(baseStyle + "-fx-border-color: transparent transparent #7281D8 transparent; -fx-border-width: 0 0 1 0;");
        } else {
            pane.setStyle(baseStyle);
        }

        // État Label - replace with SVGPath if PANNE or MAINTENANCE
        SVGPath svgIcon = new SVGPath();
        svgIcon.setLayoutX(10.0);
        svgIcon.setLayoutY(7.0);
        svgIcon.setScaleX(0.8);
        svgIcon.setScaleY(0.8);

        switch (maintenance.getEtatMaintenance()) {
            case "PANNE":
                svgIcon.setContent("M12 22C6.47715 22 2 17.5228 2 12C2 6.47715 6.47715 2 12 2C17.5228 2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22ZM12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20ZM11 15H13V17H11V15ZM11 7H13V13H11V7Z");
                svgIcon.setFill(Color.RED);
                break;
            case "MAINTENANCE":
                svgIcon.setContent("M5.32943 3.27158C6.56252 2.8332 7.9923 3.10749 8.97927 4.09446C10.1002 5.21537 10.3019 6.90741 9.5843 8.23385L20.293 18.9437L18.8788 20.3579L8.16982 9.64875C6.84325 10.3669 5.15069 10.1654 4.02952 9.04421C3.04227 8.05696 2.7681 6.62665 3.20701 5.39332L5.44373 7.63C6.02952 8.21578 6.97927 8.21578 7.56505 7.63C8.15084 7.04421 8.15084 6.09446 7.56505 5.50868L5.32943 3.27158ZM15.6968 5.15512L18.8788 3.38736L20.293 4.80157L18.5252 7.98355L16.7574 8.3371L14.6361 10.4584L13.2219 9.04421L15.3432 6.92289L15.6968 5.15512ZM8.97927 13.2868L10.3935 14.7011L5.09018 20.0044C4.69966 20.3949 4.06649 20.3949 3.67597 20.0044C3.31334 19.6417 3.28744 19.0699 3.59826 18.6774L3.67597 18.5902L8.97927 13.2868Z");
                svgIcon.setFill(Color.ORANGE);
                break;
            case "OPERATIONNEL":
                svgIcon.setContent("M4 12C4 7.58172 7.58172 4 12 4C16.4183 4 20 7.58172 20 12C20 16.4183 16.4183 20 12 20C7.58172 20 4 16.4183 4 12ZM12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2ZM17.4571 9.45711L16.0429 8.04289L11 13.0858L8.20711 10.2929L6.79289 11.7071L11 15.9142L17.4571 9.45711Z");
                svgIcon.setFill(Color.GREEN);
                break;
            default:
                Label etatLabel = new Label(maintenance.getEtatMaintenance());
                etatLabel.setLayoutX(10.0);
                etatLabel.setLayoutY(11.0);
                etatLabel.setPrefWidth(100.0);
                pane.getChildren().add(etatLabel);
                return pane;
        }

        pane.getChildren().add(svgIcon);

        // Immatriculation Label
        Label immatriculationLabel = new Label(maintenance.getNumeroImmatriculationTrain());
        immatriculationLabel.setStyle("-fx-text-fill: #7281D8");
        immatriculationLabel.setLayoutX(100.0);
        immatriculationLabel.setLayoutY(11.0);
        immatriculationLabel.setPrefWidth(150.0);

        // Description Label
        Label descriptionLabel = new Label(maintenance.getDescriptionMaintenance());
        descriptionLabel.setStyle("-fx-text-fill: #7281D8");
        descriptionLabel.setLayoutX(220.0);
        descriptionLabel.setLayoutY(11.0);
        descriptionLabel.setPrefWidth(150.0);
        descriptionLabel.setWrapText(true);

        // Technicien Label
        Label technicienLabel = new Label(maintenance.getNomTechnicien() + " " + maintenance.getPrenomTechnicien());
        technicienLabel.setLayoutX(380.0);
        technicienLabel.setLayoutY(11.0);
        technicienLabel.setPrefWidth(150.0);
        technicienLabel.setStyle("-fx-text-fill: #7281D8");

        // Date de Contrôle Label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = maintenance.getDerniereMiseAJour().format(formatter);

        Label dateControleLabel = new Label(formattedDate);
        dateControleLabel.setLayoutX(520.0);
        dateControleLabel.setLayoutY(11.0);
        dateControleLabel.setPrefWidth(120.0);
        dateControleLabel.setStyle("-fx-text-fill: #7281D8");

        SVGPath svgIcon2 = new SVGPath();
        svgIcon2.setLayoutX(680.0);
        svgIcon2.setLayoutY(7.0);
        svgIcon2.setScaleX(0.8);
        svgIcon2.setScaleY(0.8);

        svgIcon2.setContent("M7.24264 17.9967H3V13.754L14.435 2.319C14.8256 1.92848 15.4587 1.92848 15.8492 2.319L18.6777 5.14743C19.0682 5.53795 19.0682 6.17112 18.6777 6.56164L7.24264 17.9967ZM3 19.9967H21V21.9967H3V19.9967Z");
        svgIcon2.setFill(Color.web("#7281D8"));
        svgIcon2.setId("svg_onActionModify");

        // Créer un bouton avec l'icône
        Button modifyButton = new Button();
        modifyButton.setGraphic(svgIcon2);
        modifyButton.setLayoutX(670.0);
        modifyButton.setLayoutY(11.0);
        modifyButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");


        // Ajouter une action au bouton
        modifyButton.setOnAction(event -> {
            System.out.println("Action déclenchée");
            try {
                // Charger la vue ModifierMaintenance.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/technicien/ModifierMaintenance.fxml"));
                AnchorPane maintenancePane = loader.load();

                // Obtenir le contrôleur de la vue
                ModifierMaintenanceController controller = loader.getController();
                controller.setMainController(this.mainController);

                // Passer les informations nécessaires au contrôleur
                controller.setImmatriculation(maintenance.getNumeroImmatriculationTrain());
                controller.setTypeProbleme(maintenance.getDescriptionMaintenance());
                controller.setTechnicienMaintenance(maintenance.getNomTechnicien() + " " + maintenance.getPrenomTechnicien());
                controller.setIncidentId(maintenance.getIncidentId());
                controller.setEtatMaintenance(maintenance.getEtatMaintenance());
                controller.setDescription(maintenance.getDescriptionMaintenance());
                System.out.println(maintenance.getIncidentId());

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

        // Add labels to the pane
        pane.getChildren().addAll(immatriculationLabel, descriptionLabel, technicienLabel, dateControleLabel, modifyButton);

        return pane;
    }
}