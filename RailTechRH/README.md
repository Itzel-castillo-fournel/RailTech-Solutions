# Initialiser et tester le fonctionnement du projet :
1. créer une bdd mariaDB "railtechrh"
2. #### Configurer votre fichier .env :
   - Créez un fichier .env à la racine du projet. (Dossier RailTechRH) :
         RailTechRH/
         ├── src/
         ├── target/
         ├── pom.xml
         ├── .env          <-- Fichier .env à la racine
         └── README.md
   
   - Ajoutez les variables d'environnement nécessaires à ce projet :
        DB_URL=jdbc:mariadb://localhost:{votre_port}/railtechrh
        DB_USER=votre_user
        DB_PASSWORD=votre_password

   - Vérifiez que la bibliothèque dotenv-java est ajoutée à votre pom.xml  :
      <dependency>
      <groupId>io.github.cdimascio</groupId>
      <artifactId>dotenv-java</artifactId>
      <version>2.2.0</version>
      </dependency>

   - Ajoutez .env à votre .gitignore (clique droit sur .env > git > Add to .gitignore)

3. importer le sql de `bddV2.sql` dans la base de donnée "railtechrh"
4. clique-droit sur `pom.xml` -> `Maven` -> `Reload Project` pour installer les dépendances (au cas où)
5. lancer `TestConnection` et vérifier si l'utilisateur a bien été ajouté.

# A faire : 
- LoginController
- UtilisateurDAO
- UtilisateurModel
- Loginview.fxml

