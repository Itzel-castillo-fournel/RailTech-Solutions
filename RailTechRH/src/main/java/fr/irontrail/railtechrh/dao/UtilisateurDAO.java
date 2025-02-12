package fr.irontrail.railtechrh.dao;

//public class UtilisateurDAO {
//    private static final String INSERT_USER = "INSERT INTO Utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
//    private static final String GET_USER = "SELECT id, nom, prenom, email, role FROM Utilisateur WHERE id = ?";
//    private static final String UPDATE_USER = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ? WHERE id = ?";
//    private static final String DELETE_USER = "DELETE FROM Utilisateur WHERE id = ?";
//}

import fr.irontrail.railtechrh.model.UtilisateurModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {
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
                utilisateur.setRole(resultSet.getString("role"));
                return utilisateur;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Aucun utilisateur trouvé
    }
}