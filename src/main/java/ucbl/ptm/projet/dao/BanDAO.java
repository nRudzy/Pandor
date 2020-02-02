package ucbl.ptm.projet.dao;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ucbl.ptm.projet.modele.Ban;
import ucbl.ptm.projet.modele.User;

public class BanDAO {

    private EntityManager entityManager;

    public BanDAO(EntityManager em) {
        entityManager = em;
    }

    public Ban createBan(User banner, User banni, int nbJours, String motif) {
        Date fin = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fin);
        c.add(Calendar.DATE, nbJours);
        fin = c.getTime();

        Ban ban = getBanByUserBanni(banni);

        if (ban != null) {
            if (ban.getFin().getTime() < fin.getTime()) {
                ban.setBanner(banner);
                ban.setFin(fin);
                ban.setMotif(motif);
            }
        } else {
            ban = new Ban(banner, banni, fin, motif);
            entityManager.persist(ban);
        }

        return ban;
    }

    public void removeBan(User banni) {
        Ban ban = getBanByUserBanni(banni);
        if (ban != null) {
            entityManager.remove(ban);
        }
    }

    public Ban getBanByUserBanni(User banni) {
        TypedQuery<Ban> queryUser = entityManager.createQuery(
                "SELECT b FROM Ban b WHERE b.banni = ?1",
                Ban.class);

        Ban result;

        try {
            result = queryUser
                    .setParameter(1, banni)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }
}