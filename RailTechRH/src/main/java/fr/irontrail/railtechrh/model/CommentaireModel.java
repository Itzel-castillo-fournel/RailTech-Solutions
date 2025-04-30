package fr.irontrail.railtechrh.model;

import java.time.LocalDateTime;

public class CommentaireModel {
    private UtilisateurModel technicienMaintenance;
    private LocalDateTime date;
    private String commentaire;


    public CommentaireModel(UtilisateurModel technicienMaintenance, LocalDateTime date, String commentaire) {
        this.technicienMaintenance = technicienMaintenance;
        this.date = date;
        this.commentaire = commentaire;
    }

    public UtilisateurModel getTechnicienMaintenance() {
        return technicienMaintenance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setTechnicienMaintenance(UtilisateurModel technicienMaintenance) {
        this.technicienMaintenance = technicienMaintenance;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
