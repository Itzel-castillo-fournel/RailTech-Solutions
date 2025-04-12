package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.MaintenanceModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;

import java.net.SocketOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class MaintenanceListController {

    @FXML
    private VBox maintenanceListVBox;

    @FXML
    private Label pannePercentageLabel;

    @FXML
    private Label maintenancePercentageLabel;

    @FXML
    private Label operationnelPercentageLabel;

    private OperateurDAO operateurDAO;

    public MaintenanceListController() {
        this.operateurDAO = new OperateurDAO();
    }

    @FXML
    public void initialize() {
        loadMaintenanceDetails();
        updatePercentageLabels();
    }

    private void loadMaintenanceDetails() {
        try {
            List<MaintenanceModel> maintenanceDetails = operateurDAO.getMaintenanceDetails();
            Platform.runLater(() -> {
                maintenanceListVBox.getChildren().clear();

                for (int i = 0; i < maintenanceDetails.size(); i++) {
                    MaintenanceModel maintenance = maintenanceDetails.get(i);
                    Pane maintenancePane = createMaintenancePane(maintenance,
                            i == maintenanceDetails.size() - 1);
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
            Map<String, Integer> percentages = operateurDAO.getMaintenancePercentages();
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
        pane.setPrefWidth(700.0);

        String baseStyle = "-fx-background-color: transparent;";

        if (!isLastItem) {
            pane.setStyle(baseStyle + "-fx-border-color: transparent transparent #7281D8 transparent; -fx-border-width: 0 0 1 0;");
        } else {
            pane.setStyle(baseStyle);
        }

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

        pane.getChildren().addAll(immatriculationLabel, descriptionLabel, technicienLabel, dateControleLabel);

        return pane;
    }
}