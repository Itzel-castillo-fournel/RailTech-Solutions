package fr.irontrail.railtechrh.model;

import fr.irontrail.railtechrh.model.enums.Arret;

import java.time.LocalDateTime;

public class TrajetModel {
    private int id;
    private LocalDateTime heureDepart;
    private LocalDateTime heureArrivee;
    private Arret arretDepart;
    private Arret arretArrivee;
    private String trainImmat;
    private int conducteurId;

    // Constructeur par défaut
    public TrajetModel() {}

    // Constructeur avec paramètres
    public TrajetModel(int id, LocalDateTime heureDepart, LocalDateTime heureArrivee,
                  Arret arretDepart, Arret arretArrivee, String trainImmat, int conducteurId) {
        this.id = id;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.arretDepart = arretDepart;
        this.arretArrivee = arretArrivee;
        this.trainImmat = trainImmat;
        this.conducteurId = conducteurId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public Arret getArretDepart() {
        return arretDepart;
    }

    public void setArretDepart(Arret arretDepart) {
        this.arretDepart = arretDepart;
    }

    public Arret getArretArrivee() {
        return arretArrivee;
    }

    public void setArretArrivee(Arret arretArrivee) {
        this.arretArrivee = arretArrivee;
    }

    public String getTrainImmat() {
        return trainImmat;
    }

    public void setTrainImmat(String trainImmat) {
        this.trainImmat = trainImmat;
    }

    public int getConducteurId() {
        return conducteurId;
    }

    public void setConducteurId(int conducteurId) {
        this.conducteurId = conducteurId;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", heureDepart=" + heureDepart +
                ", heureArrivee=" + heureArrivee +
                ", arretDepart=" + arretDepart +
                ", arretArrivee=" + arretArrivee +
                ", trainImmat='" + trainImmat + '\'' +
                ", conducteurId=" + conducteurId +
                '}';
    }
}

