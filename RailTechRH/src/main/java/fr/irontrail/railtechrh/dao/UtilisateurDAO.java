package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    private static final String INSERT_USER = "INSERT INTO Utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_USER = "SELECT id, nom, prenom, email, role FROM Utilisateur WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM Utilisateur WHERE id = ?";
    private static final String CHECK_LOGIN = "SELECT id, nom, prenom, role FROM Utilisateur WHERE email = ? AND mdp = ?";

    public UtilisateurModel findByEmail(String email) {
        // Connexion à la base de données
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, nom, prenom, email, mdp, role FROM utilisateur WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UtilisateurModel utilisateur = new UtilisateurModel();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setPrenom(resultSet.getString("prenom"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setMdp(resultSet.getString("mdp"));
                utilisateur.setRole(Role.valueOf(resultSet.getString("role")));
                return utilisateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Aucun utilisateur trouvé
    }

    public int getNombreTrajetsCeMois(int userId) {
        int nombreTrajets = 0;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) AS nombre_trajets FROM trajet WHERE conducteurId = ? AND MONTH(heureDepart) = MONTH(CURRENT_DATE()) AND YEAR(heureDepart) = YEAR(CURRENT_DATE())";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nombreTrajets = resultSet.getInt("nombre_trajets");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreTrajets;
    }

    // Méthode pour obtenir le nombre d'heures travaillées dans le mois
    public int getNombreHeuresTravailleesCeMois(int utilisateurId) {
        String query = "SELECT heureDepart, heureArrivee " +
                "FROM trajet " +
                "WHERE conducteurId = ? " +
                "AND MONTH(heureDepart) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(heureDepart) = YEAR(CURRENT_DATE())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, utilisateurId);
            ResultSet resultSet = statement.executeQuery();

            long totalHeures = 0;

            while (resultSet.next()) {
                LocalDateTime heureDepart = resultSet.getTimestamp("heureDepart").toLocalDateTime();
                LocalDateTime heureArrivee = resultSet.getTimestamp("heureArrivee").toLocalDateTime();

                long heuresTravaillees = ChronoUnit.HOURS.between(heureDepart, heureArrivee);
                totalHeures += heuresTravaillees;
            }

            return (int) totalHeures;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Méthode pour obtenir la spécialité d'un technicien
    public String getSpecialiteTechnicien(int technicienId) {
        String query = "SELECT specialite FROM technicien WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, technicienId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("specialite");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retourne null si aucune spécialité n'est trouvée
    }

    public List<UtilisateurModel> getAllConducteurs() throws SQLException {
        List<UtilisateurModel> conducteurs = new ArrayList<>();
        String query = "SELECT id, nom, prenom, email FROM utilisateur WHERE role = 'CONDUCTEUR' ORDER BY nom, prenom";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UtilisateurModel conducteur = new UtilisateurModel();
                conducteur.setId(resultSet.getInt("id"));
                conducteur.setNom(resultSet.getString("nom"));
                conducteur.setPrenom(resultSet.getString("prenom"));
                conducteur.setEmail(resultSet.getString("email"));
                conducteur.setRole(Role.valueOf("CONDUCTEUR"));
                conducteurs.add(conducteur);
            }
        }

        return conducteurs;
    }
}