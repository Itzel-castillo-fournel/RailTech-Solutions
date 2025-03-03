package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrajetController {

    @FXML private Label l_titrePageTrajet;
    @FXML private Button b_onClickAddTrajet;
    @FXML private ChoiceBox<String> cb_trainImmat;
    @FXML private DatePicker dp_dateDepart;
    @FXML private DatePicker dp_dateArrive;
    @FXML private ChoiceBox<Arret> cb_arretDepart;
    @FXML private ChoiceBox<Arret> cb_arretArrive;
    @FXML private Label l_resultText;
    @FXML private Spinner<LocalTime> sp_heureDepart;
    @FXML private Spinner<LocalTime> sp_heureArrivee;
    @FXML private TrajetDAO trajetDAO;

    private boolean editMode = false;
    private TrajetModel trajetToEdit = null;
    private int trajetId = -1;

    public void initialize() throws SQLException {
        this.trajetDAO = new TrajetDAO();
        cb_arretDepart.setItems(FXCollections.observableArrayList(Arret.values()));
        cb_arretArrive.setItems(FXCollections.observableArrayList(Arret.values()));
        cb_trainImmat.setItems(trajetDAO.getTrain());
        configureTimeSpinner(sp_heureDepart);
        configureTimeSpinner(sp_heureArrivee);

        updateUIForMode();
    }

    // Méthode pour définir le mode édition et le trajet à modifier
    public void setEditMode(TrajetModel trajet) {
        if (trajet != null) {
            this.editMode = true;
            this.trajetToEdit = trajet;
            this.trajetId = trajet.getId();

            fillFormWithTrajetData();
            updateUIForMode();
        }
    }

    private void updateUIForMode() {
        if (editMode) {
            l_titrePageTrajet.setText("Modifier un trajet");
            b_onClickAddTrajet.setText("Enregistrer les modifications");
        } else {
            l_titrePageTrajet.setText("Créer un nouveau trajet");
            b_onClickAddTrajet.setText("Créer le trajet");
        }
    }

    private void fillFormWithTrajetData() {
        if (trajetToEdit == null) return;

        cb_trainImmat.setValue(trajetToEdit.getTrainImmat());
        cb_arretDepart.setValue(trajetToEdit.getArretDepart());
        cb_arretArrive.setValue(trajetToEdit.getArretArrivee());

        if (trajetToEdit.getHeureDepart() != null) {
            dp_dateDepart.setValue(trajetToEdit.getHeureDepart().toLocalDate());
            sp_heureDepart.getValueFactory().setValue(trajetToEdit.getHeureDepart().toLocalTime());
        }

        if (trajetToEdit.getHeureArrivee() != null) {
            dp_dateArrive.setValue(trajetToEdit.getHeureArrivee().toLocalDate());
            sp_heureArrivee.getValueFactory().setValue(trajetToEdit.getHeureArrivee().toLocalTime());
        }
    }

    private void configureTimeSpinner(Spinner<LocalTime> spinner) {
        SpinnerValueFactory<LocalTime> heure = new SpinnerValueFactory<LocalTime>() {
            {
                setValue(LocalTime.of(0, 0));
            }

            @Override
            public void decrement(int steps) {
                LocalTime time = getValue();
                setValue(time.minusMinutes(steps * 15)); // Par incréments de 15 minutes
            }

            @Override
            public void increment(int steps) {
                LocalTime time = getValue();
                setValue(time.plusMinutes(steps * 15));
            }
        };

        StringConverter<LocalTime> converter = new StringConverter<LocalTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime time) {
                if (time == null) {
                    return "";
                }
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return LocalTime.of(0, 0);
                }
                try {
                    return LocalTime.parse(string, formatter);
                } catch (Exception e) {
                    return LocalTime.of(0, 0);
                }
            }
        };

        spinner.setValueFactory(heure);
        heure.setConverter(converter);

        spinner.setEditable(true);
    }

    // Méthode pour récupérer le LocalDateTime complet
    private LocalDateTime getDateTimeDepart() {
        LocalDate date = dp_dateDepart.getValue();
        LocalTime time = sp_heureDepart.getValue();

        if (date == null) {
            return null;
        }

        return LocalDateTime.of(date, time);
    }

    private LocalDateTime getDateTimeArrivee() {
        LocalDate date = dp_dateArrive.getValue();
        LocalTime time = sp_heureArrivee.getValue();

        if (date == null) {
            return null;
        }

        return LocalDateTime.of(date, time);
    }

    private void showTemporaryMessage(Label label, String message, Paint color, int duration) {
        label.setText(message);
        label.setTextFill(color);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), event -> {label.setText("");}));
        timeline.play();
    }

    @FXML
    public void b_onClickAddTrajet(ActionEvent event) throws SQLException {
        if (cb_arretArrive.getValue() == null || cb_arretDepart.getValue()== null || dp_dateArrive.getValue() == null || dp_dateDepart.getValue() == null || cb_trainImmat.getValue() == null || cb_arretArrive.getValue() == null) {
            l_resultText.setText("Erreur : Veuillez remplir tous les champs");
            l_resultText.setTextFill(Paint.valueOf("red"));
            return;
        }

        TrajetModel trajet = new TrajetModel();
        trajet.setArretDepart(cb_arretDepart.getValue());
        trajet.setArretArrivee(cb_arretArrive.getValue());
        trajet.setTrainImmat(cb_trainImmat.getValue());
        trajet.setHeureArrivee(getDateTimeArrivee());
        trajet.setHeureDepart(getDateTimeDepart());

        if (editMode && trajetId > 0) {
            trajet.setId(trajetId);
            if (trajetToEdit != null) {
                trajet.setConducteurId(trajetToEdit.getConducteurId());
            }
        }

        boolean success;

        if (editMode) {
            success = trajetDAO.updateTrajet(
                    trajet.getId(),
                    trajet.getTrainImmat(),
                    trajet.getHeureDepart(),
                    trajet.getHeureArrivee(),
                    trajet.getArretDepart(),
                    trajet.getArretArrivee(),
                    trajet.getConducteurId()
            );
        } else {
            success = TrajetDAO.createTrajet(trajet);
        }

        if (success) {
            if (editMode) {
                showTemporaryMessage(l_resultText, "Trajet modifié avec succès. Numéro de trajet : " + trajet.getId(), Paint.valueOf("green"), 5);

                trajetToEdit = trajet;
            } else {
                cb_arretDepart.setValue(null);
                cb_arretArrive.setValue(null);
                cb_trainImmat.setValue(null);
                dp_dateArrive.setValue(null);
                dp_dateDepart.setValue(null);

                configureTimeSpinner(sp_heureDepart);
                configureTimeSpinner(sp_heureArrivee);

                showTemporaryMessage(l_resultText, "Trajet créé. Numéro de trajet : " + trajet.getId(), Paint.valueOf("green"), 5);
            }
        } else {
            showTemporaryMessage(l_resultText, "Erreur dans la " + (editMode ? "modification" : "création") + " du trajet : veuillez vérifier que toutes les informations sont saisies correctement.", Paint.valueOf("red"), 10);
        }
    }
}