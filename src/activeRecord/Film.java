package activeRecord;

import activeRecord.exception.RealisateurAbsentException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Film {

    private String titre;
    private int id;
    private int id_real;

    public Film(String titre, Personne personne) {
        this.titre = titre;
        if (personne.getId() == -1)
            throw new RealisateurAbsentException("Le réalisateur n'existe pas dans la DB");
        this.id_real = personne.getId();
        this.id = -1;
    }

    private Film(int id, String titre, int id_real) {
        this.id = id;
        this.titre = titre;
        this.id_real = id_real;
    }

    public static Film findById(int id) throws SQLException {
        Film f = null;
        String sql = "select * from Film where id = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            f = new Film(rs.getInt("id"), rs.getString("titre"), rs.getInt("id_rea"));
        }
        return f;
    }

    public static ArrayList<Film> findByRealisateur(Personne p) throws SQLException {
        String sql = "select * from Film where id_rea = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, p.getId());
        ResultSet rs = ps.executeQuery();
        ArrayList<Film> films = new ArrayList<>();
        while (rs.next()) {
            films.add(new Film(rs.getInt("id"), rs.getString("titre"), rs.getInt("id_rea")));
        }
        return films;
    }

    public Personne getRealisateur() throws SQLException {
        Personne p = null;
        String sql = "select * from Personne inner join Film on Personne.id = Film.id_rea where id_rea = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, this.id_real);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(rs.getInt("id"));
        }
        return p;
    }

    public static void createTable() throws SQLException {
        String createString = "CREATE TABLE IF NOT EXISTS Film( "
                + "ID INTEGER  AUTO_INCREMENT, " + "TITRE varchar(40) NOT NULL, "
                + "ID_REA INTEGER, " + "PRIMARY KEY (ID))";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(createString);
        prep.execute();
    }

    public static void deleteTable() throws SQLException {
        String dropString = "DROP TABLE Film";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(dropString);
        prep.execute();
    }

    public void delete() throws SQLException {
        String deleteString = "DELETE FROM Film WHERE id = ?";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(deleteString);
        prep.setInt(1, this.id);
        prep.execute();
        this.id = -1;
    }

    public void save() throws SQLException {
        if (this.id != -1) {
            this.update();
        } else {
            this.saveNew();
        }
    }

    private void saveNew() throws SQLException {
        if (this.id_real != -1) {
            String sql = "INSERT INTO Film (titre, id_rea) VALUES (?, ?);";
            PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, this.titre);
            prep.setInt(2, this.id_real);
            prep.executeUpdate();

            ResultSet rs = prep.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else
            throw new RealisateurAbsentException("Le Réalisateur n'existe pas dans la DB");
    }

    private void update() throws SQLException {
        if (this.id_real != -1) {
            String sql = "UPDATE Film SET titre = ?, id_rea = ? WHERE id = ?;";
            PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql);
            prep.setString(1, this.titre);
            prep.setInt(2, this.id_real);
            prep.setInt(3, this.id);
            prep.execute();
        } else
            throw new RealisateurAbsentException("Le réalisateur n'existe pas dans la DB");
    }

    public String getTitre() {
        return titre;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id + " " + this.titre + " " + this.id_real;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
