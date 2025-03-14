package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.UtilisateurDAO;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;
import fr.irontrail.railtechrh.model.enums.Specialite;

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

        // Test de l'ajout d'un technicien
        testAddTechnicien(utilisateurDAO);
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
        int utilisateurId = utilisateurDAO.addUser(nouvelUtilisateur);

        if (utilisateurId != -1) {
            System.out.println("Test addUser : Utilisateur ajouté avec succès ! ID : " + utilisateurId);

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

    /**
     * Teste l'ajout d'un technicien dans la table technicien.
     *
     * @param utilisateurDAO L'instance de UtilisateurDAO à tester.
     */
    public static void testAddTechnicien(UtilisateurDAO utilisateurDAO) {
        // Création d'un nouvel utilisateur technicien
        UtilisateurModel nouvelUtilisateur = new UtilisateurModel();
        nouvelUtilisateur.setNom("Dupont");
        nouvelUtilisateur.setPrenom("Jean");
        nouvelUtilisateur.setEmail("jean.dupont@example.com");
        nouvelUtilisateur.setMdp("password123");
        nouvelUtilisateur.setRole(Role.TECHNICIEN);

        // Ajout de l'utilisateur
        int utilisateurId = utilisateurDAO.addUser(nouvelUtilisateur);

        if (utilisateurId != -1) {
            System.out.println("Utilisateur technicien ajouté avec succès ! ID : " + utilisateurId);

            // Ajout de la spécialité du technicien
            boolean ajoutReussi = utilisateurDAO.addTechnicien(utilisateurId, Specialite.ELECTRIQUE);
            if (ajoutReussi) {
                System.out.println("Technicien ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du technicien.");
            }
        } else {
            System.out.println("Échec de l'ajout de l'utilisateur technicien.");
        }
    }
}