package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;

public class TestUtilisateurDAO {
    public static void main(String[] args) {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        // Exemple : Récupérer un utilisateur par son email
        UtilisateurModel utilisateur = utilisateurDAO.findByEmail("jean.dupont@example.com");
        if (utilisateur != null) {
            System.out.println("Utilisateur trouvé : " + utilisateur.getNom());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }
}
