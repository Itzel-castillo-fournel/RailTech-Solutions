<div style="display: flex; align-items: center;">
<img src="assets/img/RailTech-Solutions.png" alt="logo railtech-solutions" width="50" style="margin-right: 10px;"/>

# RailTech-Solutions
</div>
Itzel CASTILLO FOURNEL, Malika AMRANI, Dylan OG, ESIEE-IT (BTS - SIO), 10/12/2024

## Modèle relationnel

Planning(id: int(10), date: localdate, heureDebut: localdatetime, heureFin: localdatetime, conducteurId: int(10), trainId: int(10), lieuDepart: varchar(255), lieuArrivee: varchar(255), etatTrain: varchar(50), statutPlanning: varchar(50))
- Clé primaire : id
- Clé étrangère :
    - conducteurId référence à Conducteur.id
    - trainId référence à Train.id

Conducteur(id: int(10), nom: varchar(50), prenom: varchar(50), email: varchar(100), numeroTelephone: varchar(15))
- Clé primaire : id
- Champs unique : email

Train(id: int(10), numeroTrain: varchar(50), etat: varchar(50), dateDerniereMaintenance: localdate)
- Clé primaire : id

Gestionnaire(id: int(10), nom: varchar(50), prenom: varchar(50), email: varchar(100), numeroTelephone: varchar(15))
- Clé primaire : id
- Champs unique : email
	
Trajet(id: int(10), date: localdate, heureDepart: localdatetime, heureArrivee: localdatetime, arrets: varchar(255), trainId: int(10), conducteurId: int(10), etat: varchar(50))
- Clé primaire : id
- Clé étrangère :
    - trainId référence à Train.id
    - conducteurId référence à Conducteur.id

Maintenance(id: int(10), dateMaintenance: localdatetime, description: varchar(255), trainId: int(10), typeMaintenance: varchar(50), personnelId: int(10))
- Clé primaire : id
- Clé étrangère :
    - trainId référence à Train.id
    - personnelId référence à PersonnelDeMaintenance.id

PersonnelDeMaintenance(id: int(10), nom: varchar(50), prenom: varchar(50), specialite: varchar(50), numeroTelephone: varchar(15), email: varchar(100))
- Clé primaire : id
- Champs unique : email

