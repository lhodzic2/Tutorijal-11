package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GlavnaController {
    @FXML
    private TableView<Grad> tableViewGradovi;
    @FXML
    private TableColumn colGradId;
    @FXML
    private TableColumn colGradNaziv;
    @FXML
    private TableColumn colGradStanovnika;
    @FXML
    private TableColumn colGradDrzava;
    private GeografijaDAO dao;
    public ObservableList<Grad> lista = FXCollections.observableArrayList(GeografijaDAO.getInstance().gradovi());

    public void initialize() {
//        colGradId.setCellFactory(new PropertyValueFactory<Grad,Integer>("Id"));
//        colGradNaziv.setCellFactory(new PropertyValueFactory<Grad,String>("Naziv"));
//        colGradStanovnika.setCellFactory(new PropertyValueFactory<Grad,Integer>("Stanovnika"));

        tableViewGradovi.getItems().setAll(lista);
    }

    public void dodajDrzavu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/drzava.fxml"));
        Stage newWindow = new Stage();
        newWindow.setTitle("Države");
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void dodajGrad() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/grad.fxml"));
        Stage newWindow = new Stage();
        newWindow.setTitle("Gradovi");
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void izmijeniGrad() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/grad.fxml"));
        Stage newWindow = new Stage();
        newWindow.setTitle("Gradovi");
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }

}
