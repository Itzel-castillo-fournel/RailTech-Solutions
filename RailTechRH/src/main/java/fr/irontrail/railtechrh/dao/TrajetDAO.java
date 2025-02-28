package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrainModel;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {

    private static final String GET_TRAIN = "SELECT immatriculation FROM Train";
    private static final String GET_TRAIN_BY_IMMAT = "SELECT immatriculation, modele, marque FROM Train WHERE immatriculation = ?";
    private static Connection connection = null;

    public List<TrajetModel> getTrajetsConducteurParDate(int conducteurId, LocalDate date) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String query = "SELECT * FROM Trajet WHERE conducteurId = ? AND DATE(heureDepart) = ? ORDER BY heureDepart";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, conducteurId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(new TrajetModel(
                            rs.getInt("id"),
                            rs.getTimestamp("heureDepart").toLocalDateTime(),
                            rs.getTimestamp("heureArrivee").toLocalDateTime(),
                            Arret.valueOf(rs.getString("arretDepart")),
                            Arret.valueOf(rs.getString("arretArrivee")),
                            rs.getString("trainImmat"),
                            rs.getInt("conducteurId")
                    ));
                }
            }
        }
        return trajets;
    }

    // Méthode pour récupérer tous les trajets (pour l'opérateur)
    public List<TrajetModel> getAllTrajets() throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String query = "SELECT * FROM Trajet ORDER BY heureDepart";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                trajets.add(new TrajetModel(
                        rs.getInt("id"),
                        rs.getTimestamp("heureDepart").toLocalDateTime(),
                        rs.getTimestamp("heureArrivee").toLocalDateTime(),
                        Arret.valueOf(rs.getString("arretDepart")),
                        Arret.valueOf(rs.getString("arretArrivee")),
                        rs.getString("trainImmat"),
                        rs.getInt("conducteurId")
                ));
            }
        }
        return trajets;
    }

    // Méthode pour créer un nouveau trajet
    public static boolean createTrajet(TrajetModel trajetModel) throws SQLException {

        String query = "INSERT INTO Trajet (trainImmat, heureDepart, heureArrivee, arretDepart, arretArrivee) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, trajetModel.getTrainImmat());
            pstmt.setTimestamp(2, Timestamp.valueOf(trajetModel.getHeureDepart()));
            pstmt.setTimestamp(3, Timestamp.valueOf(trajetModel.getHeureArrivee()));
            pstmt.setString(4, trajetModel.getArretDepart().toString());
            pstmt.setString(5, trajetModel.getArretArrivee().toString());

            pstmt.executeUpdate();
        }
        return true;
    }

    // Méthode pour mettre à jour un trajet existant
    public void updateTrajet(int trajetId, String trainImmat, LocalDateTime heureDepart, LocalDateTime heureArrivee,
                             Arret arretDepart, Arret arretArrivee, int conducteurId) throws SQLException {

        String query = "UPDATE Trajet SET trainImmat = ?, heureDepart = ?, heureArrivee = ?, " +
                "arretDepart = ?, arretArrivee = ?, conducteurId = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, trainImmat);
            pstmt.setTimestamp(2, Timestamp.valueOf(heureDepart));
            pstmt.setTimestamp(3, Timestamp.valueOf(heureArrivee));
            pstmt.setString(4, arretDepart.toString());
            pstmt.setString(5, arretArrivee.toString());
            pstmt.setInt(6, conducteurId);
            pstmt.setInt(7, trajetId);

            pstmt.executeUpdate();
        }
    }

    // Méthode pour récupérer un trajet par son ID
    public TrajetModel getTrajetById(int trajetId) throws SQLException {
        String query = "SELECT * FROM Trajet WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, trajetId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TrajetModel(
                            rs.getInt("id"),
                            rs.getTimestamp("heureDepart").toLocalDateTime(),
                            rs.getTimestamp("heureArrivee").toLocalDateTime(),
                            Arret.valueOf(rs.getString("arretDepart")),
                            Arret.valueOf(rs.getString("arretArrivee")),
                            rs.getString("trainImmat"),
                            rs.getInt("conducteurId")
                    );
                }
            }
        }
        return null;
    }

    // Méthode pour supprimer un trajet
    public void deleteTrajet(int trajetId) throws SQLException {
        String query = "DELETE FROM Trajet WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, trajetId);
            pstmt.executeUpdate();
        }
    }

    public ObservableList<String> getTrain() throws SQLException {
        ObservableList<String> train = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TRAIN);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String trainImmat = resultSet.getString("immatriculation");
                train.add(trainImmat);
            }
        } catch (SQLException e) {
            System.out.println("Error getting train : " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection : " + e.getMessage());
            }
        }
        return train;
    }

    public static TrainModel getTrainByImmat(String immat) throws SQLException {

        TrainModel train = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TRAIN_BY_IMMAT);
            statement.setString(1, immat);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                train = new TrainModel();
                train.setImmatriculation(resultSet.getString("immatriculation"));
                train.setMarque(resultSet.getString("marque"));
                train.setModele(resultSet.getString("modele"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting train : " + e.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection : " + e.getMessage());
            }
        }
        return train;
    }
}