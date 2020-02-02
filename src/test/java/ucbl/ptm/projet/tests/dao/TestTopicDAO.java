package ucbl.ptm.projet.tests.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.Topic;
import ucbl.ptm.projet.modele.User;
import ucbl.ptm.projet.dao.MessageDAO;
import ucbl.ptm.projet.dao.RubriqueDAO;
import ucbl.ptm.projet.dao.TopicDAO;
import ucbl.ptm.projet.dao.UserDAO;
import ucbl.ptm.projet.modele.Message;


public class TestTopicDAO {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private TopicDAO topicDAO;
    private MessageDAO msgDAO;
    private RubriqueDAO rubriqueDAO;
    private UserDAO usrDAO;
    private String titreTopic = "Topic test";
    private String usrEmail = "junit@mail.com";
    private String nomRubrique = "JUnit Rubrique";
    private String contenuMsg = "Ceci est un message test";


    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        rubriqueDAO = new RubriqueDAO(em);
        usrDAO = new UserDAO(em);
        msgDAO = new MessageDAO(em);
        topicDAO = new TopicDAO(em);
        User auteur = usrDAO.getUserByEmail(usrEmail);


        if (msgDAO.getMessageByAuteur(auteur) != null) {
            em.getTransaction().begin();
            em.remove(msgDAO.getMessageByAuteur(auteur));
            em.getTransaction().commit();
        }


        if (usrDAO.getUserByEmail(usrEmail) != null) {
            em.getTransaction().begin();
            em.remove(usrDAO.getUserByEmail(usrEmail));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        auteur = usrDAO.createUser(usrEmail, "Unit", "John", "jUnit1", "h4sHpass", "p1909989", "Etudiant");
        em.getTransaction().commit();

        em.getTransaction().begin();
        Message m = msgDAO.createMessage(auteur, contenuMsg, null);
        em.getTransaction().commit();

        if (rubriqueDAO.getRubriqueByNom(nomRubrique) != null) {
            em.getTransaction().begin();
            em.remove(rubriqueDAO.getRubriqueByNom(nomRubrique));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        Rubrique r = rubriqueDAO.createRubrique(nomRubrique, "Junit");
        em.getTransaction().commit();

        if (topicDAO.getTopicByTitre(titreTopic) != null) {
            em.getTransaction().begin();
            em.remove(topicDAO.getTopicByTitre(titreTopic));
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        topicDAO.createTopic(m, r, titreTopic);
        em.getTransaction().commit();
    }

    @After
    public void cleanAll() {

        Rubrique r = rubriqueDAO.getRubriqueByNom(nomRubrique);
        User u = usrDAO.getUserByEmail(usrEmail);
        Message m = msgDAO.getMessageByAuteur(u);
        Topic topic = topicDAO.getTopicByTitre(titreTopic);

        if (topic != null) {
            em.getTransaction().begin();
            em.remove(topic);
            em.getTransaction().commit();
        }
        if (m != null) {
            em.getTransaction().begin();
            em.remove(m);
            em.getTransaction().commit();
        }
        if (u != null) {
            em.getTransaction().begin();
            em.remove(u);
            em.getTransaction().commit();
        }
        if (r != null) {
            em.getTransaction().begin();
            em.remove(r);
            em.getTransaction().commit();
        }


        em.close();
        em = null;
    }

    @Test
    public void getTopicsByUserIdTest() {
        User auteur = usrDAO.getUserByEmail(usrEmail);
        List<Topic> t = topicDAO.getTopicsByUserId((int) auteur.getId());

        assertNotNull(t);
        assertEquals(t.get(0).getTitre(), titreTopic);
    }

    @Test
    public void getTopicByTitreTest() {
        Topic t = topicDAO.getTopicByTitre(titreTopic);
        assertEquals(titreTopic, t.getTitre());
    }

    @Test
    public void getAllTopicsTest() {
        List<Topic> lTopics = topicDAO.getAllTopics();
        assertNotNull(lTopics);
    }
}
