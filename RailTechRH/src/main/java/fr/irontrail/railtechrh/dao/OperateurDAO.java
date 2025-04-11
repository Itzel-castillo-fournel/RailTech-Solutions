package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.MaintenanceModel;
import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperateurDAO {

    public List<TrajetModel> getTrajetsProgrammes() throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String sql = "SELECT id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainImmat, conducteurId FROM trajet WHERE heureDepart >= CURDATE()";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                TrajetModel trajet = new TrajetModel();
                trajet.setId(resultSet.getInt("id"));
                trajet.setHeureDepart(resultSet.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setHeureArrivee(resultSet.getTimestamp("heureArrivee").toLocalDateTime());
                trajet.setArretDepart(Arret.valueOf(resultSet.getString("arretDepart")));
                trajet.setArretArrivee(Arret.valueOf(resultSet.getString("arretArrivee")));
                trajet.setTrainImmat(resultSet.getString("trainImmat"));
                trajet.setConducteurId(resultSet.getInt("conducteurId"));
                trajets.add(trajet);
            }
        }
        return trajets;
    }

    //Récupérer les détails du train
    public TrainModel getTrainDetails(String trainImmat) throws SQLException {
        TrainModel train = null;
        String sql = "SELECT immatriculation, marque, modele FROM train WHERE immatriculation = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trainImmat);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    train = new TrainModel();
                    train.setImmatriculation(resultSet.getString("immatriculation"));
                    train.setMarque(resultSet.getString("marque"));
                    train.setModele(resultSet.getString("modele"));
                }
            }
        }
        return train;
    }

    public String getConducteurName(int conducteurId) throws SQLException {
        String sql = "SELECT nom FROM utilisateur WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, conducteurId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nom");
                }
            }
        }
        return "Conducteur Inconnu";
    }

    public String getConducteurPrenom(int conducteurId) throws SQLException {
        String sql = "SELECT prenom FROM utilisateur WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, conducteurId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prenom");
                }
            }
        }
        return "Conducteur Inconnu";
    }

    public List<TrajetModel> getTrajetsByDate(LocalDate date) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String sql = "SELECT id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainImmat, conducteurId FROM trajet WHERE DATE(heureDepart) >= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TrajetModel trajet = new TrajetModel();
                trajet.setId(rs.getInt("id"));
                trajet.setHeureDepart(rs.getTimestamp("heureDepart").toLocalDateTime());
                trajet.setHeureArrivee(rs.getTimestamp("heureArrivee").toLocalDateTime());
                trajet.setArretDepart(Arret.valueOf(rs.getString("arretDepart")));
                trajet.setArretArrivee(Arret.valueOf(rs.getString("arretArrivee")));
                trajet.setTrainImmat(rs.getString("trainImmat"));
                trajet.setConducteurId(rs.getInt("conducteurId"));

                // Ajoutez ce log pour vérifier les valeurs récupérées
                System.out.println("Trajet récupéré: " + trajet);

                trajets.add(trajet);
            }
        }
        return trajets;
    }

    public List<MaintenanceModel> getMaintenanceDetails() throws SQLException {
        List<MaintenanceModel> maintenanceDetailsList = new ArrayList<>();
        String sql = "" +
                "SELECT " +
                "    m.etat AS etat_maintenance, " +
                "    i.trainImmat AS numero_immatriculation_train, " +
                "    m.description AS description_maintenance, " +
                "    u.nom AS nom_technicien, " +
                "    u.prenom AS prenom_technicien, " +
                "    m.dateMaintenance AS derniere_mise_a_jour " +
                "FROM " +
                "    maintenance m " +
                "JOIN " +
                "    incident i ON m.incidentId = i.id " +
                "LEFT JOIN " +
                "    utilisateur u ON m.technicienId = u.id " +
                "ORDER BY " +
                "    m.dateMaintenance DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MaintenanceModel details = new MaintenanceModel();
                details.setEtatMaintenance(resultSet.getString("etat_maintenance"));
                details.setNumeroImmatriculationTrain(resultSet.getString("numero_immatriculation_train"));
                details.setDescriptionMaintenance(resultSet.getString("description_maintenance"));
                details.setNomTechnicien(resultSet.getString("nom_technicien"));
                details.setPrenomTechnicien(resultSet.getString("prenom_technicien"));
                details.setDerniereMiseAJour(resultSet.getTimestamp("derniere_mise_a_jour").toLocalDateTime());
                maintenanceDetailsList.add(details);
            }
        }
        return maintenanceDetailsList;
    }

    public Map<String, Integer> getMaintenancePercentages() throws SQLException {
        List<MaintenanceModel> maintenanceDetailsList = getMaintenanceDetails();
        int totalMaintenances = maintenanceDetailsList.size();

        if (totalMaintenances == 0) {
            return new HashMap<>(); // Return an empty map if there are no maintenances
        }

        int panneCount = 0;
        int operationnelCount = 0;
        int maintenanceCount = 0;

        for (MaintenanceModel maintenance : maintenanceDetailsList) {
            switch (maintenance.getEtatMaintenance()) {
                case "PANNE":
                    panneCount++;
                    break;
                case "OPERATIONNEL":
                    operationnelCount++;
                    break;
                case "MAINTENANCE":
                    maintenanceCount++;
                    break;
                default:
                    break;
            }
        }

        Map<String, Integer> percentages = new HashMap<>();
        percentages.put("PANNE", (int) Math.round((double) panneCount / totalMaintenances * 100));
        percentages.put("OPÉRATIONNEL", (int) Math.round((double) operationnelCount / totalMaintenances * 100));
        percentages.put("MAINTENANCE", (int) Math.round((double) maintenanceCount / totalMaintenances * 100));

        return percentages;
    }

    public boolean updateTrajet(int trajetId, int conducteurId) throws SQLException {
        String sql = "UPDATE trajet SET conducteurId = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, conducteurId);
            stmt.setInt(2, trajetId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
        return false;
    }

    public boolean assignerConducteur(int trajetId, int conducteurId) throws SQLException {
        // Récupérer d'abord le trajet pour avoir l'ancien conducteur
        TrajetDAO trajetDAO = new TrajetDAO();
        TrajetModel trajet = trajetDAO.getTrajetById(trajetId);

        if (trajet == null) {
            return false;
        }

        // Sauvegarder l'ID de l'ancien conducteur
        int ancienConducteurId = trajet.getConducteurId();

        // Mettre à jour le trajet avec le nouveau conducteur
        boolean success = updateTrajet(trajetId, conducteurId);

        if (success) {
            // Si un ancien conducteur était assigné, lui envoyer une notification de désassignation
            if (ancienConducteurId > 0) {
                String titreAncien = "Trajet retiré: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
                NotificationDAO.creerNotification(titreAncien, ancienConducteurId, trajet.getHeureDepart());
            }

            // Si un nouveau conducteur est assigné, lui envoyer une notification
            if (conducteurId > 0) {
                String titreNouveau = "Nouveau trajet assigné: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
                NotificationDAO.creerNotification(titreNouveau, conducteurId, trajet.getHeureDepart());
            }
        }

        return success;
    }

}
