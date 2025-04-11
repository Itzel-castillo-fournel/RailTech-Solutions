package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlanningController {
    @FXML private VBox trajetsContainer;
    @FXML private Label dateLabel;
    @FXML private Button previousDateButton;
    @FXML private Button nextDateButton;

    private TrajetDAO trajetDAO;
    private UtilisateurModel currentUser;
    private LocalDate dateAffichee;

    @FXML
    public void initialize() {
        trajetDAO = new TrajetDAO();
        dateAffichee = LocalDate.now();

        previousDateButton.setOnAction(e -> changerDate(-1));
        nextDateButton.setOnAction(e -> changerDate(1));
    }

    private void changerDate(int jours) {
        dateAffichee = dateAffichee.plusDays(jours);
        dateLabel.setText(dateAffichee.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (currentUser != null) {
            afficherTrajetsDuJour();
        }
    }

    public void setUser(UtilisateurModel utilisateur) {
        this.currentUser = utilisateur;
        dateLabel.setText(dateAffichee.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        afficherTrajetsDuJour();
    }

    private void afficherTrajetsDuJour() {
        try {
            trajetsContainer.getChildren().clear();
            List<TrajetModel> trajets = trajetDAO.getTrajetsConducteurParDate(currentUser.getId(), dateAffichee);

            for (TrajetModel trajet : trajets) {
                VBox carteTrajet = creerCarteTrajet(trajet);
                trajetsContainer.getChildren().add(carteTrajet);
            }

            if (trajets.isEmpty()) {
                Label noTrajetsLabel = new Label("Aucun trajet prévu pour cette date");
                noTrajetsLabel.setStyle("-fx-text-fill: #757575; -fx-font-style: italic;");
                trajetsContainer.getChildren().add(noTrajetsLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox creerCarteTrajet(TrajetModel trajet) {
        VBox carteTrajet = new VBox();
        carteTrajet.setStyle("-fx-padding: 20px; -fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-border-radius: 20; -fx-border-color: #E0E0E0; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        carteTrajet.setSpacing(15);
        carteTrajet.setPrefWidth(650);
        carteTrajet.setMaxWidth(Region.USE_PREF_SIZE);
        carteTrajet.setMinHeight(100);

        HBox enTete = new HBox();
        enTete.setAlignment(Pos.CENTER_LEFT);
        enTete.setSpacing(10);
        enTete.setPrefHeight(30);
        enTete.setStyle("-fx-padding: 10px;");

        ImageView trainIcon = new ImageView(new Image(getClass().getResource("/fr/irontrail/railtechrh/icons/train-planning-icon.png").toExternalForm()));
        trainIcon.setFitHeight(20);
        trainIcon.setFitWidth(20);

        Label trainLabel = new Label("TGV " + trajet.getTrainImmat());
        trainLabel.setStyle("-fx-font-weight: 900; -fx-text-fill: #1a237e; -fx-font-size: 18px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        LocalDateTime now = LocalDateTime.now();
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-padding: 5px 10px; -fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 12px;");

        if (trajet.getHeureDepart().isAfter(now)) {
            statusLabel.setText("À venir");
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: rgb(28, 40, 124); -fx-background-color: rgba(28, 40, 124, 0.33);");
        } else if (trajet.getHeureArrivee().isBefore(now)) {
            statusLabel.setText("Terminé");
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: #999999; -fx-background-color: #f0f0f0;");
        } else {
            statusLabel.setText("En cours");
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: #ffffff; -fx-background-color: #43a047;");
        }

        enTete.getChildren().addAll(trainIcon, trainLabel, spacer, statusLabel);

        Separator separator = new Separator();

        HBox infoTrajet = new HBox();
        infoTrajet.setAlignment(Pos.CENTER_LEFT);
        infoTrajet.setSpacing(80);
        infoTrajet.setPrefHeight(60);
        infoTrajet.setStyle("-fx-padding: 10px;");

        Region spacers = new Region();
        spacers.setPrefWidth(120);

        VBox departInfo = new VBox();
        departInfo.setSpacing(10);

        HBox departTitre = new HBox(10);
        ImageView locationIconDepart = new ImageView(new Image(getClass().getResource("/fr/irontrail/railtechrh/icons/localisation-icon.png").toExternalForm()));
        locationIconDepart.setFitHeight(16);
        locationIconDepart.setFitWidth(16);
        Label departLabel = new Label("Départ : " + trajet.getArretDepart());
        departLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-font-size: 14px;");
        departTitre.getChildren().addAll(locationIconDepart, departLabel);

        HBox departHeure = new HBox(10);
        ImageView timeIconDepart = new ImageView(new Image(getClass().getResource("/fr/irontrail/railtechrh/icons/time-icon.png").toExternalForm()));
        timeIconDepart.setFitHeight(16);
        timeIconDepart.setFitWidth(16);
        Label heureDepart = new Label(trajet.getHeureDepart().format(DateTimeFormatter.ofPattern("HH:mm")));
        heureDepart.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-font-size: 14px;");
        departHeure.getChildren().addAll(timeIconDepart, heureDepart);

        departInfo.getChildren().addAll(departTitre, departHeure);

        VBox arriveeInfo = new VBox();
        arriveeInfo.setSpacing(10);

        HBox arriveeTitre = new HBox(10);
        ImageView locationIconArrivee = new ImageView(new Image(getClass().getResource("/fr/irontrail/railtechrh/icons/localisation-icon.png").toExternalForm()));
        locationIconArrivee.setFitHeight(16);
        locationIconArrivee.setFitWidth(16);
        Label arriveeLabel = new Label("Arrivée : " + trajet.getArretArrivee());
        arriveeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-font-size: 14px;");
        arriveeTitre.getChildren().addAll(locationIconArrivee, arriveeLabel);

        HBox arriveeHeure = new HBox(10);
        ImageView timeIconArrivee = new ImageView(new Image(getClass().getResource("/fr/irontrail/railtechrh/icons/time-icon.png").toExternalForm()));
        timeIconArrivee.setFitHeight(16);
        timeIconArrivee.setFitWidth(16);
        Label heureArrivee = new Label(trajet.getHeureArrivee().format(DateTimeFormatter.ofPattern("HH:mm")));
        heureArrivee.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-font-size: 14px;");
        arriveeHeure.getChildren().addAll(timeIconArrivee, heureArrivee);

        arriveeInfo.getChildren().addAll(arriveeTitre, arriveeHeure);

        infoTrajet.getChildren().addAll(departInfo, spacers, arriveeInfo);

        carteTrajet.getChildren().addAll(enTete, separator, infoTrajet);
        return carteTrajet;
    }
}
