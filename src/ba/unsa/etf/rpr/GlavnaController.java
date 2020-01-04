package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
    private ObservableList<Grad> gradovi = dao.getGradovi();
    private ObservableList<Drzava> drzave = dao.drzave();

    public void initialize() {
        colGradId.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<Grad,String>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<Grad,Drzava>("drzava"));
        tableViewGradovi.setItems(gradovi);
        gradovi.addListener((ListChangeListener<Grad>) l -> {
                    gradovi = FXCollections.observableArrayList(dao.gradovi());
                    tableViewGradovi.setItems(gradovi);
                    tableViewGradovi.refresh();
                }
            );
    }

    public void dodajDrzavu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        DrzavaController controller = new DrzavaController(null,dao.gradovi());
        loader.setController(controller);

        Parent root = loader.load();
        Stage newWindow = new Stage();
        newWindow.setTitle("Drzave");
        newWindow.setScene(new Scene(root));
        newWindow.show();
        newWindow.setOnHiding(e -> {
            Drzava d = controller.getDrzava();
            if (d.getNaziv().equals("") || d.getGlavniGrad() == null) return;
            d.setId(dao.generisiIdDrzave());
            dao.dodajDrzavu(d);
            tableViewGradovi.refresh();
        });
    }

    public void dodajGrad() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        GradController controller = new GradController(null,drzave);
        loader.setController(controller);

        Parent root = loader.load();
        Stage newWindow = new Stage();
        newWindow.setTitle("Gradovi");
        newWindow.setScene(new Scene(root));
        newWindow.show();
        newWindow.setOnHiding(e -> {
            int id = dao.dajIdGrada();
            Grad g;
            try {
                g = controller.getGrad();
            } catch (NumberFormatException i) {
                return;
            }
            if (g.getNaziv() == null) return;
            g.setId(id);
            dao.dodajGrad(g);
        });
    }

    public void izmijeniGrad() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        Grad izabraniGrad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (izabraniGrad == null) return;
        GradController controller = new GradController(izabraniGrad,drzave);
        loader.setController(controller);
        Parent root = loader.load();
        Stage newWindow = new Stage();
        newWindow.setTitle("Gradovi");
        newWindow.setScene(new Scene(root));
        newWindow.show();
        newWindow.setOnHiding(e -> {
            Grad g;
            try {
                g = controller.getGrad();
            } catch (NumberFormatException i) {
                return;
            }
            g.setId(izabraniGrad.getId());
            dao.izmijeniGrad(g);
//            gradovi.remove(tableViewGradovi.getSelectionModel().getSelectedItem());
//            gradovi.add(g);
            tableViewGradovi.getSelectionModel().select(g);
            tableViewGradovi.refresh();
        });
    }

    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }

    public void obrisiGrad(ActionEvent actionEvent) {
        if (tableViewGradovi.getSelectionModel().getSelectedItem() == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Da li ste sigurni da zelite obrisati " + tableViewGradovi.getSelectionModel().getSelectedItem().getNaziv() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.CANCEL) return;
        dao.obrisiGrad(tableViewGradovi.getSelectionModel().getSelectedItem());
        gradovi = FXCollections.observableArrayList(dao.gradovi());
        tableViewGradovi.setItems(gradovi);
        tableViewGradovi.refresh();
    }
}
