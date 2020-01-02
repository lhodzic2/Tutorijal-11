package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GradController {
    @FXML
    private TextField fieldNaziv;
    @FXML
    private TextField fieldBrojStanovnika;
    @FXML
    private ChoiceBox choiceDrzava;
    private ObservableList<Drzava> drzave;

    public GradController(ObservableList<Drzava> drzave, Object o) {
        this.drzave = drzave;
    }

    public void initialize() {
        choiceDrzava.setItems(drzave);
        fieldNaziv.textProperty().addListener((obs,oldValue,newValue) -> {

        });

        fieldBrojStanovnika.textProperty().addListener((obs,oldValue,newValue) -> {

        });

        choiceDrzava.selectionModelProperty().addListener((obs,oldValue,newValue) -> {

        });
    }

    public void validiraj() {
        if (fieldNaziv.getText().equals("")) {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        }
        try {
            if (Integer.parseInt(fieldBrojStanovnika.getText()) < 0) {
                fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
                fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
            } else {
                fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
                fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
            }
        } catch (NumberFormatException e) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
        }
    }

    public void zatvori(javafx.event.ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage window = (Stage) n.getScene().getWindow();
        window.close();
    }

    public Grad getGrad() {
        return new Grad(0,fieldNaziv.getText(),Integer.parseInt(fieldBrojStanovnika.getText()), (Drzava) choiceDrzava.getSelectionModel().getSelectedItem());
    }
}
