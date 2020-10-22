# SmartEye : Application de Vidéosurveillance
![image](https://user-images.githubusercontent.com/67594250/96917070-4fbc5e80-14a0-11eb-8c63-87fcceb4fe70.png)

La vidéosurveillance est un système de caméras et de transmission d'images,disposé dans un espace public ou privé pour le surveiller à distance ; il s'agit donc d'un type de télésurveillance. Les images obtenues avec ce système, peuvent être traitées automatiquement et/ou visionnées puis archivées ou détruites. Les causes de l'installation de systèmes de vidéosurveillance sont diverses, toutefois la sécurité publique ainsi que la protection des biens mobiliers ou immobiliers font office d'éléments phares dans la justification de la vidéosurveillance. L'industrie de la vidéosurveillance englobe aujourd'hui toute une variété de systèmes et d'équipements de surveillance et de protection des personnes et des biens. Une vidéosurveillance performante c'est une solution contre le vol à l'étalage, en cas de cambriolage... et une certaine tranquillité les nuits de garde.

# Outils et technologies utilisés

Voici l'ensemble des outils et technologies utilisés : 

Eclipse, Java, JavaFX, SceneBuilder, JDBC, MySQL, HeidiSQL, NGINX, Maven, JavaMail, OpenCV.

# L'application réalisée
Voici l'application réalisée:

![image](https://user-images.githubusercontent.com/67594250/96917918-64e5bd00-14a1-11eb-8953-e857ad50e057.png)

Au lancement de l’application, il est demandé de saisir le login et le mot de passe pour s’authentifier. Une fois le cordonnées sont exactes et l’authentification a été bien passé, l’interface de l’application s’affiche automatiquement :

![image](https://user-images.githubusercontent.com/67594250/96918067-a1191d80-14a1-11eb-81d1-e4613d45ad0e.png)

En cliquant sur le bouton Start, la caméra se lance et les données de l’application se chargent:

![image](https://user-images.githubusercontent.com/67594250/96918196-d9b8f700-14a1-11eb-89a2-6891df2ed020.png)

En cliquant sur le bouton d’activation de détecteur de mouvement ; ce dernier sera lancer et une fois un mouvement sera détecté, un message sera afficher en
haut en rouge et l’alerte de mouvement va être envoyer.

![image](https://user-images.githubusercontent.com/67594250/96918283-fb19e300-14a1-11eb-88ac-20c3601db7e9.png)

En cliquant sur le bouton d’activation de reconnaissance faciale, l’application va afficher le nom du visage détecté et ses informations s’il est enregistrer dans la base de données au préalable. Dans le cas contraire, nous pouvons enregistrer les données de cette personne pour la première fois ; Par la suite, s’elle est détecté par l’application, elle va la (la personne) reconnaitre.

![image](https://user-images.githubusercontent.com/67594250/96918431-30263580-14a2-11eb-9a58-144556410cf2.png)






