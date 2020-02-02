package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.Tag;

import static org.junit.Assert.*;

public class TestTag {

    @Test
    public void testNom() {
        Tag tag = new Tag();
        tag.setNom("java");
        assertEquals("java", tag.getNom());
    }
}
