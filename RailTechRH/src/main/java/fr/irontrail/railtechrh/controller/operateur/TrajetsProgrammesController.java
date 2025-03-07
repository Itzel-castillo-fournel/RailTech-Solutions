package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.TrajetModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TrajetsProgrammesController implements Initializable {

    @FXML
    private VBox trajetsContainer;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label noTrajetsLabel;

    private final OperateurDAO operateurDAO = new OperateurDAO();

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTrajetsProgrammes();

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                loadTrajetsProgrammes();
            }
        });
    }

    @FXML
    public void b_onClickAddTrajet(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/operateur/TrajetForm.fxml"));
            Parent content = loader.load();

            if (mainController != null) {
                mainController.getContentContainer().getChildren().setAll(content);
            } else {
                showAlert("Erreur", "Impossible d'accéder au conteneur principal.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de création de trajet.");
        }
    }

    private void loadTrajetsProgrammes() {
        try {
            trajetsContainer.getChildren().clear();
            LocalDate today = LocalDate.now();
            List<TrajetModel> trajets = operateurDAO.getTrajetsProgrammes();
            if (trajets.isEmpty()) {
                noTrajetsLabel.setText("Aucun trajet prévu à partir du " + today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
                noTrajetsLabel.setVisible(true);
            } else {
                noTrajetsLabel.setVisible(false);
                for (TrajetModel trajet : trajets) {
                    TitledPane trajetPane = createTrajetPane(trajet);
                    trajetsContainer.getChildren().add(trajetPane);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filterTrajetsByDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            try {
                trajetsContainer.getChildren().clear();
                List<TrajetModel> filteredTrajets = operateurDAO.getTrajetsByDate(selectedDate);

                if (filteredTrajets.isEmpty()) {
                    noTrajetsLabel.setText("Aucun trajet programmé à partir de cette date : " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
                    noTrajetsLabel.setVisible(true);
                } else {
                    noTrajetsLabel.setVisible(false);
                    for (TrajetModel trajet : filteredTrajets) {
                        TitledPane trajetPane = createTrajetPane(trajet);
                        trajetsContainer.getChildren().add(trajetPane);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du filtrage des trajets par date.");
            }
        } else {
            loadTrajetsProgrammes();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Affiche les détails du trajet
    private TitledPane createTrajetPane(TrajetModel trajet) {
        TitledPane titledPane = new TitledPane();
        titledPane.setStyle("-fx-background-color: none;");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: white;");

        Pane infoPane = new Pane();
        infoPane.setStyle("-fx-background-color: #E1E2E6; -fx-background-radius: 15; -fx-border-radius: 15;");
        infoPane.setPrefSize(645, 83);
        infoPane.setLayoutX(37);
        infoPane.setLayoutY(21);

        Label immatriculationLabel = new Label("Immatriculation : ");
        immatriculationLabel.setLayoutX(14);
        immatriculationLabel.setLayoutY(33);
        immatriculationLabel.setTextFill(Color.web("#2d45c9"));

        Label immatriculationValue = new Label(trajet.getTrainImmat() != null ? trajet.getTrainImmat() : "Inconnu");
        immatriculationValue.setLayoutX(109);
        immatriculationValue.setLayoutY(34);
        immatriculationValue.setFont(new Font("System Bold", 12));
        immatriculationValue.setTextFill(Color.web("#2d45c9"));

        Label modeleLabel = new Label("Modèle :");
        modeleLabel.setLayoutX(14);
        modeleLabel.setLayoutY(52);
        modeleLabel.setTextFill(Color.web("#2d45c9"));

        Label modeleValue = new Label();
        modeleValue.setLayoutX(67);
        modeleValue.setLayoutY(52);
        modeleValue.setFont(new Font("System Bold", 12));
        modeleValue.setTextFill(Color.web("#2d45c9"));

        Label marqueLabel = new Label("Marque :");
        marqueLabel.setLayoutX(278);
        marqueLabel.setLayoutY(52);
        marqueLabel.setTextFill(Color.web("#2d45c9"));

        Label marqueValue = new Label();
        marqueValue.setLayoutX(332);
        marqueValue.setLayoutY(52);
        marqueValue.setFont(new Font("System Bold", 12));
        marqueValue.setTextFill(Color.web("#2d45c9"));

        Label conducteurLabel = new Label("Conducteur :");
        conducteurLabel.setLayoutX(278);
        conducteurLabel.setLayoutY(34);
        conducteurLabel.setTextFill(Color.web("#2d45c9"));

        Label conducteurValue = new Label();
        conducteurValue.setLayoutX(352);
        conducteurValue.setLayoutY(34);
        conducteurValue.setFont(new Font("System Bold", 12));
        conducteurValue.setTextFill(Color.web("#2d45c9"));

        Label infoTitle = new Label("Informations du train");
        infoTitle.setLayoutX(14);
        infoTitle.setLayoutY(9);
        infoTitle.setFont(new Font("System Bold", 14));
        infoTitle.setTextFill(Color.web("#2d45c9"));

        try {
            TrainModel trainDetails = operateurDAO.getTrainDetails(trajet.getTrainImmat());
            if (trainDetails != null) {
                modeleValue.setText(trainDetails.getModele());
                marqueValue.setText(trainDetails.getMarque());
            } else {
                System.out.println("Train details not found for immatriculation: " + trajet.getTrainImmat());
                modeleValue.setText("Inconnu");
                marqueValue.setText("Inconnu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            modeleValue.setText("Erreur");
            marqueValue.setText("Erreur");
        }

        try {
            String conducteurName = operateurDAO.getConducteurName(trajet.getConducteurId());
            conducteurValue.setText(conducteurName != null ? conducteurName : "Inconnu");
        } catch (SQLException e) {
            e.printStackTrace();
            conducteurValue.setText("Erreur");
        }

        javafx.scene.control.Button modifyButton = new javafx.scene.control.Button("Modifier");
        modifyButton.setStyle("-fx-background-color: #2d45c9; -fx-text-fill: white; -fx-background-radius: 5;");
        modifyButton.setLayoutX(550);
        modifyButton.setLayoutY(30);
        modifyButton.setPrefWidth(80);
        modifyButton.setOnAction(event -> handleModifyTrajet(trajet));

        infoPane.getChildren().addAll(immatriculationLabel, immatriculationValue, modeleLabel, modeleValue, marqueLabel, marqueValue, conducteurLabel, conducteurValue, infoTitle, modifyButton);

        anchorPane.getChildren().add(infoPane);

        titledPane.setContent(anchorPane);
        titledPane.setExpanded(false);
        titledPane.setGraphic(createGraphicPane(trajet));

        return titledPane;
    }

    private void handleModifyTrajet(TrajetModel trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/operateur/TrajetForm.fxml"));
            Parent content = loader.load();

            TrajetController controller = loader.getController();
            controller.setEditMode(trajet);

            if (mainController != null) {
                mainController.getContentContainer().getChildren().setAll(content);
            } else {
                showAlert("Erreur", "Impossible d'accéder au conteneur principal.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de modification du trajet.");
        }
    }

    //Trajet de base
    private Pane createGraphicPane(TrajetModel trajet) {
        Pane graphicPane = new Pane();

        HBox hBox = new HBox();
        hBox.setPrefHeight(65);

        VBox iconBox = new VBox();
        iconBox.setAlignment(javafx.geometry.Pos.CENTER);
        iconBox.setPrefWidth(43);

        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M17.2 20L19 21.5V22H5V21.5L6.8 20H5C3.89543 20 3 19.1046 3 18V7C3 4.79086 4.79086 3 7 3H17C19.2091 3 21 4.79086 21 7V18C21 19.1046 20.1046 20 19 20H17.2ZM7 5C5.89543 5 5 5.89543 5 7V18H19V7C19 5.89543 18.1046 5 17 5H7ZM12 17C10.8954 17 10 16.1046 10 15C10 13.8954 10.8954 13 12 13C13.1046 13 14 13.8954 14 15C14 16.1046 13.1046 17 12 17ZM6 7H18V11H6V7Z");
        svgPath.setFill(Color.web("#2d45c9"));

        iconBox.getChildren().add(svgPath);

        VBox infoBox = new VBox();
        infoBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        infoBox.setPrefWidth(206);

        HBox routeBox = new HBox();
        routeBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        routeBox.setPrefWidth(202);
        routeBox.setSpacing(10); // Ajoutez de l'espacement entre les éléments

        Label departLabel = new Label(trajet.getArretDepart() != null ? trajet.getArretDepart().toString() : "Inconnu");
        departLabel.setFont(new Font("System Bold", 14));
        departLabel.setTextFill(Color.web("#2d45c9"));

        SVGPath arrow = new SVGPath();
        arrow.setContent("M16.1716 10.9999L10.8076 5.63589L12.2218 4.22168L20 11.9999L12.2218 19.778L10.8076 18.3638L16.1716 12.9999H4V10.9999H16.1716Z");
        arrow.setFill(Color.web("#2d45c9"));

        Label arriveeLabel = new Label(trajet.getArretArrivee() != null ? trajet.getArretArrivee().toString() : "Inconnu");
        arriveeLabel.setFont(new Font("System Bold", 14));
        arriveeLabel.setTextFill(Color.web("#2d45c9"));

        routeBox.getChildren().addAll(departLabel, arrow, arriveeLabel);

        Label trainInfo = new Label("Train - " + (trajet.getTrainImmat() != null ? trajet.getTrainImmat() : "Inconnu"));
        trainInfo.setTextFill(Color.web("#2d45c9"));
        trainInfo.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        infoBox.getChildren().addAll(routeBox, trainInfo);

        VBox dateBox = new VBox();
        dateBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        dateBox.setPrefWidth(375);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String formattedDateTime = trajet.getHeureDepart() != null
                ? trajet.getHeureDepart().format(formatter)
                : "Inconnu";

        Label dateLabel = new Label(formattedDateTime);
        dateLabel.setFont(new Font("System Bold", 14));
        dateLabel.setTextFill(Color.web("#2d45c9"));

        Label departText = new Label("Départ");
        departText.setTextFill(Color.web("#2d45c9"));

        dateBox.getChildren().addAll(dateLabel, departText);

        hBox.getChildren().addAll(iconBox, infoBox, dateBox);
        graphicPane.getChildren().add(hBox);

        return graphicPane;
    }
}