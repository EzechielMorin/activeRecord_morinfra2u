package activeRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Personne {

    private int id;
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Personne> findAll() throws SQLException {
        ArrayList<Personne> personnes = new ArrayList<>();
        String sql = "SELECT * FROM Personne;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(rs.getInt("id"));
            personnes.add(p);
        }
        return personnes;
    }

    public static Personne findById(int id) throws SQLException {
        Personne p = null;
        String sql = "SELECT * FROM Personne WHERE id = ?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql);
        prep.setInt(1, id);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        if (rs.next()) {
            p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(id);
        }
        return p;
    }

    public static ArrayList<Personne> findByName(String nom) throws SQLException {
        ArrayList<Personne> personnes = new ArrayList<>();
        String sql = "SELECT * FROM Personne WHERE nom = ?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql);
        prep.setString(1, nom);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(rs.getInt("id"));
            personnes.add(p);
        }
        return personnes;
    }

    public static void createTable() throws SQLException {
        String createString = "CREATE TABLE IF NOT EXISTS Personne ( "
                + "ID INTEGER  AUTO_INCREMENT, " + "NOM varchar(40) NOT NULL, "
                + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(createString);
        prep.execute();
    }

    public static void deleteTable() throws SQLException {
        String dropString = "DROP TABLE Personne;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(dropString);
        prep.execute();
    }

    public void save() throws SQLException {
        if (this.id != -1) {
            this.update();
        } else {
            this.saveNew();
        }
    }

    private void saveNew() throws SQLException {
        String sql = "INSERT INTO Personne (nom, prenom) VALUES (?, ?);";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.executeUpdate();

        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);
        }
    }

    private void update() throws SQLException {
        String sql = "UPDATE Personne SET nom = ?, prenom = ? WHERE id = ?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(sql);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.setInt(3, this.id);
        prep.execute();
    }

    public void delete() throws SQLException {
        String deleteString = " DELETE FROM Personne WHERE id = ?;";
        PreparedStatement prep = DBConnection.getConnection().prepareStatement(deleteString);
        prep.setInt(1, this.id);
        prep.execute();
        this.id = -1;
    }

    public String toString() {
        return this.nom + " " + this.prenom + " " + this.id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
