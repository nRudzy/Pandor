package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.User;

import static org.junit.Assert.*;


public class TestUser {

    @Test
    public void testEmail() {
        User utilisateur = new User();
        utilisateur.setEmail("toto@gmail.com");
        assertEquals("toto@gmail.com", utilisateur.getEmail());
    }

    @Test
    public void testNom() {
        User utilisateur = new User();
        utilisateur.setNom("toto");
        assertEquals("toto", utilisateur.getNom());
    }

    @Test
    public void testPrenom() {
        User utilisateur = new User();
        utilisateur.setPrenom("jeff");
        assertEquals("jeff", utilisateur.getPrenom());
    }

    @Test
    public void testPseudo() {
        User utilisateur = new User();
        utilisateur.setPseudo("totoche");
        assertEquals("totoche", utilisateur.getPseudo());
    }

    @Test
    public void testHashMdp() {
        User utilisateur = new User();
        utilisateur.setHashMdp("t0T0d3sTr0y3uR");
        assertEquals("t0T0d3sTr0y3uR", utilisateur.getHashMdp());
    }

    @Test
    public void testNumEtu() {
        User utilisateur = new User();
        utilisateur.setNumEtu("p1906900");
        assertEquals("p1906900", utilisateur.getNumEtu());
    }

    @Test
    public void testTitre() {
        User utilisateur = new User();
        utilisateur.setTitre("Etudiant");
        assertEquals("Etudiant", utilisateur.getTitre());
    }

    @Test
    public void testIsAdmin() {
        User utilisateur = new User();
        utilisateur.setIsAdmin(true);
        assertTrue(utilisateur.getIsAdmin());
    }
}
