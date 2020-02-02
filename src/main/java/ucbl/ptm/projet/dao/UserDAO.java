package ucbl.ptm.projet.dao;

import java.util.ArrayList;
import java.util.List;

import ucbl.ptm.projet.modele.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import ucbl.ptm.projet.modele.Rubrique;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager em) {
        entityManager = em;
    }

    public User getUserByEmail(String email) {
        TypedQuery<User> queryUser = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = ?1",
                User.class);

        User result;

        try {
            result = queryUser
                    .setParameter(1, email)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    public User getUserByPseudo(String pseudo) {
        TypedQuery<User> queryUser = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.pseudo = ?1",
                User.class);

        User result;

        try {
            result = queryUser
                    .setParameter(1, pseudo)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    public User getUserByNumEtu(String numEtu) {
        TypedQuery<User> queryUser = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.numEtu = ?1",
                User.class);

        User result;

        try {
            result = queryUser
                    .setParameter(1, numEtu)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    @Transactional
    public User createUser(String email, String nom, String prenom, String pseudo, String hashMdp, String numEtu, String titre) {
        User user = new User(email, nom, prenom, pseudo, hashMdp, numEtu, titre, false);
        entityManager.persist(user);

        return user;
    }

    public void deleteUser(User user) {
        entityManager.remove(user);
    }

    public User getUserByPseudoAndHashMdp(String pseudo, String hashMdp) {
        TypedQuery<User> queryUser1 = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.pseudo = ?1 AND u.hashMdp = ?2",
                User.class);

        User result;

        try {
            result = queryUser1
                    .setParameter(1, pseudo)
                    .setParameter(2, hashMdp)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    public List<User> getAllUser() {
        TypedQuery<User> queryUser1 = entityManager.createQuery(
                "SELECT u FROM User u",
                User.class);

        List<User> result;

        try {
            result = queryUser1.getResultList();
        } catch (NoResultException e) {
            result = new ArrayList<>();
        }

        return result;
    }

    public void abonneRubrique(User user, Rubrique rubrique) {
        user.addAbonneRubrique(rubrique);
    }

    public void promoteModeratorOfRubrique(User user, Rubrique rubrique) {
        user.addModeratedRubrique(rubrique);
    }

    public void removeModeratorOfRubrique(User user, Rubrique rubrique) {
        user.removeModeratedRubrique(rubrique);
    }

    public void promoteAdmin(User user) {
        user.setIsAdmin(true);
    }

    public void demoteAdmin(User user) {
        user.setIsAdmin(false);
    }

    //Update Pseudo
    @Transactional
    public User updatePseudo(String email, String pseudo) {
        TypedQuery<User> queryUser = entityManager.createQuery(
                "select u from User u where u.email = ?1",
                User.class);
        User result;
        try {
            result = queryUser
                    .setParameter(1, email)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        if (result != null) {
            result.setPseudo(pseudo);
        }

        return result;
    }
}
