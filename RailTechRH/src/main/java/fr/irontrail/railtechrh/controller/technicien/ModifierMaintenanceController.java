package fr.irontrail.railtechrh.controller.technicien;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.TechnicienDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Etat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierMaintenanceController implements Initializable {

    @FXML
    private Label immatriculationLabel;
    @FXML
    private Label typeProblemeLabel;
    @FXML
    private ChoiceBox<String> etatChoiceBox;
    @FXML
    private Label l_technicienMaintenance;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label l_pkMaintenance;

    // Variable qui stocke l'id d'un incident
    private int incidentId;

    private final TechnicienDAO technicienDAO = new TechnicienDAO();

    // Référence au MainController
    private MainController mainController;

    // Méthode pour définir le MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Etat etat : Etat.values()) {
            etatChoiceBox.getItems().add(etat.toString());
        }

    }

    public void setImmatriculation(String immatriculation) {
        immatriculationLabel.setText(immatriculation);
    }

    public void setTypeProbleme(String typeProbleme) {
        typeProblemeLabel.setText(typeProbleme);
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public void setL_technicienMaintenance(String technicien) {
        this.l_technicienMaintenance.setText(technicien);
    }

    public void majMaintenance() {
        String etat = etatChoiceBox.getValue();
        String description = descriptionTextArea.getText();

        // On vérifie que ces 3 champs ne sont pas vides
        if (etat == null || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", false);
            return;
        }

        // On récupére l'ID du technicien
        int technicienId = getTechnicienIdFromName(technicien);

        // On ajoute la maintenance à la base de données
        boolean success = technicienDAO.modifierMaintenance(description, etat, incidentId);

        if (success) {
            showAlert("Succès", "La maintenance a été ajoutée avec succès.", true);
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la maintenance.", false);
        }
    }

    private int getTechnicienIdFromName(String fullName) {
        List<UtilisateurModel> techniciens = technicienDAO.getTechniciens();
        for (UtilisateurModel technicien : techniciens) {
            if ((technicien.getNom() + " " + technicien.getPrenom()).equals(fullName)) {
                return technicien.getId();
            }
        }
        return -1; // Retourne -1 si le technicien n'est pas trouvé
    }

    private void showAlert(String title, String message, boolean redirectToNotifications) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Si la redirection est demandée et que mainController est disponible
        if (redirectToNotifications && mainController != null) {
            alert.setOnCloseRequest(event -> mainController.loadContent("/fr/irontrail/railtechrh/technicien/Notifications.fxml"));
        }

        alert.showAndWait();
    }
}
