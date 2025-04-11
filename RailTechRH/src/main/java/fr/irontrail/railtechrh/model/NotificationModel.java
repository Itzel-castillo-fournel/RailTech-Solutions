package fr.irontrail.railtechrh.model;

import java.time.LocalDateTime;

public class NotificationModel {
    private int id;
    private String titre;
    private int utilisateurId;
    private int incidentId;
    private LocalDateTime date;

    // Constructeur par défaut
    public NotificationModel() {}

    // Constructeur avec paramètres
    public NotificationModel(int id, String titre, int utilisateurId, int incidentId, LocalDateTime date) {
        this.id = id;
        this.titre = titre;
        this.utilisateurId = utilisateurId;
        this.incidentId = incidentId;
        this.date = date;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", utilisateurId=" + utilisateurId +
                ", incidentId=" + incidentId +
                ", date=" + date +
                '}';
    }
}