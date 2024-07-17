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

### branch feature/step3

Nous implémentons la partie port (User-side).\
La méthode `execute` définie dans l'interface `ValidateGeniteur`(port User-side) est implémentée par `GeniteurUseCase`.

### branch feature/step4

Nous implémentons la partie adapter (User-side).\
Dans le module `infrastructure`, l'objet `GeniteurRequest` est crée.\
Un endpoint `/validateGeniteur` est crée pour valider la règle "le géniteur est née avant la saillie"

Dans un premier temps, nous mettons en place des règles de validation s/ l'objet `GeniteurRequest` en validant en amont le format des dates de saillie et de naissance transmises.\
Puis dans un second temps, nous validons la règle métier.