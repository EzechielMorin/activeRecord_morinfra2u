package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TestPersonne {


    ArrayList<Personne> personnes;
    Personne perso1, perso2, perso3, perso4;

    @BeforeEach
    void setUp() throws SQLException {
        try {
            Personne.createTable();
        } catch (SQLException e) {
            Personne.deleteTable();
            Personne.createTable();
        }
        personnes = new ArrayList<>();
        perso1 = new Personne("Spielberg", "Steven");
        perso2 = new Personne("Scott", "Ridley");
        perso3 = new Personne("Kubrick", "Stanley");
        perso4 = new Personne("Fincher", "David");
        personnes.add(perso1);
        personnes.add(perso2);
        personnes.add(perso3);
        personnes.add(perso4);

        for (Personne personne : personnes) {
            personne.save();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        Personne.deleteTable();
    }

    @Test
    void test_findAll_OK() throws SQLException {
        ArrayList<Personne> p = new ArrayList<>();
        p = Personne.findAll();
        for (int i = 0; i < p.size(); i++) {
            assertEquals(p.get(i).toString(), personnes.get(i).toString());
        }
    }

    @Test
    void test_findById_OK() throws SQLException {
        Personne p = Personne.findById(1);
        assertEquals(personnes.getFirst().toString(), p.toString());
    }

    @Test
    void test_findByName_OK() throws SQLException {
        ArrayList<Personne> p = new ArrayList<>();
        p = Personne.findByName("Spielberg");
        assertEquals(personnes.getFirst().toString(), p.getFirst().toString());
    }

    @Test
    void test_delete_OK() throws SQLException {
        perso4.delete();
        assertNull(Personne.findById(4));
    }
}