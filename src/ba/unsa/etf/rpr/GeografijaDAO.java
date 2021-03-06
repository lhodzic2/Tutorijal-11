package ba.unsa.etf.rpr;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;
    private PreparedStatement dajGradoveUpit, dajGlavniGrad,obrisiGradoveZaDrzavu,obrisiDrzavuUpit,dajIdDrzave,dodajGrad,dodajDrzavu,izmijeniGradUpit, dajDrzavuPoIdUpit,dajDrzaveUpit,dajIdGrada,generisiIdDrzave,obrisiGrad,nadjiGrad,obrisiDrzavuZaGrad;
    private ObservableList<Drzava> drzave = FXCollections.observableArrayList();
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();
    private ObjectProperty<Grad> trenutniGrad = null;

    public Grad getTrenutniGrad() {
        return trenutniGrad.get();
    }

    public ObjectProperty<Grad> trenutniGradProperty() {
        return trenutniGrad;
    }

    public void setTrenutniGrad(Grad trenutniGrad) {
        this.trenutniGrad.set(trenutniGrad);
    }

    public ObservableList<Drzava> getDrzave() {
        return drzave;
    }

    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dajGlavniGrad = conn.prepareStatement("SELECT grad.id,grad.naziv,grad.broj_stanovnika,grad.drzava FROM grad,drzava WHERE grad.drzava=drzava.id AND  drzava.naziv LIKE ?");
        } catch (SQLException e) {
            regenerisiBazu();
            try {
                dajGlavniGrad = conn.prepareStatement("SELECT grad.id,grad.naziv,grad.broj_stanovnika,grad.drzava FROM grad,drzava WHERE grad.drzava=drzava.id AND  drzava.naziv LIKE ?");
            } catch (SQLException e1) {

            }
        }
        try {
            //pripremanje upita
            dajDrzavuPoIdUpit = conn.prepareStatement("SELECT * FROM drzava WHERE id=?");
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            obrisiGradoveZaDrzavu = conn.prepareStatement("DELETE FROM grad WHERE drzava = ?");
            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE id=?");
            dajIdDrzave = conn.prepareStatement("SELECT id FROM drzava WHERE naziv LIKE ?");
            dodajGrad = conn.prepareStatement("INSERT INTO grad VALUES (?,?,?,?)");
            dodajDrzavu = conn.prepareStatement("INSERT INTO drzava VALUES (?,?,?)");
            izmijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?,broj_stanovnika=?,drzava=? WHERE id=?");
            dajDrzaveUpit = conn.prepareStatement("SELECT * FROM drzava");
            dajIdGrada = conn.prepareStatement("SELECT max(id) FROM grad");
            generisiIdDrzave = conn.prepareStatement("SELECT max(id) FROM drzava");
            obrisiGrad = conn.prepareStatement("DELETE FROM grad WHERE id=?");
            nadjiGrad = conn.prepareStatement("SELECT * FROM grad WHERE naziv LIKE ?");
            obrisiDrzavuZaGrad = conn.prepareStatement("DELETE FROM drzava WHERE glavni_grad=?");
            //inicijalizacija listi
            ResultSet gr = dajGradoveUpit.executeQuery();
            while(gr.next()) {
                Grad pomocna = new Grad(gr.getInt(1),gr.getString("naziv"),gr.getInt(3),null);
                pomocna.setDrzava(dajDrzavu(gr.getInt(4),pomocna));
                gradovi.add(pomocna);
            }
            ResultSet dr = dajDrzaveUpit.executeQuery();
            while(dr.next()) {
                drzave.add(new Drzava(dr.getInt("id"),dr.getString("naziv"),glavniGrad(dr.getString("naziv"))));
            }
        } catch (SQLException e) {
        }
//        gradovi = FXCollections.observableArrayList(gradovi());
//        drzave = FXCollections.observableArrayList();

    }

    public void obrisiGrad(Grad grad) {
        try {
            obrisiDrzavuZaGrad.setInt(1,grad.getId());
            obrisiDrzavuZaGrad.executeUpdate();
            obrisiGrad.setInt(1,grad.getId());
            obrisiGrad.executeUpdate();
            gradovi.remove(grad);
//            gradovi.clear();
//            gradovi = FXCollections.observableArrayList(gradovi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int generisiIdDrzave() {
        ResultSet rs = null;
        try {
            rs = generisiIdDrzave.executeQuery();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int dajIdGrada() {
        ResultSet rs = null;
        try {
            rs = dajIdGrada.executeQuery();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ObservableList<Drzava> drzave() {
        if (drzave.size() != 0) drzave.clear();
        try {
            ResultSet rs = dajDrzaveUpit.executeQuery();
            while(rs.next()) {
                drzave.add(new Drzava(rs.getInt("id"),rs.getString("naziv"),glavniGrad(rs.getString("naziv"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drzave;
    }

    private void regenerisiBazu()  {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String upit = "";
            while(ulaz.hasNext()) {
                upit = upit + ulaz.nextLine();
                if (upit.length() > 1 && upit.charAt(upit.length()-1) == ';') {
                    try {
                        Statement s = conn.createStatement();
                        s.execute(upit);
                        upit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ne postoji SQL datoteka.");
        }
    }

    public static GeografijaDAO getInstance() {
        if (instance == null) instance = new GeografijaDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance == null) return;
        try {
            instance.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        instance  = null;
    }

    private Drzava dajDrzavu(int id,Grad grad) throws SQLException {
        dajDrzavuPoIdUpit.setInt(1,id);
        ResultSet rs = dajDrzavuPoIdUpit.executeQuery();
        if (!rs.next()) return null;
        return new Drzava(id,rs.getString(2),grad);
    }

    public ArrayList<Grad> gradovi()  {
        ArrayList<Grad> lista = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = dajGradoveUpit.executeQuery();
        while (rs.next()) {
            Grad pomocna = new Grad(rs.getInt(1),rs.getString("naziv"),rs.getInt(3),null);
            pomocna.setDrzava(dajDrzavu(rs.getInt(4),pomocna));
            lista.add(pomocna);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Grad glavniGrad(String drzava) {
        Grad pomocna = null;
        try {
            dajGlavniGrad.setString(1, drzava);
            ResultSet rs1 = dajGlavniGrad.executeQuery();
            if (!rs1.next()) return null;
            int id = rs1.getInt(4);
            pomocna = new Grad(rs1.getInt(1),rs1.getString(2),rs1.getInt(3),null);
            pomocna.setDrzava(dajDrzavu(id,pomocna));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pomocna;
    }

    public void obrisiDrzavu(String drzava) {
        try {
            dajIdDrzave.setString(1,drzava);
            ResultSet rs = dajIdDrzave.executeQuery();
            if (!rs.next()) return;
            int id = rs.getInt("id");
            obrisiGradoveZaDrzavu.setInt(1,id);
            obrisiGradoveZaDrzavu.execute();
            obrisiDrzavuUpit.setInt(1,id);
            obrisiDrzavuUpit.execute();
            drzave.remove(nadjiDrzavu(drzava));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajGrad(Grad grad) {
        try {
            dodajGrad.setInt(1, grad.getId());
            dodajGrad.setString(2, grad.getNaziv());
            dodajGrad.setInt(3, grad.getBrojStanovnika());
            dodajGrad.setInt(4, grad.getDrzava().getId());
            dodajGrad.execute();
            gradovi.clear();
            gradovi = FXCollections.observableArrayList(gradovi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            dodajDrzavu.setInt(1,drzava.getId());
            dodajDrzavu.setString(2,drzava.getNaziv());
            dodajDrzavu.setInt(3,drzava.getGlavniGrad().getId());
            dodajDrzavu.execute();
//            drzave.clear();
//            drzave = drzave();
            drzave.add(drzava);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try {
            izmijeniGradUpit.setString(1,grad.getNaziv());
            izmijeniGradUpit.setInt(2,grad.getBrojStanovnika());
            izmijeniGradUpit.setInt(3,grad.getDrzava().getId());
            izmijeniGradUpit.setInt(4,grad.getId());
            izmijeniGradUpit.execute();
            gradovi.clear();
            gradovi = FXCollections.observableArrayList(gradovi());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String drzava) {
        Drzava pom = null;
        try {
            dajIdDrzave.setString(1,drzava);
            ResultSet rs = null;
            rs = dajIdDrzave.executeQuery();
            if (!rs.next()) return null;
            pom = new Drzava(rs.getInt(1),drzava,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return pom;
    }

    public Grad nadjiGrad(String grad) {
        try {
            nadjiGrad.setString(1,grad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs;
        Grad g = null;
        try {
            rs = nadjiGrad.executeQuery();
            while(rs.next()) {
                Drzava d = dajDrzavu(rs.getInt(4),g);
                g = new Grad(rs.getInt(1),rs.getString(2),rs.getInt(3),d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return g;
    }


    public ObservableList<Grad> getGradovi() {
        return gradovi;
    }
}
