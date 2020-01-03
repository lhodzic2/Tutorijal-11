package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DrzavaController {
    @FXML
    private TextField fieldNaziv;
    @FXML
    private ChoiceBox<Grad> choiceGrad;
    ObservableList<Grad> gradovi;
    public DrzavaController(Object o, ArrayList<Grad> gradovi) {
        this.gradovi = FXCollections.observableArrayList(gradovi);
    }

    public void initialize() {
        choiceGrad.setItems(gradovi);
    }

    public void validiraj(ActionEvent actionEvent) {
        if(!fieldNaziv.getText().equals("")) {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
        }
        if (fieldNaziv.getText().equals("")) {
            return;
        }
        if (choiceGrad.getSelectionModel().getSelectedItem() == null) choiceGrad.getSelectionModel().selectFirst();
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }

    public Drzava getDrzava() {
        return new Drzava(0,fieldNaziv.getText(),choiceGrad.getSelectionModel().getSelectedItem());
    }

    public void zatvori(ActionEvent actionEvent) {
        fieldNaziv.clear();
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }
}
