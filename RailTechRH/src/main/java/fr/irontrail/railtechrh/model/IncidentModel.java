package fr.irontrail.railtechrh.model;

import fr.irontrail.railtechrh.model.enums.Gravite;
import fr.irontrail.railtechrh.model.enums.TypeIncident;

public class IncidentModel {
    private int id;
    private String description;
    private TypeIncident typeIncident;
    private Gravite gravite;
    private TrainModel trainImmat;

    //CONSTRUCTEURS
    public IncidentModel() {}

    public IncidentModel(int id, String description, TypeIncident typeIncident, Gravite gravite, TrainModel trainImmat) {
        this.id = id;
        this.description = description;
        this.typeIncident = typeIncident;
        this.gravite = gravite;
        this.trainImmat = trainImmat;
    }

    //GETTERS
    public int getId() {return id;}
    public String getDescription() {return description;}
    public TypeIncident getTypeIncident() {return typeIncident;}
    public Gravite getGravite() {return gravite;}
    public TrainModel getTrainImmat() {return trainImmat;}

    //SETTERS
    public void setId(int id) {this.id = id;}
    public void setDescription(String description) {this.description = description;}
    public void setTypeIncident(TypeIncident typeIncident) {this.typeIncident = typeIncident;}
    public void setTrainImmat(TrainModel trainImmat) {this.trainImmat = trainImmat;}
    public void setGravite(Gravite gravite) {this.gravite = gravite;}

}
