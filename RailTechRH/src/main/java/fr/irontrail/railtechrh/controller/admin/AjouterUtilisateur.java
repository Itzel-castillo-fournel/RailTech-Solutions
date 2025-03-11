package fr.irontrail.railtechrh.controller.admin;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class AjouterUtilisateur {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField mdpField;

    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    @FXML
    private Button ajouterButton;

    @FXML
    private Label infoLabel; // Label pour afficher les informations

    private UtilisateurDAO utilisateurDAO;

    public AjouterUtilisateur() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    @FXML
    public void initialize() {
        // Initialiser le ChoiceBox avec les valeurs de l'énumération Role
        roleChoiceBox.getItems().setAll(Role.values());
    }

    @FXML
    private void handleAjouterButton() {
        // Récupérer les valeurs des champs
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String mdp = mdpField.getText();
        Role role = roleChoiceBox.getValue();

        // Vérifier que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || role == null) {
            infoLabel.setText("Erreur : Tous les champs doivent être remplis.");
            infoLabel.setStyle("-fx-text-fill: red;"); // Texte en rouge pour les erreurs
            return;
        }

        // Créer un nouvel utilisateur
        UtilisateurModel nouvelUtilisateur = new UtilisateurModel();
        nouvelUtilisateur.setNom(nom);
        nouvelUtilisateur.setPrenom(prenom);
        nouvelUtilisateur.setEmail(email);
        nouvelUtilisateur.setMdp(mdp);
        nouvelUtilisateur.setRole(role);

        // Ajouter l'utilisateur à la base de données
        boolean success = utilisateurDAO.addUser(nouvelUtilisateur);

        if (success) {
            // Afficher les informations de l'utilisateur ajouté dans le Label
            String message = String.format(
                    "Utilisateur ajouté avec succès : %s %s (%s, %s)",
                    nouvelUtilisateur.getPrenom(),
                    nouvelUtilisateur.getNom(),
                    nouvelUtilisateur.getEmail(),
                    nouvelUtilisateur.getRole()
            );
            infoLabel.setText(message);
            infoLabel.setStyle("-fx-text-fill: green;"); // Texte en vert pour le succès
            clearFields(); // Vider les champs après l'ajout
        } else {
            infoLabel.setText("Erreur : L'ajout de l'utilisateur a échoué.");
            infoLabel.setStyle("-fx-text-fill: red;"); // Texte en rouge pour les erreurs
        }
    }

    /**
     * Vide les champs du formulaire.
     */
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        mdpField.clear();
        roleChoiceBox.setValue(null);
    }
}