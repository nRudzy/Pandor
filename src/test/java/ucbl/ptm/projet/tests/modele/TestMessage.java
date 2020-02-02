package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.Message;
import ucbl.ptm.projet.modele.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestMessage {

    @Test
    public void testAuteur() {
        Message message = new Message();
        User u = new User("toto@gmail.com", "Toto", "John", "JohnTo", "t0rTu3", "p1906900", "Etudiant", false);
        message.setAuteur(u);
        assertEquals(u, message.getAuteur());
    }

    @Test
    public void testContenu() {
        Message message = new Message();
        message.setContenu("contenu de qualité");
        assertEquals("contenu de qualité", message.getContenu());
    }

    @Test
    public void testSousPseudo() {
        Message message = new Message();
        message.setSousPseudo("totoche");
        assertEquals("totoche", message.getSousPseudo());
    }

    @Test
    public void testAddReponse() {
        Message message = new Message();
        Message reponse = new Message("Totoche", "Voilà une belle réponse.");
        message.addReponse(reponse);
        assertEquals(message.getReponses().get(0), reponse);
    }

    @Test
    public void testGetReponses() {
        Message message = new Message();
        Message reponse = new Message("Totoche", "Voilà une belle réponse.");
        Message reponse_deux = new Message("Totoche", "Voilà une autre belle réponse.");
        List<Message> reponse_list = new ArrayList<>();
        reponse_list.add(reponse);
        reponse_list.add(reponse_deux);
        message.addReponse(reponse);
        message.addReponse(reponse_deux);
        assertEquals(reponse_list, message.getReponses());
    }

    @Test
    public void testRemoveReponse() {
        Message message = new Message();
        Message reponse = new Message("Totoche", "Voilà une belle réponse.");
        Message reponse_deux = new Message("Totoche", "Voilà une autre belle réponse.");
        message.addReponse(reponse);
        message.addReponse(reponse_deux);
        assertEquals(2, message.getReponses().size());
        message.removeReponse(reponse);
        assertEquals(1, message.getReponses().size());
    }
}
