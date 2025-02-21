package fr.irontrail.railtechrh.controller;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlanningController {
    @FXML private Label dateLabel;
    @FXML private Button previousDateButton;
    @FXML private Button nextDateButton;

    // Labels pour le trajet
    @FXML private Label trainNumero;
    @FXML private Label departVille;
    @FXML private Label departHeure;
    @FXML private Label arriveeVille;
    @FXML private Label arriveeHeure;

    private TrajetDAO trajetDAO;
    private UtilisateurModel conducteurConnecte;
    private LocalDate dateAffichee;

    @FXML
    public void initialize() {
        trajetDAO = new TrajetDAO();
        dateAffichee = LocalDate.now();
        updateDateLabel();

        // Configuration des boutons de navigation
        previousDateButton.setOnAction(e -> changerDate(-1));
        nextDateButton.setOnAction(e -> changerDate(1));
    }

    public void setUtilisateur(UtilisateurModel utilisateur) {
        this.conducteurConnecte = utilisateur;
        chargerTrajets();
    }

    private void changerDate(int jours) {
        dateAffichee = dateAffichee.plusDays(jours);
        updateDateLabel();
        chargerTrajets();
    }

    private void updateDateLabel() {
        dateLabel.setText(dateAffichee.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void chargerTrajets() {
        try {
            List<TrajetModel> trajets = trajetDAO.getTrajetsConducteurParDate(
                    conducteurConnecte.getId(),
                    dateAffichee
            );

            if (!trajets.isEmpty()) {
                // Afficher le premier trajet
                afficherTrajet(trajets.get(0));
            } else {
                // Effacer les informations du trajet
                effacerTrajet();
            }

        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des trajets", e);
        }
    }

    private void afficherTrajet(TrajetModel trajet) {
        trainNumero.setText("TGV " + trajet.getTrainImmat());
        departVille.setText("Départ: " + trajet.getArretDepart());
        departHeure.setText(trajet.getHeureDepart().format(DateTimeFormatter.ofPattern("HH:mm")));
        arriveeVille.setText("Arrivée: " + trajet.getArretArrivee());
        arriveeHeure.setText(trajet.getHeureArrivee().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private void effacerTrajet() {
        trainNumero.setText("");
        departVille.setText("Aucun trajet prévu pour cette date");
        departHeure.setText("");
        arriveeVille.setText("");
        arriveeHeure.setText("");
    }

    private void afficherErreur(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}