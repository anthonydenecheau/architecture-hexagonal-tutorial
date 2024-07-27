## Projet Architecture hexagonale

### Source
 * https://blog.octo.com/architecture-hexagonale-trois-principes-et-un-exemple-dimplementation

### run

```
mvn clean install -U
cd infrastructure
mvn spring-boot:run
```

### branch feature/step1

Structure du projet

* module domain `geniteur` (Business Logic)
```
 - | api 
    Application protocol interface == port
 - | config
    Configuration des règles du domain
 - | error
    Exception
 - | model
    Objets du domain
 - | spi
    Service protocol interface == port
```

* module infrastructure

```
 -| config
    Configuration des adapters
 -| controller (User-side)
    Adapter du port api
 -| repository (Server-side)
    Adapter du port spi
```

### branch feature/step2

Note : on commence toujours par implémenter le domain avant d'entreprendre l'implémentation des adapters.

La première règle sera de valider que le géniteur est née avant la saillie.\
Dans un premier temps, nous commencons par écrire un test simple `should_validate_date_naissance`.\
Dans un second temps, cette fonctionnalité est intégrée à notre entité métier `Geniteur`. Le test `should_validate_date_naissance_geniteur` valide son bon fonctionnement.

### branch feature/step3

Nous implémentons la partie port (User-side).\
La méthode `execute` définie dans l'interface `ValidateGeniteur`(port User-side) est implémentée par `GeniteurUseCase`.

### branch feature/step4

Nous implémentons la partie adapter (User-side).\
Dans le module `infrastructure`, l'objet `GeniteurRequest` est crée. Il implémente le port `ValidateGeniteur`.\
Un endpoint `/validateGeniteur` est crée pour valider la règle "le géniteur est née avant la saillie"

Dans un premier temps, nous mettons en place des règles de validation s/ l'objet `GeniteurRequest` en validant en amont le format des dates de saillie et de naissance transmises.\
Puis dans un second temps, nous validons la règle métier.

### branch feature/step5

Une nouvelle règle est donnée : pour une race donnée, le chien doit avoir un âge minimum pour être autorisé à reproduire.\
Cette information doit être extraite de la bdd.\
Il est l'heure de mettre en place la partie repository (port Service-side). Nous repartons donc dans le module `domain`.\
Note: à cette étape, nous imaginons une équipe dev qui travaille s/ le Front et donc pour ne pas les retarder dans leur développement, nous allons stubber cette information.

Nous implémentons la partie port (Server-side).\
La méthode `byGeniteurId` définie dans l'interface `RaceInventory`(port Server-side) est stubbée dans `RaceInventoryStub`.

Côté module infrastucture, pour que le Stub soit pris en charge, il faut ajouter une configuration dans nos tests
```
    @TestConfiguration
    @ComponentScan(
            basePackages = {"fr.scc.saillie.geniteur"},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }
```
Pour le démarrage de l'application, il faut également préciser la partie Stub
```
   @Configuration
   @ComponentScan(
         basePackages = {"fr.scc.saillie.geniteur"},
         includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class,Stub.class})})
   public class DomainConfiguration {
   }
```

### branch feature/step6

Nous implémentons la partie adapter (Server-side).\
Dans le module `infrastructure`, l'objet `RaceRepository` est crée. Il implémente le port `RaceInventory`.\
Nous désactivons le stub mis en place au step5
```
    @TestConfiguration
    @ComponentScan(
            basePackages = {"fr.scc.saillie.geniteur"},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})},
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RaceInventoryStub.class})})
    static class StubConfiguration {
    }
```

```
   @Configuration
   @ComponentScan(
         basePackages = {"fr.scc.saillie.geniteur"},
         includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class,Stub.class})})
   public class DomainConfiguration {
   }
```

Toujours depuis nos tests, nous mettons en place la partie Bdd (`testcontainers`, `flywaydb`)\
Nous faisons le choix d'une Bdd Oracle afin de pouvoir reprendre les packages Oracle dans lesquels sont intégrées des règles métier, règles qui pourront progressivement être migrées dans le module `domain`.

Nous définissions dans le fichier `application.properties` les éléments de connexion à la Bdd du `testcontainer`.\
Nous créons un fichier pour initialiser le schéma (`flywaydb`) via le fichier `V1__init.sql`.

Il nous reste à compléter le repository `RaceRepository`et le mapper qui va alimenter l'objet `Race` du domain.

### branch feature/step7

Avant de compléter les règles métier, arrêtons-nous un moment pour mettre en place la documentation.
* Java documentation
```
mvn javadoc:javadoc
```
La documentation est générée sous chacun des modules sous leur répetoire `target/site/apidocs`.

Références : [Javadoc](https://maven.apache.org/plugins/maven-javadoc-plugin/examples/aggregate.html)

* Swagger
```
http://localhost:1977/swagger-ui/index.html
```

### branch feature/step8

Nous revisons notre stratégie de gestion des anomalies.\
Le domain doit pouvoir retourner des messages de nature I(nfo), W(arning) ou E(rror).\
Certaines règles peuvent lever un avertissement ou plusieurs erreurs qu'il faudra présenter au client.\
L'ensemble des messages sera analysé par l'adapter pour retourner un code 200, 400 ou encore 500.

Selon le sexe du géniteur, les règles sont différentes.\
Pour plus de commodités, les données du géniteurs sont lues depuis le port server-side `GeniteurInventory`.\
Cela nous permet de simplifier la construction de l'objet `GeniteurRequest`.\
Par conséquent, l'envoi de la date de naissance n'est plus nécessaire; elle sera lue par la méthode `byId`.

Une opération de refactoring est effectuée sur nos tests.\
Nous y intégrons les tests d'intégration sous la classe `GeniteurApplicationITTests`.\
Cela nous permet de vérifier que l’ensemble des parties de notre application fonctionnent correctement ensemble, que ce soit coté `api` que du côté `spi`.

La classe `GeniteurControllerTest` est donc le test unitaire côté `api`.\
Les classes `RaceRepositoryTest` et `GeniteurRepositoryTest` sont les tests unitaires côté `spi`.


### branch feature/step9

Résumé des règles implémentées.
* Contrôle de l'absence de litiges sur l'éleveur
  * GeniteurUseCase
      - Level : Error
      - Code : 971
      - Message : l'éleveur a un litige
  * GeniteurUseCaseTest
      - should_not_authorize_eleveur_litiges
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndEleveurLitige_thenCorrectReponse
* Contrôle que le sexe annoncé est correct
  * GeniteurUseCase
      - Level : Error
      - Code : 910
      - Message : le géniteur n'est pas du bon sexe
  * GeniteurUseCaseTest
      - should_not_authorize_sexe
  * GeniteurApplicationITTests
      - [TODO]
* Contrôle la date de décès pour la femelle
  * GeniteurUseCase
      - Level : Error
      - Code : 940
      - Message : la lice est déclarée morte à la date de saillie
  * GeniteurUseCaseTest
      - should_not_authorize_femelle_deces
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndLiceDeces_thenCorrectReponse
* Contrôle du nombre maximum de portées autorisées pour la femelle
  * GeniteurUseCase
      - Level : Error
      - Code : 977
      - Message : la lice a déjà fait 8 portées avec des chiots inscrits au LOF
  * GeniteurUseCaseTest
      - should_not_authorize_lice_portees
  * GeniteurApplicationITTests
      - [TODO]
* Alerte s/ le maximum de portées autorisées pour la femelle
  * GeniteurUseCase
      - Level : Warning
      - Code : 978
      - Message : la portée sera la 8ème portée, ce sera donc la dernière portée pour la lice
  * GeniteurUseCaseTest
      - should_warning_lice_portees
  * GeniteurApplicationITTests
      - [TODO] 
* Contrôle du type d'inscription
  * GeniteurUseCase
      - Level : Error
      - Code : 950
      - Message : le géniteur est inscrit à titre provisoire
  * GeniteurUseCaseTest
      - should_not_authorize_type_inscription
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndInscriptionProvisoire_thenCorrectReponse
* Contrôle cohérence date saillie Vs date naissance
  * GeniteurUseCase
      - Level : Error
      - Code : 930
      - Message : le géniteur est née après la saillie
  * GeniteurUseCaseTest
      - should_not_authorize_geniteur
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndDateNaissanceErreur_thenCorrectReponse
* Contrôle le géniteur a bien l'âge requis pour effectuer une saillie
  * GeniteurUseCase
      - Level : Error
      - Code : 920
      - Message : le géniteur n'est pas en âge de reproduire
  * GeniteurUseCaseTest
      - should_not_authorize_age_minimum
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndTropJeune_thenCorrectReponse
* Contrôle la lice ne doit pas être âgée de plus de 9 mois
  * GeniteurUseCase
      - Level : Error
      - Code : 960
      - Message : la lice est trop âgée pour reproduire
  * GeniteurUseCaseTest
      - should_not_authorize_lice_age_maximum
  * GeniteurApplicationITTests
      - whenPostRequestAndValidGeniteurAndTropAgee_thenCorrectReponse
* Contrôle la lice n'a pas fait de saillie depuis 5 mois
  * GeniteurUseCase
      - Level : Error
      - Code : 975
      - Message : une saillie a déjà eu lieu lors des 5 derniers mois pour cette lice
  * GeniteurUseCaseTest
      - should_not_authorize_lice_saillie
  * GeniteurApplicationITTests
      - whenPostRequestAndValidLiceSaillie_thenCorrectReponse
* Contrôle des litiges s/ le propriétaire
  * GeniteurUseCase
      - Level : Error
      - Code : 976
      - Message : le propriétaire du géniteur a un litige
  * GeniteurUseCaseTest
      - N/A == ne peut pas être écrit car pose un litige s/ la classe `Personne` sans aucune distinction selon le profil
  * GeniteurApplicationITTests
      - N/A
* Contrôle des litiges s/ le géniteur
  * GeniteurUseCase
      - Level : Error
      - Code : 972
      - Message : le géniteur possède des litiges
  * GeniteurUseCaseTest
      - should_not_authorize_geniteur_litiges
  * GeniteurApplicationITTests
      - [TODO]
* Contrôle des information de la confirmation
  * le chien s'est présenté à une séance de confirmation mais le chien est en appel de sa confirmation
    - GeniteurUseCase
        - Level : Error
        - Code : 973
        - Message : le géniteur a un appel sur la confirmation
    - GeniteurUseCaseTest
        - should_not_authorize_appel_confirmation
    - GeniteurApplicationITTests
        - [TODO]
  * le chien s'est présenté à une séance de confirmation mais le chien est ajourné ou inapte
    - GeniteurUseCase
        - Level : Error
        - Code : 974
        - Message : le géniteur a été ajourné ou déclaré inapte à la confirmation
    - GeniteurUseCaseTest
        - should_not_authorize_ajourne_confirmation
    - GeniteurApplicationITTests
        - [TODO]
  * le chien ne s'est jamais présenté à une séance de confirmation et ne répond à aucune des règles d'exception
    - GeniteurUseCase
        - Level : Error
        - Code : 970
        - Message : le géniteur n'est pas confirmé
    - GeniteurUseCaseTest
        - should_not_authorize_confirmation
    - GeniteurApplicationITTests
        - [TODO]
* Contrôle de l'enregistrement d'une généalogie complète
  * GeniteurUseCase
      - [TODO]
  * GeniteurUseCaseTest
      - [TODO]
  * GeniteurApplicationITTests
      - [TODO]
* Contrôle de l'enregistrement empreinte ADN
  * GeniteurUseCase
      - [TODO]
  * GeniteurUseCaseTest
      - [TODO]
  * GeniteurApplicationITTests
      - [TODO]


Note : règles d'exception d'un géniteur non confirmé
* le géniteur est une femelle et l'éleveur déclarant réside dans les DOMTOM ou à l'étranger 
* le géniteur est un mâle et le propriétaire réside dans les DOMTOM ou à l'étanger
* le géniteur est inscrit au titre du LIVRE D'ATTENTE
* le géniteur est un mâle et inscrit au titre du LIVRE ETRANGER

Note : Spi
* La classe `PersonneInventory`nous permet d'extraire les informations éleveur/propriétaire.\
La méthode `byId` accepte selon que nous recherchons un éleveur, l'`idEleveur` de l'objet `GeniteurRequest` et l'`id` de l'objet `Geniteur` pour la lecture du propriétaire.\
Selon le profil, la présence de litiges sera lue soit s/ l'éleveur ou s/ le géniteur.

[TODO]
Créer un référentiel des messages