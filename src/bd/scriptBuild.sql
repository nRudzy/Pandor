------------------ TABLES ------------------

--------- ENTITIES ---------
DROP TABLE IF EXISTS pdr_user CASCADE;
CREATE TABLE pdr_user
(
    id      SERIAL       NOT NULL,
    email   VARCHAR(256) NOT NULL,
    nom     VARCHAR(64)  NOT NULL,
    prenom  VARCHAR(64)  NOT NULL,
    pseudo  VARCHAR(64)  NOT NULL,
    hashMdp VARCHAR(256) NOT NULL,
    numEtu  VARCHAR(8)   NOT NULL,
    titre   VARCHAR(64)  NOT NULL,
    isAdmin BOOLEAN      NOT NULL,

    CONSTRAINT pdr_user_pk PRIMARY KEY (id),

    UNIQUE (email),
    UNIQUE (pseudo),
    UNIQUE (numEtu)
);

DROP TABLE IF EXISTS pdr_message CASCADE;
CREATE TABLE pdr_message
(
    id         SERIAL        NOT NULL,
    auteur     INTEGER       NOT NULL,
    sousPseudo VARCHAR(64),
    contenu    VARCHAR(2048) NOT NULL,
    datePost   TIMESTAMP     NOT NULL,

    CONSTRAINT pdr_message_pk PRIMARY KEY (id),

    FOREIGN KEY (auteur) REFERENCES pdr_user (id)
);

DROP TABLE IF EXISTS pdr_rubrique CASCADE;
CREATE TABLE pdr_rubrique
(
    id           SERIAL        NOT NULL,
    nom          VARCHAR(64)   NOT NULL,
    presentation VARCHAR(2048) NOT NULL,

    CONSTRAINT pdr_rubrique_pk PRIMARY KEY (id),

    UNIQUE (nom)
);

DROP TABLE IF EXISTS pdr_topic CASCADE;
CREATE TABLE pdr_topic
(
    id       SERIAL       NOT NULL,
    msg      INTEGER      NOT NULL,
    rubrique INTEGER      NOT NULL,
    titre    VARCHAR(256) NOT NULL,

    CONSTRAINT pdr_topic_pk PRIMARY KEY (id),

    UNIQUE (msg, rubrique),
    UNIQUE (titre, rubrique),

    FOREIGN KEY (msg) REFERENCES pdr_message (id),
    FOREIGN KEY (rubrique) REFERENCES pdr_rubrique (id)
);

DROP TABLE IF EXISTS pdr_tag CASCADE;
CREATE TABLE pdr_tag
(
    id  SERIAL      NOT NULL,
    nom VARCHAR(64) NOT NULL,

    CONSTRAINT pdr_tag_pk PRIMARY KEY (id),

    UNIQUE (nom)
);

DROP TABLE IF EXISTS pdr_ban CASCADE;
CREATE TABLE pdr_ban
(
    id     SERIAL        NOT NULL,
    banner INTEGER       NOT NULL,
    banni  INTEGER       NOT NULL,
    fin    TIMESTAMP     NOT NULL,
    motif  VARCHAR(2048) NOT NULL,

    CONSTRAINT pdr_ban_pk PRIMARY KEY (id),

    UNIQUE (banni),

    FOREIGN KEY (banni) REFERENCES pdr_user (id),
    FOREIGN KEY (banner) REFERENCES pdr_user (id)
);

DROP TABLE IF EXISTS pdr_vote CASCADE;
CREATE TABLE pdr_vote
(
    id      SERIAL  NOT NULL,
    voteur  INTEGER NOT NULL,
    message INTEGER NOT NULL,
    quanta  INTEGER NOT NULL,

    CONSTRAINT pdr_vote_pk PRIMARY KEY (id),

    UNIQUE (voteur, message),

    FOREIGN KEY (voteur) REFERENCES pdr_user (id),
    FOREIGN KEY (message) REFERENCES pdr_message (id)
);

--------- RELATIONS ---------
DROP TABLE IF EXISTS pdr_est_tag CASCADE;
CREATE TABLE pdr_est_tag
(
    id_topic INTEGER NOT NULL,
    id_tag   INTEGER NOT NULL,

    CONSTRAINT pdr_est_tag_pk PRIMARY KEY (id_topic, id_tag),

    FOREIGN KEY (id_topic) REFERENCES pdr_topic (id),
    FOREIGN KEY (id_tag) REFERENCES pdr_tag (id)
);

DROP TABLE IF EXISTS pdr_est_modo CASCADE;
CREATE TABLE pdr_est_modo
(
    id_user     INTEGER NOT NULL,
    id_rubrique INTEGER NOT NULL,

    CONSTRAINT pdr_est_modo_pk PRIMARY KEY (id_user, id_rubrique),

    FOREIGN KEY (id_user) REFERENCES pdr_user (id),
    FOREIGN KEY (id_rubrique) REFERENCES pdr_rubrique (id)
);

DROP TABLE IF EXISTS pdr_est_commentaire CASCADE;
CREATE TABLE pdr_est_commentaire
(
    id_reponse INTEGER NOT NULL,
    id_message INTEGER NOT NULL,

    CONSTRAINT pdr_est_commentaire_pk PRIMARY KEY (id_reponse, id_message),

    FOREIGN KEY (id_reponse) REFERENCES pdr_message (id),
    FOREIGN KEY (id_message) REFERENCES pdr_message (id)
);

DROP TABLE IF EXISTS pdr_est_abonne CASCADE;
CREATE TABLE pdr_est_abonne
(
    id_user     INTEGER NOT NULL,
    id_rubrique INTEGER NOT NULL,

    CONSTRAINT pdr_est_abonne_pk PRIMARY KEY (id_user, id_rubrique),

    FOREIGN KEY (id_user) REFERENCES pdr_user (id),
    FOREIGN KEY (id_rubrique) REFERENCES pdr_rubrique (id)
);

------------------ FONCTIONS ------------------

--------- ADDER ---------
DROP FUNCTION IF EXISTS pdr_add_user CASCADE;
CREATE FUNCTION pdr_add_user(_email VARCHAR(256), _nom VARCHAR(64), _prenom VARCHAR(64), _pseudo VARCHAR(64),
                             _hashMdp VARCHAR(256), _numEtu VARCHAR(8), _titre VARCHAR(64), _isAdmin BOOLEAN)
    RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO pdr_user(email, nom, prenom, pseudo, hashMdp, numEtu, titre, isAdmin)
    VALUES (_email, _nom, _prenom, _pseudo, _hashMdp, _numEtu, _titre, _isAdmin);

    RETURN (SELECT CURRVAL('pdr_user_id_seq'));
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_add_message CASCADE;
CREATE FUNCTION pdr_add_message(_auteur INTEGER, _sousPseudo VARCHAR(64), _contenu VARCHAR(2048))
    RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO pdr_message(auteur, sousPseudo, contenu, datePost)
    VALUES (_auteur, _sousPseudo, _contenu, (SELECT NOW()));

    RETURN (SELECT CURRVAL('pdr_message_id_seq'));
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_add_rubrique CASCADE;
CREATE FUNCTION pdr_add_rubrique(_nom VARCHAR(64), _presentation VARCHAR(2048))
    RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO pdr_rubrique(nom, presentation)
    VALUES (_nom, _presentation);

    RETURN (SELECT CURRVAL('pdr_rubrique_id_seq'));
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_add_topic CASCADE;
CREATE FUNCTION pdr_add_topic(_msg INTEGER, _rubrique INTEGER, _titre VARCHAR(256))
    RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO pdr_topic(msg, rubrique, titre)
    VALUES (_msg, _rubrique, _titre);

    RETURN (SELECT CURRVAL('pdr_topic_id_seq'));
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_add_tag CASCADE;
CREATE FUNCTION pdr_add_tag(_nom VARCHAR(64))
    RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO pdr_tag(nom)
    VALUES (_nom);

    RETURN (SELECT CURRVAL('pdr_tag_id_seq'));
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_add_ban CASCADE;
CREATE FUNCTION pdr_add_ban(_banner INTEGER, _banni INTEGER, nbJours INTEGER, _motif VARCHAR(2048))
    RETURNS INTEGER
AS
$$
BEGIN
    IF ((SELECT pdr_user.isAdmin FROM pdr_user WHERE pdr_user.id = _banner)) THEN
        INSERT INTO pdr_ban(banner, banni, fin, motif)
        VALUES (_banner, _banni, (SELECT (NOW() + (SELECT make_interval(days => nbJours)))), _motif);

        RETURN (SELECT CURRVAL('pdr_ban_id_seq'));
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

--------- LINKER ---------
DROP FUNCTION IF EXISTS pdr_set_tag CASCADE;
CREATE FUNCTION pdr_set_tag(_id_topic INTEGER, _nom VARCHAR(64))
    RETURNS VOID
AS
$$
DECLARE
    _id_tag INTEGER;
BEGIN
    IF (SELECT pdr_tag.id FROM pdr_tag WHERE pdr_tag.nom = _nom) IS NULL THEN
        _id_tag = (SELECT pdr_add_tag(_nom));
    ELSE
        _id_tag = (SELECT pdr_tag.id FROM pdr_tag WHERE pdr_tag.nom = _nom);
    END IF;

    INSERT INTO pdr_est_tag(id_topic, id_tag)
    VALUES (_id_topic, _id_tag);
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_set_modo CASCADE;
CREATE FUNCTION pdr_set_modo(_id_user INTEGER, _id_rubrique INTEGER)
    RETURNS VOID
AS
$$
BEGIN
    INSERT INTO pdr_est_modo(id_user, id_rubrique)
    VALUES (_id_user, _id_rubrique);
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_set_commentaire CASCADE;
CREATE FUNCTION pdr_set_commentaire(_id_reponse INTEGER, _id_message INTEGER)
    RETURNS VOID
AS
$$
BEGIN
    INSERT INTO pdr_est_commentaire(id_reponse, id_message)
    VALUES (_id_reponse, _id_message);
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS pdr_set_abonne CASCADE;
CREATE FUNCTION pdr_set_abonne(_id_user INTEGER, _id_rubrique INTEGER)
    RETURNS VOID
AS
$$
BEGIN
    INSERT INTO pdr_est_abonne(id_user, id_rubrique)
    VALUES (_id_user, _id_rubrique);
END;
$$ LANGUAGE plpgsql;

------------------ VIEWS ------------------
DROP VIEW IF EXISTS pdr_get_user CASCADE;
CREATE VIEW pdr_get_user AS
SELECT *
FROM USER;

------------------ SEQUENCES ------------------
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence;
