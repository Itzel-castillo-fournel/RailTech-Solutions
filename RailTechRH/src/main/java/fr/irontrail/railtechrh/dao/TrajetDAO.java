package fr.irontrail.railtechrh.dao;

import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrajetDAO {
    // Requêtes SQL
    private static final String GET_TRAJETS_CONDUCTEUR_PAR_DATE =
            "SELECT id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainImmat " +
                    "FROM Trajet " +
                    "WHERE conducteurId = ? AND DATE(heureDepart) = ? " +
                    "ORDER BY heureDepart";

    private static final String GET_TRAJETS_CONDUCTEUR =
            "SELECT id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainImmat " +
                    "FROM Trajet " +
                    "WHERE conducteurId = ? " +
                    "AND heureDepart >= CURRENT_DATE() " +
                    "ORDER BY heureDepart";

    private static final String GET_PROCHAIN_TRAJET_CONDUCTEUR =
            "SELECT id, heureDepart, heureArrivee, arretDepart, arretArrivee, trainImmat " +
                    "FROM Trajet " +
                    "WHERE conducteurId = ? " +
                    "AND heureDepart > NOW() " +
                    "ORDER BY heureDepart LIMIT 1";

    private static final String VERIFIER_DISPONIBILITE_CONDUCTEUR =
            "SELECT COUNT(*) FROM Trajet " +
                    "WHERE conducteurId = ? " +
                    "AND ((heureDepart BETWEEN ? AND ?) " +
                    "OR (heureArrivee BETWEEN ? AND ?))";

    /**
     * Récupère tous les trajets d'un conducteur pour une date donnée
     */
    public List<TrajetModel> getTrajetsConducteurParDate(int conducteurId, LocalDate date) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_TRAJETS_CONDUCTEUR_PAR_DATE)) {

            pstmt.setInt(1, conducteurId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(mapResultSetToTrajet(rs, conducteurId));
                }
            }
        }
        return trajets;
    }

    /**
     * Récupère tous les trajets à venir d'un conducteur
     */
    public List<TrajetModel> getTrajetsConducteur(int conducteurId) throws SQLException {
        List<TrajetModel> trajets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_TRAJETS_CONDUCTEUR)) {

            pstmt.setInt(1, conducteurId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trajets.add(mapResultSetToTrajet(rs, conducteurId));
                }
            }
        }
        return trajets;
    }

    /**
     * Récupère le prochain trajet d'un conducteur
     */
    public TrajetModel getProchainTrajet(int conducteurId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_PROCHAIN_TRAJET_CONDUCTEUR)) {

            pstmt.setInt(1, conducteurId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTrajet(rs, conducteurId);
                }
            }
        }
        return null;
    }

    /**
     * Vérifie si un conducteur est disponible pour un créneau
     */
    public boolean estConducteurDisponible(int conducteurId, LocalDateTime debut, LocalDateTime fin)
            throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(VERIFIER_DISPONIBILITE_CONDUCTEUR)) {

            pstmt.setInt(1, conducteurId);
            pstmt.setTimestamp(2, Timestamp.valueOf(debut));
            pstmt.setTimestamp(3, Timestamp.valueOf(fin));
            pstmt.setTimestamp(4, Timestamp.valueOf(debut));
            pstmt.setTimestamp(5, Timestamp.valueOf(fin));

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        }
    }

    /**
     * Convertit un ResultSet en objet TrajetModel
     */
    private TrajetModel mapResultSetToTrajet(ResultSet rs, int conducteurId) throws SQLException {
        return new TrajetModel(
                rs.getInt("id"),
                rs.getTimestamp("heureDepart").toLocalDateTime(),
                rs.getTimestamp("heureArrivee").toLocalDateTime(),
                Arret.valueOf(rs.getString("arretDepart")),
                Arret.valueOf(rs.getString("arretArrivee")),
                rs.getString("trainImmat"),
                conducteurId
        );
    }
}