package ucbl.ptm.projet.tests.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucbl.ptm.projet.modele.User;
import ucbl.ptm.projet.dao.MessageDAO;
import ucbl.ptm.projet.dao.UserDAO;
import ucbl.ptm.projet.modele.Message;


public class TestMessageDAO {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private String contenuMsg = "message test junit";
    private String usrEmail = "junit@mail.com";
    private UserDAO usrDAO;
    private MessageDAO msgDAO;

    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        usrDAO = new UserDAO(em);
        msgDAO = new MessageDAO(em);

        if (msgDAO.getMessageByContenu(contenuMsg) != null) {
            em.getTransaction().begin();
            em.remove(msgDAO.getMessageByContenu(contenuMsg));
            em.getTransaction().commit();
        }

        if (usrDAO.getUserByEmail(usrEmail) != null) {
            em.getTransaction().begin();
            em.remove(usrDAO.getUserByEmail(usrEmail));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        User u = usrDAO.createUser(usrEmail, "Unit", "John", "junit", "mo7depass3!", "p1909999", "Etudiant");
        em.getTransaction().commit();


        em.getTransaction().begin();
        msgDAO.createMessage(u, contenuMsg, null);
        em.getTransaction().commit();

    }

    @After
    public void cleanAll() {
        if (msgDAO.getMessageByAuteur(usrDAO.getUserByEmail(usrEmail)) != null) {
            em.getTransaction().begin();
            em.remove(msgDAO.getMessageByAuteur(usrDAO.getUserByEmail(usrEmail)));
            em.getTransaction().commit();
        }

        if (usrDAO.getUserByEmail(usrEmail) != null) {
            em.getTransaction().begin();
            em.remove(usrDAO.getUserByEmail(usrEmail));
            em.getTransaction().commit();
        }

        em.close();
        em = null;
    }

    @Test
    public void getMessageByContenuTest() {
        Message m = msgDAO.getMessageByContenu(contenuMsg);
        assertEquals(contenuMsg, m.getContenu());
    }

    @Test
    public void getMessageByAuteurTest() {
        User u = usrDAO.getUserByEmail(usrEmail);
        Message m = msgDAO.getMessageByAuteur(u);

        assertEquals(u, m.getAuteur());
    }

    @Test
    public void getMessagesByAuteurTest() {
        User u = usrDAO.getUserByEmail(usrEmail);
        List<Message> lMsg = msgDAO.getMessagesByAuteur(u);

        for (Message m : lMsg) {
            assertEquals(u, m.getAuteur());
        }
    }


}