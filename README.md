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
