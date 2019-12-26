package ba.unsa.etf.rpr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();
    }

    public static void glavniGrad() {
        System.out.println("Unesite drzavu\n");
        Scanner unos = new Scanner(System.in);
        String d = unos.nextLine();
        Grad g = GeografijaDAO.getInstance().glavniGrad(d);
        if (g == null) {
            System.out.println("Nepostojeca drzava");
            return;
        }
        System.out.println("Glavni grad drzave " + d +" "+g.getNaziv() );
    }

    public static String ispisiGradove() {
        ArrayList<Grad> lista = GeografijaDAO.getInstance().gradovi();
        String s = "";
        for ( Grad g : lista) {
            s = s + g.getNaziv() + " (" + g.getDrzava().getNaziv() + ") - " + g.getBrojStanovnika() + "\n";
        }
        return s;
    }
}
