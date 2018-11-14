# Mini projet 1 : Moteur de requête RDF étoile
## Développé par Florian Novellon et Fabien Feraud

### Composition du dossier
* Le rapport ```Rapport_RDF_Engine_Novellon_Feraud.pdf```
* Le dossier ```res``` contenant les données rdfxml
* Le dossier ```queries``` contenant les requêtes SPARQL
* Le dossier ```src``` contenant les sources du projet
* Le JAR exécutable ```rdfstar.jar``` de notre mini projet

### Execution du jar
Pour lancer le jar entrer la commande suivante dans un terminal pointant sur ce dossier : 
```
java -jar rdfstar.jar -data "./res" -queries "./queries" -workload_time
```
Ci-dessous la liste des options que l'on peut passer en paramètre :
```
-data "chemin/du/dossier" : dossier où se trouve les documents à interroger
-queries "chemin/du/dossier" : dossier où se trouve les requête à évaluer
-output "chemin/du/dossier" : où seront stockés les temps d'execution pour chaque fichier
-verbose : affiche pour chaque requête le temps d'execution
-export_results : stock dans le dossier output le résultat des requêtes
-exports_stats : stock dans le dossier output les statisques des requêtes
-workload_time : affiche le temps total d'execution des requêtes
```

> **Attention**, ```-data "chemin/du/dossier"``` et ```-queries "chemin/du/dossier"``` sont obligatoires et, avec ```-output "chemin/du/dossier"```, doivent donner le chemin d'un dossier existant.

