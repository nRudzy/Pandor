package ucbl.ptm.projet.tests.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ucbl.ptm.projet.dao.UserDAO;
import ucbl.ptm.projet.modele.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

import ucbl.ptm.projet.dao.RubriqueDAO;
import ucbl.ptm.projet.modele.Rubrique;


public class TestUserDAO {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserDAO userDAO;
    private RubriqueDAO rubriqueDAO;
    private String userDAOMail = "test@junit.fr";

    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        userDAO = new UserDAO(em);
        rubriqueDAO = new RubriqueDAO(em);

        if (userDAO.getUserByEmail(userDAOMail) != null) {
            em.getTransaction().begin();
            em.remove(userDAO.getUserByEmail(userDAOMail));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        userDAO.createUser(userDAOMail, "jUnit", "John", "jUnit", "h4sH3dP4sS", "p1909999", "Etudiant");
        em.getTransaction().commit();


        if (rubriqueDAO.getRubriqueByNom("jUnit") != null) {
            em.getTransaction().begin();
            em.remove(rubriqueDAO.getRubriqueByNom("jUnit"));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        rubriqueDAO.createRubrique("jUnit", "jUnit");
        em.getTransaction().commit();
    }

    @After
    public void cleanAll() {
        if (userDAO.getUserByEmail("test@junit.fr") != null) {
            em.getTransaction().begin();
            em.remove(userDAO.getUserByEmail("test@junit.fr"));
            em.getTransaction().commit();
        }

        if (rubriqueDAO.getRubriqueByNom("jUnit") != null) {
            em.getTransaction().begin();
            em.remove(rubriqueDAO.getRubriqueByNom("jUnit"));
            em.getTransaction().commit();
        }

        em.close();
        em = null;
    }

    @Test
    public void getUserByEmailTest() {
        User u = userDAO.getUserByEmail(userDAOMail);
        assertEquals(userDAOMail, u.getEmail());
    }

    @Test
    public void getUserByNumTest() {
        User u = userDAO.getUserByNumEtu("p1909999");
        assertEquals("p1909999", u.getNumEtu());

    }

    @Test
    public void getUserByPseudoTest() {
        User u = userDAO.getUserByPseudo("jUnit");
        assertEquals("jUnit", u.getPseudo());

    }

    @Test
    public void getUserByPseudoAndHashMdpTest() {
        User u = userDAO.getUserByPseudoAndHashMdp("jUnit", "h4sH3dP4sS");
        assertEquals("jUnit", u.getPseudo());
        assertEquals("h4sH3dP4sS", u.getHashMdp());

    }

    @Test
    public void getAllUserTest() {
        List<User> l = userDAO.getAllUser();
        boolean found = false;

        for (User u : l) {
            if (u.getPseudo().equals("jUnit")) {
                found = true;
            }
        }

        assertTrue(found);
    }

    @Test
    public void promoteAdminTest() {
        User u = userDAO.getUserByEmail(userDAOMail);

        assertFalse(u.getIsAdmin());

        userDAO.promoteAdmin(u);

        assertTrue(u.getIsAdmin());
    }

    @Test
    public void demoteAdminTest() {
        User u = userDAO.getUserByEmail(userDAOMail);

        assertFalse(u.getIsAdmin());

        userDAO.promoteAdmin(u);

        assertTrue(u.getIsAdmin());

        userDAO.demoteAdmin(u);

        assertFalse(u.getIsAdmin());

    }

    @Test
    public void promoteModerateurTest() {
        User u = userDAO.getUserByEmail(userDAOMail);
        Rubrique r = rubriqueDAO.getRubriqueByNom("jUnit");

        assertTrue(u.getAllModeratedRubrique().isEmpty());

        userDAO.promoteModeratorOfRubrique(u, r);

        boolean found = false;

        for (Rubrique rr : u.getAllModeratedRubrique()) {
            if (rr.getNom().equals("jUnit")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void removeModerateurTest() {
        User u = userDAO.getUserByEmail(userDAOMail);
        Rubrique r = rubriqueDAO.getRubriqueByNom("jUnit");

        assertTrue(u.getAllModeratedRubrique().isEmpty());

        userDAO.promoteModeratorOfRubrique(u, r);

        assertTrue(u.getAllModeratedRubrique().contains(r));

        userDAO.removeModeratorOfRubrique(u, r);

        assertFalse(u.getAllModeratedRubrique().contains(r));
    }

}
