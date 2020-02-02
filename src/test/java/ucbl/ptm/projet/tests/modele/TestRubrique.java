package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.Rubrique;

import static org.junit.Assert.*;

public class TestRubrique {

    @Test
    public void testNom() {
        Rubrique rubrique = new Rubrique();
        rubrique.setNom("MULTIMIF");
        assertEquals("MULTIMIF", rubrique.getNom());
    }

    @Test
    public void testPresentation() {
        Rubrique rubrique = new Rubrique();
        rubrique.setPresentation("Présentation");
        assertEquals("Présentation", rubrique.getPresentation());
    }
}
