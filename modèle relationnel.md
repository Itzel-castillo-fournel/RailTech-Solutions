<div style="display: flex; align-items: center;">
<img src="assets/img/RailTech-Solutions.png" alt="logo railtech-solutions" width="50" style="margin-right: 10px;"/>

# RailTech-Solutions
</div>
Itzel CASTILLO FOURNEL, Malika AMRANI, Dylan OG, ESIEE-IT (BTS - SIO), 16/01/2025

## Modèle relationnel

Utilisateur(id: int(10), nom: varchar(50), prenom: varchar(50), email: varchar(100), mdp: varchar(255), role: enum('ADMIN', 'TECHNICIEN', 'CONDUCTEUR', 'OPERATEUR'))
- Clé primaire : id
- Clé étrangère : .

---

Technicien(id: int(10), specialite: enum('MECANIQUE', 'ELECTRONIQUE', 'ELECTRIQUE', 'INFORMATIQUE', 'INFRASTRUCTURE'))
- Clé primaire : id
- Clé étrangère : id référence à Utilisateur.id

---

Train(immatriculation: varchar(50), marque: varchar(50), modele: varchar(50))
- Clé primaire : immatriculation
- Clé étrangère : .

---

Trajet(id: int(10), heureDepart: localdatetime, heureArrivee: localdatetime, arretDepart: enum('PARIS', 'LYON', 'MARSEILLE', 'BORDEAUX', 'TOULOUSE', 'LILLE', 'NANTES', 'STRASBOURG', 'NICE', 'RENNES', 'MONTPELLIER'), arretArrivee: enum('PARIS', 'LYON', 'MARSEILLE', 'BORDEAUX', 'TOULOUSE', 'LILLE', 'NANTES', 'STRASBOURG', 'NICE', 'RENNES', 'MONTPELLIER'), trainImmat: int(10), conducteurId: int(10))
- Clé primaire : id
- Clé étrangère : 
    - trainImmat référence à Train.immatriculation, 
    - conducteurId référence à Utilisateur.id

---

Incident(id: int(10), description: varchar(255), typeIncident: enum('PANNE_TECHNIQUE', 'RETARD_TRAIN','VOIE_ENDOMMAGEE', 'INCIDENT_A_BORD'), gravite: enum('MINEUR', 'MODERE', 'MAJEUR', 'CRITIQUE'), trainImmat: int(10))
- Clé primaire : id
- Clé étrangère : trainImmat référence à Train.immatriculation

---

Maintenance(dateMaintenance: localdatetime, description: varchar(255), etat: enum('PANNE','MAINTENANCE','OPERATIONNEL'), incidentId: int(10), technicienId: int(10))
- Clé primaire : incidentId
- Clé étrangère : 
    - incidentId référence à Incident.id
    - technicienId référence à Utilisateur.id
