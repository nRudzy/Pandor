TRUNCATE pdr_user CASCADE;
TRUNCATE pdr_message CASCADE;
TRUNCATE pdr_rubrique CASCADE;
TRUNCATE pdr_topic CASCADE;
TRUNCATE pdr_tag CASCADE;
TRUNCATE pdr_ban CASCADE;

DO
$$
    BEGIN
        PERFORM pdr_add_rubrique('Modération', 'Sert à contacter les administrateurs');
    END
$$;

DO
$$
    BEGIN
		PERFORM pdr_add_user('Administrateur@admin', 'Administrateur', 'Administrateur', 'Administrateur',
                             'b651763c0408b8e4544b6b40bbe023d29180c1d7719345ad2b07516394a5fc87116be265801f773bfd43fbaa2e2103e57b6f7ae8d09f2d61114b5390329753d9',
                             '00000000', 'Administrateur', true);
    END
$$;

DO
$$
    BEGIN
        PERFORM pdr_add_topic(
                        (
                            SELECT pdr_add_message(
                                           (
                                               SELECT id
                                               FROM pdr_user
                                               WHERE pseudo = 'Administrateur'
                                           ),
                                           NULL,
                                           'Pandor est le forum de l''Université Lyon 1 :
										   <br>En tant que visiteur vous pouvez uniquement visiter le site.
										   <br>En tant que membre vous pouvez créer des topics ou y répondre.
										   <br>En tant que modérateur vous pouvez supprimer les messages de certains utilisateurs.
										   <br>En tant qu''administrateur vous avez un panneau exclusif permetant la gestion du site.')
                        ),
                        (SELECT id FROM pdr_rubrique WHERE nom = 'Modération'),
                        'Bienvenue'
                    );
    END
$$;

DO
$$
    BEGIN
        PERFORM pdr_set_tag(
                        (
                            SELECT id
                            FROM pdr_topic
                            WHERE rubrique = (SELECT id FROM pdr_rubrique WHERE nom = 'Modération')
                              AND titre = 'Bienvenue'
                        ),
                        'bienvenue'
                    );
    END
$$;

DO
$$
    BEGIN
        PERFORM pdr_set_tag(
                        (
                            SELECT id
                            FROM pdr_topic
                            WHERE rubrique = (SELECT id FROM pdr_rubrique WHERE nom = 'Modération')
                              AND titre = 'Bienvenue'
                        ),
                        'règles'
                    );
    END
$$;