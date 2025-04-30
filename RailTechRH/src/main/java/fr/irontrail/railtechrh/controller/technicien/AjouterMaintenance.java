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
import java.util.ResourceBundle;

public class AjouterMaintenance implements Initializable {

    @FXML
    private Label immatriculationLabel;
    @FXML
    private Label typeProblemeLabel;
    @FXML
    private ChoiceBox<String> etatChoiceBox;
    @FXML
    private Label technicienLabel;
    @FXML
    private TextArea descriptionTextArea;

    // Variable qui stocke l'id d'un incident
    private int incidentId;

    private final TechnicienDAO technicienDAO = new TechnicienDAO();
    private UtilisateurModel currentUser;

    // Référence au MainController
    private MainController mainController;

    // Méthode pour définir le MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des états disponibles dans le choicebox
        for (Etat etat : Etat.values()) {
            etatChoiceBox.getItems().add(etat.toString());
        }
    }

    public void setUser(UtilisateurModel utilisateur) {
        this.currentUser = utilisateur;
        if (utilisateur != null && technicienLabel != null) {
            technicienLabel.setText(utilisateur.getNom() + " " + utilisateur.getPrenom());
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

    public void envoyerMaintenance() {
        // On récupère les valeurs du ChoiceBox (état) ainsi que le contenu du TextArea (la description)
        String etat = etatChoiceBox.getValue();
        String description = descriptionTextArea.getText();

        // On vérifie que ces champs ne sont pas vides
        if (etat == null || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", false);
            return;
        }

        // Vérification que l'utilisateur est bien défini
        if (currentUser == null) {
            showAlert("Erreur", "Utilisateur non identifié.", false);
            return;
        }


        // On ajoute la maintenance à la base de données avec l'ID du technicien connecté
        boolean success = technicienDAO.ajouterMaintenance(description, typeProblemeLabel.getText(), etat, incidentId, currentUser.getId());
        technicienDAO.nouveauCommentaire(description, incidentId, currentUser.getId());


        if (success) {
            showAlert("Succès", "La maintenance a été ajoutée avec succès.", true);
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la maintenance.", false);
        }
    }

    private void showAlert(String title, String message, boolean redirectToNotifications) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Si la redirection est demandée et que mainController est disponible
        if (redirectToNotifications && mainController != null) {
            alert.setOnCloseRequest(event -> mainController.loadContent("/fr/irontrail/railtechrh/technicien/NotificationsTechnicien.fxml"));
        }

        alert.showAndWait();
    }
}