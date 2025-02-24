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

    // Constructeur
    public TrajetModel(int id, LocalDateTime heureDepart, LocalDateTime heureArrivee,
                       Arret arretDepart, Arret arretArrivee, String trainImmat,
                       int conducteurId) {
        this.id = id;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.arretDepart = arretDepart;
        this.arretArrivee = arretArrivee;
        this.trainImmat = trainImmat;
        this.conducteurId = conducteurId;
    }

    // Getters
    public int getId() { return id; }
    public LocalDateTime getHeureDepart() { return heureDepart; }
    public LocalDateTime getHeureArrivee() { return heureArrivee; }
    public Arret getArretDepart() { return arretDepart; }
    public Arret getArretArrivee() { return arretArrivee; }
    public String getTrainImmat() { return trainImmat; }
    public int getConducteurId() { return conducteurId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setHeureDepart(LocalDateTime heureDepart) { this.heureDepart = heureDepart; }
    public void setHeureArrivee(LocalDateTime heureArrivee) { this.heureArrivee = heureArrivee; }
    public void setArretDepart(Arret arretDepart) { this.arretDepart = arretDepart; }
    public void setArretArrivee(Arret arretArrivee) { this.arretArrivee = arretArrivee; }
    public void setTrainImmat(String trainImmat) { this.trainImmat = trainImmat; }
    public void setConducteurId(int conducteurId) { this.conducteurId = conducteurId; }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", départ=" + arretDepart + " à " + heureDepart +
                ", arrivée=" + arretArrivee + " à " + heureArrivee +
                ", train='" + trainImmat + '\'' +
                ", conducteurId=" + conducteurId +
                '}';
    }
}