package fr.irontrail.railtechrh.dao;

public class UtilisateurDAO {
    private static final String INSERT_USER = "INSERT INTO Utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_USER = "SELECT id, nom, prenom, email, role FROM Utilisateur WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE Utilisateur SET nom = ?, prenom = ?, email = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM Utilisateur WHERE id = ?";
    private static final String CHECK_LOGIN = "SELECT id, nom, prenom, role FROM Utilisateur WHERE email = ? AND mdp = ?";
}
