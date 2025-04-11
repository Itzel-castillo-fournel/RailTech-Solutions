package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.NotificationModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    /**
     * Crée une nouvelle notification dans la base de données
     * @param titre Le titre de la notification
     * @param utilisateurId L'ID de l'utilisateur concerné
     * @param date La date associée au trajet (pour les modifications de trajets)
     * @return true si l'ajout a réussi, false sinon
     */
    public static boolean creerNotification(String titre, int utilisateurId, LocalDateTime date) {
        String query = "INSERT INTO notification (titre, utilisateur_id, date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, titre);
            pstmt.setInt(2, utilisateurId);
            pstmt.setTimestamp(3, Timestamp.valueOf(date));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crée une nouvelle notification liée à un incident
     * @param titre Le titre de la notification
     * @param utilisateurId L'ID de l'utilisateur concerné
     * @param incidentId L'ID de l'incident concerné
     * @return true si l'ajout a réussi, false sinon
     */
    public static boolean creerNotificationIncident(String titre, int utilisateurId, int incidentId) {
        String query = "INSERT INTO notification (titre, utilisateur_id, incident_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, titre);
            pstmt.setInt(2, utilisateurId);
            pstmt.setInt(3, incidentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère toutes les notifications d'un utilisateur
     * @param utilisateurId L'ID de l'utilisateur
     * @return La liste des notifications de l'utilisateur
     */
    public List<NotificationModel> getNotificationsUtilisateur(int utilisateurId) {
        List<NotificationModel> notifications = new ArrayList<>();
        String query = "SELECT id, titre, utilisateur_id, incident_id, date FROM notification WHERE utilisateur_id = ? AND afficher = TRUE ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, utilisateurId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NotificationModel notification = new NotificationModel();
                    notification.setId(rs.getInt("id"));
                    notification.setTitre(rs.getString("titre"));
                    notification.setUtilisateurId(rs.getInt("utilisateur_id"));

                    // Gérer les valeurs NULL pour incident_id
                    if (rs.getObject("incident_id") != null) {
                        notification.setIncidentId(rs.getInt("incident_id"));
                    }

                    // Gérer les valeurs NULL pour date
                    if (rs.getTimestamp("date") != null) {
                        notification.setDate(rs.getTimestamp("date").toLocalDateTime());
                    }

                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    /**
     * Récupère toutes les notifications pour les techniciens concernant les incidents non traités
     * @return La liste des notifications pour les techniciens
     */
    public List<NotificationModel> getNotificationsTechniciens() {
        List<NotificationModel> notifications = new ArrayList<>();
        String query = "SELECT n.id, n.titre, n.utilisateur_id, n.incident_id, n.date " +
                "FROM notification n " +
                "JOIN incident i ON n.incident_id = i.id " +
                "WHERE i.id NOT IN (SELECT incidentId FROM maintenance) " +
                "AND i.typeIncident IN ('PANNE_TECHNIQUE', 'VOIE_ENDOMMAGEE') " +
                "ORDER BY n.date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NotificationModel notification = new NotificationModel();
                notification.setId(rs.getInt("id"));
                notification.setTitre(rs.getString("titre"));
                notification.setUtilisateurId(rs.getInt("utilisateur_id"));

                if (rs.getObject("incident_id") != null) {
                    notification.setIncidentId(rs.getInt("incident_id"));
                }

                if (rs.getTimestamp("date") != null) {
                    notification.setDate(rs.getTimestamp("date").toLocalDateTime());
                }

                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    /**
     * Vérifie le rôle d'un utilisateur
     * @param utilisateurId L'ID de l'utilisateur
     * @return Le rôle de l'utilisateur
     */
    public Role getRoleUtilisateur(int utilisateurId) {
        String query = "SELECT role FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, utilisateurId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Role.valueOf(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean masquerNotification(int id) {
        String query = "UPDATE notification SET afficher = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}