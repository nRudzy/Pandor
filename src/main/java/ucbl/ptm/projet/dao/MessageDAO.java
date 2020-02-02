package ucbl.ptm.projet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ucbl.ptm.projet.modele.Vote;
import ucbl.ptm.projet.modele.Message;
import ucbl.ptm.projet.modele.User;

public class MessageDAO {
    private EntityManager entityManager;

    public MessageDAO(EntityManager em) {
        entityManager = em;
    }

    public Message createMessage(User auteur, String contenu, String sousPseudo) {
        Message msg = new Message(sousPseudo, contenu);
        msg.setAuteur(auteur);
        entityManager.persist(msg);

        return msg;
    }

    public void removeMessage(Message msg) {
        entityManager.remove(msg);
    }

    public Message getMessageByAuteur(User auteur) {
        TypedQuery<Message> queryMsg = entityManager.createQuery(
                "SELECT m FROM Message m WHERE auteur = ?1",
                Message.class);
        Message result;
        try {
            result = queryMsg
                    .setParameter(1, auteur)
                    .getSingleResult();

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public Message getMessageByContenu(String contenuMsg) {
        TypedQuery<Message> queryMsg = entityManager.createQuery(
                "SELECT m FROM Message m WHERE contenu = ?1",
                Message.class);
        Message result;

        try {
            result = queryMsg
                    .setParameter(1, contenuMsg)
                    .getSingleResult();

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public List<Message> getMessagesByAuteur(User auteur) {
        TypedQuery<Message> queryMsg = entityManager.createQuery(
                "SELECT distinct m FROM Message m WHERE auteur = ?1",
                Message.class);
        List<Message> result;
        try {
            result = queryMsg
                    .setParameter(1, auteur)
                    .getResultList();

        } catch (NoResultException e) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void addResponse(Message msg, Message rep) {
        msg.addReponse(rep);
    }

    public boolean isUpvoteur(Message msg, User usr) {
        TypedQuery<Message> queryMsg = entityManager.createQuery(
                "SELECT m FROM Message m JOIN m.votes v WHERE m = ?1 AND v.voteur = ?2 AND v.quanta = 1",
                Message.class);
        Message result;

        try {
            result = queryMsg
                    .setParameter(1, msg)
                    .setParameter(2, usr)
                    .getSingleResult();

        } catch (Exception e) {
            result = null;
        }

        return result != null;
    }

    public boolean isDownvoteur(Message msg, User usr) {
        TypedQuery<Message> queryMsg = entityManager.createQuery(
                "SELECT m FROM Message m JOIN m.votes v WHERE m = ?1 AND v.voteur = ?2 AND v.quanta = -1",
                Message.class);
        Message result;

        try {
            result = queryMsg
                    .setParameter(1, msg)
                    .setParameter(2, usr)
                    .getSingleResult();

        } catch (Exception e) {
            result = null;
        }

        return result != null;
    }

    public Vote getVoteByMsgAndUsr(Message msg, User usr) {
        TypedQuery<Vote> queryMsg = entityManager.createQuery(
                "SELECT v FROM Message m JOIN m.votes v WHERE m = ?1 AND v.voteur = ?2",
                Vote.class);
        Vote result;

        try {
            result = queryMsg
                    .setParameter(1, msg)
                    .setParameter(2, usr)
                    .getSingleResult();

        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    public void upVote(Message msg, User usr) {
        Vote oldVote = getVoteByMsgAndUsr(msg, usr);
        if (oldVote != null) {
            entityManager.remove(oldVote);
            msg.removeVote(oldVote);
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();

            if (oldVote.getQuanta() == 1)
                return;
        }

        Vote vote = new Vote(usr, msg, 1);
        entityManager.persist(vote);
        msg.addVote(vote);
    }

    public void downVote(Message msg, User usr) {
        Vote oldVote = getVoteByMsgAndUsr(msg, usr);
        if (oldVote != null) {
            entityManager.remove(oldVote);
            msg.removeVote(oldVote);
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();

            if (oldVote.getQuanta() == -1)
                return;
        }

        Vote vote = new Vote(usr, msg, -1);
        entityManager.persist(vote);
        msg.addVote(vote);
    }
}
