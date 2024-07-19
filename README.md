## Projet Architecture hexagonale

### Source
 * https://blog.octo.com/architecture-hexagonale-trois-principes-et-un-exemple-dimplementation

### run

```
mvn clean install -U
cd infrastructure
mvn spring-boot:run

curl -v --header "Content-Type: application/json" \
--request POST \
--data '{"id":1,"dateSaillie":null,"dateNaissance":"01/01/2021"}' \
http://localhost:1977/validateGeniteur

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