package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DrzavaController {
    @FXML
    private TextField fieldNaziv;
    @FXML
    private ChoiceBox<Grad> choiceGrad;
    ObservableList<Grad> gradovi;
    public DrzavaController(ObservableList<Grad> gradovi) {
        this.gradovi = gradovi;
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
        if (fieldNaziv.getStyleClass().equals("poljeNijeIspravno") || choiceGrad.getSelectionModel().getSelectedItem() == null ) {
            return;
        }
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
