package fr.irontrail.railtechrh.model;


import fr.irontrail.railtechrh.model.enums.Role;

public class UtilisateurModel {
    // Attributs (propriétés de l'utilisateur)
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;



    private Role role;

    public UtilisateurModel() {}
    // Constructeur (pour créer un nouvel utilisateur)
    public UtilisateurModel(int id, String nom, String motDePasse, Role role) {
        this.id = id;
        this.nom = nom;
        this.mdp = motDePasse;
        this.role = role;
    }

    // Getters (pour accéder aux attributs)
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMdp() {
        return mdp;
    }

    // Setters (pour modifier les attributs)
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMdp(String motDePasse) {
        this.mdp = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}