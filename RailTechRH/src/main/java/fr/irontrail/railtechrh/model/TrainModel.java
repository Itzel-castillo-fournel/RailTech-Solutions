package fr.irontrail.railtechrh.model;

import fr.irontrail.railtechrh.model.enums.Arret;

import java.time.LocalDateTime;

public class TrainModel {

    private String immatriculation;
    private String marque;
    private String modele;

    public TrainModel() {
    }

    public TrainModel(String immatriculation, String marque, String modele) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
    }


    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }
}


