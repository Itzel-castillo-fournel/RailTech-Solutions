package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AssignerConducteurController {

    @FXML private Label l_conducteur;
    @FXML private Label l_noTrajetFound;
    @FXML private VBox vb_trajetsContainer;
    @FXML private Button b_assignDriver;
    @FXML private SVGPath svg_conducteurIcon;


    private OperateurDAO operateurDAO = new OperateurDAO();
    private TrajetModel selectedTrajet = null;
    private Pane selectedPane = null;
    private MainController mainController;

    //Défini le MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() throws SQLException {

        svg_conducteurIcon.setScaleX(1.2);
        svg_conducteurIcon.setScaleY(1.2);

        l_conducteur.setText(operateurDAO.getConducteurPrenom(3) + " " + operateurDAO.getConducteurName(3));
        loadTrajetsProgrammes();

        b_assignDriver.setDisable(true);
    }

    //Charge tout les trajets à venir (même si un conducteur est déjà assigné)
    private void loadTrajetsProgrammes() {
        try {
            vb_trajetsContainer.getChildren().clear();
            LocalDate today = LocalDate.now();
            List<TrajetModel> trajets = operateurDAO.getTrajetsProgrammes();

            if (trajets.isEmpty()) {
                l_noTrajetFound.setText("Aucun trajet prévu à partir du " + today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
                l_noTrajetFound.setVisible(true);
            } else {
                l_noTrajetFound.setVisible(false);

                // Limite d'affichage à 100
                int maxTrajetsToShow = Math.min(trajets.size(), 100);

                for (int i = 0; i < maxTrajetsToShow ; i++) {

                    Pane trajetPane = createGraphicPane(trajets.get(i));

                    // Ajouter une bordure inférieure sauf pour le dernier élément
                    if (i < maxTrajetsToShow - 1) {
                        trajetPane.setStyle(trajetPane.getStyle() + "-fx-border-color: #D0D0D0; -fx-border-width: 0 0 1 0;");
                    }

                    vb_trajetsContainer.getChildren().add(trajetPane);
                }

                // Indique s'il y a plus de trajets que ceux affichés
                if (trajets.size() > maxTrajetsToShow) {
                    Label moreLabel = new Label("+ " + (trajets.size() - maxTrajetsToShow) + " trajets supplémentaires");
                    moreLabel.setStyle("-fx-text-fill: #2d45c9; -fx-font-weight: bold;");
                    vb_trajetsContainer.getChildren().add(moreLabel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            l_noTrajetFound.setText("Erreur lors du chargement des trajets: " + e.getMessage());
            l_noTrajetFound.setVisible(true);
        }
    }

    // Méthode pour créer les panneaux trajets
    private Pane createGraphicPane(TrajetModel trajet) {
        Pane graphicPane = new Pane();
        // Stocker l'état normal dans une variable pour pouvoir y revenir
        String normalStyle = "-fx-background-color: #E1E2E6; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 5;";
        String hoverStyle = "-fx-background-color: #D0D2E0; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 5;";
        String selectedStyle = "-fx-background-color: #B9C0E0; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #2d45c9; -fx-border-width: 2; -fx-padding: 5;";

        graphicPane.setStyle(normalStyle);

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
        routeBox.setSpacing(10);

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

        // Formater la date et l'heure
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

        // Gère les effets de survol seulement si ce n'est pas l'élément sélectionné
        graphicPane.setOnMouseEntered(e -> {
            if (selectedTrajet == null || !selectedTrajet.equals(trajet)) {
                graphicPane.setStyle(hoverStyle);
            }
        });

        graphicPane.setOnMouseExited(e -> {
            if (selectedTrajet == null || !selectedTrajet.equals(trajet)) {
                graphicPane.setStyle(normalStyle);
            }
        });

        // Ajoute un gestionnaire de clic avec UserData pour identifier l'élément
        graphicPane.setUserData(trajet);
        graphicPane.setOnMouseClicked(e -> selectTrajet(trajet, graphicPane));

        // Si ce trajet est déjà sélectionné, appliquer directement le style sélectionné
        if (selectedTrajet != null && selectedTrajet.equals(trajet)) {
            graphicPane.setStyle(selectedStyle);
        }

        return graphicPane;
    }

    // Méthode pour sélectionner le trajet
    private void selectTrajet(TrajetModel trajet, Pane pane) {
        // Référence au style normal et sélectionné
        String normalStyle = "-fx-background-color: #E1E2E6; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 5;";
        String selectedStyle = "-fx-background-color: #B9C0E0; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #2d45c9; -fx-border-width: 2; -fx-padding: 5;";

        // Désélectionner l'ancien trajet s'il existe
        if (selectedPane != null) {
            selectedPane.setStyle(normalStyle);
        }

        // Sélectionner le nouveau
        selectedTrajet = trajet;
        selectedPane = pane;
        pane.setStyle(selectedStyle);

        // Active le bouton quand un trajet est sélectionner
        b_assignDriver.setDisable(false);
    }

    //Permet d'afficher un message temporairement
    private void showTemporaryMessage(Label label, String message, Paint color, int duration) {
        label.setText(message);
        label.setTextFill(color);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), event -> {label.setText("");}));
        timeline.play();
    }

    //Assigne le conducteur au trajet sélectionné
    public void b_onClickAssignDriver(ActionEvent actionEvent) throws SQLException {
        boolean updateTrajet = operateurDAO.updateTrajet(selectedTrajet.getId(), 3);

        if (updateTrajet) {
            //Redirige vers le plannin du conducteur
            //A MODIFIER
            mainController.loadContent("/fr/irontrail/railtechrh/operateur/TrajetsProgrammes.fxml");
        } else {
            showTemporaryMessage(l_noTrajetFound, "Erreur lors de l'assignation du conducteur.", Paint.valueOf("red"), 10);
        }
    }
}
