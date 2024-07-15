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

* module domain (Business Logic)
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

