package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.TrajetDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestTrajet {
    public static void main(String[] args) {
        TrajetDAO trajetDAO = new TrajetDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try {
            // Test pour le conducteur avec ID = 3 (selon votre jeu de données)
            int conducteurId = 3;
            LocalDate date = LocalDate.now();

            System.out.println("=== Test récupération des trajets ===");
            System.out.println("Conducteur ID: " + conducteurId);
            System.out.println("Date: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------------------------");

            List<TrajetModel> trajets = trajetDAO.getTrajetsConducteurParDate(conducteurId, date);

            if (trajets.isEmpty()) {
                System.out.println("Aucun trajet trouvé pour cette date.");
            } else {
                for (TrajetModel trajet : trajets) {
                    System.out.println("\nTrajet n°" + trajet.getId());
                    System.out.println("TGV: " + trajet.getTrainImmat());
                    System.out.println("Départ: " + trajet.getArretDepart() +
                            " à " + trajet.getHeureDepart().format(formatter));
                    System.out.println("Arrivée: " + trajet.getArretArrivee() +
                            " à " + trajet.getHeureArrivee().format(formatter));
                    System.out.println("----------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des trajets:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}