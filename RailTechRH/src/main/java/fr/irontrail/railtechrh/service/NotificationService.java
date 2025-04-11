package fr.irontrail.railtechrh.service;

import fr.irontrail.railtechrh.dao.NotificationDAO;
import fr.irontrail.railtechrh.model.TrajetModel;
import fr.irontrail.railtechrh.model.IncidentModel;
import fr.irontrail.railtechrh.model.UtilisateurModel;
import fr.irontrail.railtechrh.model.enums.Role;
import fr.irontrail.railtechrh.model.enums.TypeIncident;
import fr.irontrail.railtechrh.dao.UtilisateurDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour gérer les notifications du système
 * Cette classe contient des méthodes statiques qui peuvent être appelées
 * depuis les différents contrôleurs sans avoir à créer d'instance
 */
public class NotificationService {

    /**
     * Notifie un conducteur qu'un trajet a été créé
     * @param trajet Le trajet créé
     */
    public static void notifierCreationTrajet(TrajetModel trajet) {
        if (trajet.getConducteurId() > 0) {
            String titre = "Nouveau trajet assigné: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
            NotificationDAO.creerNotification(titre, trajet.getConducteurId(), trajet.getHeureDepart());
        }
    }

    /**
     * Notifie un conducteur que son trajet a été modifié
     * @param trajet Le trajet modifié
     * @param ancienTrajet L'ancien trajet (avant modification)
     */
    public static void notifierModificationTrajet(TrajetModel trajet, TrajetModel ancienTrajet) {
        // Si le conducteur a changé, notifier le nouveau conducteur
        if (ancienTrajet != null && ancienTrajet.getConducteurId() != trajet.getConducteurId()) {
            if (trajet.getConducteurId() > 0) {
                String titre = "Nouveau trajet assigné: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
                NotificationDAO.creerNotification(titre, trajet.getConducteurId(), trajet.getHeureDepart());
            }
        }
        // Si c'est le même conducteur, l'informer de la modification
        else if (trajet.getConducteurId() > 0) {
            String titre = "Trajet modifié: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
            NotificationDAO.creerNotification(titre, trajet.getConducteurId(), trajet.getHeureDepart());
        }
    }

    /**
     * Notifie un conducteur que son trajet a été supprimé
     * @param trajet Le trajet supprimé
     */
    public static void notifierSuppressionTrajet(TrajetModel trajet) {
        if (trajet != null && trajet.getConducteurId() > 0) {
            String titre = "Trajet annulé: " + trajet.getArretDepart() + " → " + trajet.getArretArrivee();
            NotificationDAO.creerNotification(titre, trajet.getConducteurId(), trajet.getHeureDepart());
        }
    }

    /**
     * Notifie les opérateurs et techniciens concernés par un incident
     * @param incident L'incident signalé
     */
    public static void notifierIncident(IncidentModel incident) {
        try {
            // Récupérer les utilisateurs concernés
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

            // Construire le titre de la notification
            String graviteStr = incident.getGravite().toString();
            String typeStr = incident.getTypeIncident().toString().replace("_", " ");
            String titre = typeStr + " - " + graviteStr + " - " + incident.getTrainImmat().getImmatriculation();

            // Notifier les opérateurs (tous les opérateurs reçoivent les notifications d'incidents)
            List<UtilisateurModel> operateurs = utilisateurDAO.getUtilisateursByRole(Role.OPERATEUR);
            for (UtilisateurModel operateur : operateurs) {
                NotificationDAO.creerNotificationIncident(titre, operateur.getId(), incident.getId());
            }

            // Si c'est une panne technique ou une voie endommagée, notifier aussi les techniciens
            if (incident.getTypeIncident() == TypeIncident.PANNE_TECHNIQUE ||
                    incident.getTypeIncident() == TypeIncident.VOIE_ENDOMMAGEE) {

                List<UtilisateurModel> techniciens = utilisateurDAO.getUtilisateursByRole(Role.TECHNICIEN);
                for (UtilisateurModel technicien : techniciens) {
                    NotificationDAO.creerNotificationIncident(titre, technicien.getId(), incident.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Notifie un conducteur d'une assignation à un trajet (pour les assignations de trajets existants)
     * @param trajetId L'ID du trajet
     * @param conducteurId L'ID du conducteur
     * @param heureDepart L'heure de départ du trajet
     * @param arretDepart L'arrêt de départ
     * @param arretArrivee L'arrêt d'arrivée
     */
    public static void notifierAssignationTrajet(int trajetId, int conducteurId,
                                                 LocalDateTime heureDepart,
                                                 String arretDepart, String arretArrivee) {
        if (conducteurId > 0) {
            String titre = "Vous avez été assigné au trajet " + trajetId + ": " + arretDepart + " → " + arretArrivee;
            NotificationDAO.creerNotification(titre, conducteurId, heureDepart);
        }
    }
}