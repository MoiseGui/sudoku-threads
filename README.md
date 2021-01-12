# sudoku-threads

Ceci est un resolveur de Sudoku 9x9 avec les fonctionnalités suivantes:
      <ul>
         <li>Un validateur de grille 9x9 en utilisant des Threads</li>
         <li>Un résolveur basé aussi sur l'utilisation de Threads</li>
         <li>La possibilité d'enregistrer et de charger une grille à partir d'un fichier</li>
    </ul>


Ce projet est réalisé avec JavaFx en respectant l'architecture MVC.
Ainsi donc
    <ul type="none">
         <li>le paquage application contient la classe Main et le Controller</li>
         <li>le paquage application.view contient les interfaces graphiques,</li>
         <li>le paquage application.model les classes modèles d'exécution</li>
    </ul>


<h1>Pour l'exécuter il existe deux façons:</h1>

<ol>
  <li>Un fichier exécutable est déjà généré et introduit à l'emplacement <b>/Sudoku/build/deploy/bundles</b></li>
  
  <li>
    A partir d'un éditeur de code Java et dans ce cas incluire les dépendances suivantes:
      <ul>
         <li>JDK et JRE version 1.8.0_271</li>
         <li>JavaFx 11</li>
         <li>JafaFx SDK</li>
    </ul>
  </li>
</ol>
