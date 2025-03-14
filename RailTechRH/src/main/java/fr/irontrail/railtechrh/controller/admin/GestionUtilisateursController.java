package fr.irontrail.railtechrh.controller.admin;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;

public class GestionUtilisateursController {

    @FXML
    private VBox mainContainer;

    @FXML
    private ChoiceBox<UtilisateurModel> utilisateurChoiceBox;

    @FXML
    private VBox detailsContainer;

    @FXML
    private Label idLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button supprimerButton;

    private MainController mainController;

    private UtilisateurDAO utilisateurDAO;
    private UtilisateurModel selectedUser;
    private UtilisateurModel currentUser;
    private boolean isRoleTextEnlarged = false;

    public GestionUtilisateursController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public void setCurrentUser(UtilisateurModel user) {
        this.currentUser = user;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private MainController getMainController() {
        return mainController;
    }

    @FXML
    public void initialize() {
        setupChoiceBox();

        chargerUtilisateurs();

        utilisateurChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                afficherDetailsUtilisateur(newValue);
            } else {
                detailsContainer.setVisible(false);
            }
        });
    }

    private void setupChoiceBox() {
        utilisateurChoiceBox.setConverter(new StringConverter<UtilisateurModel>() {
            @Override
            public String toString(UtilisateurModel user) {
                if (user == null) {
                    return "Sélectionnez un utilisateur";
                }
                return user.getPrenom() + " " + user.getNom();
            }

            @Override
            public UtilisateurModel fromString(String string) {
                return null;
            }
        });
    }

    private void afficherDetailsUtilisateur(UtilisateurModel utilisateur) {
        selectedUser = utilisateur;

        idLabel.setText(String.valueOf(utilisateur.getId()));
        nomLabel.setText(utilisateur.getNom());
        prenomLabel.setText(utilisateur.getPrenom());
        emailLabel.setText(utilisateur.getEmail());
        roleLabel.setText(utilisateur.getRole().toString());

        if (currentUser != null && utilisateur.getId() == currentUser.getId()) {
            supprimerButton.setDisable(true);
            supprimerButton.setStyle("-fx-background-color: #cccccc; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        } else {
            supprimerButton.setDisable(false);
            supprimerButton.setStyle("-fx-background-color: #ff5252; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        }

        detailsContainer.setVisible(true);
    }

    private void chargerUtilisateurs() {
        try {
            List<UtilisateurModel> utilisateurs = utilisateurDAO.getAllUsers();
            ObservableList<UtilisateurModel> data = FXCollections.observableArrayList(utilisateurs);
            utilisateurChoiceBox.setItems(data);

            if (!data.isEmpty()) {
                utilisateurChoiceBox.setValue(data.get(0));
            }
        } catch (Exception e) {
            infoLabel.setText("Erreur lors du chargement des utilisateurs: " + e.getMessage());
            infoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/admin/AjouterUtilisateur.fxml"));
            Parent content = loader.load();

            MainController mainController = getMainController();
            if (mainController != null) {
                mainController.getContentContainer().getChildren().setAll(content);
            }
        } catch (IOException e) {
            infoLabel.setText("Erreur lors du chargement du formulaire d'ajout: " + e.getMessage());
            infoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimerButton() {
        if (selectedUser == null) {
            infoLabel.setText("Aucun utilisateur sélectionné.");
            infoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        if (currentUser != null && selectedUser.getId() == currentUser.getId()) {
            infoLabel.setText("Vous ne pouvez pas supprimer votre propre compte.");
            infoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        supprimerUtilisateur(selectedUser);
    }

    private void supprimerUtilisateur(UtilisateurModel utilisateur) {
        boolean supprime = utilisateurDAO.deleteUser(utilisateur.getId());
        if (supprime) {
            infoLabel.setText("Utilisateur supprimé avec succès !");
            infoLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            detailsContainer.setVisible(false);
            chargerUtilisateurs();
        } else {
            infoLabel.setText("Erreur lors de la suppression de l'utilisateur.");
            infoLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }
}