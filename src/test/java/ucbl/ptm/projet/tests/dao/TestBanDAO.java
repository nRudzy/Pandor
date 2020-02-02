package ucbl.ptm.projet.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucbl.ptm.projet.dao.BanDAO;
import ucbl.ptm.projet.dao.UserDAO;
import ucbl.ptm.projet.modele.Ban;
import ucbl.ptm.projet.modele.User;

public class TestBanDAO {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserDAO usrDAO;
    private BanDAO banDAO;
    private String motif = "Junit";
    private String emailBanner = "banTest@junit.com";
    private String emailBanni = "banni@junit.com";


    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        usrDAO = new UserDAO(em);
        banDAO = new BanDAO(em);
        User banni = usrDAO.getUserByEmail(emailBanni);

        if (banDAO.getBanByUserBanni(banni) != null) {
            em.getTransaction().begin();
            em.remove(banDAO.getBanByUserBanni(banni));
            em.getTransaction().commit();
        }

        if (usrDAO.getUserByEmail(emailBanni) != null) {
            em.getTransaction().begin();
            em.remove(usrDAO.getUserByEmail(emailBanni));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        banni = usrDAO.createUser(emailBanni, "banni", "Ben", "BB", "S3cr3t!", "p1234569", "Etudiant");
        em.getTransaction().commit();

        if (usrDAO.getUserByEmail(emailBanner) != null) {
            em.getTransaction().begin();
            em.remove(usrDAO.getUserByEmail(emailBanner));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        User userAdmin = usrDAO.createUser(emailBanner, "banner", "Bruce", "elBatoLoco", "m0d3p4sse", "p4567891", "Etudiant");
        usrDAO.promoteAdmin(userAdmin);
        em.getTransaction().commit();


        em.getTransaction().begin();
        banDAO.createBan(userAdmin, banni, 5, motif);
        em.getTransaction().commit();
    }

    @After
    public void cleanAll() {
        User banni = usrDAO.getUserByEmail(emailBanni);
        User banner = usrDAO.getUserByEmail(emailBanner);

        if (banDAO.getBanByUserBanni(banni) != null) {
            em.getTransaction().begin();
            em.remove(banDAO.getBanByUserBanni(banni));
            em.getTransaction().commit();
        }

        if (banner != null) {
            em.getTransaction().begin();
            em.remove(banner);
            em.getTransaction().commit();
        }
        if (banni != null) {
            em.getTransaction().begin();
            em.remove(banni);
            em.getTransaction().commit();
        }


        em.close();
        em = null;
    }

    @Test
    public void getBanByUserBanniTest() {
        User banni = usrDAO.getUserByEmail(emailBanni);
        Ban b = banDAO.getBanByUserBanni(banni);

        assertEquals(banni, b.getBanni());
    }

    @Test
    public void removeBanTest() {
        User banni = usrDAO.getUserByEmail(emailBanni);
        em.getTransaction().begin();
        banDAO.removeBan(banni);
        em.getTransaction().commit();
        Ban b = banDAO.getBanByUserBanni(banni);
        assertNull(b);
    }

}

