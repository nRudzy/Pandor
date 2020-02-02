package ucbl.ptm.projet.dao;

import java.util.ArrayList;
import java.util.Date;
import ucbl.ptm.projet.modele.Message;
import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.Topic;
import ucbl.ptm.projet.modele.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.TemporalType;

public class TopicDAO {
    private static final Logger LOGGER = Logger.getLogger(TopicDAO.class.getName());

    private EntityManager entityManager;

    public TopicDAO(EntityManager em) {
        entityManager = em;
    }

    @Transactional
    public Topic getTopicByTitre(String titre) {
        TypedQuery<Topic> queryTopic = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.titre = ?1",
                Topic.class);
        Topic result;

        try {
            result = queryTopic
                    .setParameter(1, titre)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = null;
        }

        return result;
    }


    //Get all topics created by a specified user
    @Transactional
    public List<Topic> getTopicsByUserId(int id) {
        //A verifier
        TypedQuery<Topic> query = entityManager.createQuery(
                "SELECT DISTINCT t FROM Topic t WHERE t.msg.auteur.id = ?1",
                Topic.class
        );
        List<Topic> result;
        try {
            result = query
                    .setParameter(1, id)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = null;
        }
        return result;
    }

    @Transactional
    public List<Topic> getAllTopics() {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics.getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = null;
        }

        return result;
    }

    @Transactional
    public Topic createTopic(Message msg, Rubrique rubrique, String titre) {
        Topic topic = new Topic(titre);

        topic.setMsg(msg);
        topic.setRubrique(rubrique);
        entityManager.persist(topic);

        return topic;
    }

    public void removeTopic(Topic topic){
        entityManager.remove(topic);
    }
    
    @Transactional
    public List<Topic> getAllTopicsOfRubrique(Rubrique rubrique, Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1 AND t.rubrique = ?2",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date)
                    .setParameter(2, rubrique)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
    
    @Transactional
    public List<Topic> getAllTopicsOfUserWithAnonyme(User user, Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1 AND t.msg.auteur = ?2",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date)
                    .setParameter(2, user)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
    
    @Transactional
    public List<Topic> getAllTopicsOfUser(User user, Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1 AND t.msg.auteur = ?2 AND t.msg.sousPseudo IS NULL",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date)
                    .setParameter(2, user)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
    
    @Transactional
    public List<Topic> getAllTopicsOfRubriqueAndUserWithAnonyme(Rubrique rubrique, User user, Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1 AND t.msg.auteur = ?2 AND t.rubrique = ?3",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date)
                    .setParameter(2, user)
                    .setParameter(3, rubrique)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
    
    @Transactional
    public List<Topic> getAllTopicsOfRubriqueAndUser(Rubrique rubrique, User user, Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1 AND t.msg.auteur = ?2 AND t.rubrique = ?3 AND t.msg.sousPseudo IS NULL",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date)
                    .setParameter(2, user)
                    .setParameter(3, rubrique)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
    
    @Transactional
    public List<Topic> getAllTopicsBefore(Date date) {
        TypedQuery<Topic> queryTopics = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.msg.datePost > ?1",
                Topic.class);
        List<Topic> result;
        try {
            result = queryTopics
                    .setParameter(1, date, TemporalType.TIMESTAMP)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = new ArrayList();
        }

        return result;
    }
}
