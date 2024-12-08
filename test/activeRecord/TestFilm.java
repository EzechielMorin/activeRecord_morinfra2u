package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilm {

    ArrayList<Film> films;
    Film film1, film2, film3, film4, film5, film6, film7;
    Personne perso1, perso2, perso3, perso4;

    @BeforeEach
    void setUp() throws SQLException {
        try {
            Personne.createTable();
            Film.createTable();
        } catch (SQLException e) {
            Personne.deleteTable();
            Film.deleteTable();
            Personne.createTable();
            Film.createTable();
        }

        ArrayList<Personne> personnes = new ArrayList<>();

        perso1 = new Personne("Spielberg", "Steven");
        perso2 = new Personne("Scott", "Ridley");
        perso3 = new Personne("Kubrick", "Stanley");
        perso4 = new Personne("Fincher", "David");

        personnes.add(perso1);
        personnes.add(perso2);
        personnes.add(perso3);
        personnes.add(perso4);

        for (Personne p : personnes) {
            System.out.println(p);
            p.save();
        }

        films = new ArrayList<>();

        film1 = new Film("Arche perdue", perso1);
        film2 = new Film("Alien", perso2);
        film3 = new Film("Temple Maudit", perso1);
        film4 = new Film("Blade Runner", perso2);
        film5 = new Film("Alien3", perso4);
        film6 = new Film("Fight Club", perso4);
        film7 = new Film("Orange Mecanique", perso3);

        films.add(film1);
        films.add(film2);
        films.add(film3);
        films.add(film4);
        films.add(film5);
        films.add(film6);
        films.add(film7);

        for (Film film : films) {
            System.out.println(film);
            film.save();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        Personne.deleteTable();
        Film.deleteTable();
    }

    @Test
    void test_findById_OK() throws SQLException {
        Film film = Film.findById(1);
        assertEquals(film1.toString(), film.toString());
    }

    @Test
    void test_findByRealisateur_OK() throws SQLException {
        ArrayList<Film> films = Film.findByRealisateur(perso1);
        assertEquals(film1.toString(), films.getFirst().toString());
        assertEquals(film3.toString(), films.getLast().toString());
    }

    @Test
    void test_getRealisateur_OK() throws SQLException {
        Personne p = film1.getRealisateur();
        assertEquals(perso1.toString(), p.toString());
    }

    @Test
    void test_delete_Ok() throws SQLException {
        film1.delete();
        assertNull(Film.findById(1));
    }
}
