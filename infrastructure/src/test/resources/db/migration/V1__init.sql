CREATE TABLE RRACE (
    IDENT_RRACE NUMBER(28)
    , NB_AGE_MINI_CONFIRMATION NUMBER(28)
);

INSERT INTO RRACE (IDENT_RRACE, NB_AGE_MINI_CONFIRMATION) VALUES (56, 12);

CREATE TABLE RCHIEN (
    IDENT_RCHIEN NUMBER(28)
    , IDENT_RRACE NUMBER(28)
);

INSERT INTO RCHIEN (IDENT_RCHIEN, IDENT_RRACE) VALUES (1, 56);