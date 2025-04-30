package fr.irontrail.railtechrh.controller.technicien;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.TechnicienDAO;
import fr.irontrail.railtechrh.model.CommentaireModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Etat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.time.format.DateTimeFormatter;
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
    @FXML
    private ScrollPane commentairesScrollPane;
    @FXML
    private VBox commentairesVBox;

    // Variable qui stocke l'id d'un incident
    private int incidentId;

    private final TechnicienDAO technicienDAO = new TechnicienDAO();

    // Référence au MainController
    private MainController mainController;
    private UtilisateurModel currentUser;

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
        chargerCommentaires();
    }

    public void setTechnicienMaintenance(String technicien) {
        this.l_technicienMaintenance.setText(technicien);
    }

    public void setDescription(String description) {this.descriptionTextArea.setText(description);}

    public void setEtatMaintenance(String etatMaintenance) {this.etatChoiceBox.setValue(etatMaintenance);}

    public void setUser(UtilisateurModel utilisateur) {
        this.currentUser = utilisateur;
    }


    // Méthode pour charger les commentaires
    private void chargerCommentaires() {
        try {
            List<CommentaireModel> commentaires = technicienDAO.getCommentairesByMaintenanceId(incidentId);
            commentairesVBox.getChildren().clear();

            if (commentaires.isEmpty()) {
                Label noCommentLabel = new Label("Aucun commentaire pour cette maintenance");
                noCommentLabel.setStyle("-fx-text-fill: #757575; -fx-font-style: italic;");
                commentairesVBox.getChildren().add(noCommentLabel);
                return;
            }

            for (CommentaireModel commentaire : commentaires) {
                // Créer un conteneur pour chaque commentaire
                VBox commentaireBox = new VBox();
                commentaireBox.setSpacing(5);
                commentaireBox.setPadding(new Insets(10));
                commentaireBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

                // En-tête avec le nom du technicien et la date
                HBox headerBox = new HBox();
                headerBox.setSpacing(5);

                UtilisateurModel technicien = commentaire.getTechnicienMaintenance();
                Label technicienLabel = new Label(technicien.getNom() + " " + technicien.getPrenom());
                technicienLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                technicienLabel.setTextFill(Color.web("#1c287c"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                Label dateLabel = new Label(commentaire.getDate().format(formatter));
                dateLabel.setFont(Font.font("System", 12));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                headerBox.getChildren().addAll(technicienLabel, spacer, dateLabel);

                // Contenu du commentaire
                Label commentaireLabel = new Label(commentaire.getCommentaire());
                commentaireLabel.setWrapText(true);

                // Ajouter les éléments au conteneur
                commentaireBox.getChildren().addAll(headerBox, commentaireLabel);

                // Ajouter le conteneur à la VBox principale
                commentairesVBox.getChildren().add(commentaireBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des commentaires.", false);
        }
    }

    // Mettre à jour la méthode majMaintenance pour recharger les commentaires après l'ajout
    public void majMaintenance() {
        String etat = etatChoiceBox.getValue();
        String description = descriptionTextArea.getText();

        // On vérifie que ces 2 champs ne sont pas vides
        if (etat == null || description.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", false);
            return;
        }

        // On modifie la maintenance dans la base de données
        boolean success = technicienDAO.modifierMaintenance(description, etat, incidentId);

        // On ajoute un commentaire pour la modification
        if (!description.isEmpty()) {
            technicienDAO.nouveauCommentaire(description, incidentId, currentUser.getId());
        }

        if (success) {
            showAlert("Succès", "La maintenance a été modifiée avec succès.", true);
            // Recharger les commentaires après l'ajout d'un nouveau
            chargerCommentaires();
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification de la maintenance.", false);
        }
    }


    private void showAlert(String title, String message, boolean redirectToNotifications) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Si la redirection est demandée et que mainController est disponible
        if (redirectToNotifications && mainController != null) {
            alert.setOnCloseRequest(event -> mainController.loadContent("/fr/irontrail/railtechrh/technicien/MaintenanceList.fxml"));
        }

        alert.showAndWait();
    }

}
