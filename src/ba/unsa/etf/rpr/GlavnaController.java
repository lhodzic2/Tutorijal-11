package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    ObservableList<Grad> gradovi = FXCollections.observableArrayList(dao.gradovi());

    public void initialize() {
        colGradId.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<Grad,String>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<Grad,Integer>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<Grad,Drzava>("drzava"));
        tableViewGradovi.setItems(gradovi);
        tableViewGradovi.refresh();

        gradovi.addListener((ListChangeListener<Grad>) l -> {
                    gradovi.sorted( (g1,g2) -> {
                       if (g1.getBrojStanovnika() > g2.getBrojStanovnika() ) return 1;
                       if (g1.getBrojStanovnika() < g2.getBrojStanovnika() ) return -1;
                       return 0;
                    });
                    tableViewGradovi.refresh();
                }
            );
    }

    public void dodajDrzavu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        DrzavaController controller = new DrzavaController(FXCollections.observableArrayList(dao.gradovi()));
        loader.setController(controller);

        Parent root = loader.load();
        Stage newWindow = new Stage();
        newWindow.setTitle("Gradovi");
        newWindow.setScene(new Scene(root));
        newWindow.show();
    }

    public void dodajGrad() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        GradController controller = new GradController(dao.dajDrzave(),null);
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
            gradovi.add(g);
        });
    }

    public void izmijeniGrad() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        Grad izabraniGrad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (izabraniGrad == null) return;
        GradController controller = new GradController(dao.dajDrzave(),izabraniGrad);
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
            dao.izmijeniGrad(g);
            gradovi.remove(tableViewGradovi.getSelectionModel().getSelectedItem());
            gradovi.add(g);
            tableViewGradovi.getSelectionModel().select(g);
        });
    }

    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }

}
