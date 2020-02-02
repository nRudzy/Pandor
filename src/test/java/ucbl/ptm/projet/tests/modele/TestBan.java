package ucbl.ptm.projet.tests.modele;

import org.junit.Test;
import ucbl.ptm.projet.modele.Ban;
import ucbl.ptm.projet.modele.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TestBan {

    @Test
    public void testBanner() {
        Ban ban = new Ban();
        User u = new User("toto@gmail.com", "Toto", "John", "JohnTo", "t0rTu3", "p1906900", "Etudiant", false);
        ban.setBanner(u);
        assertEquals(u, ban.getBanner());
    }

    @Test
    public void testBannni() {
        Ban ban = new Ban();
        User u = new User("toto@gmail.com", "Toto", "John", "JohnTo", "t0rTu3", "p1906900", "Etudiant", false);
        ban.setBanni(u);
        assertEquals(u, ban.getBanni());
    }

    @Test
    public void testMotif() {
        Ban ban = new Ban();
        ban.setMotif("motif");
        assertEquals("motif", ban.getMotif());
    }

    @Test
    public void testFin() throws ParseException {
        Ban ban = new Ban();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = "31-08-1982";
        Date date = sdf.parse(dateInString);
        ban.setFin(date);
        assertEquals(date, ban.getFin());
    }
}
