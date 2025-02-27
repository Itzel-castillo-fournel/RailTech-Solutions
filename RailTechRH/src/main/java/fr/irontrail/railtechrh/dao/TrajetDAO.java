package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {
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
    public void createTrajet(String trainImmat, LocalDateTime heureDepart, LocalDateTime heureArrivee,
                             Arret arretDepart, Arret arretArrivee, int conducteurId) throws SQLException {

        String query = "INSERT INTO Trajet (trainImmat, heureDepart, heureArrivee, arretDepart, arretArrivee, conducteurId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, trainImmat);
            pstmt.setTimestamp(2, Timestamp.valueOf(heureDepart));
            pstmt.setTimestamp(3, Timestamp.valueOf(heureArrivee));
            pstmt.setString(4, arretDepart.toString());
            pstmt.setString(5, arretArrivee.toString());
            pstmt.setInt(6, conducteurId);

            pstmt.executeUpdate();
        }
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
}