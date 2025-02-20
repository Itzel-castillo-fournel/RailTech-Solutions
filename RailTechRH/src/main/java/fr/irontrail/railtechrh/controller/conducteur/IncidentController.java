package fr.irontrail.railtechrh.controller.conducteur;

import fr.irontrail.railtechrh.dao.IncidentDAO;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.TypeIncident;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class IncidentController {
    @FXML private ChoiceBox<TypeIncident> cb_typeIncident;
    @FXML private ChoiceBox<String> cb_train;
    @FXML private ChoiceBox<Gravite> cb_gravite;
    @FXML private TextArea ta_description;
    @FXML private IncidentDAO incidentDAO;

    public void initialize() throws SQLException {
        this.incidentDAO = new IncidentDAO();
        cb_gravite.setItems(FXCollections.observableArrayList(Gravite.values()));
        cb_typeIncident.setItems(FXCollections.observableArrayList(TypeIncident.values()));
        cb_train.setItems(incidentDAO.getTrain());
    }
}
