package ucbl.ptm.projet.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;


import ucbl.ptm.projet.modele.Tag;

public class TagDAO {
    private static final Logger LOGGER = Logger.getLogger(TagDAO.class.getName());

    private EntityManager entityManager;

    public TagDAO(EntityManager em) {
        entityManager = em;
    }

    @Transactional
    public Tag getTagByName(String tagName) {

        TypedQuery<Tag> queryTag = entityManager.createQuery(
                "SELECT t FROM Tag t WHERE t.nom = ?1",
                Tag.class);

        Tag result;
        try {
            result = queryTag
                    .setParameter(1, tagName)
                    .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = null;
        }

        return result;

    }

    @Transactional
    public List<Tag> getTagsByTopic(int idTopic) {
        TypedQuery<Tag> queryTags = entityManager.createQuery(
                "SELECT t FROM Tag t, Topic to WHERE to.tags.id = t.id "
                        + "AND to.id = ?1 ",
                Tag.class);

        List<Tag> lRes;
        try {
            lRes = queryTags
                    .setParameter(1, idTopic)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            lRes = new ArrayList<>();
        }

        return lRes;
    }

    @Transactional
    public List<Tag> getTagsByUserId(int usrId) {
        TypedQuery<Tag> queryTags = entityManager.createQuery(
                "SELECT t FROM Tag t, Topic to JOIN to.tags tgs WHERE tgs.id = t.id "
                        + "AND to.msg.auteur.id = ?1 ",
                Tag.class);

        List<Tag> lRes;
        try {
            lRes = queryTags
                    .setParameter(1, usrId)
                    .getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            lRes = new ArrayList<>();
        }

        return lRes;
    }


    @Transactional
    public List<Tag> getAllTags() {
        TypedQuery<Tag> queryTags = entityManager.createQuery(
                "SELECT distinct t FROM Tag t",
                Tag.class);
        List<Tag> result;

        try {
            result = queryTags.getResultList();
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            result = null;
        }

        return result;
    }


    //Tag creation
    public Tag createTag(String nom) {
        Tag tag = new Tag(nom);
        entityManager.persist(tag);
        return tag;
    }

    public void removeTag(Tag tag) {
        entityManager.remove(tag);
    }

}
