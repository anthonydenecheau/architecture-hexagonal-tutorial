CREATE TABLE TYP_PAYS (
    IDENT_TYP_PAYS NUMBER(28),
    LIBELLE VARCHAR2(80)
);

INSERT INTO TYP_PAYS (IDENT_TYP_PAYS, LIBELLE) VALUES (234,'FRANCE');
INSERT INTO TYP_PAYS (IDENT_TYP_PAYS, LIBELLE) VALUES (216,'BELGIQUE');
INSERT INTO TYP_PAYS (IDENT_TYP_PAYS, LIBELLE) VALUES (12528,'GUADELOUPE');

CREATE TABLE RPERSONNE (
    IDENT_RPERSONNE NUMBER(28),
    XCODE_POSTAL    VARCHAR2(80),
    IDENT_TYP_PAYS  NUMBER(28)
);

INSERT INTO RPERSONNE (IDENT_RPERSONNE, XCODE_POSTAL, IDENT_TYP_PAYS) VALUES (1, '44', 234);
INSERT INTO RPERSONNE (IDENT_RPERSONNE, XCODE_POSTAL, IDENT_TYP_PAYS) VALUES (2, '44', 234);

CREATE TABLE RPERSONNE_LITIGE (
    IDENT_RPERSONNE_LITIGE    NUMBER(28),
    IDENT_RPERSONNE           NUMBER(28),
    IDENT_TYP_LITIGE_PERS NUMBER(28),
    DATE_OUVERTURE  DATE,
    DATE_FERMETURE  DATE
);

CREATE TABLE RELEV (
    IDENT_RELEV     NUMBER(28),
    IDENT_RPERSONNE NUMBER(28)
);    

INSERT INTO RELEV (IDENT_RELEV, IDENT_RPERSONNE) VALUES (1, 1);
INSERT INTO RELEV (IDENT_RELEV, IDENT_RPERSONNE) VALUES (2, 2);

CREATE TABLE RELEV_LITIGE (
    IDENT_RELEV_LITIGE NUMBER(28),
    IDENT_RELEV     NUMBER(28),
    IDENT_TYP_LITIGE_ELEV NUMBER(28),
    DATE_OUVERTURE  DATE,
    DATE_FERMETURE  DATE
);

INSERT INTO RELEV_LITIGE (IDENT_RELEV_LITIGE, IDENT_RELEV, IDENT_TYP_LITIGE_ELEV, DATE_OUVERTURE, DATE_FERMETURE) VALUES (1, 2, 2662, TO_DATE('01/01/2020','DD/MM/YYYY'), NULL);

CREATE TABLE TYP_DEMANDE_INSCRI_LOF (
    IDENT_TYP_DEMANDE_INSCRI_LOF NUMBER(28),
    LIBELLE VARCHAR2(80)
);

INSERT INTO TYP_DEMANDE_INSCRI_LOF (IDENT_TYP_DEMANDE_INSCRI_LOF, LIBELLE) VALUES (537,'DESCENDANCE');
INSERT INTO TYP_DEMANDE_INSCRI_LOF (IDENT_TYP_DEMANDE_INSCRI_LOF, LIBELLE) VALUES (890,'PROVISOIRE');

CREATE TABLE RRACE (
    IDENT_RRACE NUMBER(28)
    , NOM_RACE_FRANCAIS VARCHAR2(80)
    , DATE_DEROGATION_ADN   DATE
    , NB_AGE_MINI_CONFIRMATION NUMBER(28)
);

INSERT INTO RRACE (IDENT_RRACE, NOM_RACE_FRANCAIS, NB_AGE_MINI_CONFIRMATION) VALUES (56, 'AKITA', 12);

CREATE TABLE RCHIEN (
    IDENT_RCHIEN NUMBER(28)
    , NOM_CHIEN VARCHAR2(80)
    , IDENT_RCHIEN_PERE NUMBER(28)
    , IDENT_RCHIEN_MERE NUMBER(28)
    , IDENT_RRACE NUMBER(28)
    , XCODE_TATOUAGE VARCHAR2(25)
    , XCODE_TRANSPONDEUR VARCHAR2(25)
    , IDENT_TYP_DEMANDE_INSCRI_LOF NUMBER(28)
    , DATE_NAISSANCE    DATE
    , DATE_DECES    DATE
    , ON_SEXE_MALE  VARCHAR2(1)
    , NUM_DOSSIER_CONFIRMATION NUMBER(12)
    , NUM_CONFIRMATION NUMBER(12)
    , DATE_CONFIRMATION DATE
    , ON_APTE_CONFIRMATION VARCHAR2(1) DEFAULT 'N'
    , ON_APPEL_ENCOURS_CONF VARCHAR2(1) DEFAULT 'N'
    , ON_AJOURNE_CONFIRMATION VARCHAR2(1) DEFAULT 'N'
);

INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (1, 56, 101, 401, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), 'O', 'N', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (2, 56, 537, TO_DATE('01/01/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (3, 56, 537, TO_DATE('01/08/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (4, 56, 537, TO_DATE('01/08/2020','DD/MM/YYYY'), TO_DATE('01/08/2023','DD/MM/YYYY'),'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (5, 56, 890, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (6, 56, 537, TO_DATE('01/01/2001','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (7, 56, 537, TO_DATE('01/01/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (8, 56, 537, TO_DATE('01/01/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (9, 56, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, NULL, TO_DATE('01/01/2023','DD/MM/YYYY'), 'N', 'O', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (10, 56, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, NULL, TO_DATE('01/01/2023','DD/MM/YYYY'), 'N', 'N', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (11, 56, 101, 401, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), 'O', 'N', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (12, 56, 101, NULL, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), 'O', 'N', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (13, 56, 101, 402, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), 'O', 'N', 'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE
    , NUM_DOSSIER_CONFIRMATION, NUM_CONFIRMATION, DATE_CONFIRMATION, ON_APTE_CONFIRMATION, ON_APPEL_ENCOURS_CONF, ON_AJOURNE_CONFIRMATION) 
VALUES (14, 56, 101, 403, 538, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N'
    , 202300001, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), 'O', 'N', 'N');


-- Généalogie complète 
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (101, 'SUJET P', 537, 110, 210,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (110, 'SUJET PP', 537, 111, 121,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (111, 'SUJET PPP', 537, NULL, NULL,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (121, 'SUJET PPM', 537, NULL, NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (210, 'SUJET PM', 537, 211, 221,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (211, 'SUJET PMP', 537, NULL, NULL,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (221, 'SUJET PMM', 537, NULL, NULL,'N');

INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (401, 'SUJET M', 537, 410, 510,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (410, 'SUJET MP', 537, 411, 421,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (411, 'SUJET MPP', 537, NULL, NULL,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (421, 'SUJET MPM', 537, NULL, NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (510, 'SUJET MM', 537, 511, 521,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (511, 'SUJET MMP', 537, NULL, NULL,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (521, 'SUJET MMM', 537, NULL, NULL,'N');

INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (402, 56, 'SUJET M TITRE INITIAL', 761, NULL, NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, ON_SEXE_MALE)
VALUES (156, 56, 'CHIEN NON INSCRIT AUX LIVRES DES ORIGINES', 540,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, ON_SEXE_MALE)
VALUES (456, 56, 'CHIEN NON INSCRIT AUX LIVRES DES ORIGINES', 540,'N');

INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (403, 56, 'SUJET M IMPORT', 540, 413, 414,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (413, 56, 'SUJET MP IMPORT', 540, NULL, NULL,'O');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, NOM_CHIEN, IDENT_TYP_DEMANDE_INSCRI_LOF, IDENT_RCHIEN_PERE, IDENT_RCHIEN_MERE, ON_SEXE_MALE)
VALUES (414, 56, 'SUJET MM IMPORT', 540, NULL, NULL,'N');


CREATE TABLE RCHIEN_LITIGE (
    IDENT_RCHIEN_LITIGE    NUMBER(28),
    IDENT_RCHIEN           NUMBER(28),
    IDENT_TYP_LITIGE_CHIEN NUMBER(28),
    DATE_OUVERTURE  DATE,
    DATE_FERMETURE  DATE
);

INSERT INTO RCHIEN_LITIGE (IDENT_RCHIEN_LITIGE, IDENT_RCHIEN, IDENT_TYP_LITIGE_CHIEN, DATE_OUVERTURE, DATE_FERMETURE) 
    VALUES (1, 8, 1, TO_DATE('01/01/2023','DD/MM/YYYY'), NULL);

CREATE TABLE RPROPRIO_CHIEN (
    IDENT_RPROPRIO  NUMBER(28),
    IDENT_RCHIEN    NUMBER(28)
);    

INSERT INTO RPROPRIO_CHIEN (IDENT_RPROPRIO, IDENT_RCHIEN) VALUES (1, 1);

CREATE TABLE RPROPRIO (
    IDENT_RPROPRIO  NUMBER(28),
    IDENT_RPERSONNE NUMBER(28)
);    

INSERT INTO RPROPRIO (IDENT_RPROPRIO, IDENT_RPERSONNE) VALUES (1, 1);

CREATE TABLE TYP_STATUTDOSSIERSAILLIE (
    IDENT_TYP_STATUTDOSSIERSAILLIE NUMBER(28),
    LIBELLE VARCHAR2(80)
);

INSERT INTO TYP_STATUTDOSSIERSAILLIE (IDENT_TYP_STATUTDOSSIERSAILLIE, LIBELLE) VALUES (582,'DS SAISIE');
INSERT INTO TYP_STATUTDOSSIERSAILLIE (IDENT_TYP_STATUTDOSSIERSAILLIE, LIBELLE) VALUES (584,'DI SAISIE');
INSERT INTO TYP_STATUTDOSSIERSAILLIE (IDENT_TYP_STATUTDOSSIERSAILLIE, LIBELLE) VALUES (10046,'DS FORCLOS');

CREATE TABLE RSAILLIE (
    NUM_DOSSIER_SAILLIE NUMBER(28)
    , IDENT_TYP_STATUTDOSSIERSAILLIE    NUMBER(28)
    , DATE_SAILLIE  DATE
    , IDENT_RCHIEN_MERE NUMBER(28)
);

INSERT INTO RSAILLIE (NUM_DOSSIER_SAILLIE, IDENT_TYP_STATUTDOSSIERSAILLIE, DATE_SAILLIE, IDENT_RCHIEN_MERE) 
    VALUES (20230001, 582, TO_DATE('01/05/2024','DD/MM/YYYY'), 7);

CREATE TABLE RADN_EMPREINTE (
    IDENT_RADN_EMPREINTE    NUMBER(28)
    , IDENT_RCHIEN  NUMBER(28)
    , ON_ACTIF  VARCHAR2(1) DEFAULT 'N'
    , ON_FILIATION VARCHAR2(1) DEFAULT 'N'
);

CREATE TABLE RADN_EMPREINTE_SNP_P1 (
    IDENT_RADN_EMPREINTE_SNP_P1    NUMBER(28)
    , IDENT_RCHIEN  NUMBER(28)
    , ON_ACTIF  VARCHAR2(1) DEFAULT 'N'
    , ON_FILIATION VARCHAR2(1) DEFAULT 'N'
);

INSERT INTO RADN_EMPREINTE (IDENT_RADN_EMPREINTE, IDENT_RCHIEN, ON_ACTIF, ON_FILIATION) VALUES (1,1,'O','O');
INSERT INTO RADN_EMPREINTE (IDENT_RADN_EMPREINTE, IDENT_RCHIEN, ON_ACTIF, ON_FILIATION) VALUES (2,11,'O','N');
