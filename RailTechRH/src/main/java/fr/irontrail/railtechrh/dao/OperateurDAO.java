package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperateurDAO {

    public List<TrajetModel> getTrajetsProgrammes() throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet WHERE heureDepart >= CURDATE()";

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
        String sql = "SELECT * FROM trajet WHERE DATE(heureDepart) >= ?";
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

}
