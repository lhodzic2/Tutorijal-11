package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
        GlavnaController ctrl = new GlavnaController();
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Grad");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.setResizable(false);
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
