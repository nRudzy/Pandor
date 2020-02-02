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

import ucbl.ptm.projet.modele.Tag;
import ucbl.ptm.projet.dao.TagDAO;


public class TestTagDAO {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private TagDAO daoTag;
    private String _nom = "Tagtest";

    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        daoTag = new TagDAO(em);


        if (daoTag.getTagByName(_nom) != null) {
            em.getTransaction().begin();
            em.remove(daoTag.getTagByName(_nom));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        daoTag.createTag(_nom);
        em.getTransaction().commit();

    }

    @After
    public void cleanAll() {

        if (daoTag.getTagByName(_nom) != null) {
            em.remove(daoTag.getTagByName(_nom));
        }

        em.close();
        em = null;
    }

    @Test
    public void getTagByNameTest() {
        Tag tag = daoTag.getTagByName(_nom);
        assertEquals(_nom, tag.getNom());
    }

    @Test
    public void getAllTagsTest() {

        List<Tag> lTags = daoTag.getAllTags();

        boolean found = false;

        for (Tag t : lTags) {
            if (t.getNom().equals(_nom)) {
                found = true;
            }
        }

        assertTrue(found);
    }

}