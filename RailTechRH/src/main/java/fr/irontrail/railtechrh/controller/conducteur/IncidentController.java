package fr.irontrail.railtechrh.controller.conducteur;

import fr.irontrail.railtechrh.dao.IncidentDAO;
import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.TypeIncident;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.sql.SQLException;

public class
IncidentController {
    @FXML private ChoiceBox<TypeIncident> cb_typeIncident;
    @FXML private ChoiceBox<String> cb_train;
    @FXML private ChoiceBox<Gravite> cb_gravite;
    @FXML private TextArea ta_description;
    @FXML private IncidentDAO incidentDAO;
    @FXML private Label l_resultText;

    public void initialize() throws SQLException {
        this.incidentDAO = new IncidentDAO();
        cb_gravite.setItems(FXCollections.observableArrayList(Gravite.values()));
        cb_typeIncident.setItems(FXCollections.observableArrayList(TypeIncident.values()));
        cb_train.setItems(incidentDAO.getTrain());
    }

    private void showTemporaryMessage(Label label, String message, Paint color, int duration) {
        label.setText(message);
        label.setTextFill(color);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(duration), event -> {label.setText("");}));
        timeline.play();
    }

    public void b_onClickAddIncident(ActionEvent event) throws SQLException {
        if (cb_typeIncident.getValue() == null || cb_train.getValue()== null || cb_gravite.getValue() == null) {
            l_resultText.setText("Erreur : Veuillez remplir tout les champs");
            l_resultText.setTextFill(Paint.valueOf("red"));
        } else {
            IncidentModel newIncident = new IncidentModel();
            newIncident.setGravite(cb_gravite.getValue());
            newIncident.setTrainImmat(IncidentDAO.getTrainByImmat(cb_train.getValue()));
            newIncident.setTypeIncident(cb_typeIncident.getValue());
            newIncident.setDescription(ta_description.getText());
            boolean ajoutIncident = IncidentDAO.addIncident(newIncident);
            if (ajoutIncident) {
                cb_typeIncident.setValue(null);
                cb_train.setValue(null);
                cb_gravite.setValue(null);
                ta_description.clear();
                showTemporaryMessage(l_resultText, "Incident créé, il sera communiqué à l'opérateur. Numéro d'incident : " + newIncident.getId(), Paint.valueOf("green"), 5);
            } else {
                showTemporaryMessage(l_resultText, "Erreur dans la création de l'incident : veuillez vérifier que toutes les informations sont saisies correctement.", Paint.valueOf("red"), 10);
            }
        }
    }
}
