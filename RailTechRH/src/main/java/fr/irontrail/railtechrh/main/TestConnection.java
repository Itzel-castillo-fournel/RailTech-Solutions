package fr.irontrail.railtechrh.main;
import fr.irontrail.railtechrh.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String query = "INSERT INTO Utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "Test");
            pstmt.setString(2, "User");
            pstmt.setString(3, "test.user@example.com");
            pstmt.setString(4, "password123");
            pstmt.setString(5, "CONDUCTEUR");

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " utilisateur ajouté");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}