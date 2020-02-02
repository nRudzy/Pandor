package ucbl.ptm.projet.dao;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.util.List;

import ucbl.ptm.projet.modele.Rubrique;

public class RubriqueDAO {
    private EntityManager entityManager;

    public RubriqueDAO(EntityManager em) {
        entityManager = em;
    }

    public Rubrique createRubrique(String nom, String presentation) {
        Rubrique rubrique = new Rubrique(nom, presentation);
        entityManager.persist(rubrique);

        return rubrique;
    }

    public void removeRubrique(Rubrique r) {
        entityManager.remove(r);
    }

    public List<Rubrique> getAllRubriques() {
        TypedQuery<Rubrique> queryRubriques = entityManager.createQuery(
                "SELECT distinct r FROM Rubrique r",
                Rubrique.class);

        List<Rubrique> result;

        try {
            result = queryRubriques.getResultList();
        } catch (NoResultException e) {
            result = new ArrayList<>();
        }

        return result;
    }

    public Rubrique getRubriqueByNom(String nom) {
        TypedQuery<Rubrique> queryRubrique = entityManager.createQuery(
                "SELECT r FROM Rubrique r WHERE r.nom = ?1",
                Rubrique.class);

        Rubrique result;

        try {
            result = queryRubrique
                    .setParameter(1, nom)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    public Rubrique getRubriqueById(int id) {
        TypedQuery<Rubrique> queryRubrique = entityManager.createQuery(
                "SELECT r FROM Rubrique r WHERE r.id = ?1",
                Rubrique.class);

        Rubrique result;

        try {
            result = queryRubrique
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }
}
