package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
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
import java.util.List;

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

        String query = "INSERT INTO maintenance (dateMaintenance, description, probleme, etat, incidentId, technicienId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, dateMaintenance);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, probleme);
            preparedStatement.setString(4, etat);
            preparedStatement.setInt(5, incidentId);
            preparedStatement.setInt(6, technicienId);

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

}
