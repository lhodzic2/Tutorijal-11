package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;
    private PreparedStatement dajGradoveUpit, dajGlavniGrad,obrisiGradoveZaDrzavu,obrisiDrzavuUpit,dajIdDrzave,dodajGrad,dodajDrzavu,izmijeniGradUpit, dajDrzavuIdUpit,dajDrzavuNazivUpit;

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
            dajDrzavuIdUpit = conn.prepareStatement("SELECT * FROM drzava WHERE id=?");
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            obrisiGradoveZaDrzavu = conn.prepareStatement("DELETE FROM grad WHERE drzava = ?");
            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE id=?");
            dajIdDrzave = conn.prepareStatement("SELECT id FROM drzava WHERE naziv LIKE ?");
            dodajGrad = conn.prepareStatement("INSERT INTO grad VALUES (?,?,?,?)");
            dodajDrzavu = conn.prepareStatement("INSERT INTO drzava VALUES (?,?,?)");
            izmijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?");
        } catch (SQLException e) {

        }
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
        dajDrzavuIdUpit.setInt(1,id);
        ResultSet rs = dajDrzavuIdUpit.executeQuery();
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
            int id = rs.getInt("id");
            obrisiGradoveZaDrzavu.setInt(1,id);
            obrisiGradoveZaDrzavu.execute();
            obrisiDrzavuUpit.setInt(1,id);
            obrisiDrzavuUpit.execute();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try {
            izmijeniGradUpit.setString(1,grad.getNaziv());
            izmijeniGradUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String drzava) {
        try {
            dajIdDrzave.setString(1,drzava);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = dajIdDrzave.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (!rs.first()) return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Drzava pom = null;
        try {
            pom = new Drzava(rs.getInt(1),drzava,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pom;
    }
}
