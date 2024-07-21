CREATE TABLE TYP_DEMANDE_INSCRI_LOF (
    IDENT_TYP_DEMANDE_INSCRI_LOF NUMBER(28),
    LIBELLE VARCHAR2(80)
);

INSERT INTO TYP_DEMANDE_INSCRI_LOF (IDENT_TYP_DEMANDE_INSCRI_LOF, LIBELLE) VALUES (537,'DESCENDANCE');
INSERT INTO TYP_DEMANDE_INSCRI_LOF (IDENT_TYP_DEMANDE_INSCRI_LOF, LIBELLE) VALUES (761,'PROVISOIRE');

CREATE TABLE RRACE (
    IDENT_RRACE NUMBER(28)
    , NB_AGE_MINI_CONFIRMATION NUMBER(28)
);

INSERT INTO RRACE (IDENT_RRACE, NB_AGE_MINI_CONFIRMATION) VALUES (56, 12);

CREATE TABLE RCHIEN (
    IDENT_RCHIEN NUMBER(28)
    , IDENT_RRACE NUMBER(28)
    , IDENT_TYP_DEMANDE_INSCRI_LOF NUMBER(28)
    , DATE_NAISSANCE    DATE
    , DATE_DECES    DATE
    , ON_SEXE_MALE  VARCHAR2(1)
);

INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (1, 56, 537, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (2, 56, 537, TO_DATE('01/01/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (3, 56, 537, TO_DATE('01/08/2023','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (4, 56, 537, TO_DATE('01/08/2020','DD/MM/YYYY'), TO_DATE('01/08/2023','DD/MM/YYYY'),'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (5, 56, 761, TO_DATE('01/01/2022','DD/MM/YYYY'), NULL,'N');
INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE, IDENT_TYP_DEMANDE_INSCRI_LOF, DATE_NAISSANCE, DATE_DECES, ON_SEXE_MALE) VALUES (6, 56, 537, TO_DATE('01/01/2001','DD/MM/YYYY'), NULL,'N');
