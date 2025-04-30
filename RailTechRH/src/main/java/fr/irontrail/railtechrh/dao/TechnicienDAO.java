package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.*;
import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.Role;
import fr.irontrail.railtechrh.model.enums.TypeIncident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechnicienDAO {

    // Cette méthode permet de récupérer les utilisateurs qui ont un rôle technicien.
    public List<UtilisateurModel> getTechniciens() {
        List<UtilisateurModel> techniciens = new ArrayList<>();
        String query = "SELECT id, nom, prenom, email FROM utilisateur WHERE role = 'TECHNICIEN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");

                UtilisateurModel technicien = new UtilisateurModel();
                technicien.setId(id);
                technicien.setNom(nom);
                technicien.setPrenom(prenom);
                technicien.setEmail(email);
                technicien.setRole(Role.TECHNICIEN);

                techniciens.add(technicien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return techniciens;
    }

    // Cette méthode permet d'ajouter une maintenance.
    public boolean ajouterMaintenance(String description, String probleme, String etat, int incidentId, Integer technicienId) {
        // On ajoute la date du jour de la maintenance
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateMaintenance = now.format(formatter);

        String query = "INSERT INTO maintenance (dateMaintenance, description, etat, incidentId, technicienId) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, dateMaintenance);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, etat);
            preparedStatement.setInt(4, incidentId);
            preparedStatement.setInt(5, technicienId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Cette méthode permet de récupérer les incidents de type panne technique ou voie endommagée et qui ne sont pas traités en maintenance.
    public static List<IncidentModel> getIncidents() {
        List<IncidentModel> incidents = new ArrayList<>();
        String query = "SELECT id, description, typeIncident, gravite, trainImmat\n" +
                "FROM incident\n" +
                "WHERE typeIncident IN ('PANNE_TECHNIQUE', 'VOIE_ENDOMMAGEE')\n" +
                "AND id NOT IN (SELECT incidentId FROM maintenance);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                TypeIncident typeIncident = TypeIncident.valueOf(resultSet.getString("typeIncident"));
                Gravite gravite = Gravite.valueOf(resultSet.getString("gravite"));
                TrainModel trainImmat = new TrainModel(); // Assurez-vous de définir correctement cet objet
                trainImmat.setImmatriculation(resultSet.getString("trainImmat"));

                IncidentModel incident = new IncidentModel(id, description, typeIncident, gravite, trainImmat);
                incidents.add(incident);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incidents;
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
                "    m.dateMaintenance AS derniere_mise_a_jour, " +
                "    m.incidentId AS incident_id " +
                "FROM " +
                "    maintenance m " +
                "JOIN " +
                "    incident i ON m.incidentId = i.id " +
                "JOIN " +
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
                details.setIncidentId(resultSet.getInt("incident_id"));
                details.setListCommentaire(getCommentairesByMaintenanceId(details.getIncidentId()));
                maintenanceDetailsList.add(details);
            }
        }
        return maintenanceDetailsList;
    }

    public List<CommentaireModel> getCommentairesByMaintenanceId(int incidentId) throws SQLException {
        List<CommentaireModel> commentaireList = new ArrayList<>();
        String query = "SELECT " +
                        "   c.commentaire, " +
                        "   c.date, " +
                        "   u.id AS technicien_id, " +
                        "   u.nom, " +
                        "   u.prenom " +
                        "FROM " +
                        "   commentaire c " +
                        "JOIN " +
                        "   utilisateur u ON c.id_technicien = u.id " +
                        "WHERE " +
                        "   c.id_maintenance= ? " +
                        "ORDER BY " +
                        "   c.date DESC";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, incidentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    UtilisateurModel utilisateur = new UtilisateurModel();
                    utilisateur.setId(resultSet.getInt("technicien_id"));
                    utilisateur.setNom(resultSet.getString("nom"));
                    utilisateur.setPrenom(resultSet.getString("prenom"));

                    LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                    String description = resultSet.getString("commentaire");

                    CommentaireModel commentaire = new CommentaireModel(utilisateur, date, description);

                    commentaireList.add(commentaire);

                }
            }
        }

        return commentaireList;
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

    public boolean modifierMaintenance(String description, String etat, int incidentId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateMaintenance = now.format(formatter);

        String query = "UPDATE maintenance SET dateMaintenance = ?, description = ?, etat = ? WHERE incidentId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, dateMaintenance);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, etat);
            preparedStatement.setInt(4, incidentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void nouveauCommentaire(String description, int incidentId, int id) {
        String query = "INSERT INTO commentaire (id_maintenance, id_technicien, commentaire) VALUES (?, ?, ?)";;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, incidentId);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, description);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


}
