package fr.irontrail.railtechrh.controller.operateur;

import fr.irontrail.railtechrh.controller.MainController;
import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class PlanningController implements Initializable {

    @FXML
    private GridPane planningGrid;

    @FXML
    private Button b_onClickAddTrajet;

    @FXML
    private ChoiceBox<UtilisateurModel> cb_conducteur;

    @FXML
    private ChoiceBox<WeekInfo> cb_semaine;

    @FXML
    private Label noTrajetsLabel;

    @FXML
    private VBox lundiContainer;

    @FXML
    private VBox mardiContainer;

    @FXML
    private VBox mercrediContainer;

    @FXML
    private VBox jeudiContainer;

    @FXML
    private VBox vendrediContainer;

    @FXML
    private VBox samediContainer;

    @FXML
    private VBox dimancheContainer;

    private MainController mainController;
    private final TrajetDAO trajetDAO = new TrajetDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private LocalDate currentWeekStart;
    private Image timeIcon;

    // Classe interne pour stocker les informations de semaine
    public static class WeekInfo {
        private final LocalDate weekStart;
        private final int weekNumber;
        private final int year;

        public WeekInfo(LocalDate weekStart, int weekNumber, int year) {
            this.weekStart = weekStart;
            this.weekNumber = weekNumber;
            this.year = year;
        }

        public LocalDate getWeekStart() {
            return weekStart;
        }

        public int getWeekNumber() {
            return weekNumber;
        }

        public int getYear() {
            return year;
        }

        @Override
        public String toString() {
            return "Semaine " + weekNumber + " (" +
                    weekStart.format(DateTimeFormatter.ofPattern("dd/MM")) + " - " +
                    weekStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")";
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Charger l'icône d'horloge
            timeIcon = new Image(getClass().getResourceAsStream("/fr/irontrail/railtechrh/icons/time-icon.png"));

            // Initialiser la date de début de semaine courante (lundi de la semaine actuelle)
            currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);

            // Charger la liste des conducteurs
            loadConducteurs();

            // Charger la liste des semaines (4 semaines avant et après la semaine courante)
            loadWeeks();

            // Configurer le comportement du bouton Ajouter
            b_onClickAddTrajet.setOnAction(this::handleAddTrajet);

            // Ajouter les écouteurs sur les changements de sélection
            cb_conducteur.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> loadPlanningForSelectedOptions());

            cb_semaine.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> loadPlanningForSelectedOptions());

            // Sélectionner la semaine courante par défaut
            selectCurrentWeek();

        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Erreur d'initialisation", "Une erreur est survenue lors de l'initialisation: " + e.getMessage());
        }
    }

    private void loadConducteurs() {
        try {
            List<UtilisateurModel> conducteurs = utilisateurDAO.getAllConducteurs();
            ObservableList<UtilisateurModel> observableConducteurs = FXCollections.observableArrayList(conducteurs);
            cb_conducteur.setItems(observableConducteurs);

            // Définir comment afficher les conducteurs dans le ChoiceBox
            cb_conducteur.setConverter(new StringConverter<UtilisateurModel>() {
                @Override
                public String toString(UtilisateurModel user) {
                    if (user == null) {
                        return "Sélectionnez le conducteur";
                    }
                    return user.getPrenom() + " " + user.getNom();
                }

                @Override
                public UtilisateurModel fromString(String string) {
                    return null; // Pas nécessaire pour un ChoiceBox
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la liste des conducteurs: " + e.getMessage());
        }
    }

    private void loadWeeks() {
        try {
            // Récupérer tous les trajets pour déterminer la première et la dernière date
            List<TrajetModel> allTrajets = trajetDAO.getAllTrajets();

            if (allTrajets.isEmpty()) {
                // Si aucun trajet n'existe, afficher seulement la semaine courante
                LocalDate now = LocalDate.now();
                LocalDate weekStart = now.with(DayOfWeek.MONDAY);

                TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                int weekNumber = weekStart.get(weekOfYear);
                int year = weekStart.getYear();

                WeekInfo currentWeek = new WeekInfo(weekStart, weekNumber, year);

                ObservableList<WeekInfo> observableWeeks = FXCollections.observableArrayList(currentWeek);
                cb_semaine.setItems(observableWeeks);
                cb_semaine.getSelectionModel().select(0);
            } else {
                // Trouver la date du premier et du dernier trajet
                LocalDateTime firstTrajetDate = allTrajets.stream()
                        .min(Comparator.comparing(TrajetModel::getHeureDepart))
                        .map(TrajetModel::getHeureDepart)
                        .orElse(LocalDateTime.now());

                LocalDateTime lastTrajetDate = allTrajets.stream()
                        .max(Comparator.comparing(TrajetModel::getHeureDepart))
                        .map(TrajetModel::getHeureDepart)
                        .orElse(LocalDateTime.now());

                // Convertir en LocalDate et trouver le premier lundi pour chaque semaine
                LocalDate firstWeekStart = firstTrajetDate.toLocalDate().with(DayOfWeek.MONDAY);
                LocalDate lastWeekStart = lastTrajetDate.toLocalDate().with(DayOfWeek.MONDAY);

                // Ajouter quelques semaines supplémentaires à la fin pour permettre la planification future
                lastWeekStart = lastWeekStart.plusWeeks(4);

                // Créer la liste des semaines
                List<WeekInfo> weeks = new ArrayList<>();
                LocalDate currentWeekStart = firstWeekStart;

                TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

                // Ajouter toutes les semaines de la première à la dernière
                while (!currentWeekStart.isAfter(lastWeekStart)) {
                    int weekNumber = currentWeekStart.get(weekOfYear);
                    int year = currentWeekStart.getYear();

                    weeks.add(new WeekInfo(currentWeekStart, weekNumber, year));

                    currentWeekStart = currentWeekStart.plusWeeks(1);
                }

                ObservableList<WeekInfo> observableWeeks = FXCollections.observableArrayList(weeks);
                cb_semaine.setItems(observableWeeks);

                // Trouver et sélectionner la semaine courante si elle existe dans la liste
                selectCurrentWeek();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les semaines: " + e.getMessage());
        }
    }

    private void selectCurrentWeek() {
        LocalDate now = LocalDate.now();
        LocalDate currentWeekStart = now.with(DayOfWeek.MONDAY);

        // Chercher la semaine actuelle dans la liste
        for (int i = 0; i < cb_semaine.getItems().size(); i++) {
            WeekInfo weekInfo = cb_semaine.getItems().get(i);
            if (weekInfo.getWeekStart().equals(currentWeekStart)) {
                cb_semaine.getSelectionModel().select(i);
                return;
            }
        }

        // Si la semaine actuelle n'est pas trouvée, sélectionner la première semaine
        if (!cb_semaine.getItems().isEmpty()) {
            cb_semaine.getSelectionModel().select(0);
        }
    }

    private void loadPlanningForSelectedOptions() {
        UtilisateurModel selectedConducteur = cb_conducteur.getSelectionModel().getSelectedItem();
        WeekInfo selectedWeek = cb_semaine.getSelectionModel().getSelectedItem();

        if (selectedConducteur != null && selectedWeek != null) {
            loadPlanningForConducteur(selectedConducteur.getId(), selectedWeek.getWeekStart());
        } else {
            clearPlanning();
            if (selectedConducteur == null && selectedWeek != null) {
                noTrajetsLabel.setText("Veuillez sélectionner un conducteur");
                noTrajetsLabel.setVisible(true);
            } else if (selectedConducteur != null && selectedWeek == null) {
                noTrajetsLabel.setText("Veuillez sélectionner une semaine");
                noTrajetsLabel.setVisible(true);
            } else {
                noTrajetsLabel.setText("Veuillez sélectionner un conducteur et une semaine");
                noTrajetsLabel.setVisible(true);
            }
        }
    }

    private void loadPlanningForConducteur(int conducteurId, LocalDate weekStart) {
        try {
            // Vider les conteneurs de jours
            clearPlanning();

            // Calculer les dates de la semaine
            List<LocalDate> weekDates = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                weekDates.add(weekStart.plusDays(i));
            }

            // Récupérer les trajets du conducteur pour chaque jour de la semaine
            boolean hasAnyTrajet = false;
            for (int i = 0; i < 7; i++) {
                LocalDate date = weekDates.get(i);
                List<TrajetModel> trajets = trajetDAO.getTrajetsConducteurParDate(conducteurId, date);

                if (!trajets.isEmpty()) {
                    hasAnyTrajet = true;
                    // Ajouter les trajets au conteneur du jour correspondant
                    VBox dayContainer = getDayContainer(i);
                    for (TrajetModel trajet : trajets) {
                        addTrajetToContainer(dayContainer, trajet);
                    }
                }
            }

            if (!hasAnyTrajet) {
                noTrajetsLabel.setText("Aucun trajet planifié pour ce conducteur cette semaine");
                noTrajetsLabel.setVisible(true);
            } else {
                noTrajetsLabel.setVisible(false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger le planning du conducteur: " + e.getMessage());
        }
    }

    private void addTrajetToContainer(VBox container, TrajetModel trajet) {
        // Créer une carte de trajet selon le modèle de l'interface
        VBox trajetCard = new VBox();
        trajetCard.setStyle("-fx-background-color: rgba(114, 129, 216, 0.5); -fx-background-radius: 10; -fx-padding: 15;");
        trajetCard.setPrefHeight(150);
        trajetCard.setPrefWidth(120); // Augmenté de 102 à 120 pour plus d'espace

        // Créer le conteneur pour les informations de départ et arrivée
        VBox infoContainer = new VBox();
        infoContainer.setPrefHeight(77);
        infoContainer.setPrefWidth(100); // Augmenté de 82 à 100

        // Ajouter le départ
        Label departLabel = new Label("Départ : ");
        departLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D45C9;");

        Label departValue = new Label(trajet.getArretDepart().toString());
        departValue.setStyle("-fx-text-fill: #2D45C9;");
        departValue.setPrefWidth(100); // Définir une largeur suffisante
        departValue.setWrapText(true); // Permettre le retour à la ligne si nécessaire

        // Espace
        VBox spacer = new VBox();
        spacer.setPrefHeight(20); // Réduit de 37 à 20 pour gagner de l'espace
        spacer.setPrefWidth(100);

        // Ajouter l'arrivée
        Label arriveeLabel = new Label("Arrivée : ");
        arriveeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D45C9;");

        Label arriveeValue = new Label(trajet.getArretArrivee().toString());
        arriveeValue.setStyle("-fx-text-fill: #2D45C9;");
        arriveeValue.setPrefWidth(100); // Augmenté de 26 à 100
        arriveeValue.setWrapText(true); // Permettre le retour à la ligne si nécessaire

        // Ajouter tous les éléments à l'infoContainer
        infoContainer.getChildren().addAll(departLabel, departValue, spacer, arriveeLabel, arriveeValue);

        // Créer le conteneur pour les horaires
        HBox timeContainer = new HBox();
        timeContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        timeContainer.setPrefHeight(65);
        timeContainer.setPrefWidth(100); // Augmenté de 72 à 100
        timeContainer.setSpacing(5);

        // Ajouter l'icône d'horloge
        ImageView clockIcon = new ImageView(timeIcon);
        clockIcon.setFitHeight(15);
        clockIcon.setFitWidth(15);

        // Conteneur pour les heures
        VBox hoursContainer = new VBox();
        hoursContainer.setAlignment(javafx.geometry.Pos.CENTER);
        hoursContainer.setPrefHeight(52);
        hoursContainer.setPrefWidth(70); // Augmenté de 32 à 70

        // Formatage des heures
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Heures de départ et d'arrivée
        Label departTimeLabel = new Label(" " + trajet.getHeureDepart().format(timeFormatter));
        departTimeLabel.setStyle("-fx-text-fill: #2D45C9;");
        departTimeLabel.setPrefHeight(17);
        departTimeLabel.setPrefWidth(60); // Augmenté de 44 à 60

        Label arriveeTimeLabel = new Label(trajet.getHeureArrivee().format(timeFormatter));
        arriveeTimeLabel.setStyle("-fx-text-fill: #2D45C9;");
        arriveeTimeLabel.setPrefWidth(60); // Ajout d'une largeur fixe

        hoursContainer.getChildren().addAll(departTimeLabel, arriveeTimeLabel);
        timeContainer.getChildren().addAll(clockIcon, hoursContainer);

        // Assembler la carte
        trajetCard.getChildren().addAll(infoContainer, timeContainer);

        // Ajouter la carte au conteneur du jour
        container.getChildren().add(trajetCard);
    }

    private VBox getDayContainer(int dayIndex) {
        switch (dayIndex) {
            case 0: return lundiContainer;
            case 1: return mardiContainer;
            case 2: return mercrediContainer;
            case 3: return jeudiContainer;
            case 4: return vendrediContainer;
            case 5: return samediContainer;
            case 6: return dimancheContainer;
            default: return lundiContainer;
        }
    }

    private void clearPlanning() {
        lundiContainer.getChildren().clear();
        mardiContainer.getChildren().clear();
        mercrediContainer.getChildren().clear();
        jeudiContainer.getChildren().clear();
        vendrediContainer.getChildren().clear();
        samediContainer.getChildren().clear();
        dimancheContainer.getChildren().clear();
        noTrajetsLabel.setVisible(false);
    }

    @FXML
    private void handleAddTrajet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/irontrail/railtechrh/operateur/TrajetForm.fxml"));
            Parent content = loader.load();

            TrajetController controller = loader.getController();

            if (mainController != null) {
                mainController.getContentContainer().getChildren().setAll(content);
            } else {
                showAlert("Erreur", "Impossible d'accéder au conteneur principal");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de création de trajet: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}