package ucbl.ptm.projet.test.metier;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ucbl.ptm.projet.modele.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

import ucbl.ptm.projet.metier.Pandor;

public class TestPandor {
    private static EntityManagerFactory emf;
    private EntityManager em;
    private Pandor pandor;

    @BeforeClass
    public static void setupEmf() {
        emf = Persistence.createEntityManagerFactory("pu-pandor");
    }

    @Before
    public void setupEm() {
        em = emf.createEntityManager();
        pandor = new Pandor(em);

        //Users
        pandor.removeUser(pandor.getUser("test@junit.fr"));
        pandor.removeUser(pandor.getUser("test2@junit.fr"));
        pandor.removeUser(pandor.getUser("test3@junit.fr"));

        pandor.addUser("test@junit.fr", "Unit", "John", "jUnit",
                "h4sH3dP4sS", "p1909999", "Etudiant");
        pandor.addUser("test2@junit.fr", "Unit", "John", "qwerty",
                "h4sH3dP4sS", "p1909998", "Etudiant");
        pandor.addUser("test3@junit.fr", "Unit", "John", "ertyui",
                "h4sH3dP4sS", "p1909997", "Etudiant");

        //Rubriques
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit"));
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit2"));
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit3"));

        pandor.addRubrique("jUnit", "jUnit");
        pandor.addRubrique("jUnit2", "jUnit2");
        pandor.addRubrique("jUnit3", "jUnit3");

        //Messages
        pandor.removeMessage(pandor.getMessage("content"));
        pandor.removeMessage(pandor.getMessage("content2"));
        pandor.removeMessage(pandor.getMessage("content3"));

        pandor.addMessage(pandor.getUser("test@junit.fr"),
                "content", "sousPseudo1");
        pandor.addMessage(pandor.getUser("test2@junit.fr"),
                "content2", "sousPseudo2");
        pandor.addMessage(pandor.getUser("test@junit.fr"),
                "content3", null);

        //Topics
        pandor.removeTopic(pandor.getTopic("topic1"));
        pandor.removeTopic(pandor.getTopic("topic2"));
        pandor.removeTopic(pandor.getTopic("topic3"));

        pandor.addTopic(pandor.getMessage("content"), pandor.getRubriqueByName("jUnit"), "topic1");
        pandor.addTopic(pandor.getMessage("content2"), pandor.getRubriqueByName("jUnit2"), "topic2");
        pandor.addTopic(pandor.getMessage("content3"), pandor.getRubriqueByName("jUnit"), "topic3");
    }

    @After
    public void cleanAll() {
        pandor.removeTopic(pandor.getTopic("topic1"));
        pandor.removeTopic(pandor.getTopic("topic2"));
        pandor.removeTopic(pandor.getTopic("topic3"));
        pandor.removeMessage(pandor.getMessage("content"));
        pandor.removeMessage(pandor.getMessage("content2"));
        pandor.removeMessage(pandor.getMessage("content3"));
        pandor.removeUser(pandor.getUser("test@junit.fr"));
        pandor.removeUser(pandor.getUser("test2@junit.fr"));
        pandor.removeUser(pandor.getUser("test3@junit.fr"));
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit"));
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit2"));
        pandor.removeRubrique(pandor.getRubriqueByName("jUnit3"));

        em.close();
        em = null;
    }


    ////// USER TESTS //////

    @Test
    public void getUserTest() {
        User user = pandor.getUser("test@junit.fr");
        assertEquals("test@junit.fr", user.getEmail());
        assertEquals("Unit", user.getNom());
        assertEquals("John", user.getPrenom());
        assertEquals("jUnit", user.getPseudo());
        assertEquals("h4sH3dP4sS", user.getHashMdp());
        assertEquals("p1909999", user.getNumEtu());
        assertEquals("Etudiant", user.getTitre());

        user = pandor.getUser((int) user.getId());
        assertEquals("test@junit.fr", user.getEmail());
        assertEquals("Unit", user.getNom());
        assertEquals("John", user.getPrenom());
        assertEquals("jUnit", user.getPseudo());
        assertEquals("h4sH3dP4sS", user.getHashMdp());
        assertEquals("p1909999", user.getNumEtu());
        assertEquals("Etudiant", user.getTitre());
    }

    @Test
    public void addRmUserTest() {
        pandor.addUser("1est3@junit.fr", "1nit", "1ohn", "1rtyui",
                "14sH3dP4sS", "11909997", "1tudiant");
        User user = pandor.getUser("1est3@junit.fr");
        assertEquals("1rtyui", user.getPseudo());
        assertEquals("1tudiant", user.getTitre());
        assertEquals("11909997", user.getNumEtu());
        assertEquals("14sH3dP4sS", user.getHashMdp());
        assertEquals("1ohn", user.getPrenom());
        assertEquals("1nit", user.getNom());
        assertEquals("1est3@junit.fr", user.getEmail());
        assertFalse(user.getIsAdmin());

        pandor.removeUser(pandor.getUser("1est3@junit.fr"));
        assertNull(pandor.getUser("1est3@junit.fr"));

        pandor.addUser("1est3@junit.fr", "1nit", "1ohn", "1rtyui",
                "14sH3dP4sS", "11909997", "1tudiant");
        assertNotNull(pandor.getUser("1est3@junit.fr"));

        pandor.removeUser("1est3@junit.fr");
        assertNull(pandor.getUser("1est3@junit.fr"));

        User user1 = pandor.addUser("1est3@junit.fr", "1nit", "1ohn", "1rtyui",
                "14sH3dP4sS", "11909997", "1tudiant");
        assertNotNull(pandor.getUser("1est3@junit.fr"));

        pandor.removeUser((int) user1.getId());
        assertNull(pandor.getUser("1est3@junit.fr"));

        pandor.removeUser(pandor.getUser("notusedEmail"));
    }

    @Test
    public void validateUserTest() {
        User user = pandor.getUser("test@junit.fr");
        User user1 = pandor.validateUser("jUnit", "h4sH3dP4sS");
        assertEquals(user, user1);
    }

    @Test
    public void emailTakenTest() {
        assertTrue(pandor.emailTaken("test3@junit.fr"));
        assertFalse(pandor.emailTaken("emailNotTaken@junit.fr"));
    }

    @Test
    public void pseudoTakenTest() {
        assertTrue(pandor.pseudoTaken("jUnit"));
        assertFalse(pandor.pseudoTaken("pseudoNotTaken"));
    }

    @Test
    public void numEtuTakenTest() {
        assertTrue(pandor.numEtuTaken("p1909998"));
        assertFalse(pandor.numEtuTaken("p11111111"));
    }

    @Test
    public void getAllUserTest() {
        List<User> l = pandor.getAllUser();
        assertTrue(l.contains(pandor.getUser("test@junit.fr")));
        assertTrue(l.contains(pandor.getUser("test2@junit.fr")));
        assertTrue(l.contains(pandor.getUser("test3@junit.fr")));
    }

    @Test
    public void getAllUserWithNameContainingTest() {
        List<User> usersToGet = new ArrayList<>();
        usersToGet.add(pandor.getUser("test@junit.fr"));
        assertEquals(usersToGet, pandor.getAllUserWithNameContaining("jUnit"));

        List<User> usersToGet2 = new ArrayList<>();
        usersToGet2.add(pandor.getUser("test2@junit.fr"));
        usersToGet2.add(pandor.getUser("test3@junit.fr"));
        assertEquals(usersToGet2, pandor.getAllUserWithNameContaining("ert"));
    }

    @Test
    public void setModeratorRmModeratorTest() {
        User u = pandor.getUser("test@junit.fr");
        Rubrique r = pandor.getRubriqueByName("jUnit");
        assertFalse(u.getAllModeratedRubrique().contains(r));

        pandor.setModerator(u, r);
        assertTrue(u.getAllModeratedRubrique().contains(r));

        pandor.rmModerator(u, r);
        assertFalse(u.getAllModeratedRubrique().contains(r));
    }

    @Test
    public void setAdminRmAdminTest() {
        User u = pandor.getUser("test@junit.fr");
        assertFalse(u.getIsAdmin());

        pandor.setAdmin(u);
        assertTrue(u.getIsAdmin());

        pandor.rmAdmin(u);
        assertFalse(u.getIsAdmin());
    }

    @Test
    public void modifierPseudoTest() {
        User u = pandor.getUser("test@junit.fr");
        assertEquals("jUnit", u.getPseudo());

        pandor.modifierPseudo(u, "newPseudo");
        assertEquals("newPseudo", u.getPseudo());

        pandor.modifierPseudo(u, "jUnit");
        assertEquals("jUnit", u.getPseudo());

        pandor.modifierPseudo(null, "test");
    }

    @Test
    public void updateTitleTest() {
        User u = pandor.getUser("test@junit.fr");
        assertEquals("Etudiant", u.getTitre());

        pandor.updateTitle(u, "newTitle");
        assertEquals("newTitle", u.getTitre());

        pandor.updateTitle(u, "Etudiant");
        assertEquals("Etudiant", u.getTitre());

        pandor.updateTitle(null, "stringTest");
    }

    @Test
    public void modifyMdpTest() {
        User u = pandor.getUser("test@junit.fr");
        assertEquals("h4sH3dP4sS", u.getHashMdp());

        pandor.modifierMdp(u, "newPasswordHash");
        assertEquals("newPasswordHash", u.getHashMdp());

        pandor.modifierMdp(u, "h4sH3dP4sS");
        assertEquals("h4sH3dP4sS", u.getHashMdp());

        pandor.modifierMdp(null, "testString");
    }

    @Test
    public void followUnFollowRubriqueTest() {
        User u = pandor.getUser("test@junit.fr");
        Rubrique r = pandor.getRubriqueByName("jUnit");
        assertFalse(u.getAbonneRubrique().contains(r));

        pandor.followRubrique(u, r);
        assertTrue(u.getAbonneRubrique().contains(r));

        pandor.unFollowRubrique(u, r);
        assertFalse(u.getAbonneRubrique().contains(r));

        List<User> users = r.getAbonneRubrique();
        pandor.followRubrique(null, r);
        assertEquals(users, r.getAbonneRubrique());
        pandor.unFollowRubrique(null, r);
        assertEquals(users, r.getAbonneRubrique());
    }

    ////// BAN TESTS //////

    @Test
    public void banUnBanUserTest() {
        User u = pandor.getUser("test@junit.fr");
        assertNull(pandor.getBanByUser(u));

        pandor.banUser(u, u, 1, "test");
        assertNotNull(pandor.getBanByUser(u));

        pandor.banUser(u, u, 5, "test");
        assertNotNull(pandor.getBanByUser(u));

        pandor.unBanUser(u);
        assertNull(pandor.getBanByUser(u));
    }

    @Test
    public void getBanByUserTest() {
        User u = pandor.getUser("test@junit.fr");
        User banner = pandor.getUser("test2@junit.fr");
        assertNull(pandor.getBanByUser(u));

        pandor.banUser(banner, u, 1, "motif");
        Ban ban = pandor.getBanByUser(u);
        assertSame(u, ban.getBanni());
        assertSame(banner, ban.getBanner());
        assertEquals("motif", ban.getMotif());

        pandor.unBanUser(u);
        assertNull(pandor.getBanByUser(u));
    }

    @Test
    public void isBannedTest() {
        User u = pandor.getUser("test@junit.fr");
        User banner = pandor.getUser("test2@junit.fr");
        assertFalse(pandor.isBanned(u));

        pandor.banUser(banner, u, 0, "motif");
        assertFalse(pandor.isBanned(u));

        pandor.banUser(banner, u, 1, "motif");
        assertTrue(pandor.isBanned(u));

        pandor.banUser(banner, u, 1000, "motif");
        assertTrue(pandor.isBanned(u));

        pandor.unBanUser(u);
        assertNull(pandor.getBanByUser(u));
    }

    ////// RUBRIQUE TESTS //////

    @Test
    public void getRubriqueByNameTest() {
        Rubrique r = pandor.getRubriqueByName("jUnit");
        assertEquals("jUnit", r.getNom());
        assertEquals("jUnit", r.getPresentation());

        assertNull(pandor.getRubriqueByName("NoRubrique"));
    }

    @Test
    public void rubriqueTakenTest() {
        assertTrue(pandor.rubriqueTaken("jUnit"));
        assertFalse(pandor.rubriqueTaken("NoRubrique"));

    }

    @Test
    public void getRubriqueTest() {
        Rubrique r = pandor.getRubriqueByName("jUnit");
        int id = (int) r.getId();
        assertSame(r, pandor.getRubrique(id));
    }

    @Test
    public void getAllRubriquesTest() {
        List<Rubrique> rubriques = pandor.getAllRubriques();
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit")));
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit2")));
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit3")));
        assertFalse(rubriques.contains(pandor.getRubriqueByName("jUnitNotExisting")));
    }

    @Test
    public void getAllRubriquesWithNameContainingTest() {
        List<Rubrique> rubriques = pandor.getAllRubriqueWithNameContaining("Unit");
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit")));
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit2")));
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit3")));
        assertFalse(rubriques.contains(pandor.getRubriqueByName("jUnitNotExisting")));

        rubriques = pandor.getAllRubriqueWithNameContaining("t2");
        assertFalse(rubriques.contains(pandor.getRubriqueByName("jUnit")));
        assertTrue(rubriques.contains(pandor.getRubriqueByName("jUnit2")));
        assertFalse(rubriques.contains(pandor.getRubriqueByName("jUnit3")));

    }

    @Test
    public void addRemoveRubriqueTest() {
        assertNull(pandor.getRubriqueByName("rubiqueTest"));

        pandor.addRubrique("rubriqueTest", "test");
        Rubrique r = pandor.getRubriqueByName("rubriqueTest");
        assertNotNull(r);

        pandor.removeRubrique("rubriqueTest");
        assertNull(pandor.getRubriqueByName("rubiqueTest"));

        pandor.addRubrique("rubriqueTest", "test");
        r = pandor.getRubriqueByName("rubriqueTest");
        assertNotNull(r);

        pandor.removeRubrique((int) r.getId());
        assertNull(pandor.getRubriqueByName("rubiqueTest"));
    }

    @Test
    public void modifierNomRubriqueTest() {
        Rubrique r = pandor.getRubriqueByName("jUnit");
        assertEquals("jUnit", r.getNom());

        pandor.modifierNomRubrique(r, "newNameRubrique");
        assertEquals("newNameRubrique", r.getNom());

        pandor.modifierNomRubrique(r, "jUnit");
        assertEquals("jUnit", r.getNom());

        pandor.modifierNomRubrique(null, "testString");
    }

    @Test
    public void modifierPresentationRubriqueTest() {
        Rubrique r = pandor.getRubriqueByName("jUnit");
        assertEquals("jUnit", r.getPresentation());

        pandor.modifierPresentationRubrique(r, "newPresentationRubrique");
        assertEquals("newPresentationRubrique", r.getPresentation());

        pandor.modifierPresentationRubrique(r, "jUnit");
        assertEquals("jUnit", r.getPresentation());

        pandor.modifierPresentationRubrique(null, "TestString");
    }

    ////// TOPIC TESTS //////

    @Test
    public void getTopicTest() {
        Topic topic1 = pandor.getTopic("topic1");
        assertEquals("topic1", topic1.getTitre());
        assertSame(pandor.getRubriqueByName("jUnit"), topic1.getRubrique());
        assertSame(pandor.getMessage("content"), topic1.getMsg());

        Message msg = pandor.addMessage(pandor.getUser("test@junit.fr"),"contentTest", "pseudo");
        Topic topic2 = pandor.addTopic(pandor.getMessage("contentTest"),
                pandor.getRubriqueByName("jUnit"), "topicTestUnused");
        assertEquals(msg, pandor.getTopic((int) topic2.getId()).getMsg());

        pandor.removeTopic(pandor.getTopic("topicTestUnused"));
        pandor.removeMessage(pandor.getMessage("contentTest"));
    }

    @Test
    public void addRemoveTopicTest() {
        Message msg = pandor.addMessage(pandor.getUser("test@junit.fr"),"contentTest", "pseudo");
        pandor.addTopic(pandor.getMessage("contentTest"),
                pandor.getRubriqueByName("jUnit"), "topicTestUnused");
        assertSame(msg, pandor.getTopic("topicTestUnused").getMsg());
        pandor.removeTopic("topicTestUnused");
        assertNull(pandor.getTopic("topicTestUnused"));

        Topic topic = pandor.addTopic(pandor.getMessage("contentTest"),
                pandor.getRubriqueByName("jUnit"), "topicTestUnused");
        assertSame(msg, pandor.getTopic("topicTestUnused").getMsg());
        pandor.removeTopic(topic);
        assertNull(pandor.getTopic("topicTestUnused"));

        Topic topic2 = pandor.addTopic(pandor.getMessage("contentTest"),
                pandor.getRubriqueByName("jUnit"), "topicTestUnused");
        assertSame(msg, pandor.getTopic("topicTestUnused").getMsg());
        pandor.removeTopic((int) topic2.getId());
        assertNull(pandor.getTopic("topicTestUnused"));
        pandor.removeMessage(pandor.getMessage("contentTest"));
    }

    @Test
    public void topicTakenTest() {
        assertTrue(pandor.topicTaken("topic1"));
        assertFalse(pandor.topicTaken("topic4"));
    }

    @Test
    public void getTopicsTest() {
        List<Topic> topicList = pandor.getTopics();
        assertTrue(topicList.contains(pandor.getTopic("topic1")));
        assertTrue(topicList.contains(pandor.getTopic("topic2")));
        assertTrue(topicList.contains(pandor.getTopic("topic3")));
        assertFalse(topicList.contains(pandor.getTopic("topic4")));
    }

    @Test
    public void getTopicByRubriqueTest() throws ParseException {
        List<Topic> topicRubrique1 = pandor.getTopicByRubrique(pandor.getRubriqueByName("jUnit"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertTrue(topicRubrique1.contains(pandor.getTopic("topic1")));
        assertFalse(topicRubrique1.contains(pandor.getTopic("topic2")));
        assertTrue(topicRubrique1.contains(pandor.getTopic("topic3")));

        List<Topic> topicRubrique2 = pandor.getTopicByRubrique(pandor.getRubriqueByName("jUnit2"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topicRubrique2.contains(pandor.getTopic("topic1")));
        assertTrue(topicRubrique2.contains(pandor.getTopic("topic2")));
        assertFalse(topicRubrique2.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void getTopicByUserTest() throws ParseException {
        List<Topic> topic1 = pandor.getTopicByUser(pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic1.contains(pandor.getTopic("topic1")));
        assertFalse(topic1.contains(pandor.getTopic("topic2")));
        assertTrue(topic1.contains(pandor.getTopic("topic3")));

        List<Topic> topic2 = pandor.getTopicByUser(pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic2.contains(pandor.getTopic("topic1")));
        assertFalse(topic2.contains(pandor.getTopic("topic2")));
        assertFalse(topic2.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void getTopicByAnonTest() throws ParseException {
        List<Topic> topic1 = pandor.getTopicByUserWithAnonyme(pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertTrue(topic1.contains(pandor.getTopic("topic1")));
        assertFalse(topic1.contains(pandor.getTopic("topic2")));
        assertTrue(topic1.contains(pandor.getTopic("topic3")));

        List<Topic> topic2 = pandor.getTopicByUserWithAnonyme(pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic2.contains(pandor.getTopic("topic1")));
        assertTrue(topic2.contains(pandor.getTopic("topic2")));
        assertFalse(topic2.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void getTopicByRubriqueAndUserTest() throws ParseException {
        List<Topic> topic1 = pandor.getTopicByRubriqueAndUser(pandor.getRubriqueByName("jUnit"),
                pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic1.contains(pandor.getTopic("topic1")));
        assertFalse(topic1.contains(pandor.getTopic("topic2")));
        assertTrue(topic1.contains(pandor.getTopic("topic3")));

        List<Topic> topic3 = pandor.getTopicByRubriqueAndUser(pandor.getRubriqueByName("jUnit2"),
                pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic3.contains(pandor.getTopic("topic1")));
        assertFalse(topic3.contains(pandor.getTopic("topic2")));
        assertFalse(topic3.contains(pandor.getTopic("topic3")));

        List<Topic> topic2 = pandor.getTopicByRubriqueAndUser(pandor.getRubriqueByName("jUnit"),
                pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic2.contains(pandor.getTopic("topic1")));
        assertFalse(topic2.contains(pandor.getTopic("topic2")));
        assertFalse(topic2.contains(pandor.getTopic("topic3")));

        List<Topic> topic4 = pandor.getTopicByRubriqueAndUser(pandor.getRubriqueByName("jUnit2"),
                pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic4.contains(pandor.getTopic("topic1")));
        assertFalse(topic4.contains(pandor.getTopic("topic2")));
        assertFalse(topic4.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void getTopicByRubriqueAndUserWithAnonymeTest() throws ParseException {
        List<Topic> topic1 = pandor.getTopicByRubriqueAndUserWithAnonyme(pandor.getRubriqueByName("jUnit"),
                pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertTrue(topic1.contains(pandor.getTopic("topic1")));
        assertFalse(topic1.contains(pandor.getTopic("topic2")));
        assertTrue(topic1.contains(pandor.getTopic("topic3")));

        List<Topic> topic3 = pandor.getTopicByRubriqueAndUserWithAnonyme(pandor.getRubriqueByName("jUnit2"),
                pandor.getUser("test@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic3.contains(pandor.getTopic("topic1")));
        assertFalse(topic3.contains(pandor.getTopic("topic2")));
        assertFalse(topic3.contains(pandor.getTopic("topic3")));

        List<Topic> topic2 = pandor.getTopicByRubriqueAndUserWithAnonyme(pandor.getRubriqueByName("jUnit"),
                pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic2.contains(pandor.getTopic("topic1")));
        assertFalse(topic2.contains(pandor.getTopic("topic2")));
        assertFalse(topic2.contains(pandor.getTopic("topic3")));

        List<Topic> topic4 = pandor.getTopicByRubriqueAndUserWithAnonyme(pandor.getRubriqueByName("jUnit2"),
                pandor.getUser("test2@junit.fr"),
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertFalse(topic4.contains(pandor.getTopic("topic1")));
        assertTrue(topic4.contains(pandor.getTopic("topic2")));
        assertFalse(topic4.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void getTopicBeforeTest() throws ParseException {
        List<Topic> topic1 = pandor.getTopicBefore(
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), new String[0]);
        assertTrue(topic1.contains(pandor.getTopic("topic1")));
        assertTrue(topic1.contains(pandor.getTopic("topic2")));
        assertTrue(topic1.contains(pandor.getTopic("topic3")));

        List<Topic> topic2 = pandor.getTopicBefore(
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"), new String[0]);
        assertFalse(topic2.contains(pandor.getTopic("topic1")));
        assertFalse(topic2.contains(pandor.getTopic("topic2")));
        assertFalse(topic2.contains(pandor.getTopic("topic3")));
    }

    @Test
    public void changeSousPseudoTest() {
        Topic topic = pandor.getTopic("topic1");
        assertEquals("sousPseudo1", topic.getMsg().getSousPseudo());

        pandor.changeSousPseudo(topic, "sousPseudoTest");
        assertEquals("sousPseudoTest", topic.getMsg().getSousPseudo());

        pandor.changeSousPseudo(topic, "sousPseudo1");
        assertEquals("sousPseudo1", topic.getMsg().getSousPseudo());

        pandor.changeSousPseudo(null, "testString");
    }

    @Test
    public void addRemoveTagToTopic() {
        Topic topic = pandor.getTopic("topic1");
        assertTrue(topic.getTags().isEmpty());

        Tag tag1 = pandor.addTag("tag1");
        pandor.addTagToTopic(topic, tag1);
        assertTrue(topic.getTags().contains(tag1));

        pandor.removeTagToTopic(topic, tag1);
        assertTrue(topic.getTags().isEmpty());

        pandor.addTagToTopic(null, tag1);
        pandor.removeTagToTopic(null, tag1);

        pandor.removeTag(tag1);
    }

    @Test
    public void checkTagsTest() {
        Tag tag1 = pandor.addTag("tag1");
        Tag tag2 = pandor.addTag("tag2");
        Tag tag3 = pandor.addTag("tag3");
        Topic topic1 = pandor.getTopic("topic1");
        Topic topic2 = pandor.getTopic("topic2");
        pandor.addTagToTopic(topic1, tag1);
        pandor.addTagToTopic(topic1, tag2);
        pandor.addTagToTopic(topic2, tag2);
        String[] tagList1 = new String[1];
        tagList1[0] = tag2.getNom();
        List<Topic> topicsChecked = pandor.checkTags(pandor.getTopics(), tagList1);
        assertTrue(topicsChecked.contains(topic1));
        assertTrue(topicsChecked.contains(topic2));
        assertFalse(topicsChecked.contains(pandor.getTopic("topic3")));

        String[] tagList2 = new String[2];
        tagList2[0] = tag1.getNom();
        tagList2[1] = tag2.getNom();
        List<Topic> topicsChecked2 = pandor.checkTags(pandor.getTopics(), tagList2);
        assertTrue(topicsChecked2.contains(topic1));
        assertFalse(topicsChecked2.contains(topic2));
        assertFalse(topicsChecked2.contains(pandor.getTopic("topic3")));

        assertTrue(pandor.checkTags(null, tagList1).isEmpty());
        String[] taglist3 = new String[1];
        taglist3[0] = tag3.getNom();
        assertTrue(pandor.checkTags(pandor.getTopics(), taglist3).isEmpty());

        pandor.removeTagToTopic(topic1, tag1);
        pandor.removeTagToTopic(topic1, tag2);
        pandor.removeTagToTopic(topic2, tag2);
        pandor.removeTag(tag3);
        pandor.removeTag(tag2);
        pandor.removeTag(tag1);
    }

    ////// MESSAGE TESTS //////

    @Test
    public void addRemoveMessageTest() {
        assertNull(pandor.getMessage("contentTestFnc"));

        pandor.addMessage(pandor.getUser("test@junit.fr"), "contentTestFnc", "sousPseudo");
        assertNotNull(pandor.getMessage("contentTestFnc"));

        pandor.removeMessage(pandor.getMessage("contentTestFnc"));
        assertNull(pandor.getMessage("contentTestFnc"));

        Message msg = pandor.addMessage(pandor.getUser("test@junit.fr"), "contentTestFnc", "sousPseudo");
        assertNotNull(pandor.getMessage("contentTestFnc"));

        pandor.removeMessage((int) msg.getId());
        assertNull(pandor.getMessage("contentTestFnc"));
    }

    @Test
    public void getMessageTest() {
        assertNotNull(pandor.getMessage("content"));
        assertNull(pandor.getMessage("contentNotExisting"));

        Message msg = pandor.getMessage("content");
        assertEquals(msg, pandor.getMessage((int) msg.getId()));
    }

    @Test
    public void addReponseTest() {
        Message msg = pandor.getMessage("content");
        Message rep = pandor.getMessage("content3");
        pandor.addResponse(msg, rep);
        assertTrue(msg.getReponses().contains(rep));
        assertFalse(rep.getReponses().contains(msg));

        pandor.addResponse(null, rep);
        pandor.addResponse(msg, null);
    }

    @Test
    public void upVoteAndDownVoteRelatedFunctionsTest() {
        Message msg = pandor.getMessage("content");
        User u = pandor.getUser("test@junit.fr");
        assertFalse(pandor.isDownvoteur(msg, u));
        assertFalse(pandor.isUpvoteur(msg, u));

        pandor.upVote(msg, u);
        assertFalse(pandor.isDownvoteur(msg, u));
        assertTrue(pandor.isUpvoteur(msg, u));

        pandor.upVote(msg, u);
        assertFalse(pandor.isDownvoteur(msg, u));
        assertFalse(pandor.isUpvoteur(msg, u));

        pandor.downVote(msg, u);
        assertTrue(pandor.isDownvoteur(msg, u));
        assertFalse(pandor.isUpvoteur(msg, u));

        pandor.downVote(msg, u);
        assertFalse(pandor.isDownvoteur(msg, u));
        assertFalse(pandor.isUpvoteur(msg, u));
    }

    @Test
    public void changeContenu() {
        Message msg = pandor.getMessage("content");
        assertEquals("content", msg.getContenu());

        pandor.changeContenu(msg, "newContent");
        assertEquals("newContent", msg.getContenu());

        pandor.changeContenu(msg, "content");
        assertEquals("content", msg.getContenu());

        pandor.changeContenu(null, "TestString");
    }

    ////// TAG TESTS //////

    @Test
    public void TagRelatedFunctionsTest() {
        Tag t = pandor.addTag("Tag1");
        assertTrue(pandor.tagTaken("Tag1"));
        assertFalse(pandor.tagTaken("Tag2"));

        Tag t2 = pandor.addTag("Tag2");
        Tag t3 = pandor.addTag("Tag3");
        List<Tag> tagList = pandor.getTags();
        assertTrue(tagList.contains(t));
        assertTrue(tagList.contains(t2));
        assertTrue(tagList.contains(t3));

        assertEquals(t, pandor.getTag("Tag1"));

        pandor.removeTag(t);
        pandor.removeTag("Tag2");
        pandor.removeTag(t3);
    }

}
