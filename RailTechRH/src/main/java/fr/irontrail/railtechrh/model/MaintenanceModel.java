package fr.irontrail.railtechrh.model;

import java.time.LocalDateTime;

public class MaintenanceModel {
    private String etatMaintenance;
    private String numeroImmatriculationTrain;
    private String descriptionMaintenance;
    private String nomTechnicien;
    private String prenomTechnicien;
    private LocalDateTime derniereMiseAJour;

    // Getters et setters
    public String getEtatMaintenance() {
        return etatMaintenance;
    }

    public void setEtatMaintenance(String etatMaintenance) {
        this.etatMaintenance = etatMaintenance;
    }

    public String getNumeroImmatriculationTrain() {
        return numeroImmatriculationTrain;
    }

    public void setNumeroImmatriculationTrain(String numeroImmatriculationTrain) {
        this.numeroImmatriculationTrain = numeroImmatriculationTrain;
    }

    public String getDescriptionMaintenance() {
        return descriptionMaintenance;
    }

    public void setDescriptionMaintenance(String descriptionMaintenance) {
        this.descriptionMaintenance = descriptionMaintenance;
    }

    public String getNomTechnicien() {
        return nomTechnicien;
    }

    public void setNomTechnicien(String nomTechnicien) {
        this.nomTechnicien = nomTechnicien;
    }

    public String getPrenomTechnicien() {
        return prenomTechnicien;
    }

    public void setPrenomTechnicien(String prenomTechnicien) {
        this.prenomTechnicien = prenomTechnicien;
    }

    public LocalDateTime getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(LocalDateTime derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }
}
