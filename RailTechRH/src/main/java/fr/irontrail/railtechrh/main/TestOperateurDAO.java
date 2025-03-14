package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.IncidentDAO;
import fr.irontrail.railtechrh.dao.OperateurDAO;
import fr.irontrail.railtechrh.model.MaintenanceModel;
import fr.irontrail.railtechrh.model.TrajetModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestOperateurDAO {
    public static void main(String[] args) {
        OperateurDAO operateurDAO = new OperateurDAO();

        try {
            // Récupérer les trajets programmés
            List<TrajetModel> trajets = operateurDAO.getTrajetsProgrammes();
            if (!trajets.isEmpty()) {
                System.out.println("Trajets programmés trouvés : ");
                for (TrajetModel trajet : trajets) {
                    System.out.println(trajet);
                }
            } else {
                System.out.println("Aucun trajet programmé trouvé.");
            }

            // Tester la méthode getMaintenanceDetails
            List<MaintenanceModel> maintenanceDetailsList = operateurDAO.getMaintenanceDetails();
            if (!maintenanceDetailsList.isEmpty()) {
                System.out.println("Détails de maintenance trouvés : ");
                for (MaintenanceModel details : maintenanceDetailsList) {
                    System.out.println("État : " + details.getEtatMaintenance());
                    System.out.println("Numéro d'immatriculation du train : " + details.getNumeroImmatriculationTrain());
                    System.out.println("Description : " + details.getDescriptionMaintenance());
                    System.out.println("Nom du technicien : " + details.getNomTechnicien());
                    System.out.println("Prénom du technicien : " + details.getPrenomTechnicien());
                    System.out.println("Dernière mise à jour : " + details.getDerniereMiseAJour());
                    System.out.println("----------------------------------------");
                }
            } else {
                System.out.println("Aucun détail de maintenance trouvé.");
            }

            // Tester la méthode getMaintenancePercentages
            Map<String, Integer> percentages = operateurDAO.getMaintenancePercentages();
            System.out.println("Pourcentages des états de maintenance : ");
            for (Map.Entry<String, Integer> entry : percentages.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + "%");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des données : " + e.getMessage());
        }
    }
}
