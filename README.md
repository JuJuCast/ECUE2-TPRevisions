# TP Révisions : Héritage, Polymorphisme et Gestion de Fichiers

### Objectifs
Approfondir la programmation orientée objet :

- Manipuler l'héritage et le polymorphisme
- Utiliser des classes abstraites
- Gérer les exceptions (try/catch, throws, exceptions personnalisées)
- Lire et écrire dans des fichiers texte
- Manipuler des tableaux d'objets
- Appliquer les bonnes pratiques de la POO

### Prérequis
- Cloner le projet sur votre poste dans le répertoire de votre choix
- Ouvrir le projet :
  - Sur l'écran d'accueil d'IntelliJ, cliquer sur **Open**
  - Sélectionner le dossier **ECUE2-TPRevisions** qui a été copié depuis GitHub puis cliquer sur **OK**
  - Le projet s'ouvre
  - Vérifier que le SDK est bien sélectionné dans **File > Project Structure**, onglet **Project**

### Utilisation de Git

- Créer une nouvelle branche **prenomNom**
- Faire **1 commit** par exercice
- Ouvrir une *pull request* sur github :
  - Aller sur votre projet sur GitHub
  - Cliquer sur **New Pull Request**,
  - Sélectionner base repository = **harf18/ECUE2-TPRevisions**
  - Sélectionner compare = votre branche **prenomNom**

----

### Modalités du TP

- Utiliser la méthode `main()` de la classe **Exec** pour tester vos développements (création d'objets, appels de méthodes, etc.) au rythme où vous le souhaitez : ce n'est pas précisé dans chaque exercice.
- Toutes les classes des exercices doivent être ajoutées au package **net.lecnam.ecue2.revisions**
- Le TP est long. Les exercices marqués **(cœur)** sont à faire en priorité : 1 à 8, 10, 11 et 13. Les exercices 9 et 12 sont à faire si vous avez le temps, les exercices 14 et 15 sont des bonus.

> Si vous ne l'avez pas encore fait, pensez à créer une nouvelle branche **prenomNom**

### Contexte du TP

Vous allez développer un système de gestion de comptes bancaires. Une banque gère différents types de comptes (compte courant, compte épargne, compte entreprise) pour ses clients. Chaque type de compte a des caractéristiques et des règles de gestion spécifiques.

---

### Exercice 1 : Classe Client (cœur)

- Créer une classe **Client**
- Attributs :
  - `nom` (String)
  - `prenom` (String)
  - `numeroClient` (String) : identifiant unique du client
- Créer un **constructeur** qui initialise tous les attributs
- Créer les **getters** pour tous les attributs
- Redéfinir la méthode **toString()** qui retourne une représentation textuelle du client
- Redéfinir la méthode **equals(Object)** pour que deux clients soient égaux s'ils ont le même numéro client

> Remarque : en Java, quand on redéfinit `equals()`, on doit normalement redéfinir aussi `hashCode()`. Vous verrez pourquoi au prochain cours sur les collections ; ce n'est pas demandé ici.
>
> Pensez à tester dans Exec en créant quelques clients
>
> Pensez à faire votre 1er commit !

### Exercice 2 : Exception personnalisée (cœur)

- Créer une classe d'exception **SoldeInsuffisantException** qui hérite de **Exception**
- Cette exception doit avoir :
  - Un constructeur prenant un message en paramètre
  - Un constructeur prenant un message et le montant manquant (double)
  - Une méthode **getMontantManquant()** qui retourne le montant manquant

> **Rappel : checked ou unchecked ?** Cette grille vous servira dans tout le TP.
>
> - Une exception **checked** (qui hérite de `Exception`) signale une situation **prévisible et normale** dans la vie de l'application, que l'appelant **doit** traiter : un solde insuffisant, un compte qui n'existe pas, un fichier introuvable ou corrompu. Le compilateur oblige l'appelant à la gérer, on ne peut donc pas l'oublier.
> - Une exception **unchecked** (qui hérite de `RuntimeException`) signale une **erreur de programmation** : l'appelant n'a pas respecté le contrat de la méthode, par exemple un montant négatif passé à `deposer()`, un retrait au-delà du plafond, ou `null` là où on attend un objet. En général, on ne la capture pas : on corrige le code appelant pour qu'elle ne se produise plus.
>
> Pensez à faire un commit !

### Exercice 3 : Classe abstraite CompteBancaire (cœur)

- Créer une classe **abstraite CompteBancaire**
- Attributs :
  - `numeroCompte` (String)
  - `solde` (double)
  - `titulaire` (Client)
  - `dateOuverture` (LocalDate) : utilisez `java.time.LocalDate`
- Créer **deux constructeurs** :
  - `CompteBancaire(String numeroCompte, Client titulaire)` : pour un nouveau compte. La date d'ouverture est la date du jour (`LocalDate.now()`) et le solde initial est à 0.
  - `CompteBancaire(String numeroCompte, Client titulaire, LocalDate dateOuverture, double solde)` : pour recréer un compte existant (il servira à l'exercice 11 lors du chargement depuis un fichier)
- Tous les attributs sont `private`
- Créer les **getters** pour tous les attributs. **Pas de setter pour le solde** : on ne doit pas pouvoir modifier un solde depuis l'extérieur d'un compte.
- Créer une méthode **deposer(double montant)** qui ajoute le montant au solde. Si le montant est négatif ou nul, lever une **IllegalArgumentException**.
- Créer une méthode `protected void debiter(double montant)` qui retire le montant du solde **sans aucune vérification**. Elle sera utilisée par les classes filles dans leur méthode `retirer()`, après avoir vérifié leurs propres règles. Pourquoi `protected` et pas `public` ? Répondez en commentaire au-dessus de la méthode.
- Créer une méthode **abstraite** `retirer(double montant) throws SoldeInsuffisantException` (chaque type de compte aura ses propres règles de retrait)
- Créer une méthode **abstraite** `calculerInterets()` qui retourne un double
- Créer une méthode **appliquerInterets()** (non abstraite) qui ajoute directement au solde le résultat de `calculerInterets()`. Remarquez qu'elle fonctionnera pour tous les types de comptes grâce au polymorphisme. Attention : n'utilisez pas `deposer()` ici. Pourquoi ? (Pensez au compte courant, qui ne rapporte pas d'intérêts.)
- Redéfinir **toString()** pour afficher les informations du compte

> Important : comme `solde` est `private`, les classes filles ne peuvent pas y accéder directement. Elles doivent passer par `getSolde()` pour le lire, et par `debiter()` pour le diminuer.
>
> Remarque : on utilise ici `double` pour les montants pour rester simple. Dans une vraie application bancaire, c'est à proscrire : les `double` font des erreurs d'arrondi (essayez d'afficher `0.1 + 0.2`). On utilise plutôt `BigDecimal`, ou un `long` qui compte les centimes.
>
> Pensez à faire un commit !

### Exercice 4 : Classe CompteCourant (cœur)

- Créer une classe **CompteCourant** qui hérite de **CompteBancaire**
- Attribut supplémentaire :
  - `decouvertAutorise` (double) : montant du découvert autorisé
- Créer les **constructeurs** :
  - un constructeur qui prend en paramètre le numéro de compte, le titulaire et le découvert autorisé
  - un constructeur qui prend seulement le numéro de compte et le titulaire, avec un découvert autorisé **par défaut de 500 €** (pensez à `this(...)`)
  - un constructeur complet (numéro, titulaire, date d'ouverture, solde, découvert) pour l'exercice 11
- Implémenter la méthode **retirer(double montant)** :
  - On peut retirer tant que : `getSolde() - montant >= -decouvertAutorise`. Dans ce cas, appeler `debiter(montant)`.
  - Sinon, lever une **SoldeInsuffisantException** avec le montant manquant, c'est-à-dire `montant - (getSolde() + decouvertAutorise)`
- Implémenter **calculerInterets()** : un compte courant ne rapporte pas d'intérêts, retourner 0
- Ajouter une méthode **getDecouvertAutorise()**

> Pensez à tester avec des retraits valides et invalides dans Exec
>
> Pensez à faire un commit !

### Exercice 5 : Classe CompteEpargne (cœur)

- Créer une classe **CompteEpargne** qui hérite de **CompteBancaire**
- Attribut supplémentaire :
  - `tauxInteret` (double) : taux d'intérêt annuel (par exemple 0.03 pour 3 %)
- Créer les **constructeurs** :
  - un constructeur qui prend en paramètre le numéro de compte, le titulaire et le taux d'intérêt
  - un constructeur complet (numéro, titulaire, date d'ouverture, solde, taux) pour l'exercice 11
- Implémenter la méthode **retirer(double montant)** :
  - On ne peut pas avoir un solde négatif sur un compte épargne
  - Lever une **SoldeInsuffisantException** avec le montant manquant si le retrait n'est pas possible, sinon appeler `debiter(montant)`
- Implémenter **calculerInterets()** : retourne `getSolde() * tauxInteret`

> Pensez à faire un commit !

### Exercice 6 : Classe CompteEntreprise (cœur)

- Créer une classe **CompteEntreprise** qui hérite de **CompteBancaire**
- Attributs supplémentaires :
  - `nomEntreprise` (String)
  - `siret` (String)
  - `plafondRetrait` (double) : montant maximum qu'on peut retirer en une fois
- Créer les **constructeurs** :
  - un constructeur (numéro, titulaire, nom de l'entreprise, siret), qui utilise un plafond **par défaut de 10 000 €** (pensez à `this(...)`)
  - un constructeur (numéro, titulaire, nom de l'entreprise, siret, plafond)
  - un constructeur complet (numéro, titulaire, date d'ouverture, solde, nom de l'entreprise, siret, plafond) pour l'exercice 11
- Implémenter la méthode **retirer(double montant)** :
  - Vérifier que le montant ne dépasse pas le plafond de retrait (sinon lever une **IllegalArgumentException**)
  - Vérifier que le solde est suffisant (sinon lever une **SoldeInsuffisantException** avec le montant manquant)
  - Si tout est bon, appeler `debiter(montant)`
- Implémenter **calculerInterets()** : un compte entreprise rapporte 1 % d'intérêts, retourner `getSolde() * 0.01`
- Redéfinir **toString()** pour inclure les informations de l'entreprise (pensez à `super.toString()`)

> Question : pourquoi le dépassement de plafond lève-t-il une IllegalArgumentException (*unchecked*) alors que le solde insuffisant lève une SoldeInsuffisantException (*checked*) ? Répondez en commentaire au-dessus de la méthode `retirer()`.
>
> Pensez à faire un commit !

### Exercice 7 : Classe Banque (cœur)

- Créer une classe **Banque**
- Attributs :
  - `nom` (String)
  - `comptes` (`CompteBancaire[]`) : tableau des comptes de la banque
  - `nbComptes` (int) : nombre de comptes réellement présents dans le tableau
- Créer un **constructeur** `Banque(String nom, int capaciteMax)` qui initialise le nom, crée le tableau de comptes avec la capacité donnée et met `nbComptes` à 0

> Un tableau a une taille fixe, choisie à sa création. Si on crée un tableau de 100 cases et qu'on y range 3 comptes, les 97 autres cases contiennent `null`. C'est pour ça qu'on a besoin de `nbComptes` : les comptes occupent les cases `0` à `nbComptes - 1`, et **toutes vos boucles doivent s'arrêter à `nbComptes`**, pas à `comptes.length`. Sinon, gare à la NullPointerException !

- Créer une méthode **ajouterCompte(CompteBancaire compte)** qui range le compte dans la première case libre (la case `nbComptes`) puis incrémente `nbComptes`.
  - Si un compte avec le même numéro existe déjà dans la banque, lever une **IllegalArgumentException**
  - Si le tableau est plein, lever une **IllegalStateException** avec le message "La banque est pleine"
- Créer une méthode **getNbComptes()**
- Créer une méthode **rechercherCompte(String numeroCompte)** qui retourne le compte correspondant, ou `null` s'il n'existe pas
- Créer une méthode **listerComptesClient(Client client)** qui retourne un tableau `CompteBancaire[]` contenant uniquement les comptes du client donné (utilisez le `equals()` de l'exercice 1). Le tableau retourné doit avoir exactement la bonne taille, sans case vide. Astuce : faites une première boucle pour compter les comptes du client, créez le tableau de la bonne taille, puis faites une seconde boucle pour le remplir.
- Créer une méthode **calculerSoldeTotal()** qui retourne la somme des soldes de tous les comptes

> Pour aller plus loin : au lieu de lever une exception quand le tableau est plein, créez un nouveau tableau deux fois plus grand, recopiez-y les comptes (voir `Arrays.copyOf()`) et remplacez l'ancien tableau par le nouveau. La banque n'est alors plus jamais pleine. Vous venez de réécrire, en gros, ce que fait la classe `ArrayList`, qu'on verrra au prochain cours'
>
> Pensez à faire un commit !

### Exercice 8 : Virement entre comptes (cœur)

Dans la classe **Banque** :
- Créer une méthode `public boolean effectuerVirement(String numeroCompteSource, String numeroCompteDestination, double montant) throws SoldeInsuffisantException`
- Cette méthode doit :
  - Vérifier que le montant est strictement positif (sinon lever une **IllegalArgumentException**)
  - Rechercher les deux comptes **avant toute opération** (lever une **IllegalArgumentException** si l'un des deux n'existe pas)
  - Retirer le montant du compte source (peut lever une SoldeInsuffisantException)
  - Déposer le montant sur le compte destination
  - Retourner `true` si le virement a réussi

> Testez avec différents scénarios dans Exec (virement valide, compte inexistant, solde insuffisant)
>
> Pensez à faire un commit !

### Exercice 9 : Exception personnalisée CompteInexistantException (si vous avez le temps)

- Créer une classe d'exception **CompteInexistantException** qui hérite de **Exception**
- Ajouter un constructeur qui prend en paramètre le numéro de compte inexistant et qui construit un message du type "Le compte *numeroCompte* n'existe pas"
- Modifier la méthode **effectuerVirement()** pour lever cette exception au lieu de l'IllegalArgumentException. Que devez-vous modifier dans la signature de la méthode ? Et dans Exec ?

> Relisez la grille « checked ou unchecked ? » de l'exercice 2 : pourquoi CompteInexistantException est-elle plus adaptée que IllegalArgumentException pour un virement ?
>
> Pensez à faire un commit !

### Exercice 10 : Sauvegarde dans un fichier (cœur)

Chaque type de compte sait mieux que personne quelles informations il doit sauvegarder. On va donc s'appuyer sur le polymorphisme plutôt que sur une suite de `instanceof` dans la classe Banque :

- Dans **CompteBancaire**, ajouter une méthode **abstraite** `toLigneFichier()` qui retourne un String
- Dans **chaque classe fille**, implémenter `toLigneFichier()` pour qu'elle retourne la ligne complète du compte (au format ci-dessous)
- Pour ne pas répéter trois fois le même code, ajoutez dans **CompteBancaire** une méthode `protected String ligneCommune()` qui retourne la partie commune à tous les comptes (`numeroCompte;numeroClient;nom;prenom;solde;dateOuverture`). Chaque classe fille n'a plus qu'à ajouter le type devant et ses paramètres spécifiques derrière, par exemple : `"COURANT;" + ligneCommune() + ";" + decouvertAutorise`

Dans la classe **Banque** :
- Créer une méthode `sauvegarderComptes(String nomFichier) throws IOException`
- Cette méthode écrit dans un fichier texte tous les comptes de la banque, une ligne par compte, en appelant simplement `toLigneFichier()` sur chaque compte. **Aucun `instanceof` ne doit apparaître dans cette méthode.**
- Format (une ligne par compte) :
  ```
  TYPE_COMPTE;numeroCompte;numeroClient;nom;prenom;solde;dateOuverture;[paramètres spécifiques]
  ```
  Les paramètres spécifiques sont :
  - COURANT : `decouvertAutorise`
  - EPARGNE : `tauxInteret`
  - ENTREPRISE : `nomEntreprise;siret;plafondRetrait`

  Par exemple :
  ```
  COURANT;CC001;C123;Dupont;Jean;1500.5;2024-01-15;500.0
  EPARGNE;CE001;C123;Dupont;Jean;5000.0;2024-02-01;0.03
  ENTREPRISE;CEN001;C456;Martin;Paul;25000.0;2024-03-10;SARL ABC;12345678901234;10000.0
  ```
- Bien fermer les ressources (try-with-resources recommandé)

> Pensez à faire un commit !

### Exercice 11 : Lecture depuis un fichier (cœur)

Dans la classe **Banque** :
- Créer une méthode `chargerComptes(String nomFichier) throws IOException`
- Cette méthode lit un fichier texte et recrée tous les comptes :
  - Parser chaque ligne pour extraire les informations
  - Recréer les objets Client et CompteBancaire appropriés selon le type de compte (en utilisant les constructeurs complets créés aux exercices 3 à 6)
  - Ajouter ces comptes à la banque

Astuces :
- Utiliser **String.split(";")** pour découper la ligne
- Utiliser un `switch` sur le premier champ (le type de compte) pour savoir quel compte recréer
- Utiliser **LocalDate.parse()** pour parser la date
- Utiliser **Double.parseDouble()** pour convertir les nombres

> Questions (répondez en commentaire dans `chargerComptes()`) :
>
> - À la sauvegarde, on a pu se passer de `instanceof` grâce au polymorphisme. Pourquoi est-ce impossible ici, et pourquoi un `switch` sur le type est-il inévitable ?
> - Dans l'exemple de l'exercice 10, Jean Dupont possède deux comptes. Après rechargement, combien d'objets `Client` ont été créés pour lui ? `listerComptesClient()` retrouve-t-elle bien ses deux comptes ? Pourquoi ?
>
> Pensez à faire un commit !

### Exercice 12 : Gestion des erreurs de fichier (si vous avez le temps)

- Créer une exception personnalisée **FichierCorrompuException** qui hérite de **Exception**
- Modifier **chargerComptes()** pour :
  - Vérifier le format de chaque ligne (type de compte connu, bon nombre de champs, nombres et dates valides)
  - Lever une **FichierCorrompuException** si une ligne est mal formatée, avec un message qui indique le numéro et le contenu de la ligne qui pose problème
  - Sa signature devient donc `chargerComptes(String nomFichier) throws IOException, FichierCorrompuException`
- Remarque : `LocalDate.parse()` et `Double.parseDouble()` lèvent des exceptions *unchecked* (`DateTimeParseException` et `NumberFormatException`). Pensez à les capturer pour lever votre FichierCorrompuException à la place.

> Pensez à faire un commit !

### Exercice 13 : Programme principal complet (cœur)

Dans la classe **Exec**, créer un programme complet qui :
1. Crée une banque "Banque du CNAM"
2. Crée plusieurs clients
3. Crée différents types de comptes pour ces clients
4. Effectue plusieurs opérations :
  - Dépôts
  - Retraits (avec gestion des exceptions)
  - Virements entre comptes
  - Calcul et application des intérêts
5. Affiche le solde total de la banque
6. Sauvegarde tous les comptes dans un fichier "comptes.txt"
7. Crée une nouvelle instance de Banque (vide)
8. Charge les comptes depuis le fichier
9. Affiche les comptes chargés pour vérifier que tout fonctionne

Gérez toutes les exceptions avec des try/catch appropriés et des messages clairs pour l'utilisateur.

> Pensez à faire votre commit !
> Pensez à faire un push (`git push origin prenomNom`)
> Si elle n'est pas déjà ouverte, ouvrez une pull request (branche **prenomNom** vers **master**). NE PAS LA FERMER NI LA MERGER !

### Exercice 14 (bonus) : Historique des opérations

> **Petite introduction aux enum**
>
> Une **enum** (énumération) est un type qui ne peut prendre qu'un nombre fixe de valeurs, connues à l'avance. C'est idéal pour les jours de la semaine, les couleurs d'un jeu de cartes… ou les types d'opérations bancaires.
>
> On la déclare comme une classe, dans son propre fichier `TypeOperation.java`, avec le mot-clé `enum` :
>
> ```java
> public enum TypeOperation {
>     DEPOT, RETRAIT, VIREMENT_EMIS, VIREMENT_RECU
> }
> ```
>
> On l'utilise ensuite comme n'importe quel type :
>
> ```java
> TypeOperation type = TypeOperation.DEPOT;
>
> if (type == TypeOperation.DEPOT) {   // on compare avec == (chaque valeur n'existe qu'en un seul exemplaire)
>     System.out.println("C'est un dépôt");
> }
>
> ```
>
> Pourquoi ne pas simplement utiliser un `String` comme `"DEPOT"` ? Parce qu'avec un String, rien n'empêche d'écrire `"DEPOOT"` ou `"depot"` : l'erreur ne sera découverte qu'à l'exécution, si elle l'est. Avec une enum, `TypeOperation.DEPOOT` ne compile pas : **le compilateur vérifie pour vous** que la valeur existe.
>
> Pour la sauvegarde dans un fichier, deux méthodes vous seront utiles :
>
> ```java
> String texte = type.name();                                 // TypeOperation → "DEPOT"
> TypeOperation t = TypeOperation.valueOf("VIREMENT_RECU");   // "VIREMENT_RECU" → TypeOperation
> ```
>
> Attention : `valueOf()` lève une `IllegalArgumentException` si la chaîne ne correspond à aucune valeur de l'enum. Exception checked ou unchecked ? Et dans le cas d'un fichier corrompu, que faudrait-il en faire ?

- Créer une **enum TypeOperation** avec les valeurs DEPOT, RETRAIT, VIREMENT_EMIS, VIREMENT_RECU
- Créer une classe **Operation** avec :
  - `date` (LocalDateTime)
  - `type` (TypeOperation)
  - `montant` (double)
  - `description` (String)
- Ajouter dans CompteBancaire un attribut **historique** (`Operation[]`, de capacité 100) et un compteur `nbOperations`, sur le même principe que les comptes de la Banque (si l'historique est plein, lever une IllegalStateException, ou mieux, agrandir le tableau comme dans le « Pour aller plus loin » de l'exercice 7)
- Modifier toutes les méthodes de transaction pour enregistrer les opérations. Attention : un virement doit apparaître comme VIREMENT_EMIS / VIREMENT_RECU, et pas en plus comme un RETRAIT et un DEPOT.
> **Le piège du virement** : un virement appelle `retirer()` sur le compte source et `deposer()` sur le compte destination. Si vous enregistrez l'opération dans ces deux méthodes, l'historique affichera RETRAIT et DEPOT au lieu de VIREMENT_EMIS et VIREMENT_RECU. Et vous ne pouvez pas contourner `retirer()`, sinon vous perdez les contrôles de découvert et de plafond !
>
> Deux pistes :
>
> - **Surcharge** : ajoutez des versions `deposer(double montant, TypeOperation type)` et `retirer(double montant, TypeOperation type)`. Les versions sans type appellent simplement celles-ci avec `DEPOT` ou `RETRAIT`, et `effectuerVirement()` appelle directement les versions avec type.
> - **Pour les plus courageux** : transformez `retirer()` en méthode concrète de `CompteBancaire` qui (1) appelle une méthode abstraite `verifierRetrait(double montant)` implémentée par chaque classe fille, (2) débite le compte, puis (3) enregistre l'opération. L'historique n'est alors géré qu'à un seul endroit. Ce schéma (« une méthode concrète de la classe mère qui appelle des méthodes abstraites redéfinies par les filles ») s'appelle le *patron de méthode* (Template Method).
>
> N'oubliez pas les intérêts : `appliquerInterets()` modifie aussi le solde. Ajoutez une valeur `INTERETS` à votre enum.
- Ajouter une méthode **afficherHistorique()** dans CompteBancaire
- Ajouter la sauvegarde et le chargement de l'historique (par exemple dans un second fichier `operations.txt`)

> Pensez à faire un commit !

### Exercice 15 (bonus) : Statistiques

Dans la classe **Banque**, ajouter des méthodes :
- **getCompteLePlusRiche()** : retourne le compte avec le solde le plus élevé
- **getCompteAvecDecouvertMaximum()** : retourne le compte courant dont le solde est le plus négatif, ou `null` si aucun compte courant n'est à découvert
- **calculerInteretsTotaux()** : calcule la somme des intérêts de tous les comptes (une seule boucle suffit grâce au polymorphisme)
- **genererRapport(String nomFichier)** : génère un fichier rapport avec toutes ces statistiques

> Pensez à faire un commit !

---

## Critères d'évaluation

- Respect de l'héritage et du polymorphisme
- Gestion correcte des exceptions
- Lecture et écriture de fichiers fonctionnelles
- Code propre et bien structuré
- Tests complets dans la méthode main
- Commits réguliers et bien nommés
