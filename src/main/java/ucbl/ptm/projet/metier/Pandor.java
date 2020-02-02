package ucbl.ptm.projet.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import ucbl.ptm.projet.dao.UserDAO;
import ucbl.ptm.projet.modele.User;
import ucbl.ptm.projet.modele.Ban;
import ucbl.ptm.projet.modele.Message;

import javax.persistence.EntityManager;

import ucbl.ptm.projet.dao.BanDAO;
import ucbl.ptm.projet.dao.MessageDAO;
import ucbl.ptm.projet.dao.RubriqueDAO;
import ucbl.ptm.projet.dao.TagDAO;
import ucbl.ptm.projet.dao.TopicDAO;
import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.Tag;
import ucbl.ptm.projet.modele.Topic;

public class Pandor {
    private UserDAO userDAO;
    private BanDAO banDAO;
    private RubriqueDAO rubriqueDAO;
    private MessageDAO messageDAO;
    private TopicDAO topicDAO;
    private TagDAO tagDAO;
    private EntityManager em;

    public Pandor(EntityManager em) {
        this.em = em;
        this.userDAO = new UserDAO(this.em);
        this.banDAO = new BanDAO(this.em);
        this.rubriqueDAO = new RubriqueDAO(this.em);
        this.tagDAO = new TagDAO(em);
        this.topicDAO = new TopicDAO(em);
        this.messageDAO = new MessageDAO(em);
    }

    ////// USER PART ///////

    public User getUser(String emailUser) {
        return userDAO.getUserByEmail(emailUser);
    }

    public User getUser(int id) {
        return em.find(User.class, id);
    }

    public User addUser(String email, String nom, String prenom, String pseudo, String hashMdp, String numEtu, String titre) {
        User user;

        em.getTransaction().begin();
        user = userDAO.createUser(email, nom, prenom, pseudo, hashMdp, numEtu, titre);
        em.getTransaction().commit();

        return user;
    }

    public void removeUser(String email) {
        removeUser(getUser(email));
    }

    public void removeUser(int id) {
        removeUser(getUser(id));
    }

    public void removeUser(User user) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        userDAO.deleteUser(user);
        em.getTransaction().commit();
    }

    public User validateUser(String pseudo, String hashMdp) {
        return userDAO.getUserByPseudoAndHashMdp(pseudo, hashMdp);
    }

    public boolean emailTaken(String email) {
        return userDAO.getUserByEmail(email) != null;
    }

    public boolean pseudoTaken(String pseudo) {
        return userDAO.getUserByPseudo(pseudo) != null;
    }

    public boolean numEtuTaken(String numEtu) {
        return userDAO.getUserByNumEtu(numEtu) != null;
    }

    public List<User> getAllUser() {
        return userDAO.getAllUser();
    }

    public List<User> getAllUserWithNameContaining(String str) {
        List<User> ret = new ArrayList<>();
        for (User user : userDAO.getAllUser()) {
            if (user.getPseudo().contains(str)) {
                ret.add(user);
            }
        }
        return ret;
    }

    public void setModerator(User user, Rubrique rubrique) {
        em.getTransaction().begin();
        userDAO.promoteModeratorOfRubrique(user, rubrique);
        em.getTransaction().commit();
    }

    public void rmModerator(User user, Rubrique rubrique) {
        em.getTransaction().begin();
        userDAO.removeModeratorOfRubrique(user, rubrique);
        em.getTransaction().commit();
    }

    public void setAdmin(User user) {
        em.getTransaction().begin();
        userDAO.promoteAdmin(user);
        em.getTransaction().commit();
    }

    public void rmAdmin(User user) {
        em.getTransaction().begin();
        userDAO.demoteAdmin(user);
        em.getTransaction().commit();
    }

    public void modifierPseudo(User user, String nouveauPseudo) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        user.setPseudo(nouveauPseudo);
        em.getTransaction().commit();
    }

    public void updateTitle(User user, String newTitle) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        user.setTitre(newTitle);
        em.getTransaction().commit();
    }

    public void modifierMdp(User user, String nouveauMdp) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        user.setHashMdp(nouveauMdp);
        em.getTransaction().commit();
    }

    public void followRubrique(User user, Rubrique rubrique) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        user.addAbonneRubrique(rubrique);
        rubrique.addAbo(user);
        em.getTransaction().commit();
    }

    public void unFollowRubrique(User user, Rubrique rubrique) {
        if (user == null) {
            return;
        }
        em.getTransaction().begin();
        user.removeAbonneRubrique(rubrique);
        rubrique.removeAbo(user);
        em.getTransaction().commit();
    }

    ////// BAN PART ///////

    public void banUser(User banner, User banni, int nbJour, String motif) {
        em.getTransaction().begin();
        banDAO.createBan(banner, banni, nbJour, motif);
        em.getTransaction().commit();
    }

    public void unBanUser(User banni) {
        em.getTransaction().begin();
        banDAO.removeBan(banni);
        em.getTransaction().commit();
    }

    public Ban getBanByUser(User banni) {
        return banDAO.getBanByUserBanni(banni);
    }

    public boolean isBanned(User user) {
        Ban ban = banDAO.getBanByUserBanni(user);
        if (ban == null) {
            return false;
        }
        DateTime banTime = new DateTime(ban.getFin());
        DateTime currentTime = new DateTime();
        if (banTime.getYear() <= currentTime.getYear() && banTime.getDayOfYear() <= currentTime.getDayOfYear()) {
            return banTime.getSecondOfDay() > currentTime.getSecondOfDay();
        }
        return true;
    }

    ////// RUBRIQUE PART //////

    public Rubrique getRubriqueByName(String nom) {
        return rubriqueDAO.getRubriqueByNom(nom);
    }

    public boolean rubriqueTaken(String nom) {
        return rubriqueDAO.getRubriqueByNom(nom) != null;
    }

    public Rubrique getRubrique(int id) {
        return rubriqueDAO.getRubriqueById(id);
    }

    public List<Rubrique> getAllRubriques() {
        return rubriqueDAO.getAllRubriques();
    }

    public List<Rubrique> getAllRubriqueWithNameContaining(String str) {
        List<Rubrique> ret = new ArrayList<>();
        for (Rubrique rubrique : rubriqueDAO.getAllRubriques()) {
            if (rubrique.getNom().contains(str)) {
                ret.add(rubrique);
            }
        }
        return ret;
    }

    public void addRubrique(String nom, String presentation) {
        em.getTransaction().begin();
        rubriqueDAO.createRubrique(nom, presentation);
        em.getTransaction().commit();
    }

    public void removeRubrique(String name) {
        removeRubrique(getRubriqueByName(name));
    }

    public void removeRubrique(int id) {
        removeRubrique(getRubrique(id));
    }

    public void removeRubrique(Rubrique r) {
        if (r == null) {
            return;
        }
        em.getTransaction().begin();
        rubriqueDAO.removeRubrique(r);
        em.getTransaction().commit();
    }

    public void modifierNomRubrique(Rubrique rubrique, String nouveauNom) {
        if (rubrique == null) {
            return;
        }
        em.getTransaction().begin();
        rubrique.setNom(nouveauNom);
        em.getTransaction().commit();
    }

    public void modifierPresentationRubrique(Rubrique rubrique, String nouvellePresentation) {
        if (rubrique == null) {
            return;
        }
        em.getTransaction().begin();
        rubrique.setPresentation(nouvellePresentation);
        em.getTransaction().commit();
    }

    ////// TOPIC PART //////

    public Topic getTopic(String titre) {
        return topicDAO.getTopicByTitre(titre);
    }

    public Topic getTopic(int id) {
        return em.find(Topic.class, id);
    }

    public Topic addTopic(Message msg, Rubrique rubrique, String titre) {
        Topic t;

        em.getTransaction().begin();
        t = topicDAO.createTopic(msg, rubrique, titre);
        rubrique.addTopics(t);
        em.getTransaction().commit();

        return t;
    }

    public void removeTopic(String name) {
        removeTopic(getTopic(name));
    }

    public void removeTopic(int id) {
        removeTopic(getTopic(id));
    }

    public void removeTopic(Topic topic) {
        if (topic == null) {
            return;
        }
        em.getTransaction().begin();
        topicDAO.removeTopic(topic);
        em.getTransaction().commit();
    }

    public boolean topicTaken(String titre) {
        return topicDAO.getTopicByTitre(titre) != null;
    }

    public List<Topic> getTopics() {
        return topicDAO.getAllTopics();
    }

    public List<Topic> getTopicByRubrique(Rubrique rubrique, Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsOfRubrique(rubrique, date);
        return checkTags(topics, tags);
    }

    public List<Topic> getTopicByUser(User user, Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsOfUser(user, date);
        return checkTags(topics, tags);
    }

    public List<Topic> getTopicByUserWithAnonyme(User user, Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsOfUserWithAnonyme(user, date);
        return checkTags(topics, tags);
    }

    public List<Topic> getTopicByRubriqueAndUser(Rubrique rubrique, User user, Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsOfRubriqueAndUser(rubrique, user, date);
        return checkTags(topics, tags);
    }

    public List<Topic> getTopicByRubriqueAndUserWithAnonyme(Rubrique rubrique, User user, Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsOfRubriqueAndUserWithAnonyme(rubrique, user, date);
        return checkTags(topics, tags);
    }

    public List<Topic> getTopicBefore(Date date, String[] tags) {
        List<Topic> topics = topicDAO.getAllTopicsBefore(date);
        return checkTags(topics, tags);
    }

    public void changeSousPseudo(Topic top, String sousPseudo) {
        if (top == null) {
            return;
        }
        em.getTransaction().begin();
        top.getMsg().setSousPseudo(sousPseudo);
        em.getTransaction().commit();
    }

    public void addTagToTopic(Topic top, Tag tag) {
        if (top == null) {
            return;
        }
        em.getTransaction().begin();
        top.addTag(tag);
        em.getTransaction().commit();
    }

    public void removeTagToTopic(Topic top, Tag tag) {
        if (top == null) {
            return;
        }
        em.getTransaction().begin();
        top.removeTag(tag);
        em.getTransaction().commit();
    }

    public List<Topic> checkTags(List<Topic> topics, String[] tags) {
        if (topics == null) {
            return new ArrayList<>();
        }
        List<Topic> result = new ArrayList<>();
        for (Topic t : topics) {
            boolean valid = true;

            for (String s : tags) {
                if (!tagTaken(s.trim()) || !t.getTags().contains(getTag(s.trim())))
                    valid = false;
            }

            if (valid)
                result.add(t);
        }

        return result;
    }

    ////// MESSAGE PART //////

    public Message addMessage(User auteur, String contenu, String sousPseudo) {
        Message m;

        em.getTransaction().begin();
        m = messageDAO.createMessage(auteur, contenu, sousPseudo);
        em.getTransaction().commit();

        return m;
    }

    public void removeMessage(int id) {
        removeMessage(getMessage(id));
    }

    public void removeMessage(Message msg) {
        if (msg == null) {
            return;
        }
        em.getTransaction().begin();
        messageDAO.removeMessage(msg);
        em.getTransaction().commit();
    }

    public Message getMessage(int id) {
        return em.find(Message.class, id);
    }

    public Message getMessage(String content) {
        return messageDAO.getMessageByContenu(content);
    }

    public void addResponse(Message msg, Message rep) {
        if (msg == null || rep == null) {
            return;
        }
        em.getTransaction().begin();
        messageDAO.addResponse(msg, rep);
        em.getTransaction().commit();
    }

    public boolean isUpvoteur(Message msg, User usr) {
        return messageDAO.isUpvoteur(msg, usr);
    }

    public boolean isDownvoteur(Message msg, User usr) {
        return messageDAO.isDownvoteur(msg, usr);
    }

    public void upVote(Message msg, User usr) {
        em.getTransaction().begin();
        messageDAO.upVote(msg, usr);
        em.getTransaction().commit();
    }

    public void downVote(Message msg, User usr) {
        em.getTransaction().begin();
        messageDAO.downVote(msg, usr);
        em.getTransaction().commit();
    }

    public void changeContenu(Message msg, String content) {
        if (msg == null) {
            return;
        }
        em.getTransaction().begin();
        msg.setContenu(content);
        em.getTransaction().commit();
    }

    ////// TAG PART //////

    public Tag addTag(String nom) {
        Tag t;
        em.getTransaction().begin();
        t = tagDAO.createTag(nom);
        em.getTransaction().commit();
        return t;
    }

    public void removeTag(String nom) {
        removeTag(getTag(nom));
    }

    public void removeTag(Tag tag) {
        em.getTransaction().begin();
        tagDAO.removeTag(tag);
        em.getTransaction().commit();
    }

    public boolean tagTaken(String nom) {
        return tagDAO.getTagByName(nom) != null;
    }

    public List<Tag> getTags() {
        return tagDAO.getAllTags();
    }

    public Tag getTag(String nom) {
        return tagDAO.getTagByName(nom);
    }

}

    