package ucbl.ptm.projet.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucbl.ptm.projet.dao.RubriqueDAO;
import ucbl.ptm.projet.modele.Rubrique;

public class TestRubriqueDAO {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private RubriqueDAO daoRubrique;
    private String nom = "Rubrique test";
    private String presentation = "test presentation";


    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        daoRubrique = new RubriqueDAO(em);

        if (daoRubrique.getRubriqueByNom(nom) != null) {
            em.getTransaction().begin();
            em.remove(daoRubrique.getRubriqueByNom(nom));
        }

        em.getTransaction().begin();
        daoRubrique.createRubrique(nom, presentation);
        em.getTransaction().commit();
    }

    @After
    public void cleanAll() {

        if (daoRubrique.getRubriqueByNom(nom) != null) {
            em.getTransaction().begin();
            em.remove(daoRubrique.getRubriqueByNom(nom));
            em.getTransaction().commit();
        }

        em.close();
        em = null;
    }


    @Test
    public void getRubriqueByNomTest() {
        Rubrique r = daoRubrique.getRubriqueByNom(nom);
        assertEquals(nom, r.getNom());
    }

    @Test
    public void getAllRubriquesTest() {
        List<Rubrique> lRubriques = daoRubrique.getAllRubriques();
        boolean found = false;

        for (Rubrique r : lRubriques) {
            if (r.getNom().equals(nom)) {
                found = true;
            }
        }

        assertTrue(found);
    }


}