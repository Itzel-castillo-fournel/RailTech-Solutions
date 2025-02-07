# Initialiser et tester le fonctionnement du projet :
1. créer une bdd mariaDB port 3307 "railtechrh"
2. vérifier USER, PASSWORD dans `DatabaseConnection`
3. importer le sql de `bdd.sql` dans la base de donnée "railtechrh"
4. clique-droit sur `pom.xml` -> `Maven` -> `Reload Project` pour installer les dépendances (au cas où)
5. lancer `TestConnection` et vérifier si l'utilisateur a bien été ajouté.

# A faire : 
- LoginController
- UtilisateurDAO
- UtilisateurModel
- Loginview.fxml
