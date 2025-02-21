package fr.irontrail.railtechrh.model;

public class TrainModel {

    private String immatriculation;
    private String marque;
    private String modele;


    //CONSTRUCTEURS
    public TrainModel() {}

    public TrainModel(String immatriculation, String marque, String modele) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
    }
    //GETTERS
    public String getImmatriculation() {
        return immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    //SETTERS
    public void setImmatriculation(String immatriculation) {this.immatriculation = immatriculation;}
    public void setMarque(String marque) {this.marque = marque;}
    public void setModele(String modele) {this.modele = modele;}
}


