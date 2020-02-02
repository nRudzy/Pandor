package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.Message;
import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.Tag;
import ucbl.ptm.projet.modele.Topic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestTopic {

    @Test
    public void testMessage() {
        Topic topic = new Topic();
        Message message = new Message("Totoche", "Contenu");
        topic.setMsg(message);
        assertEquals(message, topic.getMsg());
    }

    @Test
    public void testRubrique() {
        Topic topic = new Topic();
        Rubrique rubrique = new Rubrique("Multimif", "Meilleur UE");
        topic.setRubrique(rubrique);
        assertEquals(rubrique, topic.getRubrique());
    }

    @Test
    public void testTitre() {
        Topic topic = new Topic();
        topic.setTitre("Titre");
        assertEquals("Titre", topic.getTitre());
    }

    @Test
    public void testAddTags() {
        Topic topic = new Topic();
        Tag tag = new Tag("java");
        Tag tag2 = new Tag("maven");
        Tag tag3 = new Tag("junit");
        Tag tag4 = new Tag("jsp");

        List<Tag> taglist = new ArrayList<>();
        taglist.add(tag);
        taglist.add(tag2);
        topic.addTag(tag);
        topic.addTag(tag2);
        assertEquals(taglist, topic.getTags());

        List<Tag> taglist2 = new ArrayList<>();
        taglist2.add(tag3);
        taglist2.add(tag4);
        topic.setTags(taglist2);
        assertEquals(taglist2, topic.getTags());
    }

    @Test
    public void testRemoveTags() {
        Topic topic = new Topic();
        Tag tag = new Tag("java");
        Tag tag2 = new Tag("maven");

        List<Tag> taglist = new ArrayList<>();
        taglist.add(tag);
        taglist.add(tag2);
        topic.setTags(taglist);
        assertEquals(2, topic.getTags().size());
        topic.removeTag(tag);
        assertEquals(1, topic.getTags().size());
    }
}
