## GFVBack - Démarrage

### Pré-requis
```
    MySQL Server 5.7
    JDK 1.8.0_231
    apache maven 3.6.3
    IntelliJ 2019+
```

1. Installer le serveur MySQL

2. Télécharger Java JDK 1.8.0_231 (Fichier zip ou tar.gz suivant votre environement)

3. Télécharger apache maven 3.6.3 (Fichier zip ou tar.gz suivant votre environement)

4. Cloner le projet dans le répertoir de votre choix : git clone https://github.com/MS2ISI9BG3/GFVBack.git

5. Démarrer Intellij > Open > "Sélectionner le dossier projet que vous avez cloner" > OK

6. Configurer IntelliJ 2019+
    - File > Settings > Plugins > Marketplace : ajouter le plugin lombok
    - File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors : Cocher -> Enable annotation processing
    - File > Settings > Other Settings > Lombok plugin : Cocher -> Enable Lombok plugin for this project
    - File > Settings > Build, Execution, Deployment > Build Tools > Maven :
        Renseigner le repertoire d'installation de Maven, le fichier de configuration et le répertoire local des artifacts
    - File > Project Structure ... > Project Settings > Project > Project SDK : Selectionner la version 1.8.0_231

7. Changer les identifiants de connexion à la base de données (mettre ceux correspondant à votre configuration)
	à partir d'IntelliJ, projet ouvert :
```
    > CTRL+SHIFT+R : application.yml
    > CTRL+F : username (mettre à jour); puis changer le 'password' juste après
```

8. Rebuilder le projet : Build > Rebuild Project

9. Démarrer l'application : Run > Run 'GfvApplication'

### Après le démarrage du projet :

1. L'adresse du serveur = [http://localhost:9988](http://localhost:9988)

1. Une base de données 'gfv' est créée à l'adresse : [localhost:3306](localhost:3306)

2. Des utilisateurs sont créés ainsi que leurs rôles associés
```
    - user : Password = user [ROLE_USER]
    - admin : Passord = admin [ROLE_ADMIN, ROLE_USER]
``` 

3. La documentation de l'api est disponible à l'adresse : [http://localhost:9988/swagger-ui.html](http://localhost:9988/swagger-ui.html)
