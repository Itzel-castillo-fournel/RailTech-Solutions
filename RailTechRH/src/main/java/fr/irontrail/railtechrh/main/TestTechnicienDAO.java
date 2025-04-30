package fr.irontrail.railtechrh.main;

import fr.irontrail.railtechrh.dao.TechnicienDAO;
import fr.irontrail.railtechrh.model.IncidentModel;

import java.util.List;

public class TestTechnicienDAO {
    public static void main(String[] args) {
        TechnicienDAO technicienDAO = new TechnicienDAO();





        // Données de test
//        String description = "Maintenance régulière";
//        String probleme = "VOIE_ENDOMMAGEE";
//        String etat = "OPERATIONNEL";
//        int incidentId = 8;
//        Integer technicienId = 2;
//
//        // Appel de la méthode à tester
//        boolean result = technicienDAO.ajouterMaintenance(description, probleme, etat, incidentId, technicienId);
//
//        // Vérification du résultat
//        if (result) {
//            System.out.println("Maintenance ajoutée avec succès.");
//        } else {
//            System.out.println("Échec de l'ajout de la maintenance.");
//        }

////        try {
//            List<IncidentModel> incidents = TechnicienDAO.getIncidents();
//
//            // Afficher les incidents récupérés
//            for (IncidentModel incident : incidents) {
//                System.out.println("Incident ID: " + incident.getId());
//                System.out.println("Description: " + incident.getDescription());
//                System.out.println("Type d'incident: " + incident.getTypeIncident());
//                System.out.println("Gravité: " + incident.getGravite());
//                System.out.println("Immatriculation du train: " + incident.getTrainImmat().getImmatriculation());
//                System.out.println("----------------------------------------");
//            }
//
//            // Vérification simple : vérifiez si des incidents ont été récupérés
//            if (incidents.isEmpty()) {
//                System.out.println("Aucun incident trouvé.");
//            } else {
//                System.out.println("Nombre d'incidents récupérés : " + incidents.size());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Une erreur s'est produite lors de la récupération des incidents.");
//        }
//
   }
}
