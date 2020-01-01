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
    private GeografijaDAO dao = GeografijaDAO.getInstance();

    public void initialize() {
        colGradId.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<Grad,String>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<Grad,Drzava>("drzava"));
        tableViewGradovi.setItems(FXCollections.observableArrayList(dao.getInstance().gradovi()));
        tableViewGradovi.refresh();
        //dodati listener koji refresha table view
    }

    public void dodajDrzavu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/drzava.fxml"));
        Stage newWindow = new Stage();
        newWindow.setTitle("Države");
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void dodajGrad() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        loader.setController(new GradController(dao.dajDrzave(),null));

        Parent root = loader.load();
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
