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

    public List<TrajetModel> getTrajetsByConducteur(int conducteurId) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();
        String query = "SELECT * FROM Trajet WHERE conducteurId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, conducteurId);

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
}