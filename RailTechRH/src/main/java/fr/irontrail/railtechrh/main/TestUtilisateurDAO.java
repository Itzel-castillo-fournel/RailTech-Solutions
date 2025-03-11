package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;

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

        // Test de la méthode addUser
        testAddUser(utilisateurDAO);
    }

    /**
     * Teste la méthode addUser de la classe UtilisateurDAO.
     *
     * @param utilisateurDAO L'instance de UtilisateurDAO à tester.
     */
    public static void testAddUser(UtilisateurDAO utilisateurDAO) {
        // Création d'un nouvel utilisateur
        UtilisateurModel nouvelUtilisateur = new UtilisateurModel();
        nouvelUtilisateur.setNom("Martin");
        nouvelUtilisateur.setPrenom("DUPRES");
        nouvelUtilisateur.setEmail("martin.dupres@example.com");
        nouvelUtilisateur.setMdp("mFRJ34@fl9"); // Mot de passe en clair
        nouvelUtilisateur.setRole(Role.CONDUCTEUR);

        // Ajout de l'utilisateur
        boolean result = utilisateurDAO.addUser(nouvelUtilisateur);

        if (result) {
            System.out.println("Test addUser : Utilisateur ajouté avec succès !");

            // Vérification que l'utilisateur a bien été ajouté
            UtilisateurModel utilisateurAjoute = utilisateurDAO.findByEmail("martin.dupres@example.com");
            if (utilisateurAjoute != null) {
                System.out.println("Utilisateur ajouté trouvé : " + utilisateurAjoute.getNom());
            } else {
                System.out.println("Erreur : L'utilisateur ajouté n'a pas été trouvé.");
            }
        } else {
            System.out.println("Test addUser : Échec de l'ajout de l'utilisateur.");
        }
    }
}