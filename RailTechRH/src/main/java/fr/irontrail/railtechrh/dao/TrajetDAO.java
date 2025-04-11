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

    private static final String GET_TRAIN = "SELECT immatriculation FROM train";
    private static final String GET_TRAIN_BY_IMMAT = "SELECT immatriculation, modele, marque FROM Train WHERE immatriculation = ?";
    private static Connection connection = null;

    public List<TrajetModel> getTrajetsConducteurParDate(int conducteurId, LocalDate date) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String query = "SELECT * FROM trajet WHERE conducteurId = ? AND DATE(heureDepart) = ? ORDER BY heureDepart";

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

    // Méthode pour récupérer tous les trajets
    public List<TrajetModel> getAllTrajets() throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String query = "SELECT * FROM trajet ORDER BY heureDepart";

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

    public static boolean createTrajet(TrajetModel trajetModel) throws SQLException {
        String query = "INSERT INTO trajet (trainImmat, heureDepart, heureArrivee, arretDepart, arretArrivee) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, trajetModel.getTrainImmat());
            pstmt.setTimestamp(2, Timestamp.valueOf(trajetModel.getHeureDepart()));
            pstmt.setTimestamp(3, Timestamp.valueOf(trajetModel.getHeureArrivee()));
            pstmt.setString(4, trajetModel.getArretDepart().toString());
            pstmt.setString(5, trajetModel.getArretArrivee().toString());

            // Gérer conducteurId qui peut être nul
            if (trajetModel.getConducteurId() > 0) {
                pstmt.setInt(6, trajetModel.getConducteurId());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        trajetModel.setId(generatedKeys.getInt(1));

                        // Ajouter la notification si un conducteur est assigné
                        if (trajetModel.getConducteurId() > 0) {
                            String titre = "Nouveau trajet assigné: " + trajetModel.getArretDepart() + " → " + trajetModel.getArretArrivee();
                            NotificationDAO.creerNotification(titre, trajetModel.getConducteurId(), trajetModel.getHeureDepart());
                        }
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour mettre à jour un trajet existant
    public boolean updateTrajet(int trajetId, String trainImmat, LocalDateTime heureDepart, LocalDateTime heureArrivee,
                                Arret arretDepart, Arret arretArrivee, int conducteurId) throws SQLException {

        // Récupérer l'ancien trajet pour comparer les conducteurs
        TrajetModel ancienTrajet = getTrajetById(trajetId);
        boolean changementConducteur = (ancienTrajet != null && ancienTrajet.getConducteurId() != conducteurId);

        String query = "UPDATE trajet SET trainImmat = ?, heureDepart = ?, heureArrivee = ?, " +
                "arretDepart = ?, arretArrivee = ?, conducteurId = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, trainImmat);
            pstmt.setTimestamp(2, Timestamp.valueOf(heureDepart));
            pstmt.setTimestamp(3, Timestamp.valueOf(heureArrivee));
            pstmt.setString(4, arretDepart.toString());
            pstmt.setString(5, arretArrivee.toString());

            // Gérer conducteurId qui peut être nul
            if (conducteurId > 0) {
                pstmt.setInt(6, conducteurId);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            pstmt.setInt(7, trajetId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                if (changementConducteur && conducteurId > 0) {
                    String titre = "Nouveau trajet assigné: " + arretDepart + " → " + arretArrivee;
                    NotificationDAO.creerNotification(titre, conducteurId, heureDepart);
                }
                else if (conducteurId > 0) {
                    String titre = "Trajet modifié: " + arretDepart + " → " + arretArrivee;
                    NotificationDAO.creerNotification(titre, conducteurId, heureDepart);
                }

                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour récupérer un trajet par son ID
    public TrajetModel getTrajetById(int trajetId) throws SQLException {
        String query = "SELECT * FROM trajet WHERE id = ?";

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