package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/glavna.fxml"));
        primaryStage.setTitle("Gradovi svijeta");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
