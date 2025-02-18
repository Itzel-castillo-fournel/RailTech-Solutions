package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TrajetsProgrammesController implements Initializable {

    @FXML
    private VBox trajetsContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTrajetsData();
    }

    private void loadTrajetsData() {
        OperateurDAO operateurDAO = new OperateurDAO();
        try {
            List<TrajetModel> trajets = operateurDAO.getTrajetsProgrammes();
            for (TrajetModel trajet : trajets) {
                VBox trajetCard = createTrajetCard(trajet);
                trajetsContainer.getChildren().add(trajetCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createTrajetCard(TrajetModel trajet) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 1; -fx-border-color: gray;");

        Label idLabel = new Label("ID: " + trajet.getId());
        Label departLabel = new Label("Heure Départ: " + trajet.getHeureDepart());
        Label arriveeLabel = new Label("Heure Arrivée: " + trajet.getHeureArrivee());
        Label arretDepartLabel = new Label("Arrêt Départ: " + trajet.getArretDepart());
        Label arretArriveeLabel = new Label("Arrêt Arrivée: " + trajet.getArretArrivee());
        Label trainLabel = new Label("Train: " + trajet.getTrainImmat());
        Label conducteurLabel = new Label("Conducteur ID: " + trajet.getConducteurId());

        card.getChildren().addAll(idLabel, departLabel, arriveeLabel, arretDepartLabel, arretArriveeLabel, trainLabel, conducteurLabel);

        return card;
    }
}
