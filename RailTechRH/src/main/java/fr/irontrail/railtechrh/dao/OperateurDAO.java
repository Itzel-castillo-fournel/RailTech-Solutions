package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
