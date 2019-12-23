package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private Connection conn;

    private PreparedStatement glavniGradUpit,dajGradoveUpit;

    public static GeografijaDAO getInstance() throws FileNotFoundException, SQLException {
        if (instance == null) instance = new GeografijaDAO();
        return instance;
    }
    private GeografijaDAO() throws SQLException, FileNotFoundException {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        try {
            glavniGradUpit = conn.prepareStatement("SELECT * FROM grad,drzava WHERE grad.drzava=drzava.id AND drzava.glavni_grad=grad.id;");
        } catch (SQLException e) {
            regenerisiBazu();
            glavniGradUpit = conn.prepareStatement("SELECT * FROM grad,drzava WHERE grad.drzava=drzava.id AND drzava.glavni_grad=grad.id;");
        }
        try {
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY grad.broj_stanovnika DESC;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() throws FileNotFoundException, SQLException {
        Scanner ulaz = new Scanner (new FileInputStream("baza.db.sql"));
        String sqlUpit = "";
        while (ulaz.hasNext()) {
            sqlUpit = sqlUpit + ulaz.nextLine();
            if (sqlUpit.charAt(sqlUpit.length()-1) == ';' ) {
                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute(sqlUpit);
                    sqlUpit = "";
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        ulaz.close();
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> rezultat = new ArrayList<>();
        ResultSet rs = dajGradoveUpit.executeQuery();
        while (rs.next()) {

        }
    }

}
