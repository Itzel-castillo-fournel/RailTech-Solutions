package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.enums.Arret;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestTrajet {
    public static void main(String[] args) {
        TrajetDAO trajetDAO = new TrajetDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int conducteurId = 3;
        LocalDate date = LocalDate.now();

        System.out.println("=== Test récupération des trajets ===");
        System.out.println("Conducteur ID: " + conducteurId);
        System.out.println("Date: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("----------------------------------------");

        try {
            // Test 1: Récupération des trajets par conducteur et date
            List<TrajetModel> trajets = trajetDAO.getTrajetsConducteurParDate(conducteurId, date);
            System.out.println("Nombre de trajets pour aujourd'hui: " + trajets.size());
            for (TrajetModel trajet : trajets) {
                System.out.println("\nTrajet n°" + trajet.getId());
                System.out.println("Train: " + trajet.getTrainImmat());
                System.out.println("Départ: " + trajet.getArretDepart() + " à " +
                        trajet.getHeureDepart().format(formatter));
                System.out.println("Arrivée: " + trajet.getArretArrivee() + " à " +
                        trajet.getHeureArrivee().format(formatter));
                System.out.println("----------------------------------------");
            }

            // Test 2: Création d'un trajet
            System.out.println("\n=== Test création d'un trajet ===");
            LocalDateTime departDateTime = LocalDateTime.now().plusHours(1);
            LocalDateTime arriveeDateTime = departDateTime.plusHours(2);



            // Test 3: Récupération d'un trajet par ID
            System.out.println("\n=== Test récupération d'un trajet par ID ===");
            TrajetModel trajet = trajetDAO.getTrajetById(1);
            if (trajet != null) {
                System.out.println("Trajet trouvé - ID: " + trajet.getId());
                System.out.println("Train: " + trajet.getTrainImmat());
                System.out.println("Départ: " + trajet.getArretDepart() + " à " +
                        trajet.getHeureDepart().format(formatter));
                System.out.println("Arrivée: " + trajet.getArretArrivee() + " à " +
                        trajet.getHeureArrivee().format(formatter));

                // Test 4: Modification d'un trajet
                System.out.println("\n=== Test modification d'un trajet ===");
                trajetDAO.updateTrajet(
                        trajet.getId(),
                        trajet.getTrainImmat(),
                        trajet.getHeureDepart().plusHours(1),
                        trajet.getHeureArrivee().plusHours(1),
                        trajet.getArretDepart(),
                        trajet.getArretArrivee(),
                        trajet.getConducteurId()
                );
                System.out.println("Trajet modifié avec succès!");

                TrajetModel trajetModifie = trajetDAO.getTrajetById(trajet.getId());
                System.out.println("Nouvelle heure de départ: " +
                        trajetModifie.getHeureDepart().format(formatter));
                System.out.println("Nouvelle heure d'arrivée: " +
                        trajetModifie.getHeureArrivee().format(formatter));
            } else {
                System.out.println("Aucun trajet trouvé avec l'ID 1");
            }

            // Test 5: Récupération de tous les trajets
            System.out.println("\n=== Test récupération de tous les trajets ===");
            List<TrajetModel> allTrajets = trajetDAO.getAllTrajets();
            System.out.println("Nombre total de trajets dans la base: " + allTrajets.size());

        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}