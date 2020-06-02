# IleInterdite
Projet POGL

Projet réalisé par VITANI Martin, groupe 1.

Le modèle, la grille et la vue sont basés sur le code du Jeu de la vie de Conway fourni par Thibaut Balabonski.

La grille de taille HAUTEUR*LARGEUR est composée de Zones. 
Ces zones possèdent des coordonnées qui correspondent à leurs coordonnées sur le modèle, un artefact et un etatzone.

Les différents artefacts sont aucun, eau, feu, air, terre, heliport. Ils sont définis par un enum.

J'ai choisi de définir l'heliport comme un artefact car cela facilite l'affichage et ne gène pas pour la suite. Cependant une zone ne pouvant possèder qu'un seul artefact, elle pourra donc pas possèder à la fois un artefact et l'héliport. Je n'ai pas trouvé cela gênant pour le jeu.

Les différents etatzones sont normale, inondee, submergee.

Les zones sont initialisées avec l'artefact "aucun" et l'etatzone "normale".

Les 5 artefacts eau, feu, air, terre, et heliport sont ensuite générés sur des cases aléatoires différentes.

J'ai rencontré mes premières difficultés pour l'affichage, en effet je souhaitais afficher les artefacts au milieu des cases, pour ce faire j'ai choisi de faire plusieurs fonctions d'affichage prenant à chaque fois un paramètre différent :
- la fonction paint quasiment identique à celle utilisée dans le jeu de la vie pour afficher les Zones;
- une fonction paint2 qui sert à afficher les artefacts au milieu des cases;
- une fonction paintJ qui sert à afficher les joueurs;

Les fonctions paint et paint2 parcourent la grille et la fonction paintJ parcoure l'ArrayList des joueurs, présente dans les attributs du modèle. Les détails sont notés en commentaire dans le code.

Chaque joueur possède un nom, une couleur, une position de type Zone et des ArrayList pour représenter les artefacts et les clés d'artefact qu'il possède.
Un joueur est initialisé à une position aléatoire, avec une couleur et un nom définis et des ArrayList vides.

Les plus grosses difficultées que j'ai rencontrées lors de ce projet sont sur la gestion des tours ainsi que des ActionEvent avec des clics sur les différents boutons que j'ai implémentés.

Les tours de mes joueurs se déroulent de la façon suivante : Le modèle possède un compteur qui garde en mêmoire le nombre d'actions effectuées par le joueur dont c'est le tour de jouer. A chaque clic sur un bouton, si l'action correspondant au bouton est possible elle est effectuée et ce compteur est incrémenté d'1, jusqu'à 3 où seul le bouton FinDeTour est activé et déclenche l'événement inondation qui inonde 3 zones au hasard.

Je trouve que ma façon de gérer les tours est peu conventionnelle car elle est ainsi faite dans la classe Controleur.

Toutes les actions de joueurs sont effectuées à l'aide de boutons :
- les boutons de déplacement : haut, bas, gauche, droite représentés avec les labels ^, v, <, >;
- les boutons pour assécher : assechHaut, assechBas, assechGauche, assechDroite, assechIci(pour assécher la zone sur laquelle le joueur se situe);
- le bouton "deverouiller" pour récupérer un artefact situé sur la zone sur laquelle le joueur se situe.

Je n'a pas réussi à implémenter le panneau affichant les artefacts et les clés possédés par les joueurs, ceux-ci sont cependant affichés dans le terminal.

Pour la partie 4 j'ai implémenté l'échange de clés, chaque joueur peut donner la première clé de sont inventaire à un autre joueur.
